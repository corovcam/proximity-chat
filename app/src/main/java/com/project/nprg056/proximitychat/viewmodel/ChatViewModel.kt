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
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import kotlin.math.roundToInt

class ChatViewModel : ViewModel() {
    private val db: DatabaseReference = Firebase.database(Constants.DB_URL).reference

    private val _otherUserName = MutableLiveData("")
    val otherUserName: LiveData<String> = _otherUserName

    private val _usersDistance = MutableLiveData<Int?>(null)
    val usersDistance: LiveData<Int?> = _usersDistance

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    private var _messages = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messages: LiveData<MutableList<Map<String, Any>>> = _messages

    private lateinit var roomId: String
    private lateinit var userId: String

    fun initChatViewModel(roomId: String, userId: String) {
        this.roomId = roomId
        this.userId = userId
        getOtherUserName()
        getUsersDistance()
        getMessages()
    }

    fun leaveChat() {
        val userIds = roomId.split("___")
        for(id in userIds) {
            db.child("users/${id}/roomId").setValue("")
        }
    }
    fun updateMessage(message: String) {
        _message.value = message
    }

    private fun getOtherUserName() {
        val userIds = roomId.split("___")
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

    private fun getUsersDistance() {
        val userIds = roomId.split("___")
        for (id in userIds) {
            if (id != userId) {
                db.child("users/${id}/usersDistance")
                    .get()
                    .addOnSuccessListener {  data: DataSnapshot ->
                        _usersDistance.value = data.getValue(Double::class.java)?.roundToInt()
                    }
            }
        }
    }

    fun addMessage() {
        val message: String = _message.value ?: throw IllegalArgumentException("Message empty")
        if (message.isNotEmpty()) {
            viewModelScope.launch {
                db.child(Constants.MESSAGES).child(roomId).push().setValue(
                    Message(message, userId, ServerValue.TIMESTAMP)
                ).addOnSuccessListener {
                    _message.value = ""
                }.addOnFailureListener{
                    Log.e("Firebase", "addMessage Error", it)
                }
            }
        }
    }

    private fun getMessages() {
        viewModelScope.launch {
            db.child(Constants.MESSAGES)
                .child(roomId)
                .orderByKey()
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val list = emptyList<Map<String, Any>>().toMutableList()

                        for (doc in dataSnapshot.children) {
                            val data = doc.getValue<Map<String, Any>>()?.toMutableMap() ?: continue
                            data[Constants.IS_CURRENT_USER] = data[Constants.USER_ID] == userId
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