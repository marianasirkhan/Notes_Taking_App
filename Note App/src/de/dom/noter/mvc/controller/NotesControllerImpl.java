package de.dom.noter.mvc.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.dom.noter.mvc.model.Model;
import de.dom.noter.mvc.model.Note;
import de.dom.noter.mvc.model.NotesIO;
import de.dom.noter.mvc.view.NoteView;
import de.dom.noter.mvc.view.NotesView;

public class NotesControllerImpl implements NotesController {

	private final Model model;
	private final NotesView view;
	private final Map<Long, NoteController> noteControllers;

	public NotesControllerImpl(final Model model, final NotesView view) {
		this.model = model;
		this.view = view;
		noteControllers = new HashMap<Long, NoteController>();

		model.addNotesChangedListener( view );

		createAndAddControllers( model.getNoteIds() );
	}

	@Override
	public Note createNewNote() {
		final Note newNote = model.createNote();
		createAndAddNoteController( newNote.getId() );
		return newNote;
	}

	private void createAndAddNoteController( final long id ) {
		final Map<Long, ? extends NoteView> noteViews = view.getNoteViews();
		final NoteView noteView = noteViews.get( id );
		final NoteController nc = new NoteControllerImpl( model, id, noteView );
		noteControllers.put( id, nc );
	}

	public Collection<Long> getNoteIds() {
		return model.getNoteIds();
	}

	public Object getView() {
		return view;
	}

	@Override
	public void removeNote( final long noteId ) {
		model.removeNote( noteId );
	}

	@Override
	public void exportAllNotes( final File fileToSave ) {
		NotesIO.exportModel( fileToSave, model );
	}

	@Override
	public Collection<Note> importAllNotes( final File fileToRead, final String separator ) {
		final Collection<Long> ids = NotesIO.importNotesFromFile( fileToRead, model, separator );
		createAndAddControllers( ids );

		final ArrayList<Note> notes = new ArrayList<Note>();
		for( final long id : ids ) {
			notes.add( model.getNote( id ) );
		}

		return notes;
	}

	private void createAndAddControllers( final Collection<Long> ids ) {
		for( final long id : ids ) {
			createAndAddNoteController( id );
		}
	}
}
