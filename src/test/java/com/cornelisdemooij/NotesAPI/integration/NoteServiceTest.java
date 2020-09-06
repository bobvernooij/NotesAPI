package com.cornelisdemooij.NotesAPI.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.cornelisdemooij.NotesAPI.entities.Note;
import com.cornelisdemooij.NotesAPI.services.NoteService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Iterator;
import java.util.Optional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
        Timestamp start = Timestamp.from(Instant.now().minusSeconds(1));

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

    @Test
    public void saveTwoNotesAndFindAll() {
        // Given:
        Note note1 = new Note();
        note1.title = "First note title";
        note1.body = "First note body";

        Note note2 = new Note();
        note2.title = "Second note title";
        note2.body = "Second note body";

        // When:
        noteService.save(note1);
        noteService.save(note2);
        Iterable<Note> foundNotes = noteService.findAll();
        Iterator<Note> foundNoteIterator = foundNotes.iterator();

        // Then:
        assertThat(foundNoteIterator.hasNext()).isTrue();
        Note foundNote1 = foundNoteIterator.next();
        assertThat(foundNote1.id).isEqualTo(1);
        assertThat(foundNote1.title).isEqualTo("First note title");
        assertThat(foundNote1.body).isEqualTo("First note body");

        assertThat(foundNoteIterator.hasNext()).isTrue();
        Note foundNote2 = foundNoteIterator.next();
        assertThat(foundNote2.id).isEqualTo(2);
        assertThat(foundNote2.title).isEqualTo("Second note title");
        assertThat(foundNote2.body).isEqualTo("Second note body");

        assertThat(foundNoteIterator.hasNext()).isFalse();
    }

    @Test
    public void saveTwoNotesAndFindById() {
        // Given:
        Note note1 = new Note();
        note1.title = "First note title";
        note1.body = "First note body";

        Note note2 = new Note();
        note2.title = "Second note title";
        note2.body = "Second note body";

        // When:
        noteService.save(note1);
        noteService.save(note2);
        Optional<Note> optionalFoundNote0 = noteService.findById((long)0);
        Optional<Note> optionalFoundNote1 = noteService.findById((long)1);
        Optional<Note> optionalFoundNote2 = noteService.findById((long)2);
        Optional<Note> optionalFoundNote3 = noteService.findById((long)3);

        // Then:
        assertThat(optionalFoundNote0.isEmpty()).isTrue();

        assertThat(optionalFoundNote1.isPresent()).isTrue();
        Note savedNote1 = optionalFoundNote1.get();
        assertThat(savedNote1.id).isEqualTo(1);
        assertThat(savedNote1.title).isEqualTo("First note title");
        assertThat(savedNote1.body).isEqualTo("First note body");

        assertThat(optionalFoundNote2.isPresent()).isTrue();
        Note savedNote2 = optionalFoundNote2.get();
        assertThat(savedNote2.id).isEqualTo(2);
        assertThat(savedNote2.title).isEqualTo("Second note title");
        assertThat(savedNote2.body).isEqualTo("Second note body");

        assertThat(optionalFoundNote3.isEmpty()).isTrue();
    }

    @Test
    public void saveNoteAndUpdateIt() {
        // Given:
        Note originalNote = new Note();
        originalNote.id = 1234;     // This id should be ignored, we will test this below.
        originalNote.title = "Original note title";
        originalNote.body = "Original note body";

        Note updatedNote = new Note();
        updatedNote.id = 9999;     // This id should be ignored, we will test this below.
        updatedNote.title = "Updated note title";
        updatedNote.body = "Updated note body";

        // When:
        Note savedOriginalNote = noteService.save(originalNote);
        Optional<Note> optionalSavedUpdatedNote;
        try {
            optionalSavedUpdatedNote = noteService.updateById(savedOriginalNote.id, updatedNote);
        } catch (Exception e) {
            Assertions.fail("updateById should not throw an exception in saveNoteAndUpdateIt");
            optionalSavedUpdatedNote = Optional.empty();
        }
        Optional<Note> optionalFoundNote = noteService.findById((long)1);

        // Then:
        assertThat(savedOriginalNote.id).isEqualTo(1);
        assertThat(savedOriginalNote.title).isEqualTo("Original note title");
        assertThat(savedOriginalNote.body).isEqualTo("Original note body");
        assertThat(savedOriginalNote.creation).isEqualTo(savedOriginalNote.modified);

        assertThat(optionalSavedUpdatedNote.isPresent()).isTrue();
        Note savedUpdatedNote = optionalSavedUpdatedNote.get();
        assertThat(savedUpdatedNote.id).isEqualTo(1);
        assertThat(savedUpdatedNote.title).isEqualTo("Updated note title");
        assertThat(savedUpdatedNote.body).isEqualTo("Updated note body");
        assertThat(savedUpdatedNote.creation).isEqualToIgnoringMillis(savedOriginalNote.creation);
        assertThat(savedUpdatedNote.modified).isNotEqualTo(savedOriginalNote.modified);

        assertThat(optionalFoundNote.isPresent()).isTrue();
        Note foundNote = optionalFoundNote.get();
        assertThat(foundNote.id).isEqualTo(1);
        assertThat(foundNote.title).isEqualTo("Updated note title");
        assertThat(foundNote.body).isEqualTo("Updated note body");
        assertThat(foundNote.creation).isEqualTo(savedUpdatedNote.creation);
        assertThat(foundNote.modified).isNotEqualTo(savedUpdatedNote.modified);
    }

    @Test
    public void saveNoteAndDeleteIt() {
        // Given:
        Note noteToSave = new Note();
        noteToSave.title = "Test title";
        noteToSave.body = "Test body";

        // When:
        Note savedNote = noteService.save(noteToSave);
        Optional<Note> optionalFoundNoteBeforeDelete = noteService.findById((long)1);
        noteService.deleteById((long)1);
        Optional<Note> optionalFoundNoteAfterDelete = noteService.findById((long)1);

        // Then:
        assertThat(savedNote.id).isEqualTo(1);
        assertThat(savedNote.title).isEqualTo("Test title");
        assertThat(savedNote.body).isEqualTo("Test body");
        assertThat(savedNote.creation).isNotNull();
        assertThat(savedNote.modified).isNotNull();

        assertThat(optionalFoundNoteBeforeDelete.isPresent()).isTrue();
        Note foundNoteBeforeDelete = optionalFoundNoteBeforeDelete.get();
        assertThat(foundNoteBeforeDelete.id).isEqualTo(1);
        assertThat(foundNoteBeforeDelete.title).isEqualTo("Test title");
        assertThat(foundNoteBeforeDelete.body).isEqualTo("Test body");

        assertThat(optionalFoundNoteAfterDelete.isPresent()).isFalse();
    }

}