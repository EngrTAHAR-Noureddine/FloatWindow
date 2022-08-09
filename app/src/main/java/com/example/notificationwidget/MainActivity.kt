package com.example.notificationwidget

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var i:Int = 0
    private lateinit var dialog: AlertDialog

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isServiceRunning()) {
            // onDestroy() method in FloatingWindowGFG
            // class will be called here
            stopService(Intent(this@MainActivity, FloatingWindowApp::class.java))
        }


    }


    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        val text :TextView = findViewById(R.id.text_hello)

        text.setOnClickListener {
            text.text = "Hi number : $i"
            i++

            // First it confirms whether the
            // 'Display over other apps' permission in given
            // First it confirms whether the
            // 'Display over other apps' permission in given
            if (checkOverlayPermission()) {
                // FloatingWindowGFG service is started
                startService(Intent(this@MainActivity, FloatingWindowApp::class.java))
                // The MainActivity closes here
                finish()
            } else {
                // If permission is not given,
                // it shows the AlertDialog box and
                // redirects to the Settings
                requestFloatingWindowPermission()
            }
        }
    }

    private fun isServiceRunning():Boolean{
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for(service in manager.getRunningServices(Int.MAX_VALUE)){
            if(FloatingWindowApp::class.java.name == service.service.className){
                return true
            }
        }

        return false

    }

    private fun requestFloatingWindowPermission(){
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(true)
            builder.setTitle("Screen Overlay Permission Needed")
            builder.setMessage("Enable 'Display over the App' from settings")
            builder.setPositiveButton("Open Settings", DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, RESULT_OK)

            })
            dialog = builder.create()
            dialog.show()
    }

    private fun checkOverlayPermission():Boolean{

        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            // If 'Display over other apps' is not enabled it
            // will return false or else true
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }


}