package com.project.nprg056.proximitychat.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.nprg056.proximitychat.api.APIService
import com.project.nprg056.proximitychat.model.LocationDetail
import com.project.nprg056.proximitychat.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QueueViewModel : ViewModel() {
    private val _userName = MutableLiveData("")
    val userName: LiveData<String> = _userName

    private val _userId = MutableLiveData("")
    val userId: LiveData<String> = _userId

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val apiService = APIService.buildService()

    // Update username
    fun updateUserName(newUserName: String) {
        _userName.value = newUserName
    }

    fun updateLoadingStatus(newLoadingStatus: Boolean) {
        _loading.value = newLoadingStatus
    }

    // Register user
    fun registerUser(locationDetail: LocationDetail, context: Context, toQueue: () -> Unit) {
        val showErrorMessage = {
            _loading.value = false
            Log.w("Register User failure", "User registration unsuccessful")
            Toast.makeText(
                context, "User registration unsuccessful. Please try again.",
                Toast.LENGTH_LONG)
                .show()
        }

        viewModelScope.launch {
            try {
                val response = apiService.registerUser(User(_userName.value!!, locationDetail))
                if (response != null) {
                    _userId.value = response.userId
                    Log.w("User ID", response.userId!!)
                    _loading.value = false
                    withContext(Dispatchers.Main) {
                        toQueue()
                    }
                } else {
                    showErrorMessage()
                }
            } catch (e: Exception) {
                Log.e("Register User ERROR", e.message!!)
                showErrorMessage()
            }
        }
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