package com.example.helloworld

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val sampleNotes = listOf(
        Note(1, "Meeting Notes", "Discuss quarterly goals and team assignments for the upcoming sprint.", "Work", Date(), Date()),
        Note(2, "Book Ideas", "List of book recommendations from Sarah: The Midnight Library, Atomic Habits, Project Hail Mary", "Personal", Date(), Date()),
        Note(3, "Grocery List", "Milk, Bread, Eggs, Tomatoes, Chicken, Rice, Apples", "Tasks", Date(), Date()),
        Note(4, "Study Plan", "Chapter 5: Database Design, Chapter 6: SQL Queries, Practice exercises 1-15", "Study", Date(), Date()),
        Note(5, "Creative Writing", "Story idea: A world where memories can be traded like currency...", "Creative", Date(), Date())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create the main scrollable layout with beautiful note-taking UI
        val scrollView = ScrollView(this).apply {
            setBackgroundColor(Color.parseColor("#FEFBF3")) // Warm cream background
        }
        
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 60, 24, 24)
        }
        
        // App Title
        val appTitle = createStyledText(
            "📝 NoteMaster",
            32f,
            Color.parseColor("#2D2A26"),
            Typeface.DEFAULT_BOLD
        ).apply {
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 8)
        }
        
        // Subtitle
        val subtitle = createStyledText(
            "Your thoughts, beautifully organized",
            16f,
            Color.parseColor("#6B6459"),
            Typeface.DEFAULT
        ).apply {
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 32)
        }
        
        // Quick Actions Section
        val quickActionsTitle = createStyledText(
            "Quick Actions",
            20f,
            Color.parseColor("#2D2A26"),
            Typeface.DEFAULT_BOLD
        ).apply {
            setPadding(0, 0, 0, 16)
        }
        
        val newNoteBtn = createActionButton("✏️ New Note", "#E67E22") {
            showToast("Creating new note...")
        }
        
        val allNotesBtn = createActionButton("📚 All Notes", "#2980B9") {
            showToast("Showing all notes...")
        }
        
        val categoriesBtn = createActionButton("🏷️ Categories", "#8E44AD") {
            showToast("Browse by categories...")
        }
        
        val searchBtn = createActionButton("🔍 Search", "#16A085") {
            showToast("Search notes...")
        }
        
        // Recent Notes Section
        val recentNotesTitle = createStyledText(
            "Recent Notes",
            20f,
            Color.parseColor("#2D2A26"),
            Typeface.DEFAULT_BOLD
        ).apply {
            setPadding(0, 24, 0, 16)
        }
        
        // Add all components to main layout
        mainLayout.addView(appTitle)
        mainLayout.addView(subtitle)
        mainLayout.addView(quickActionsTitle)
        mainLayout.addView(newNoteBtn)
        mainLayout.addView(allNotesBtn)
        mainLayout.addView(categoriesBtn)
        mainLayout.addView(searchBtn)
        mainLayout.addView(recentNotesTitle)
        
        // Add sample notes
        sampleNotes.take(3).forEach { note ->
            mainLayout.addView(createNoteCard(note))
        }
        
        // Categories Overview
        val categoriesTitle = createStyledText(
            "Note Categories",
            20f,
            Color.parseColor("#2D2A26"),
            Typeface.DEFAULT_BOLD
        ).apply {
            setPadding(0, 24, 0, 16)
        }
        
        mainLayout.addView(categoriesTitle)
        
        // Add category buttons
        NoteCategory.getAllCategories().forEach { category ->
            val categoryBtn = createCategoryButton(category)
            mainLayout.addView(categoryBtn)
        }
        
        scrollView.addView(mainLayout)
        setContentView(scrollView)
    }
    
    private fun createStyledText(text: String, size: Float, color: Int, typeface: Typeface): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = size
            setTextColor(color)
            this.typeface = typeface
        }
    }
    
    private fun createActionButton(text: String, colorHex: String, onClick: () -> Unit): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 18f
            setPadding(24, 20, 24, 20)
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor(colorHex))
            gravity = Gravity.CENTER
            
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 12)
            layoutParams = params
            
            isClickable = true
            isFocusable = true
            
            setOnClickListener { onClick() }
        }
    }
    
    private fun createNoteCard(note: Note): LinearLayout {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 16, 20, 16)
            setBackgroundColor(Color.WHITE)
            
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 12)
            layoutParams = params
        }
        
        // Note header with category
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }
        
        val categoryLabel = TextView(this).apply {
            text = "${note.getCategoryEmoji()} ${note.category}"
            textSize = 12f
            setTextColor(Color.parseColor(note.getCategoryColor()))
            setTypeface(null, Typeface.BOLD)
        }
        
        header.addView(categoryLabel)
        
        // Note title
        val title = TextView(this).apply {
            text = note.getDisplayTitle()
            textSize = 16f
            setTextColor(Color.parseColor("#2D2A26"))
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 8, 0, 4)
        }
        
        // Note preview
        val preview = TextView(this).apply {
            text = note.getPreviewContent()
            textSize = 14f
            setTextColor(Color.parseColor("#6B6459"))
            setPadding(0, 0, 0, 8)
        }
        
        card.addView(header)
        card.addView(title)
        card.addView(preview)
        
        card.setOnClickListener {
            showToast("Opening note: ${note.getDisplayTitle()}")
        }
        
        return card
    }
    
    private fun createCategoryButton(category: NoteCategory): TextView {
        return TextView(this).apply {
            text = "${category.emoji} ${category.displayName}"
            textSize = 16f
            setPadding(20, 16, 20, 16)
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor(category.color))
            gravity = Gravity.CENTER_VERTICAL
            
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 8)
            layoutParams = params
            
            isClickable = true
            isFocusable = true
            
            setOnClickListener {
                showToast("Browsing ${category.displayName} notes...")
            }
        }
    }
    
    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}