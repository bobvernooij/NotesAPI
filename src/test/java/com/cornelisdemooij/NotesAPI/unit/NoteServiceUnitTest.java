package com.cornelisdemooij.NotesAPI.unit;

import com.cornelisdemooij.NotesAPI.entities.Note;
import com.cornelisdemooij.NotesAPI.repositories.NoteRepository;
import com.cornelisdemooij.NotesAPI.services.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NoteServiceUnitTest {

    @Autowired
    private NoteService noteService;

    @MockBean
    private NoteRepository noteRepository;

    @Test
    public void contextLoads() throws Exception {
        assertThat(noteService).isNotNull();
    }

    @Test
    public void saveOneNote() {
        // Set up mock response:
        Mockito.when(noteRepository.save(Mockito.any(Note.class)))
                .thenAnswer(input -> input.getArguments()[0]);

        // Given:
        Timestamp start = Timestamp.from(Instant.now());

        Note noteToSave = new Note();
        noteToSave.title = "Test title";
        noteToSave.body = "Test body";

        // When:
        Note savedNote = noteService.save(noteToSave);

        // Then:
        assertThat(savedNote.id).isNotNull();
        assertThat(savedNote.title).isEqualTo("Test title");
        assertThat(savedNote.body).isEqualTo("Test body");
        assertThat(savedNote.creation).isNotNull();
        assertThat(savedNote.modified).isNotNull();

        assertThat(savedNote.creation.after(start)).isTrue();
        assertThat(savedNote.modified.after(start)).isTrue();
    }

    @Test
    public void saveTwoNotes() {
        // Set up mock response:
        Mockito.when(noteRepository.save(Mockito.any(Note.class)))
                .thenAnswer(input -> input.getArguments()[0]);

        // Given:
        Note note1 = new Note();
        note1.title = "First note title";
        note1.body = "First note body";

        Note note2 = new Note();
        note2.title = "Second note title";
        note2.body = "Second note body";

        // When:
        Note savedNote1 = noteService.save(note1);
        Note savedNote2 = noteService.save(note2);

        // Then:
        assertThat(savedNote1.id).isNotNull();
        assertThat(savedNote1.title).isEqualTo("First note title");
        assertThat(savedNote1.body).isEqualTo("First note body");

        assertThat(savedNote2.id).isNotNull();
        assertThat(savedNote2.title).isEqualTo("Second note title");
        assertThat(savedNote2.body).isEqualTo("Second note body");
    }

}