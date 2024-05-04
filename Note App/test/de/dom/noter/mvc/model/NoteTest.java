package de.dom.noter.mvc.model;

import junit.framework.TestCase;

public class NoteTest extends TestCase {

	public void testUid() throws Exception {
		final Note n1 = new Note();
		final Note n2 = new Note();

		assertFalse( n1.equals( n2 ) );
		assertFalse( n1.getId() == n2.getId() );
	}

	public void testTitle() throws Exception {
		final Note n = new Note();

		assertTrue( n.setTitle( "bla" ).equals( n.setTitle( "bla" ) ) );
		assertFalse( n.setTitle( "bla" ).equals( n.setTitle( "blubb" ) ) );
		assertFalse( n.equals( n.setTitle( "blubb" ) ) );
		assertFalse( n.setTitle( "bla" ).equals( n ) );

		assertEquals( "bla", n.setTitle( "bla" ).getTitle() );
	}

	public void testContent() throws Exception {
		final Note n = new Note();

		assertTrue( n.setContent( "bla" ).equals( n.setContent( "bla" ) ) );
		assertFalse( n.setContent( "bla" ).equals( n.setContent( "blubb" ) ) );
		assertFalse( n.equals( n.setContent( "blubb" ) ) );
		assertFalse( n.setContent( "bla" ).equals( n ) );

		assertEquals( "bla", n.setContent( "bla" ).getContent() );
	}

	public void testEquals() throws Exception {
		final Note n = new Note();
		assertFalse( n.equals( null ) );
		assertFalse( n.equals( new Object() ) );
		assertFalse( n.equals( new Note() ) );
		assertTrue( n.equals( n ) );
	}

	public void testHasTitle() throws Exception {
		checkHasTitle( new Note() );
		checkHasTitle( new Note( 42L ) );
	}

	private void checkHasTitle( final Note n ) {
		assertFalse( n.hasTitle() );
		assertTrue( n.setTitle( "bla" ).hasTitle() );
		assertFalse( n.setTitle( "bla" ).setTitle( "" ).hasTitle() );
		assertFalse( n.setTitle( "bla" ).setTitle( null ).hasTitle() );
		assertFalse( n.setTitle( n.getTitle() ).hasTitle() );
		assertTrue( n.setTitle( new String( n.getTitle() ) ).hasTitle() );

		assertFalse( n.equals( n.setTitle( new String( n.getTitle() ) ) ) );
	}

	public void testHasContent() throws Exception {
		checkHasContent( new Note() );
		checkHasContent( new Note( 23L ) );
	}

	private void checkHasContent( final Note n ) {
		assertFalse( n.hasContent() );
		assertTrue( n.setContent( "bla" ).hasContent() );
		assertFalse( n.setContent( "bla" ).setContent( "" ).hasContent() );
		assertFalse( n.setContent( "bla" ).setContent( null ).hasContent() );
		assertFalse( n.setContent( n.getContent() ).hasContent() );
	}

}
