package com.example.helloworld

import org.junit.Test
import org.junit.Before
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import java.util.Date

class NoteModelTest {

    private lateinit var note: Note

    @Before
    fun setup() {
        note = Note(
            id = 1,
            title = "Test Note",
            content = "This is a test note content",
            category = "Personal",
            createdAt = Date(),
            updatedAt = Date()
        )
    }

    @Test
    fun `note should be created with valid properties`() {
        assertNotNull(note)
        assertEquals(1, note.id)
        assertEquals("Test Note", note.title)
        assertEquals("This is a test note content", note.content)
        assertEquals("Personal", note.category)
        assertNotNull(note.createdAt)
        assertNotNull(note.updatedAt)
    }

    @Test
    fun `note should handle empty title gracefully`() {
        val emptyTitleNote = Note(
            id = 2,
            title = "",
            content = "Content without title",
            category = "Work",
            createdAt = Date(),
            updatedAt = Date()
        )
        
        assertNotNull(emptyTitleNote)
        assertEquals("", emptyTitleNote.title)
        assertEquals("Content without title", emptyTitleNote.content)
    }

    @Test
    fun `note should handle empty content gracefully`() {
        val emptyContentNote = Note(
            id = 3,
            title = "Title only",
            content = "",
            category = "Personal",
            createdAt = Date(),
            updatedAt = Date()
        )
        
        assertNotNull(emptyContentNote)
        assertEquals("Title only", emptyContentNote.title)
        assertEquals("", emptyContentNote.content)
    }

    @Test
    fun `note should handle null category gracefully`() {
        val noCategoryNote = Note(
            id = 4,
            title = "No category note",
            content = "This note has no category",
            category = null,
            createdAt = Date(),
            updatedAt = Date()
        )
        
        assertNotNull(noCategoryNote)
        assertEquals(null, noCategoryNote.category)
    }

    @Test
    fun `note equals should work correctly`() {
        val note1 = Note(1, "Title", "Content", "Personal", Date(), Date())
        val note2 = Note(1, "Title", "Content", "Personal", Date(), Date())
        val note3 = Note(2, "Different", "Content", "Work", Date(), Date())
        
        assertEquals(note1, note2)
        assertNotEquals(note1, note3)
    }

    @Test
    fun `note hashCode should be consistent`() {
        val note1 = Note(1, "Title", "Content", "Personal", Date(), Date())
        val note2 = Note(1, "Title", "Content", "Personal", Date(), Date())
        
        assertEquals(note1.hashCode(), note2.hashCode())
    }

    @Test
    fun `note should validate required fields`() {
        // Test that a note can be created with minimal required fields
        val minimalNote = Note(
            id = 0,
            title = "Minimal",
            content = "Content",
            category = null,
            createdAt = Date(),
            updatedAt = Date()
        )
        
        assertNotNull(minimalNote)
        assertTrue(minimalNote.title.isNotEmpty())
        assertTrue(minimalNote.content.isNotEmpty())
    }
}

// Note data class for testing
data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: String? = null,
    val createdAt: Date,
    val updatedAt: Date
)