package com.example.trafficmanagementsystem

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateData()

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.statusBarColor = ContextCompat.getColor(this , R.color.purple_200)
    }

    private fun updateData() {
        FirebaseDatabase.getInstance().reference.child("server").child("traffic-data").child("roadData")
            .child("currentStatus").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var data = snapshot.value.toString()
                        tv_best.text = "Best Route is $data"
                        if(data == "A"){
                            Log.d("Main Activity" , "Inside A")
                            iv_direction.setImageResource(R.drawable.ic_left)
                            iv_best_route.setImageResource(R.drawable.route_1)
                        }
                        else if (data == "B"){
                            Log.d("Main Activity" , "Inside B")
                            iv_direction.setImageResource(R.drawable.ic_up)
                            iv_best_route.setImageResource(R.drawable.route_2)
                        }
                        else if (data == "C"){
                            Log.d("Main Activity" , "Inside C")
                            iv_direction.setImageResource(R.drawable.ic_right)
                            iv_best_route.setImageResource(R.drawable.route_3)
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        FirebaseDatabase.getInstance().reference.child("server").child("traffic-data").child("roadData")
            .child("carsFeed1").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var data = snapshot.value.toString()
                        tv_car1.text = data
                        Handler(Looper.getMainLooper()).postDelayed({
                            tv_car1.setTextColor(Color.parseColor("#BBBBBB"))
                        }, 2000)
                        tv_car1.setTextColor(Color.parseColor("#00C845"))

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        FirebaseDatabase.getInstance().reference.child("server").child("traffic-data").child("roadData")
            .child("carsFeed2").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var data = snapshot.value.toString()
                        tv_car2.text = data
                        Handler(Looper.getMainLooper()).postDelayed({
                            tv_car2.setTextColor(Color.parseColor("#BBBBBB"))
                        }, 2000)
                        tv_car2.setTextColor(Color.parseColor("#00C845"))

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        FirebaseDatabase.getInstance().reference.child("server").child("traffic-data").child("roadData")
            .child("carsFeed3").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var data = snapshot.value.toString()
                        tv_car3.text = data
                        Handler(Looper.getMainLooper()).postDelayed({
                            tv_car3.setTextColor(Color.parseColor("#BBBBBB"))
                        }, 2000)
                        tv_car3.setTextColor(Color.parseColor("#00C845"))

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    public override fun onDestroy() {
        // Shutdown TTS

        super.onDestroy()
    }
}