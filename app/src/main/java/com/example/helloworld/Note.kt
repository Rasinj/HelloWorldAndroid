package com.example.helloworld

import java.util.Date

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val isPinned: Boolean = false,
    val tags: List<String> = emptyList()
) {
    fun getDisplayTitle(): String {
        return if (title.isBlank()) {
            content.take(50).trim().ifEmpty { "Untitled Note" }
        } else {
            title
        }
    }
    
    fun getPreviewContent(): String {
        return content.take(100).trim()
    }
    
    fun getCategoryColor(): String {
        return when (category?.lowercase()) {
            "personal" -> "#8E44AD"
            "work" -> "#2980B9"
            "study" -> "#16A085"
            "creative" -> "#E91E63"
            "ideas" -> "#FF9800"
            "tasks" -> "#795548"
            else -> "#E67E22"
        }
    }
    
    fun getCategoryEmoji(): String {
        return when (category?.lowercase()) {
            "personal" -> "👤"
            "work" -> "💼"
            "study" -> "📚"
            "creative" -> "🎨"
            "ideas" -> "💡"
            "tasks" -> "✅"
            else -> "📝"
        }
    }
}

enum class NoteCategory(val displayName: String, val emoji: String, val color: String) {
    PERSONAL("Personal", "👤", "#8E44AD"),
    WORK("Work", "💼", "#2980B9"),
    STUDY("Study", "📚", "#16A085"),
    CREATIVE("Creative", "🎨", "#E91E63"),
    IDEAS("Ideas", "💡", "#FF9800"),
    TASKS("Tasks", "✅", "#795548");
    
    companion object {
        fun getAllCategories(): List<NoteCategory> = values().toList()
        
        fun fromString(categoryName: String?): NoteCategory? {
            return values().find { it.displayName.equals(categoryName, ignoreCase = true) }
        }
    }
}