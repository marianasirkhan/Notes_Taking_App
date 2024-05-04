package de.dom.noter.framework;

import java.util.NoSuchElementException;

import junit.framework.TestCase;

public class UndoStackTest extends TestCase {

	private UndoStack<Object> us;
	private Object e1;
	private Object e2;

	@Override
	protected void setUp() throws Exception {
		us = new UndoStack<Object>();
		e1 = new Object();
		e2 = new Object();
	}

	public void testEmptySize() throws Exception {
		assertEquals( 0, us.size() );
	}

	public void testAddOnEmptyStack() throws Exception {
		us.add( e1 );
		assertEquals( 1, us.size() );
	}

	public void testaddTwoElements() throws Exception {
		us.add( e1 ).add( e2 );
		assertEquals( 2, us.size() );
	}

	public void testUndoRedo() throws Exception {
		us.add( e1 ).add( e2 );

		assertFalse( us.hasRedoElement() );
		assertTrue( us.hasUndoElement() );

		try {
			us.getNextRedoElement();
			fail();
		}
		catch( final NoSuchElementException expected ) {
		}

		assertEquals( e2, us.getNextUndoElement() );

		assertTrue( us.hasRedoElement() );
		assertTrue( us.hasUndoElement() );

		assertEquals( e2, us.getNextRedoElement() );

		assertEquals( e2, us.getNextUndoElement() );
		assertEquals( e1, us.getNextUndoElement() );

		assertTrue( us.hasRedoElement() );
		assertFalse( us.hasUndoElement() );

		assertEquals( e1, us.getNextRedoElement() );
		assertEquals( e2, us.getNextRedoElement() );
		try {
			us.getNextRedoElement();
			fail();
		}
		catch( final NoSuchElementException expected ) {
		}

		assertEquals( e2, us.getNextUndoElement() );
		assertEquals( e1, us.getNextUndoElement() );
		try {
			us.getNextUndoElement();
			fail();
		}
		catch( final NoSuchElementException expected ) {
		}
	}

	public void testPeekUndo() throws Exception {
		us.add( e1 );
		us.add( e2 );

		assertEquals( e2, us.peekNextUndoElement() );
		assertEquals( e2, us.peekNextUndoElement() );

		us.getNextUndoElement();

		assertEquals( e1, us.peekNextUndoElement() );
		assertEquals( e1, us.peekNextUndoElement() );

		us.getNextUndoElement();

		try {
			us.peekNextUndoElement();
			fail();
		}
		catch( final NoSuchElementException expected ) {
		}
	}

	public void testPeekRedo() throws Exception {
		us.add( e1 );
		us.add( e2 );

		us.getNextUndoElement();
		us.getNextUndoElement();

		assertEquals( e1, us.peekNextRedoElement() );
		assertEquals( e1, us.peekNextRedoElement() );

		us.getNextRedoElement();

		assertEquals( e2, us.peekNextRedoElement() );
		assertEquals( e2, us.peekNextRedoElement() );

		us.getNextRedoElement();

		try {
			us.peekNextRedoElement();
			fail();
		}
		catch( final NoSuchElementException expected ) {
		}
	}

}
