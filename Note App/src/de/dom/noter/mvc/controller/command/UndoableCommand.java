package de.dom.noter.mvc.controller.command;

import java.util.Collection;
import java.util.Collections;

import de.dom.noter.mvc.model.Note;

public abstract class UndoableCommand extends Command {

	private Collection<Note> redoNotes;
	private Collection<Note> undoNotes;
	private String text;

	public UndoableCommand() {
		redoNotes = Collections.emptyList();
		undoNotes = Collections.emptyList();
	}

	final public Collection<Note> getRedoNotes() {
		checkSealed();
		return redoNotes;
	}

	final public Collection<Note> getUndoNotes() {
		checkSealed();
		return undoNotes;
	}

	final public String getText() {
		checkSealed();
		return text;
	}

	final protected void setRedoNotes( final Collection<Note> redoNotes ) {
		checkNotSealed();
		this.redoNotes = redoNotes;
	}

	final protected void setUndoNotes( final Collection<Note> undoNotes ) {
		checkNotSealed();
		this.undoNotes = undoNotes;
	}

	final protected void setText( final String text ) {
		checkNotSealed();
		this.text = text;
	}

}
