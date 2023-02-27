package com.project.nprg056.proximitychat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.project.nprg056.proximitychat.model.Message
import com.project.nprg056.proximitychat.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ChatViewModel(
    private val roomId: String?,
    private val userId: String?
) : ViewModel() {
    private var db: DatabaseReference = Firebase.database(Constants.DB_URL).reference

    init {
        disconnectListener()
        getOtherUserName()
        getMessages()
    }

    private val _otherUserConnected = MutableLiveData(true)
    val otherUserConnected: LiveData<Boolean> = _otherUserConnected

    private val _otherUserName = MutableLiveData("")
    val otherUserName: LiveData<String> = _otherUserName

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    private var _messages = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messages: LiveData<MutableList<Map<String, Any>>> = _messages

    private fun disconnectListener() {
        viewModelScope.launch(Dispatchers.Default) {
            db.child("rooms")
                .child(roomId!!)
                .child("users")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val list = emptyList<Map<String, Any>>().toMutableList()

                        for (doc in dataSnapshot.children) {
                            val data = doc.getValue<Map<String, Any>>()!!.toMutableMap()
                            data[Constants.IS_CURRENT_USER] = data[Constants.SENT_BY] == userId
                            list.add(data)
                        }

                        updateMessages(list)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e(Constants.TAG, "disconnectListener: listen failed.",
                            databaseError.toException())
                    }
                })
        }
    }

    fun updateMessage(message: String) {
        _message.value = message
    }

    private fun getOtherUserName() {
        db.child("rooms")
            .child(roomId!!)
            .child("users")
            .get()
            .addOnSuccessListener { data: DataSnapshot ->
                for (doc in data.children) {
                    if (doc.key != userId) {
                        _otherUserName.value = doc.key.toString()
                    }
                }
                Log.w("Firebase", "Other username: ${_otherUserName.value}")
            }.addOnFailureListener{
                Log.e("Firebase", "Error getting data", it)
            }
    }

    fun addMessage() {
        val message: String = _message.value ?: throw IllegalArgumentException("message empty")
        if (message.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                db.child(Constants.MESSAGES).child(roomId!!).push().setValue(
                    Message(message, "0", System.currentTimeMillis().toString()
                    )
                ).addOnSuccessListener {
                    _message.value = ""
                }.addOnFailureListener{
                    Log.e("Firebase", "Error sending message", it)
                }
            }
        }
    }

    private fun getMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            db.child(Constants.MESSAGES)
                .child(roomId!!)
                .orderByChild(Constants.TIMESTAMP)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val list = emptyList<Map<String, Any>>().toMutableList()

                        for (doc in dataSnapshot.children) {
                            val data = doc.getValue<Map<String, Any>>()!!.toMutableMap()
                            data[Constants.IS_CURRENT_USER] = data[Constants.SENT_BY] == userId
                            list.add(data)
                        }

                        updateMessages(list)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("Firebase", "getMessages listen failed.",
                            databaseError.toException())
                    }
                })
        }
    }

    private fun updateMessages(list: MutableList<Map<String, Any>>) {
        _messages.value = list.asReversed()
    }
}