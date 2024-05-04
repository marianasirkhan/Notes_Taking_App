package de.dom.noter.mvc.controller.command;

import java.util.Collections;

import de.dom.noter.mvc.controller.NoteController;
import de.dom.noter.mvc.model.Note;

public class UCSetTitle extends UndoableCommand {

	NoteController noteController;
	private final String newTitle;

	public UCSetTitle(final NoteController noteController, final String newTitle) {
		this.noteController = noteController;
		this.newTitle = newTitle;
	}

	@Override
	public boolean performInternal() {
		final Note oldNote = noteController.setTitle( newTitle );
		final Note newNote = noteController.getNote();

		setUndoNotes( Collections.singleton( oldNote ) );
		setRedoNotes( Collections.singleton( newNote ) );

		return !oldNote.equals( newNote );
	}

}
