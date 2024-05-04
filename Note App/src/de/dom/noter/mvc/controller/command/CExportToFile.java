package de.dom.noter.mvc.controller.command;

import java.io.File;

import de.dom.noter.mvc.controller.NotesController;

public class CExportToFile extends Command {

	private final NotesController notesController;
	private final File fileToSave;

	public CExportToFile(final NotesController notesController, final File fileToSave) {
		this.notesController = notesController;
		this.fileToSave = fileToSave;
	}

	@Override
	public boolean performInternal() {
		notesController.exportAllNotes( fileToSave );
		return true;
	}
}
