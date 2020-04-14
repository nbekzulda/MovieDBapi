package com.example.moviedbapi.main

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.moviedbapi.R
import com.example.moviedbapi.config.Config
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    var mRegistrationBroadcastReceiver: BroadcastReceiver? = null
    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this!!.mRegistrationBroadcastReceiver!!)

        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(this!!.mRegistrationBroadcastReceiver!!, IntentFilter("registrationComplete"))
        LocalBroadcastManager.getInstance(this).registerReceiver(this!!.mRegistrationBroadcastReceiver!!, IntentFilter(
            Config.STR_PUSH))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRegistrationBroadcastReceiver = object: BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent!!.action == Config.STR_PUSH){
                    val message = intent!!.getStringExtra("message")
                    showNotification("MovieDBKotlin", message)
                }
            }
        }

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.nav_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movieListFragment,
                R.id.favoriteFragment,
                R.id.profileFragment,
                R.id.cinemaFragment,
                R.id.movieDetailsFragment,
                R.id.cinemaDetailsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun showNotification(s: String, message: String?) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(applicationContext)
        builder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setContentText(contentIntent.toString())


        val notificationManager = baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(1, builder.build())



    }
}
