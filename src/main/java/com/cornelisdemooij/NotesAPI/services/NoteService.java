package com.cornelisdemooij.NotesAPI.services;

import com.cornelisdemooij.NotesAPI.entities.Note;
import com.cornelisdemooij.NotesAPI.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public Note save(Note note) {
        return noteRepository.save(note);
    }

    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }

    public Iterable<Note> findAll() {
        return noteRepository.findAll();
    }

    public Optional<Note> updateById(Long id, Note newNote) throws Exception {
        Optional<Note> optionalOldNote = findById(id);
        if (optionalOldNote.isPresent()) {
            Note oldNote = optionalOldNote.get();
            newNote.setId(oldNote.getId());
            return Optional.of(save(newNote));
        } else {
            throw new Exception("Error: tried to update a note that does not exist.");
        }
    }

    public void deleteById(Long id) {
        noteRepository.deleteById(id);
    }
}
