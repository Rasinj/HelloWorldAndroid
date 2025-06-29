package com.example.helloworld

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Simple fallback for testing
        val textView = TextView(this)
        textView.text = "Hello, Beautiful World!"
        textView.textSize = 24f
        textView.setPadding(64, 64, 64, 64)
        setContentView(textView)
    }
}