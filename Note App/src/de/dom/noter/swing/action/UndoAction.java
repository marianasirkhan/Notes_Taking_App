package de.dom.noter.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import de.dom.noter.mvc.controller.command.CommandControl;

public class UndoAction extends AbstractNoterAction implements CommandControl.CanUndoRedoListener {
	private static final String THIS_NAME = "Undo";
	private final CommandControl commandControl;

	private UndoAction(final CommandControl commandControl) {
		this.commandControl = commandControl;
		setName( THIS_NAME );
		setDescription( "Undo the last undoable action." );
		setShortcut( 'Z' );
		commandControl.addCanUndoRedoListener( this );
	}

	@Override
	public void actionPerformed( final ActionEvent e ) {
		commandControl.undo();
	}

	public static Action create( final ActionData data ) {
		return new UndoAction( data.commandControl );
	}

	@Override
	public void onCanUndoRedoChange( final boolean canUndo, final boolean canRedo ) {
		final String text = commandControl.getNextUndoText();
		if( text != null && !text.isEmpty() ) {
			setName( THIS_NAME + ": " + text );
		}
		else {
			setName( THIS_NAME );
		}
		setEnabled( canUndo );
	}
}
