package com.cornelisdemooij.NotesAPI.services;

import com.cornelisdemooij.NotesAPI.entities.Note;
import com.cornelisdemooij.NotesAPI.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public Iterable<Note> findAll() {
        return noteRepository.findAll();
    }

}
