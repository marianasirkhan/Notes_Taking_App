package de.dom.noter.mvc.controller;

import de.dom.noter.mvc.model.Note;

public interface NoteController {

	Note setTitle( String title );

	Note setContent( String content );

	Note removeNote();

	Note getNote();

}