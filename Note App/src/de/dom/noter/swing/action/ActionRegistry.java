package de.dom.noter.swing.action;

import java.util.Map;

import javax.swing.Action;

import de.dom.noter.mvc.controller.NotesController;
import de.dom.noter.mvc.controller.command.CommandControl;
import de.dom.noter.swing.MainWindow;

public class ActionRegistry {

	Map<ActionType, Action> actions;

	public ActionRegistry(final NotesController notesController, final CommandControl commandControl, final MainWindow mainWindow) {
		final ActionData data = new ActionData( notesController, commandControl, mainWindow );
		actions = ActionType.createActions( data );
	}

	public Action get( final ActionType actionType ) {
		return actions.get( actionType );
	}

}
