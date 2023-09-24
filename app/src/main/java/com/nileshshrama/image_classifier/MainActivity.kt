package com.nileshshrama.image_classifier

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var enter:Button=findViewById(R.id.enterModel)
        var close1:Button=findViewById(R.id.close)


        enter.setOnClickListener {
            startActivity(Intent(this,Prediction_activity::class.java))
        }
        close1.setOnClickListener {
            this.finish()
            System.exit(0)
        }

    }
}