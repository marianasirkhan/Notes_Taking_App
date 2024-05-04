package de.dom.noter.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import de.dom.noter.mvc.controller.NotesController;
import de.dom.noter.mvc.controller.command.CommandControl;
import de.dom.noter.mvc.controller.command.UCCreateNewNote;
import de.dom.noter.swing.MainWindow;

public class CreateNewNoteAction extends AbstractNoterAction {
	private final CommandControl commandControl;
	private final NotesController notesController;
	private final MainWindow mainWindow;

	private CreateNewNoteAction(final CommandControl commandControl, final NotesController notesController, final MainWindow mainWindow) {
		this.commandControl = commandControl;
		this.notesController = notesController;
		this.mainWindow = mainWindow;
		setName( "New Note" );
		setDescription( "Creates a new note." );
		setShortcut( 'N' );
	}

	@Override
	public void actionPerformed( final ActionEvent e ) {
		commandControl.doCommand( new UCCreateNewNote( notesController ) );
		mainWindow.selectFirstTitle();
	}

	public static Action create( final ActionData data ) {
		return new CreateNewNoteAction( data.commandControl, data.notesController, data.mainWindow );
	}

}
