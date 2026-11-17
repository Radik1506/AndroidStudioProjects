package com.example.hello_world

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selector)

        findViewById<Button>(R.id.bPick3).setOnClickListener {
            startActivity(Intent(this, MainActivity_3::class.java))
        }
        findViewById<Button>(R.id.bPick6).setOnClickListener {
            startActivity(Intent(this, MainActivity_6::class.java))
        }
    }
}