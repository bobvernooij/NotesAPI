package com.cornelisdemooij.NotesAPI.endpoints;

import com.cornelisdemooij.NotesAPI.entities.Note;
import com.cornelisdemooij.NotesAPI.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("note")
@Component
public class NoteEndpoint {
    @Autowired
    private NoteService noteService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotes() {
        Iterable<Note> notes = noteService.findAll();
        return Response.ok(notes).build();
    }
}
