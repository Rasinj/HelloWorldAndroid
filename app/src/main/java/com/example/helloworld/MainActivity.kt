package com.example.helloworld

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create beautiful UI programmatically to avoid resource issues
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 100, 48, 48)
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, android.R.color.white))
        }
        
        // Welcome title
        val titleText = TextView(this).apply {
            text = "Welcome"
            textSize = 32f
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.black))
            setPadding(0, 0, 0, 24)
        }
        
        // Subtitle
        val subtitleText = TextView(this).apply {
            text = "Explore the beautiful world of minimalistic design"
            textSize = 16f
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.darker_gray))
            setPadding(0, 0, 0, 64)
        }
        
        // Profile button
        val profileBtn = createMenuButton("👤 Profile")
        val settingsBtn = createMenuButton("⚙️ Settings")
        val aboutBtn = createMenuButton("ℹ️ About")
        
        layout.addView(titleText)
        layout.addView(subtitleText)
        layout.addView(profileBtn)
        layout.addView(settingsBtn)
        layout.addView(aboutBtn)
        
        setContentView(layout)
    }
    
    private fun createMenuButton(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 18f
            setPadding(32, 24, 32, 24)
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.black))
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, android.R.color.background_light))
            
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 16)
            layoutParams = params
            
            isClickable = true
            isFocusable = true
            
            setOnClickListener {
                // Simple toast for now
                android.widget.Toast.makeText(this@MainActivity, "Clicked: $text", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}