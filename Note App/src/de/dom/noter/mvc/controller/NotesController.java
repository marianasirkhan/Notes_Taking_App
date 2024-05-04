package de.dom.noter.mvc.controller;

import java.io.File;
import java.util.Collection;

import de.dom.noter.mvc.model.Note;

public interface NotesController {

	Note createNewNote();

	void removeNote( long noteId );

	void exportAllNotes( File fileToSave );

	Collection<Note> importAllNotes( File fileToRead, String separator );

}