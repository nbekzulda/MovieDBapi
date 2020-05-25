package com.example.moviedbapi.service

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


class MyFirebaseIdService : FirebaseInstanceIdService(){
    override fun onTokenRefresh() {
        super.onTokenRefresh()

        val refreshedToken = FirebaseInstanceId.getInstance().token
        sendRegistrationToServer(refreshedToken!!)
    }

    private fun sendRegistrationToServer(refreshedToken: String) {
        Log.d("DEBUG", refreshedToken)
    }
}