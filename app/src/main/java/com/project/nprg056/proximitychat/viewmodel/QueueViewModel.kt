package com.project.nprg056.proximitychat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QueueViewModel : ViewModel() {
    private val _userName = MutableLiveData("")
    val userName: LiveData<String> = _userName

    private val _userId = MutableLiveData("")
    val userId: LiveData<String> = _userId

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    // Update username
    fun updateUserName(newUserName: String) {
        _userName.value = newUserName
    }

    // Register user
    fun registerUser() {
//        if (_loading.value == false) {
//            // TODO("API")
//        }
    }

    fun getChatRoom() {
        if (_loading.value == false) {
            val userId: String? = _userId.value
            if (userId.isNullOrEmpty())
                Log.w("UserId", "UserId not set")
//              Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()

//            _loading.value = true
            // TODO("API")
        }
    }
}