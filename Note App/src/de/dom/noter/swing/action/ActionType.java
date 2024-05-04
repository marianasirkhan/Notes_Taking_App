package de.dom.noter.swing.action;

import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.Map;

import javax.swing.Action;

public enum ActionType {

	// notes
	CREATE_NEW_NOTE(CreateNewNoteAction.class),

	// file
	EXPORT_TO_FILE(ExportToFileAction.class), IMPORT_FROM_FILE(ImportFromFileAction.class),

	// edit
	UNDO(UndoAction.class), REDO(RedoAction.class);

	private final Class<?> actionClass;

	private ActionType(final Class<?> actionClass) {
		this.actionClass = actionClass;
	}

	private Action create( final ActionData data ) {
		Action action = null;

		try {
			final Method create = actionClass.getMethod( "create", ActionData.class );
			action = (Action) create.invoke( null, data );
		}
		catch( final Exception e ) {
			throw new RuntimeException( "Creating action failed: " + actionClass.getName(), e );
		}

		return action;
	}

	static Map<ActionType, Action> createActions( final ActionData data ) {
		final Map<ActionType, Action> actions = new IdentityHashMap<ActionType, Action>();

		for( final ActionType t : ActionType.values() ) {
			actions.put( t, t.create( data ) );
		}

		return actions;
	}

}
