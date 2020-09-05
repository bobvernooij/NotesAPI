package com.cornelisdemooij.NotesAPI.repositories;

import com.cornelisdemooij.NotesAPI.entities.Note;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {
    Note save(Note note);

    Optional<Note> findById(Long id);
    Iterable<Note> findAll();
}
