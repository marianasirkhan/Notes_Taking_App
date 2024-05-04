package de.dom.noter.swing.action;

import de.dom.noter.mvc.controller.NotesController;
import de.dom.noter.mvc.controller.command.CommandControl;
import de.dom.noter.swing.MainWindow;

final public class ActionData {

	final CommandControl commandControl;
	final NotesController notesController;
	final MainWindow mainWindow;

	public ActionData(final NotesController notesController, final CommandControl commandControl, final MainWindow mainWindow) {
		this.commandControl = commandControl;
		this.notesController = notesController;
		this.mainWindow = mainWindow;
	}

}
