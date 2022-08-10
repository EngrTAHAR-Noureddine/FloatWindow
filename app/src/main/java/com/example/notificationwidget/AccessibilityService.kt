package com.example.notificationwidget


import android.accessibilityservice.AccessibilityGestureEvent
import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Build
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import androidx.annotation.RequiresApi

/*
@SuppressLint("StaticFieldLeak")
object customWindow {
    var context : Context? = null
    var mLayout: FrameLayout? = null
    var windowManager : WindowManager? = null
    var lp : WindowManager.LayoutParams? = null



    @SuppressLint("ClickableViewAccessibility")
    fun createWindow(ctxt: Context){
        context = ctxt
        windowManager = context!!.getSystemService(AccessibilityService.WINDOW_SERVICE) as WindowManager
        mLayout = FrameLayout(context!!)
        lp = WindowManager.LayoutParams()

        lp!!.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        lp!!.format = PixelFormat.TRANSLUCENT
        lp!!.flags = lp!!.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        lp!!.width = WindowManager.LayoutParams.MATCH_PARENT
        lp!!.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp!!.gravity = Gravity.TOP

        val inflater = LayoutInflater.from(this)
        inflater.inflate(R.layout.floating_layout, mLayout)
        windowManager!!.addView(mLayout, lp)


        mLayout?.setOnTouchListener( object : View.OnTouchListener {

            var x = 0.0
            var y = 0.0
            var px = 0.0
            var py = 0.0

            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {

                when(event!!.action){
                    MotionEvent.ACTION_DOWN ->{
                        x = lp!!.x.toDouble()
                        y = lp!!.y.toDouble()

                        px = event.rawX.toDouble()
                        py = event.rawY.toDouble()
                    }

                    MotionEvent.ACTION_MOVE -> {
                        lp!!.x = (x + event.rawX - px).toInt()
                        lp!!.y = (y + event.rawY - py).toInt()

                        windowManager!!.updateViewLayout(mLayout, lp)

                    }

                }
                return false

            }
        })
    }

    showWindow(){

    }

}

*/


class AccessibilityService : AccessibilityService() {
    var mLayout: FrameLayout? = null
    private lateinit var windowManager : WindowManager
    private lateinit var lp : WindowManager.LayoutParams

    private lateinit var receiver: BroadcastReceiver



    @RequiresApi(Build.VERSION_CODES.R)
    override fun onSystemActionsChanged() {
        super.onSystemActionsChanged()
       //disableSelf()
        /*
             if(systemActions.size != 0){
                 for (c in systemActions) println("C : ${c.label} ${c.id}")
             }



             val keyguard = this.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
             if (keyguard.isKeyguardLocked) {
                 println("hi on system action is locked")
             }else{
                 println("is not locked on system")
                 onDestroy()
             }
             */


    }


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {


              if(event != null){

                  println("event type : ${event.eventType}")
                  println("package name : ${event.packageName}")
                  println("class name : ${event.className}")
                  println("text event : ${event.text}")
              }


    }




    override fun onInterrupt() {
        println("hi")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()


        receiver = MyBroadcastReceiver()
        val filter = IntentFilter(Intent.ACTION_USER_PRESENT)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_SCREEN_ON)
        registerReceiver(receiver, filter)
        println("register on create")


    }


    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }






    @SuppressLint("ClickableViewAccessibility")
    override fun onServiceConnected() {
        println("onServiceConnected")

        val keyguard = this.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mLayout = FrameLayout(this)
        lp = WindowManager.LayoutParams()





        if (keyguard.isKeyguardLocked) {

            lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            lp.format = PixelFormat.TRANSLUCENT
            lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.TOP

            val inflater = LayoutInflater.from(this)
            inflater.inflate(R.layout.floating_layout, mLayout)
            windowManager.addView(mLayout, lp)


            mLayout?.setOnTouchListener( object : View.OnTouchListener {

                var x = 0.0
                var y = 0.0
                var px = 0.0
                var py = 0.0

                override fun onTouch(p0: View?, event: MotionEvent?): Boolean {

                    when(event!!.action){
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

        } else {
            println("is not locked")
            onDestroy()

        }


    }




}