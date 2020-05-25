package com.example.moviedbapi.service

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.moviedbapi.config.Config
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        handleNotification(remoteMessage!!.notification!!.body)
    }

    private fun handleNotification(body: String?) {
        val pushNotification = Intent(Config.STR_PUSH)
        pushNotification.putExtra("message", body)
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
    }
}