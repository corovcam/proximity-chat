package com.project.nprg056.proximitychat.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.nprg056.proximitychat.model.LocationDetail
import com.project.nprg056.proximitychat.model.User
import com.project.nprg056.proximitychat.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QueueViewModel(val context: Context) : ViewModel() {
    private var db: DatabaseReference = Firebase.database(Constants.DB_URL).reference
    private val _userName = MutableLiveData("")
    val userName: LiveData<String> = _userName

    private val _userId = MutableLiveData("")
    val userId: LiveData<String> = _userId

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _buttonLocked = MutableLiveData(false)
    val buttonLocked: LiveData<Boolean> = _buttonLocked

    private val visited = mutableSetOf<String>()

    private var location = LocationDetail(0.0,0.0)

    private lateinit var listener: ValueEventListener

    // Update username
    fun updateUserName(newUserName: String) {
        _userName.value = newUserName
    }

    fun updateLoadingStatus(newLoadingStatus: Boolean) {
        _loading.value = newLoadingStatus
    }

    private fun showErrorMessage(message: String) {
        _loading.value = false
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    // Register user
    fun registerUser(locationDetail: LocationDetail, toQueue: () -> Unit, goBack: () -> Unit, toChat: (String, String) -> Unit) {
        location = locationDetail
        viewModelScope.launch {
            _userId.value = db.push().key
            db.child("users").child(_userId.value.toString()).setValue(User(userName.value.toString()))
            _loading.value = false
            withContext(Dispatchers.Main) {
                toQueue()
                getChatRoom(goBack, toChat)
            }
        }
    }

    fun deleteUser() {
        val userId = _userId.value
        db.child("users/${userId}/roomId").removeEventListener(listener)
        db.child("queue/${userId}").removeValue()
        _userId.value = ""
    }

    fun calcDistance(lat: Double, lon: Double): Double {
        return Math.sqrt(Math.pow(location.latitude!! - lat, 2.0)+Math.pow(location.longitude!! - lon, 2.0))
    }
    fun joinQueue() {
        var foundId = ""
        db.child("queue").runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                var distance = Double.MAX_VALUE

                for (doc in mutableData.children) {
                    if(visited.contains(doc.key))
                        continue
                    val loc = doc.getValue(LocationDetail::class.java)
                    val dist = calcDistance(loc?.latitude!!, loc.longitude!!)
                    if(dist < distance) {
                        foundId = doc.key!!
                        distance = dist
                    }
                }

                if(foundId != "")
                    mutableData.child(foundId).value = null;
                else
                    mutableData.child(userId.value!!).value = location

                // Set value and report transaction success
                return Transaction.success(mutableData)
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ){
                if(databaseError == null && foundId != "") {
                    db.child("users/${userId.value}/roomId").setValue("${userId.value}___${foundId}")
                    db.child("users/${foundId}/roomId").setValue("${userId.value}___${foundId}")
                }
            }
        })

    }

    fun getChatRoom(goBack: () -> Unit, toChat: (String, String) -> Unit) {
        if (_loading.value == true)
            return

        visited.clear()

        listener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val roomId: String = dataSnapshot.value.toString()
                if(roomId != "") {
                    val userIds = roomId!!.split("___")
                    for(id in userIds) {
                        if (id != userId.value) {
                            visited.add(id)
                        }
                    }
                    toChat(roomId, userId.value!!)
                }
                else if(visited.isNotEmpty()) {
                    goBack()
                    joinQueue()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Get Chat Room failure", "Unsuccessful")
                showErrorMessage(
                    "There are no people in the queue at the moment. Please try again later."
                )
            }
        }

       db.child("users/${userId.value.toString()}/roomId").addValueEventListener(listener)
       joinQueue()
    }
}