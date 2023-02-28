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
        getOtherUserName()
        getMessages()
    }

    private val _otherUserName = MutableLiveData("")
    val otherUserName: LiveData<String> = _otherUserName

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    private var _messages = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messages: LiveData<MutableList<Map<String, Any>>> = _messages

    fun leaveChat() {
        val userIds = roomId!!.split("___")
        for(id in userIds) {
            db.child("users/${id}/roomId").setValue("")
        }
    }
    fun updateMessage(message: String) {
        _message.value = message
    }

    private fun getOtherUserName() {
        val userIds = roomId!!.split("___")
        for(id in userIds) {
            if (id != userId) {
                db.child("users/${id}/userName")
                    .get()
                    .addOnSuccessListener {  data: DataSnapshot ->
                        _otherUserName.value = data.value.toString()
                    }
            }
        }
    }

    fun addMessage() {
        val message: String = _message.value ?: throw IllegalArgumentException("message empty")
        if (message.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                db.child(Constants.MESSAGES).child(roomId!!).push().setValue(
                    Message(message, userId, ServerValue.TIMESTAMP)
                ).addOnSuccessListener {
                    _message.value = ""
                }.addOnFailureListener{
                    Log.e("Firebase", "Error sending message", it)
                }
            }
        }
    }

    private fun getMessages() {
        viewModelScope.launch {
            db.child(Constants.MESSAGES)
                .child(roomId!!)
                .orderByKey()
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