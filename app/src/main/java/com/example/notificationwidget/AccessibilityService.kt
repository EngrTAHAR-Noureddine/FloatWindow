package com.example.notificationwidget

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi


class AccessibilityService : AccessibilityService() {
    var name : String = ""


    //@RequiresApi(Build.VERSION_CODES.P)
    // used this fun to get events from all phone ( facebook , click...etc , anything )
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        println("Im here")

        if (event != null) {
            println("event")
            println(event.eventType)
            println("packageName")
            println(event.packageName)
            println("action")
            println(event.action)
            println("contentChangeTypes")
            println(event.contentChangeTypes)
            //print("windowChanges")
            //print(event.windowChanges)
            print("-------------------")

            if (event.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {

                print(event.packageName.toString())

                if (event.packageName.toString() == "com.whatsapp") {
                    val message = StringBuilder()
                    if (event.text.isNotEmpty()) {
                        for (subText in event.text) {
                            message.append(subText)
                        }
                        if (message.toString().contains("Message from")) {
                            name = message.toString().substring(13)
                        }
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    override fun onServiceConnected() {
        println("onServiceConnected")
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        info.notificationTimeout = 100
        info.packageNames = null
        serviceInfo = info
    }


}