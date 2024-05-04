package de.dom.noter.mvc.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import junit.framework.TestCase;
import de.dom.noter.mvc.model.Model;
import de.dom.noter.mvc.model.Note;
import de.dom.noter.mvc.view.NoteView;
import de.dom.noter.mvc.view.NotesView;
import de.dom.noter.mvc.view.SimpleMock;

public class NotesControllerImplTest extends TestCase {
	private final Model m = new Model();
	private final Note n = m.createNote();

	private NotesView v;
	private NotesControllerImpl nc;

	@Override
	protected void setUp() throws Exception {
		final Map<Long, NoteView> noteViews = Collections.singletonMap( n.getId(), (NoteView) SimpleMock.create( NoteView.class ) );

		v = new NotesView() {

			@Override
			public void onNotesChanged( final Collection<Long> newNoteIds ) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setController( final NotesController notesController ) {
				// TODO Auto-generated method stub

			}

			@Override
			public Map<Long, ? extends NoteView> getNoteViews() {
				return noteViews;
			}

			@Override
			public NotesController getController() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void onNoteChanged( final Long id ) {
				// TODO Auto-generated method stub

			}
		};
		nc = new NotesControllerImpl( m, v );
	}

	public void testInstantiation() throws Exception {

	}

	// public void testConnection() throws Exception {
	// assertCollectionContentEquals( Collections.singleton( n.getId() ),
	// nc.getNoteIds() );
	// assertSame( v, nc.getView() );
	// assertEquals( 1, SimpleMock.getCalls( v, "setController" ) );
	// assertEquals( 1, SimpleMock.getCalls( v, "onNotesChanged" ) );
	// assertTrue( m.isNotesChangeListener( v ) );
	// }

	// private void assertCollectionContentEquals( final Set<?> expected, final
	// Collection<?> actual ) {
	// assertEquals( expected.size(), actual.size() );
	// for( final Object o : expected ) {
	// assertTrue( actual.contains( o ) );
	// }
	// }

	// public void testCreateNewNote() throws Exception {
	// final int oldSize = nc.getNoteIds().size();
	//
	// nc.createNewNote();
	//
	// assertEquals( oldSize + 1, nc.getNoteIds().size() );
	//
	// assertEquals( 2, SimpleMock.getCalls( v, "onNotesChanged" ) );
	// }

}
