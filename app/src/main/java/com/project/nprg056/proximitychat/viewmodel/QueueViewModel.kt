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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QueueViewModel(val context: Context) : ViewModel() {
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

    private fun showErrorMessage(message: String) {
        _loading.value = false
        Log.w("Register User failure", "User registration unsuccessful")
        Toast.makeText(
            context, "User registration unsuccessful. Please try again.",
            Toast.LENGTH_LONG)
            .show()
    }

    // Register user
    fun registerUser(locationDetail: LocationDetail, toQueue: () -> Unit) {
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
                    Log.w("Register User failure", "User registration unsuccessful")
                    showErrorMessage("User registration unsuccessful. Please try again.")
                }
            } catch (e: Exception) {
                Log.e("Register User ERROR", e.message!!)
                showErrorMessage("User registration unsuccessful. Please try again.")
            }
        }
    }

    fun deleteUser() {
        val userId = _userId.value
        viewModelScope.launch {
            if (_userId.value.isNullOrEmpty()) {
                Log.w("UserId", "UserId is Empty")
                return@launch
            }
            try {
                val call = apiService.deleteUser(userId!!)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.code() == 200) {
                            Log.w("Delete User", "User removed from the queue successfully")
                        } else {
                            Log.w("Delete User failure", response.toString())
                        }
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("Delete User failure", t.message!!)
                    }
                })
            } catch (e: Exception) {
                Log.e("Delete User ERROR", e.toString())
            }
        }
        _userId.value = ""
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