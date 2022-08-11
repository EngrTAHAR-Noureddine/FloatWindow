package com.example.notificationwidget

import android.accessibilityservice.AccessibilityGestureEvent
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.*
import android.graphics.PixelFormat
import android.os.Build
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import androidx.annotation.RequiresApi


class AccessibilityService : AccessibilityService() {
    var mLayout: FrameLayout? = null
    private lateinit var windowManager : WindowManager
    private lateinit var lp : WindowManager.LayoutParams
    private lateinit var screenOnOffReceiver: BroadcastReceiver


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        println("hi im here")
        if(event!=null) println("event action is : ${event.action}")
    }


    override fun onInterrupt() {}

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()
        println("THE RECEIVER ------ ")
       // registerBroadcastReceiver()

    }

    override fun onDestroy() {
        println("BEFORE UNREGISTER RECEIVER")
       // applicationContext.unregisterReceiver(screenOnOffReceiver)
        println("THIS IS DESTROY")
        super.onDestroy()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createWindow(){
        println("add window")
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mLayout = FrameLayout(this)
        lp = WindowManager.LayoutParams()
        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        lp.format = PixelFormat.TRANSLUCENT
        lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER

        val inflater = LayoutInflater.from(this)
        inflater.inflate(R.layout.floating_layout, mLayout)

        windowManager.addView(mLayout, lp)


        mLayout?.setOnTouchListener( object : View.OnTouchListener {
            var x = 0.0
            var y = 0.0
            var px = 0.0
            var py = 0.0
            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {

                when(event?.action){
                    MotionEvent.ACTION_DOWN ->{
                        x = lp.x.toDouble()
                        y = lp.y.toDouble()

                        px = event.rawX.toDouble()
                        py = event.rawY.toDouble()
                    }

                    MotionEvent.ACTION_MOVE -> {
                        lp.x = (x + event.rawX - px).toInt()
                        lp.y = (y + event.rawY - py).toInt()

                        windowManager.updateViewLayout(mLayout, lp)

                    }

                }
                return false

            }

        })
    }

    private fun removeWindow(){
        println("remove window")
        if(mLayout?.windowToken != null){
            windowManager.removeView(mLayout)
            mLayout = null
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility")
    override fun onServiceConnected() {
        println("onServiceConnected")
        val info : AccessibilityServiceInfo = this.serviceInfo
        info.apply {
            eventTypes = AccessibilityEvent.TYPE_GESTURE_DETECTION_END or AccessibilityEvent.TYPE_GESTURE_DETECTION_START
        }

        this.serviceInfo = info
        // -----------------------------------------------------------------------------------------------------
        createWindowOnLockScreen()

    }

    private fun createWindowOnLockScreen(){
        val keyguard = this.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (keyguard.isKeyguardLocked) {
           // if(mLayout == null) {
                createWindow()
           // }
        } else {
            println("is not locked")
            onDestroy()
        }
    }
/*
    override fun onSystemActionsChanged() {
        super.onSystemActionsChanged()
        println("system action change")
        createWindowOnLockScreen()
    }

    private fun registerBroadcastReceiver() {
        val theFilter = IntentFilter()
        /** System Defined Broadcast  */
        theFilter.addAction(Intent.ACTION_SCREEN_ON)
        theFilter.addAction(Intent.ACTION_SCREEN_OFF)
        theFilter.addAction(Intent.ACTION_USER_PRESENT)
        theFilter.addAction(Intent.FLAG_ACTIVITY_NO_USER_ACTION.toString())


       screenOnOffReceiver = object : BroadcastReceiver() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onReceive(context: Context, intent: Intent) {
                val strAction = intent.action
                println("Action is : $strAction")
                val myKM = context.getSystemService(KEYGUARD_SERVICE) as KeyguardManager

                if(strAction == Intent.ACTION_SCREEN_OFF){
                    removeWindow()
                }else if (strAction == Intent.ACTION_USER_PRESENT || strAction == Intent.ACTION_SCREEN_ON) if (myKM.isDeviceLocked) {
                    println("Screen off " + "LOCKED")
                    if(mLayout == null) {
                        createWindow()
                    }
                } else {
                    println("Screen off " + "UNLOCKED")
                    removeWindow()
                }
            }
        }
        applicationContext.registerReceiver(screenOnOffReceiver, theFilter)
    }
*/
}
// disableSelf()