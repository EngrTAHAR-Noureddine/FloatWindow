package com.example.notificationwidget

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.Button


class FloatingWindowApp : Service() {

    private lateinit var floatView: ViewGroup
    private lateinit var floatWindowParams: WindowManager.LayoutParams
    private var LAYOU_TYPE:Int? = null
    private lateinit var windowManager: WindowManager
    //private  lateinit var window: Window

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val metrics = applicationContext.resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels


        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val inflater = baseContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        floatView = inflater.inflate(R.layout.floating_layout, null) as ViewGroup

        var btn = floatView.findViewById<View>(R.id.button) as Button


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            LAYOU_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }else{
            LAYOU_TYPE = WindowManager.LayoutParams.TYPE_CHANGED
        }

        floatWindowParams = WindowManager.LayoutParams(
            (width*0.55f).toInt(),
            (height*0.55f).toInt(),
            LAYOU_TYPE!!,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT,

            )



        floatWindowParams.gravity = Gravity.CENTER

        floatWindowParams.x = 0
        floatWindowParams.y = 0

        windowManager.addView(floatView, floatWindowParams)

        //window.setWindowManager(windowManager, null, null)

        //window.addContentView(floatView, floatWindowParams)



        btn.setOnClickListener {
            stopSelf()

            windowManager.removeView(floatView)

            val back = Intent(this , MainActivity::class.java)
            back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(back)
        }

        floatView.setOnTouchListener( object : View.OnTouchListener {

            val updateFloatWindowLayoutParams = floatWindowParams
            var x = 0.0
            var y = 0.0
            var px = 0.0
            var py = 0.0

            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {

                when(event!!.action){
                    MotionEvent.ACTION_DOWN ->{
                        x = updateFloatWindowLayoutParams.x.toDouble()
                        y = updateFloatWindowLayoutParams.y.toDouble()

                        px = event.rawX.toDouble()
                        py = event.rawY.toDouble()
                    }

                    MotionEvent.ACTION_MOVE ->{
                        updateFloatWindowLayoutParams.x = (x + event.rawX - px).toInt()
                        updateFloatWindowLayoutParams.y  = (y + event.rawY - py).toInt()

                        windowManager.updateViewLayout(floatView, updateFloatWindowLayoutParams)

                    }

                }
                return false

            }


        })
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        windowManager.removeView(floatView)
    }
}