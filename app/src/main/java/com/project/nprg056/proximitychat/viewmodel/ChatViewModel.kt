package com.project.nprg056.proximitychat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.nprg056.proximitychat.model.Message
import com.project.nprg056.proximitychat.util.Constants
import org.w3c.dom.Comment
import java.lang.IllegalArgumentException

class ChatViewModel(private val roomId: String?) : ViewModel() {
    private var db: DatabaseReference = Firebase.database(Constants.DB_URL).reference

    init {
        /*getOtherUserName(roomId!!)*/
        getMessages()
    }

    private val _otherUserName = MutableLiveData("")
    val otherUserName: LiveData<String> = _otherUserName

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    private var _messages = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messages: LiveData<MutableList<Map<String, Any>>> = _messages

    fun updateMessage(message: String) {
        _message.value = message
    }

    private fun getOtherUserName(roomId: String) {
        db.child("rooms").child(roomId).child("users").get()
        .addOnSuccessListener {
            Log.w("Firebase", "Other username: ${it.value}")
            _otherUserName.value = it.value.toString()
        }.addOnFailureListener{
            Log.e("Firebase", "Error getting data", it)
        }
    }

    fun addMessage() {
        val message: String = _message.value ?: throw IllegalArgumentException("message empty")
        if (message.isNotEmpty()) {
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

    private fun getMessages() {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.w("Firebase", "onChildAdded:" + dataSnapshot.key!!)

                val message = dataSnapshot.value
                val listMap = hashMapOf<String, Any>(
                    Constants.MESSAGE to message!!,
                    Constants.IS_CURRENT_USER to true
                )

                val updatedList = _messages.value
                updatedList!!.add(listMap)

                updateMessages(updatedList)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Send message failure", databaseError.toException())
                /*Toast.makeText(context, "Failed to load messages.", Toast.LENGTH_SHORT).show()*/
            }
        }

        /*db.child(Constants.MESSAGES)
            .orderByChild(Constants.TIMESTAMP)
            *//*.addChildEventListener(childEventListener)*//*
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        // TODO: handle the post
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("Firebase", "loadPost:onCancelled", databaseError.toException())
                    // ...
                }
            })*/

            /*value, e ->
                               if (e != null) {
                                   Log.w(Constants.TAG, "Listen failed.", e)
                                   return@addSnapshotListener
                               }

                               val list = emptyList<Map<String, Any>>().toMutableList()

                               if (value != null) {
                                   for (doc in value) {
                                       val data = doc.data
                                       data[Constants.IS_CURRENT_USER] = true
                                       list.add(data)
                                   }
                               }

                               updateMessages(list)
                           }*/
    }

    private fun updateMessages(list: MutableList<Map<String, Any>>) {
        _messages.value = list.asReversed()
    }
}