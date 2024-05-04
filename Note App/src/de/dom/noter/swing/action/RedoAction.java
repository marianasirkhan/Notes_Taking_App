package de.dom.noter.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import de.dom.noter.main.Noter;
import de.dom.noter.main.Noter.OsType;
import de.dom.noter.mvc.controller.command.CommandControl;

public class RedoAction extends AbstractNoterAction implements CommandControl.CanUndoRedoListener {
	private static final String THIS_NAME = "Redo";
	private final CommandControl commandControl;

	private RedoAction(final CommandControl commandControl) {
		this.commandControl = commandControl;
		setName( THIS_NAME );
		setDescription( "Redo the last redoable action." );
		if( Noter.getOs() == OsType.MACOS ) {
			setShortcut( 'Z', java.awt.event.InputEvent.SHIFT_DOWN_MASK );
		}
		else {
			setShortcut( 'Y' );
		}
		commandControl.addCanUndoRedoListener( this );
	}

	@Override
	public void actionPerformed( final ActionEvent e ) {
		commandControl.redo();
	}

	public static Action create( final ActionData data ) {
		return new RedoAction( data.commandControl );
	}

	@Override
	public void onCanUndoRedoChange( final boolean canUndo, final boolean canRedo ) {
		final String text = commandControl.getNextRedoText();
		if( text != null && !text.isEmpty() ) {
			setName( THIS_NAME + ": " + text );
		}
		else {
			setName( THIS_NAME );
		}
		setEnabled( canRedo );
	}
}
