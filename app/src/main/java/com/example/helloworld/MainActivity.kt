package com.example.helloworld

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val allNotes = mutableListOf(
        Note(1, "Meeting Notes", "Discuss quarterly goals and team assignments for the upcoming sprint.", "Work", Date(), Date()),
        Note(2, "Book Ideas", "List of book recommendations from Sarah: The Midnight Library, Atomic Habits, Project Hail Mary", "Personal", Date(), Date()),
        Note(3, "Grocery List", "Milk, Bread, Eggs, Tomatoes, Chicken, Rice, Apples", "Tasks", Date(), Date()),
        Note(4, "Study Plan", "Chapter 5: Database Design, Chapter 6: SQL Queries, Practice exercises 1-15", "Study", Date(), Date()),
        Note(5, "Creative Writing", "Story idea: A world where memories can be traded like currency...", "Creative", Date(), Date())
    )
    
    private var currentFilter: String? = null
    private var searchQuery: String = ""
    private var nextNoteId: Long = 6L
    private lateinit var mainLayout: LinearLayout
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create the main scrollable layout with beautiful note-taking UI
        scrollView = ScrollView(this).apply {
            setBackgroundColor(Color.parseColor("#FEFBF3")) // Warm cream background
        }
        
        mainLayout = LinearLayout(this).apply {
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
            showCreateNoteDialog()
        }
        
        val allNotesBtn = createActionButton("📚 All Notes", "#2980B9") {
            showAllNotes()
        }
        
        val categoriesBtn = createActionButton("🏷️ Categories", "#8E44AD") {
            showCategoriesDialog()
        }
        
        val searchBtn = createActionButton("🔍 Search", "#16A085") {
            showSearchDialog()
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
        
        // Add recent notes
        refreshNotesList()
        
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
            val categoryBtn = createCategoryButton(category) {
                filterNotesByCategory(category.displayName)
            }
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
            showNoteDetailsDialog(note)
        }
        
        return card
    }
    
    private fun createCategoryButton(category: NoteCategory, onClick: () -> Unit): TextView {
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
            
            setOnClickListener { onClick() }
        }
    }
    
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    private fun showStyledToast(message: String, color: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    private fun createStyledButton(text: String, colorHex: String, onClick: () -> Unit): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 14f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor(colorHex))
            setPadding(20, 12, 20, 12)
            gravity = Gravity.CENTER
            setTypeface(null, Typeface.BOLD)
            isClickable = true
            isFocusable = true
            
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            layoutParams = params
            
            setOnClickListener { onClick() }
        }
    }
    
    private fun showCreateNoteDialog() {
        val dialogLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 32, 40, 32)
            setBackgroundColor(Color.parseColor("#FEFBF3"))
        }
        
        // Dialog title
        val dialogTitle = TextView(this).apply {
            text = "✏️ Create New Note"
            textSize = 24f
            setTextColor(Color.parseColor("#2D2A26"))
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 24)
        }
        
        // Title input with label
        val titleLabel = TextView(this).apply {
            text = "Title (Optional)"
            textSize = 14f
            setTextColor(Color.parseColor("#6B6459"))
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 0, 0, 8)
        }
        
        val titleInput = EditText(this).apply {
            hint = "Enter a title for your note..."
            textSize = 16f
            setPadding(20, 16, 20, 16)
            setBackgroundColor(Color.WHITE)
            setTextColor(Color.parseColor("#2D2A26"))
            setHintTextColor(Color.parseColor("#999999"))
        }
        
        // Content input with label
        val contentLabel = TextView(this).apply {
            text = "Content *"
            textSize = 14f
            setTextColor(Color.parseColor("#6B6459"))
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 16, 0, 8)
        }
        
        val contentInput = EditText(this).apply {
            hint = "What's on your mind?"
            textSize = 16f
            setPadding(20, 16, 20, 16)
            setBackgroundColor(Color.WHITE)
            setTextColor(Color.parseColor("#2D2A26"))
            setHintTextColor(Color.parseColor("#999999"))
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            minLines = 4
            maxLines = 8
        }
        
        // Category input with label
        val categoryLabel = TextView(this).apply {
            text = "Category"
            textSize = 14f
            setTextColor(Color.parseColor("#6B6459"))
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 16, 0, 8)
        }
        
        val categoryInput = EditText(this).apply {
            hint = "Personal, Work, Study, Creative, Ideas, Tasks"
            textSize = 16f
            setPadding(20, 16, 20, 16)
            setBackgroundColor(Color.WHITE)
            setTextColor(Color.parseColor("#2D2A26"))
            setHintTextColor(Color.parseColor("#999999"))
        }
        
        dialogLayout.addView(dialogTitle)
        dialogLayout.addView(titleLabel)
        dialogLayout.addView(titleInput)
        dialogLayout.addView(contentLabel)
        dialogLayout.addView(contentInput)
        dialogLayout.addView(categoryLabel)
        dialogLayout.addView(categoryInput)
        
        val dialog = AlertDialog.Builder(this)
            .setView(dialogLayout)
            .setCancelable(true)
            .create()
        
        // Custom buttons layout
        val buttonLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 24, 0, 0)
            gravity = Gravity.END
        }
        
        val cancelButton = TextView(this).apply {
            text = "Cancel"
            textSize = 16f
            setTextColor(Color.parseColor("#6B6459"))
            setPadding(24, 16, 24, 16)
            gravity = Gravity.CENTER
            isClickable = true
            isFocusable = true
            setOnClickListener { dialog.dismiss() }
        }
        
        val saveButton = TextView(this).apply {
            text = "💾 Save Note"
            textSize = 16f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#E67E22"))
            setPadding(32, 16, 32, 16)
            gravity = Gravity.CENTER
            setTypeface(null, Typeface.BOLD)
            isClickable = true
            isFocusable = true
            
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(16, 0, 0, 0)
            layoutParams = params
            
            setOnClickListener {
                val title = titleInput.text.toString().trim()
                val content = contentInput.text.toString().trim()
                val category = categoryInput.text.toString().trim().ifEmpty { "Personal" }
                
                if (content.isNotEmpty()) {
                    val newNote = Note(
                        id = nextNoteId++,
                        title = title,
                        content = content,
                        category = category,
                        createdAt = Date(),
                        updatedAt = Date()
                    )
                    allNotes.add(0, newNote)
                    refreshNotesList()
                    showStyledToast("✅ Note created successfully!", "#27AE60")
                    dialog.dismiss()
                } else {
                    showStyledToast("⚠️ Please enter some content for your note", "#E74C3C")
                }
            }
        }
        
        buttonLayout.addView(cancelButton)
        buttonLayout.addView(saveButton)
        dialogLayout.addView(buttonLayout)
        
        dialog.show()
    }
    
    private fun showNoteDetailsDialog(note: Note) {
        val dialogLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 32, 40, 32)
            setBackgroundColor(Color.parseColor("#FEFBF3"))
        }
        
        // Note header with category badge
        val headerLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, 0, 0, 20)
        }
        
        val categoryBadge = TextView(this).apply {
            text = "${note.getCategoryEmoji()} ${note.category}"
            textSize = 12f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor(note.getCategoryColor()))
            setPadding(12, 6, 12, 6)
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        }
        
        val dateView = TextView(this).apply {
            text = "📅 ${android.text.format.DateFormat.format("MMM dd, yyyy", note.createdAt)}"
            textSize = 12f
            setTextColor(Color.parseColor("#999999"))
            setPadding(16, 0, 0, 0)
        }
        
        headerLayout.addView(categoryBadge)
        headerLayout.addView(dateView)
        
        val titleView = TextView(this).apply {
            text = note.getDisplayTitle()
            textSize = 22f
            setTextColor(Color.parseColor("#2D2A26"))
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 0, 0, 16)
            gravity = Gravity.START
        }
        
        val contentScrollView = ScrollView(this).apply {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                300 // Fixed height for scrollable content
            )
            layoutParams = params
        }
        
        val contentView = TextView(this).apply {
            text = note.content
            textSize = 16f
            setTextColor(Color.parseColor("#4A4A4A"))
            setPadding(20, 16, 20, 16)
            setBackgroundColor(Color.WHITE)
            setLineSpacing(4f, 1.2f)
        }
        
        contentScrollView.addView(contentView)
        
        dialogLayout.addView(headerLayout)
        dialogLayout.addView(titleView)
        dialogLayout.addView(contentScrollView)
        
        val dialog = AlertDialog.Builder(this)
            .setView(dialogLayout)
            .setCancelable(true)
            .create()
        
        // Custom action buttons
        val actionLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 24, 0, 0)
            gravity = Gravity.CENTER
        }
        
        val editButton = createStyledButton("✏️ Edit", "#3498DB") {
            dialog.dismiss()
            showEditNoteDialog(note)
        }
        
        val deleteButton = createStyledButton("🗑️ Delete", "#E74C3C") {
            dialog.dismiss()
            deleteNote(note)
        }
        
        val closeButton = createStyledButton("Close", "#95A5A6") {
            dialog.dismiss()
        }
        
        actionLayout.addView(editButton)
        actionLayout.addView(deleteButton)
        actionLayout.addView(closeButton)
        dialogLayout.addView(actionLayout)
        
        dialog.show()
    }
    
    private fun showEditNoteDialog(note: Note) {
        val dialogLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 32, 40, 32)
            setBackgroundColor(Color.parseColor("#FEFBF3"))
        }
        
        // Dialog title with emoji
        val dialogTitle = TextView(this).apply {
            text = "✏️ Edit Note"
            textSize = 24f
            setTextColor(Color.parseColor("#2D2A26"))
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 24)
        }
        
        // Title input with enhanced styling
        val titleLabel = TextView(this).apply {
            text = "Title (Optional)"
            textSize = 14f
            setTextColor(Color.parseColor("#6B6459"))
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 0, 0, 8)
        }
        
        val titleInput = EditText(this).apply {
            setText(note.title)
            hint = "Enter a title for your note..."
            textSize = 16f
            setPadding(20, 16, 20, 16)
            setBackgroundColor(Color.WHITE)
            setTextColor(Color.parseColor("#2D2A26"))
            setHintTextColor(Color.parseColor("#999999"))
        }
        
        // Content input with enhanced styling
        val contentLabel = TextView(this).apply {
            text = "Content *"
            textSize = 14f
            setTextColor(Color.parseColor("#6B6459"))
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 16, 0, 8)
        }
        
        val contentInput = EditText(this).apply {
            setText(note.content)
            hint = "What's on your mind?"
            textSize = 16f
            setPadding(20, 16, 20, 16)
            setBackgroundColor(Color.WHITE)
            setTextColor(Color.parseColor("#2D2A26"))
            setHintTextColor(Color.parseColor("#999999"))
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            minLines = 4
            maxLines = 8
        }
        
        // Category input with enhanced styling
        val categoryLabel = TextView(this).apply {
            text = "Category"
            textSize = 14f
            setTextColor(Color.parseColor("#6B6459"))
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 16, 0, 8)
        }
        
        val categoryInput = EditText(this).apply {
            setText(note.category)
            hint = "Personal, Work, Study, Creative, Ideas, Tasks"
            textSize = 16f
            setPadding(20, 16, 20, 16)
            setBackgroundColor(Color.WHITE)
            setTextColor(Color.parseColor("#2D2A26"))
            setHintTextColor(Color.parseColor("#999999"))
        }
        
        dialogLayout.addView(dialogTitle)
        dialogLayout.addView(titleLabel)
        dialogLayout.addView(titleInput)
        dialogLayout.addView(contentLabel)
        dialogLayout.addView(contentInput)
        dialogLayout.addView(categoryLabel)
        dialogLayout.addView(categoryInput)
        
        val dialog = AlertDialog.Builder(this)
            .setView(dialogLayout)
            .setCancelable(true)
            .create()
        
        // Custom buttons layout
        val buttonLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 24, 0, 0)
            gravity = Gravity.END
        }
        
        val cancelButton = TextView(this).apply {
            text = "Cancel"
            textSize = 16f
            setTextColor(Color.parseColor("#6B6459"))
            setPadding(24, 16, 24, 16)
            gravity = Gravity.CENTER
            isClickable = true
            isFocusable = true
            setOnClickListener { dialog.dismiss() }
        }
        
        val updateButton = TextView(this).apply {
            text = "🔄 Update Note"
            textSize = 16f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#3498DB"))
            setPadding(32, 16, 32, 16)
            gravity = Gravity.CENTER
            setTypeface(null, Typeface.BOLD)
            isClickable = true
            isFocusable = true
            
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(16, 0, 0, 0)
            layoutParams = params
            
            setOnClickListener {
                val title = titleInput.text.toString().trim()
                val content = contentInput.text.toString().trim()
                val category = categoryInput.text.toString().trim().ifEmpty { "Personal" }
                
                if (content.isNotEmpty()) {
                    val updatedNote = note.copy(
                        title = title,
                        content = content,
                        category = category,
                        updatedAt = Date()
                    )
                    val index = allNotes.indexOfFirst { it.id == note.id }
                    if (index != -1) {
                        allNotes[index] = updatedNote
                        refreshNotesList()
                        showStyledToast("✅ Note updated successfully!", "#27AE60")
                        dialog.dismiss()
                    }
                } else {
                    showStyledToast("⚠️ Please enter some content for your note", "#E74C3C")
                }
            }
        }
        
        buttonLayout.addView(cancelButton)
        buttonLayout.addView(updateButton)
        dialogLayout.addView(buttonLayout)
        
        dialog.show()
    }
    
    private fun deleteNote(note: Note) {
        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Delete") { _, _ ->
                allNotes.removeAll { it.id == note.id }
                refreshNotesList()
                showToast("Note deleted successfully!")
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showAllNotes() {
        currentFilter = null
        searchQuery = ""
        refreshNotesList()
        showToast("Showing all notes")
    }
    
    private fun filterNotesByCategory(category: String) {
        currentFilter = category
        searchQuery = ""
        refreshNotesList()
        showToast("Showing $category notes")
    }
    
    private fun showCategoriesDialog() {
        val categories = NoteCategory.getAllCategories().map { "${it.emoji} ${it.displayName}" }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Select Category")
            .setItems(categories) { _, which ->
                val selectedCategory = NoteCategory.getAllCategories()[which]
                filterNotesByCategory(selectedCategory.displayName)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showSearchDialog() {
        val dialogLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 32, 40, 32)
            setBackgroundColor(Color.parseColor("#FEFBF3"))
        }
        
        // Dialog title with search emoji
        val dialogTitle = TextView(this).apply {
            text = "🔍 Search Notes"
            textSize = 24f
            setTextColor(Color.parseColor("#2D2A26"))
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 24)
        }
        
        val searchLabel = TextView(this).apply {
            text = "Search in titles and content"
            textSize = 14f
            setTextColor(Color.parseColor("#6B6459"))
            setPadding(0, 0, 0, 8)
        }
        
        val searchInput = EditText(this).apply {
            hint = "Enter keywords to search..."
            textSize = 16f
            setPadding(20, 16, 20, 16)
            setBackgroundColor(Color.WHITE)
            setTextColor(Color.parseColor("#2D2A26"))
            setHintTextColor(Color.parseColor("#999999"))
        }
        
        dialogLayout.addView(dialogTitle)
        dialogLayout.addView(searchLabel)
        dialogLayout.addView(searchInput)
        
        val dialog = AlertDialog.Builder(this)
            .setView(dialogLayout)
            .setCancelable(true)
            .create()
        
        // Custom buttons layout
        val buttonLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 24, 0, 0)
            gravity = Gravity.END
        }
        
        val clearButton = TextView(this).apply {
            text = "🆑 Clear"
            textSize = 16f
            setTextColor(Color.parseColor("#6B6459"))
            setPadding(24, 16, 24, 16)
            gravity = Gravity.CENTER
            isClickable = true
            isFocusable = true
            setOnClickListener {
                searchQuery = ""
                currentFilter = null
                refreshNotesList()
                showStyledToast("📝 Showing all notes", "#3498DB")
                dialog.dismiss()
            }
        }
        
        val searchButton = TextView(this).apply {
            text = "🔍 Search"
            textSize = 16f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#16A085"))
            setPadding(32, 16, 32, 16)
            gravity = Gravity.CENTER
            setTypeface(null, Typeface.BOLD)
            isClickable = true
            isFocusable = true
            
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(16, 0, 0, 0)
            layoutParams = params
            
            setOnClickListener {
                searchQuery = searchInput.text.toString().trim()
                currentFilter = null
                refreshNotesList()
                val message = if (searchQuery.isEmpty()) "📝 Showing all notes" else "🔍 Searching for: $searchQuery"
                showStyledToast(message, "#16A085")
                dialog.dismiss()
            }
        }
        
        buttonLayout.addView(clearButton)
        buttonLayout.addView(searchButton)
        dialogLayout.addView(buttonLayout)
        
        dialog.show()
    }
    
    private fun refreshNotesList() {
        // Remove existing note cards from the layout
        val childrenToRemove = mutableListOf<Int>()
        for (i in 0 until mainLayout.childCount) {
            val child = mainLayout.getChildAt(i)
            if (child.tag == "note_card") {
                childrenToRemove.add(i)
            }
        }
        
        // Remove in reverse order to avoid index issues
        childrenToRemove.reversed().forEach { index ->
            mainLayout.removeViewAt(index)
        }
        
        // Filter notes based on current filter and search query
        val filteredNotes = allNotes.filter { note ->
            val matchesFilter = currentFilter == null || note.category?.equals(currentFilter, ignoreCase = true) == true
            val matchesSearch = searchQuery.isEmpty() || 
                note.title.contains(searchQuery, ignoreCase = true) || 
                note.content.contains(searchQuery, ignoreCase = true)
            matchesFilter && matchesSearch
        }
        
        // Add filtered notes to the layout
        val insertIndex = findInsertIndex()
        filteredNotes.take(10).forEachIndexed { index, note ->
            val noteCard = createNoteCard(note)
            noteCard.tag = "note_card"
            mainLayout.addView(noteCard, insertIndex + index)
        }
        
        // Show status message
        if (filteredNotes.isEmpty()) {
            val emptyView = TextView(this).apply {
                text = when {
                    searchQuery.isNotEmpty() -> "No notes found for: $searchQuery"
                    currentFilter != null -> "No notes in category: $currentFilter"
                    else -> "No notes yet. Create your first note!"
                }
                textSize = 16f
                setTextColor(Color.parseColor("#999999"))
                gravity = Gravity.CENTER
                setPadding(0, 32, 0, 32)
                tag = "note_card"
            }
            mainLayout.addView(emptyView, insertIndex)
        }
    }
    
    private fun findInsertIndex(): Int {
        // Find the index after "Recent Notes" title
        for (i in 0 until mainLayout.childCount) {
            val child = mainLayout.getChildAt(i)
            if (child is TextView && child.text == "Recent Notes") {
                return i + 1
            }
        }
        return mainLayout.childCount
    }
}