package com.example.helloworld

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.mockito.kotlin.any
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import java.util.Date

class NoteRepositoryTest {

    @Mock
    private lateinit var mockNoteDao: NoteDao
    
    private lateinit var repository: NoteRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = NoteRepository(mockNoteDao)
    }

    @Test
    fun `repository should get all notes`() = runTest {
        // Arrange
        val mockNotes = listOf(
            Note(1, "Note 1", "Content 1", "Personal", Date(), Date()),
            Note(2, "Note 2", "Content 2", "Work", Date(), Date())
        )
        whenever(mockNoteDao.getAllNotes()).thenReturn(mockNotes)

        // Act
        val result = repository.getAllNotes()

        // Assert
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("Note 1", result[0].title)
        verify(mockNoteDao).getAllNotes()
    }

    @Test
    fun `repository should insert note successfully`() = runTest {
        // Arrange
        val note = Note(0, "New Note", "New Content", "Personal", Date(), Date())
        whenever(mockNoteDao.insertNote(any())).thenReturn(1L)

        // Act
        val result = repository.insertNote(note)

        // Assert
        assertEquals(1L, result)
        verify(mockNoteDao).insertNote(note)
    }

    @Test
    fun `repository should update note successfully`() = runTest {
        // Arrange
        val note = Note(1, "Updated Note", "Updated Content", "Work", Date(), Date())

        // Act
        repository.updateNote(note)

        // Assert
        verify(mockNoteDao).updateNote(note)
    }

    @Test
    fun `repository should delete note successfully`() = runTest {
        // Arrange
        val note = Note(1, "Note to Delete", "Content", "Personal", Date(), Date())

        // Act
        repository.deleteNote(note)

        // Assert
        verify(mockNoteDao).deleteNote(note)
    }

    @Test
    fun `repository should get notes by category`() = runTest {
        // Arrange
        val personalNotes = listOf(
            Note(1, "Personal Note 1", "Content 1", "Personal", Date(), Date()),
            Note(2, "Personal Note 2", "Content 2", "Personal", Date(), Date())
        )
        whenever(mockNoteDao.getNotesByCategory("Personal")).thenReturn(personalNotes)

        // Act
        val result = repository.getNotesByCategory("Personal")

        // Assert
        assertNotNull(result)
        assertEquals(2, result.size)
        assertTrue(result.all { it.category == "Personal" })
        verify(mockNoteDao).getNotesByCategory("Personal")
    }

    @Test
    fun `repository should search notes successfully`() = runTest {
        // Arrange
        val searchResults = listOf(
            Note(1, "Important Note", "Important content", "Work", Date(), Date())
        )
        whenever(mockNoteDao.searchNotes("%Important%")).thenReturn(searchResults)

        // Act
        val result = repository.searchNotes("Important")

        // Assert
        assertNotNull(result)
        assertEquals(1, result.size)
        assertTrue(result[0].title.contains("Important"))
        verify(mockNoteDao).searchNotes("%Important%")
    }

    @Test
    fun `repository should handle empty search results`() = runTest {
        // Arrange
        whenever(mockNoteDao.searchNotes("%NonExistent%")).thenReturn(emptyList())

        // Act
        val result = repository.searchNotes("NonExistent")

        // Assert
        assertNotNull(result)
        assertTrue(result.isEmpty())
        verify(mockNoteDao).searchNotes("%NonExistent%")
    }

    @Test
    fun `repository should get note by id`() = runTest {
        // Arrange
        val note = Note(1, "Specific Note", "Content", "Personal", Date(), Date())
        whenever(mockNoteDao.getNoteById(1L)).thenReturn(note)

        // Act
        val result = repository.getNoteById(1L)

        // Assert
        assertNotNull(result)
        assertEquals(1L, result?.id)
        assertEquals("Specific Note", result?.title)
        verify(mockNoteDao).getNoteById(1L)
    }
}

// Mock interfaces for testing
interface NoteDao {
    suspend fun getAllNotes(): List<Note>
    suspend fun insertNote(note: Note): Long
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun getNotesByCategory(category: String): List<Note>
    suspend fun searchNotes(query: String): List<Note>
    suspend fun getNoteById(id: Long): Note?
}

class NoteRepository(private val noteDao: NoteDao) {
    suspend fun getAllNotes(): List<Note> = noteDao.getAllNotes()
    suspend fun insertNote(note: Note): Long = noteDao.insertNote(note)
    suspend fun updateNote(note: Note) = noteDao.updateNote(note)
    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
    suspend fun getNotesByCategory(category: String): List<Note> = noteDao.getNotesByCategory(category)
    suspend fun searchNotes(query: String): List<Note> = noteDao.searchNotes("%$query%")
    suspend fun getNoteById(id: Long): Note? = noteDao.getNoteById(id)
}