package de.dom.noter.mvc.controller.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import de.dom.noter.mvc.model.Note;

public class UCMacro extends UndoableCommand {

	private final List<UndoableCommand> actions;

	public UCMacro() {
		actions = new ArrayList<UndoableCommand>();
	}

	@Override
	public boolean performInternal() {
		final ListIterator<UndoableCommand> listIterator = actions.listIterator();

		boolean changed = false;
		while( listIterator.hasNext() ) {
			changed = changed | listIterator.next().perform();
		}

		setRedoNotes( createRedoNotes() );
		setUndoNotes( createUndoNotes() );

		return changed;
	}

	public Object size() {
		return actions.size();
	}

	public void add( final UndoableCommand action ) {
		checkNotSealed();
		actions.add( action );
	}

	private Collection<Note> createRedoNotes() {
		final ArrayList<Note> redoNotes = new ArrayList<Note>();

		final ListIterator<UndoableCommand> listIterator = actions.listIterator();
		while( listIterator.hasNext() ) {
			redoNotes.addAll( listIterator.next().getRedoNotes() );
		}

		return Collections.unmodifiableCollection( redoNotes );
	}

	private Collection<Note> createUndoNotes() {
		final ArrayList<Note> undoNotes = new ArrayList<Note>();

		final ListIterator<UndoableCommand> listIterator = actions.listIterator( actions.size() );
		while( listIterator.hasPrevious() ) {
			undoNotes.addAll( listIterator.previous().getUndoNotes() );
		}

		return Collections.unmodifiableCollection( undoNotes );
	}

}
