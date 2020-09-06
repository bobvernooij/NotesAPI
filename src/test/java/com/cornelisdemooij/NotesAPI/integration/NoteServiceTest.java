package com.cornelisdemooij.NotesAPI.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.cornelisdemooij.NotesAPI.entities.Note;
import com.cornelisdemooij.NotesAPI.services.NoteService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.Instant;

@SpringBootTest
public class NoteServiceTest {

    @Autowired
    private NoteService noteService;

    @Test
    public void contextLoads() throws Exception {
        assertThat(noteService).isNotNull();
    }

    @Test
    public void saveOneNote() {
        // Given:
        Timestamp start = Timestamp.from(Instant.now());

        Note noteToSave = new Note();
        noteToSave.title = "Test title";
        noteToSave.body = "Test body";

        // When:
        Note savedNote = noteService.save(noteToSave);

        // Then:
        assertThat(savedNote.id).isEqualTo(1);
        assertThat(savedNote.title).isEqualTo("Test title");
        assertThat(savedNote.body).isEqualTo("Test body");
        assertThat(savedNote.creation).isNotNull();
        assertThat(savedNote.modified).isNotNull();

        assertThat(savedNote.creation.after(start)).isTrue();
        assertThat(savedNote.modified.after(start)).isTrue();
    }

    @Test
    public void saveTwoNotes() {
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
        assertThat(savedNote1.id).isEqualTo(1);
        assertThat(savedNote1.title).isEqualTo("First note title");
        assertThat(savedNote1.body).isEqualTo("First note body");

        assertThat(savedNote2.id).isEqualTo(2);
        assertThat(savedNote2.title).isEqualTo("Second note title");
        assertThat(savedNote2.body).isEqualTo("Second note body");
    }

}