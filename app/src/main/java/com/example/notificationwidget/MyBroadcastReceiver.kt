package com.example.notificationwidget

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import androidx.annotation.RequiresApi


class MyBroadcastReceiver : BroadcastReceiver() {



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onReceive(p0: Context?, p1: Intent?) {
        val action: String? = p1?.action
        val manager = p0?.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

        println("manager is enabled ${manager.isEnabled}")


        //p0.stopService(Intent(p0, AccessibilityService::class.java))


        println("manager ${manager.javaClass}")

        if (manager.isEnabled) {
            val e = AccessibilityEvent.obtain()
            e.eventType = AccessibilityEvent.TYPE_VIEW_CLICKED
            e.className = "android.widget.FrameLayout"

            println("class name of manager: ${e.className}")

            e.packageName = p0.packageName
            e.text.add("some text")
            manager.sendAccessibilityEvent(e)
        }


        /*
        if(action!=null)
            if (Intent.ACTION_SCREEN_OFF == action) {
                print("hi the in unlocked screen")
                if(p0!=null){
                    println("is running : ${isMyServiceRunning(AccessibilityService::class.java , p0)}")

                    if(isMyServiceRunning(AccessibilityService::class.java , p0)){

                        println("is running after stop ?  : ${isMyServiceRunning(AccessibilityService::class.java , p0)}")
                    }else{
                        println("is running after stop ! ")
                    }
                }


            }
        */
    }


    private fun isMyServiceRunning(serviceClass: Class<*> , context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                println("service id : ${service.pid}")
                android.os.Process.killProcess(service.pid)
                return true
            }
        }
        return false
    }
}


/*
val manager =
                    p0?.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager


                println("manager ${manager.javaClass}")

                if (manager.isEnabled) {
                    val e = AccessibilityEvent.obtain()
                    e.eventType = AccessibilityEvent.TYPE_VIEW_CLICKED
                    e.className = "android.widget.FrameLayout"

                    println("class name of manager: ${e.className}")

                    e.packageName = p0.packageName
                    e.text.add("some text")
                    manager.sendAccessibilityEvent(e)
                }
* */

/*
Settings.Secure.putString(p0?.contentResolver,
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, "${p0?.packageName}/AccessibilityService");
                Settings.Secure.putString(p0?.contentResolver,
                    Settings.Secure.ACCESSIBILITY_ENABLED, "1");
* */