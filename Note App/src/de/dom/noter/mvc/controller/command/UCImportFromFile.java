package de.dom.noter.mvc.controller.command;

import java.io.File;
import java.util.Collection;

import de.dom.noter.mvc.controller.NotesController;
import de.dom.noter.mvc.model.Note;

public class UCImportFromFile extends UndoableCommand {

	private final NotesController notesController;
	private final File fileToRead;
	private final String separator;

	public UCImportFromFile(final NotesController notesController, final File fileToRead, final String separator) {
		this.notesController = notesController;
		this.fileToRead = fileToRead;
		this.separator = separator;
	}

	@Override
	public boolean performInternal() {
		final Collection<Note> notes = notesController.importAllNotes( fileToRead, separator );
		setRedoNotes( notes );
		return notes.size() > 0;
	}

}
