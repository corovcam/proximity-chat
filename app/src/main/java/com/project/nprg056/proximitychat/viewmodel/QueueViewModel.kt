package com.project.nprg056.proximitychat.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.nprg056.proximitychat.R
import com.project.nprg056.proximitychat.model.LocationDetail
import com.project.nprg056.proximitychat.model.User
import com.project.nprg056.proximitychat.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QueueViewModel : ViewModel() {
    private var db: DatabaseReference = Firebase.database(Constants.DB_URL).reference
    private val _userName = MutableLiveData("")
    val userName: LiveData<String> = _userName

    private val _userId = MutableLiveData("")
    val userId: LiveData<String> = _userId

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

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

    // Register user
    fun registerUser(locationDetail: LocationDetail,
                     toQueue: () -> Unit,
                     goBack: () -> Unit,
                     toChat: (String, String) -> Unit,
                     showInfoToast: (resId: Int) -> Unit
    ) {
        location = locationDetail
        viewModelScope.launch {
            _userId.value = db.push().key
            db.child("users").child(_userId.value.toString()).setValue(User(userName.value.toString()))
            _loading.value = false
            getChatRoom(goBack, toChat, showInfoToast)
            withContext(Dispatchers.Main) {
                toQueue()
            }
        }
    }

    fun deleteUser() {
        val userId = _userId.value
        db.child("users/${userId}/roomId").removeEventListener(listener)
        db.child("queue/${userId}").removeValue()
        _userId.value = ""
    }

    private fun calcDistance(lat: Double, lon: Double): Float {
        val locationA = Location("User 1")
        locationA.latitude = location.latitude!!
        locationA.longitude = location.longitude!!

        val locationB = Location("User 2")
        locationB.latitude = lat
        locationB.longitude = lon

        return locationA.distanceTo(locationB)
    }

    fun joinQueue() {
        var foundId = ""
        var distance = Float.MAX_VALUE
        db.child("queue").runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                for (doc in mutableData.children) {
                    if (visited.contains(doc.key))
                        continue
                    val loc = doc.getValue(LocationDetail::class.java)
                    val dist = calcDistance(loc?.latitude!!, loc.longitude!!)
                    if(dist < distance) {
                        foundId = doc.key!!
                        distance = dist
                    }
                }

                if(foundId != "")
                    mutableData.child(foundId).value = null
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
                databaseError?.let { error ->
                    Log.e("Firebase", "joinQueue Error: ${error.message}")
                    return
                }
                if (foundId != "") {
                    val updates: MutableMap<String, Any> = hashMapOf(
                        "users/${userId.value}/roomId" to "${userId.value}___${foundId}",
                        "users/${foundId}/roomId" to "${userId.value}___${foundId}",
                        "users/${userId.value}/usersDistance" to distance.toDouble(),
                        "users/${foundId}/usersDistance" to distance.toDouble()
                    )
                    db.updateChildren(updates)
                }
            }
        })
    }

    private fun getChatRoom(
        goBack: () -> Unit,
        toChat: (String, String) -> Unit,
        showInfoToast: (resId: Int) -> Unit
    ) {
        if (_loading.value == true)
            return

        visited.clear()

        listener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val roomId: String = dataSnapshot.value.toString()
                if (roomId != "") {
                    val userIds = roomId.split("___")
                    for (id in userIds) {
                        if (id != userId.value) {
                            visited.add(id)
                        }
                    }
                    toChat(roomId, userId.value!!)
                }
                else if (visited.isNotEmpty()) {
                    goBack()
                    joinQueue()
                    showInfoToast(R.string.chat_user_disconnected_toast)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e("Firebase", "getChatRoom Error: ${error.message}")
                _loading.value = false
                showInfoToast(R.string.chat_no_people_in_queue_toast)
            }
        }

       db.child("users/${userId.value}/roomId").addValueEventListener(listener)
       joinQueue()
    }
}