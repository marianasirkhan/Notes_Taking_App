package de.dom.noter.mvc.controller;

import de.dom.noter.mvc.model.Model;
import de.dom.noter.mvc.model.Note;
import de.dom.noter.mvc.view.NoteView;

public class NoteControllerImpl implements NoteController {

	private final Model model;
	private final NoteView view;
	private final long noteId;

	public NoteControllerImpl(final Model model, final long noteId, final NoteView view) {
		this.model = model;
		this.noteId = noteId;
		this.view = view;

		view.setController( this );
		model.addNoteChangedListener( view, noteId );
	}

	@Override
	public Note getNote() {
		return model.getNote( noteId );
	}

	public NoteView getView() {
		return view;
	}

	@Override
	public Note setTitle( final String newTitle ) {
		final Note oldNote = getNote();
		model.setNote( oldNote.setTitle( newTitle ) );
		return oldNote;
	}

	@Override
	public Note setContent( final String newContent ) {
		final Note oldNote = getNote();
		model.setNote( oldNote.setContent( newContent ) );
		return oldNote;
	}

	@Override
	public Note removeNote() {
		return model.removeNote( getId() );
	}

	private long getId() {
		return getNote().getId();
	}

}
