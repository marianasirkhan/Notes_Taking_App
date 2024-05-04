package de.dom.noter.mvc.controller.command;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import de.dom.noter.framework.CoWList;
import de.dom.noter.framework.UndoStack;
import de.dom.noter.mvc.model.Model;
import de.dom.noter.mvc.model.Note;

public class CommandControl {

	public interface CanUndoRedoListener {
		void onCanUndoRedoChange( boolean canUndo, boolean canRedo );
	}

	private static final int MAX_UNDO_STACK_SIZE = 100;

	private final UndoStack<UndoableCommand> undoStack;

	private final Model model;

	private CoWList<CanUndoRedoListener> canUndoRedoListeners;

	public CommandControl(final Model model) {
		this.model = model;
		undoStack = new UndoStack<UndoableCommand>( MAX_UNDO_STACK_SIZE );
		canUndoRedoListeners = CoWList.empty();
	}

	public void doCommand( final Command command ) {
		final boolean changed = command.perform();

		System.out.println( "CommandControl.doCommand(): " + command.getClass() + "  " + changed );

		if( changed && command instanceof UndoableCommand ) {
			final boolean oldCanUndo = canUndo();
			undoStack.add( (UndoableCommand) command );
			if( canUndo() != oldCanUndo ) {
				informCanUndoRedoListeners();
			}
			System.out.println( "CommandControl.doCommand(): undoStackSize=" + undoStack.size() );
		}
	}

	private void informCanUndoRedoListeners() {
		for( CoWList<CanUndoRedoListener> current = canUndoRedoListeners; current.hasHead(); current = current.tail() ) {
			final CanUndoRedoListener listener = current.head();
			listener.onCanUndoRedoChange( canUndo(), canRedo() );
		}
	}

	public void addCanUndoRedoListener( final CanUndoRedoListener listener ) {
		canUndoRedoListeners = canUndoRedoListeners.add( listener );
		listener.onCanUndoRedoChange( canUndo(), canRedo() );
	}

	public void undo() {
		final boolean oldCanUndo = canUndo();
		final boolean oldCanRedo = canRedo();

		final UndoableCommand command = undoStack.getNextUndoElement();
		removeFromModel( command.getRedoNotes(), command.getUndoNotes() );
		addToModel( command.getUndoNotes() );

		if( canUndo() != oldCanUndo || canRedo() != oldCanRedo ) {
			informCanUndoRedoListeners();
		}
	}

	private void addToModel( final Collection<Note> notes ) {
		final LinkedHashSet<Note> notesToAdd = new LinkedHashSet<Note>();
		for( final Note n : notes ) {
			notesToAdd.add( n );
		}

		for( final Note n : notesToAdd ) {
			model.setNote( n );
		}

	}

	private void removeFromModel( final Collection<Note> notes, final Collection<Note> notToRemove ) {
		final Set<Long> notToRemoveIds = new HashSet<Long>();
		for( final Note n : notToRemove ) {
			notToRemoveIds.add( n.getId() );
		}

		final LinkedHashSet<Note> notesToRemove = new LinkedHashSet<Note>();
		for( final Note n : notes ) {
			if( !notToRemoveIds.contains( n.getId() ) ) {
				notesToRemove.add( n );
			}
		}

		for( final Note n : notesToRemove ) {
			model.removeNote( n.getId() );
		}
	}

	public boolean canUndo() {
		return undoStack.hasUndoElement();
	}

	public void redo() {
		final boolean oldCanUndo = canUndo();
		final boolean oldCanRedo = canRedo();

		final UndoableCommand command = undoStack.getNextRedoElement();
		removeFromModel( command.getUndoNotes(), command.getRedoNotes() );
		addToModel( command.getRedoNotes() );

		if( canUndo() != oldCanUndo || canRedo() != oldCanRedo ) {
			informCanUndoRedoListeners();
		}
	}

	public boolean canRedo() {
		return undoStack.hasRedoElement();
	}

	public String getNextRedoText() {
		if( undoStack.hasRedoElement() ) {
			return undoStack.peekNextRedoElement().getText();
		}
		else {
			return null;
		}
	}

	public String getNextUndoText() {
		if( undoStack.hasUndoElement() ) {
			return undoStack.peekNextUndoElement().getText();
		}
		else {
			return null;
		}
	}
}
