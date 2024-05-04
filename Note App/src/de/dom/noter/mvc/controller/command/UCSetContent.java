package de.dom.noter.mvc.controller.command;

import java.util.Collections;

import de.dom.noter.mvc.controller.NoteController;
import de.dom.noter.mvc.model.Note;

public class UCSetContent extends UndoableCommand {

	NoteController noteController;
	private final String newContent;

	public UCSetContent(final NoteController noteController, final String newContent) {
		this.noteController = noteController;
		this.newContent = newContent;
	}

	@Override
	public boolean performInternal() {
		final Note oldNote = noteController.setContent( newContent );
		final Note newNote = noteController.getNote();

		setUndoNotes( Collections.singleton( oldNote ) );
		setRedoNotes( Collections.singleton( newNote ) );

		return !oldNote.equals( newNote );
	}
}
