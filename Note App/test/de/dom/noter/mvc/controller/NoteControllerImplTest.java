package de.dom.noter.mvc.controller;

import junit.framework.TestCase;
import de.dom.noter.mvc.model.Model;
import de.dom.noter.mvc.model.Note;
import de.dom.noter.mvc.view.NoteView;
import de.dom.noter.mvc.view.SimpleMock;

public class NoteControllerImplTest extends TestCase {
	private final Model m = new Model();
	private final Note n = m.createNote();

	private NoteView v;
	private NoteControllerImpl nc;

	@Override
	protected void setUp() throws Exception {
		v = (NoteView) SimpleMock.create( NoteView.class );
		nc = new NoteControllerImpl( m, n.getId(), v );
	}

	public void testConnection() throws Exception {
		assertSame( n, nc.getNote() );
		assertSame( v, nc.getView() );
		assertEquals( 1, SimpleMock.getCalls( v, "setController" ) );
		assertEquals( 1, SimpleMock.getCalls( v, "onNoteChanged" ) );
		assertTrue( m.isNoteChangeListener( n.getId(), v ) );
	}

	public void testSetTitle() throws Exception {
		nc.setTitle( "bla" );
		assertEquals( "bla", nc.getNote().getTitle() );
		assertEquals( 2, SimpleMock.getCalls( v, "onNoteChanged" ) );
	}

	public void testSetContent() throws Exception {
		nc.setContent( "bla" );
		assertEquals( "bla", nc.getNote().getContent() );
		assertEquals( 2, SimpleMock.getCalls( v, "onNoteChanged" ) );
	}

}
