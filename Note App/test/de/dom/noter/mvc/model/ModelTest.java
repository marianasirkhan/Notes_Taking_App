package de.dom.noter.mvc.model;

import junit.framework.TestCase;
import de.dom.noter.mvc.view.SimpleMock;

public class ModelTest extends TestCase {

	public void testCreateNote() throws Exception {
		final Model m = new Model();

		final Note n = m.createNote();
		assertTrue( m.hasNote( n.getId() ) );
	}

	public void testChangeGetNote() throws Exception {
		final Model m = new Model();
		final Note n = m.createNote();

		final Note n1 = n.setTitle( "bla1" );
		m.setNote( n1 );
		assertEquals( n1, m.getNote( n.getId() ) );

		assertNull( m.setNote( new Note() ) );

		try {
			m.getNote( 42 );
			fail();
		}
		catch( final IllegalArgumentException expected ) {
		}
	}

	public void testNoteListener() throws Exception {
		final Model m = new Model();
		final Note n = m.createNote();

		final NoteChangedListener l0 = (NoteChangedListener) SimpleMock.create( NoteChangedListener.class );
		final NoteChangedListener l1 = (NoteChangedListener) SimpleMock.create( NoteChangedListener.class );

		m.addNoteChangedListener( l0, n.getId() );
		m.addNoteChangedListener( l0, n.getId() );
		m.addNoteChangedListener( l1, n.getId() );

		assertEquals( 2, SimpleMock.getCalls( l0, "onNoteChanged" ) );
		assertEquals( 1, SimpleMock.getCalls( l1, "onNoteChanged" ) );

		m.setNote( n.setTitle( "bla" ) );

		assertEquals( 3, SimpleMock.getCalls( l0, "onNoteChanged" ) );
		assertEquals( 2, SimpleMock.getCalls( l1, "onNoteChanged" ) );
	}

	public void testNotesListener() throws Exception {
		final Model m = new Model();

		final NotesChangedListener l0 = (NotesChangedListener) SimpleMock.create( NotesChangedListener.class );

		m.addNotesChangedListener( l0 );
		assertEquals( 1, SimpleMock.getCalls( l0, "onNotesChanged" ) );

		m.createNote();
		assertEquals( 2, SimpleMock.getCalls( l0, "onNotesChanged" ) );

		final NotesChangedListener l1 = (NotesChangedListener) SimpleMock.create( NotesChangedListener.class );

		m.addNotesChangedListener( l1 );
		assertEquals( 1, SimpleMock.getCalls( l1, "onNotesChanged" ) );

		m.createNote();
		assertEquals( 3, SimpleMock.getCalls( l0, "onNotesChanged" ) );
		assertEquals( 2, SimpleMock.getCalls( l1, "onNotesChanged" ) );
	}

	public void testIsEmpty() throws Exception {
		final Model m = new Model();
		assertTrue( m.isEmpty() );
		m.createNote();
		assertFalse( m.isEmpty() );
	}

	public void testClear() throws Exception {
		final Model m = new Model();
		m.createNote();
		m.clear();
		assertTrue( m.isEmpty() );
	}

	public void testEquals() throws Exception {
		final Model m = new Model();
		final Model m2 = new Model();
		m2.createNote();

		assertTrue( m.equals( m ) );
		assertFalse( m.equals( null ) );
		assertFalse( m.equals( new Object() ) );
		assertTrue( m.equals( new Model() ) );
		assertFalse( m.equals( m2 ) );
	}

	public void testSize() throws Exception {
		final Model m = new Model();

		assertEquals( 0, m.size() );

		final Note n = m.createNote();

		assertEquals( 1, m.size() );

		m.setNote( n.setTitle( "bla" ) );

		assertEquals( 1, m.size() );

		m.createNote();

		assertEquals( 2, m.size() );

		m.clear();

		assertEquals( 0, m.size() );
	}
}
