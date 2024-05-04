package de.dom.noter.framework;

import java.util.NoSuchElementException;

import junit.framework.TestCase;

public class UndoStackCapacityTest extends TestCase {

	private UndoStack<Object> us;
	private Object e1;
	private Object e2;
	private Object e3;

	@Override
	public void setUp() {
		us = new UndoStack<Object>( 2 );
		e1 = new Object();
		e2 = new Object();
		e3 = new Object();
	}

	public void testAdd() throws Exception {
		us.add( e3 ).add( e1 ).add( e2 );
		assertEquals( 2, us.size() );
	}

	public void testUndo() throws Exception {
		us.add( e3 ).add( e1 ).add( e2 );

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

	public void _testUndoOverflow() throws Exception {
		final UndoStack<Object> usUnlimited = new UndoStack<Object>();

		final long time1 = System.currentTimeMillis();
		final int countUnlimited = countUntilFailed( usUnlimited, Integer.MAX_VALUE );
		final long time2 = System.currentTimeMillis();
		System.out.println( "countUnlimited=" + countUnlimited + "; " + (time2 - time1) );

		final long time3 = System.currentTimeMillis();
		final int countUs = countUntilFailed( us, countUnlimited << 1 );
		final long time4 = System.currentTimeMillis();
		System.out.println( "countUs=" + countUs + "; " + (time4 - time3) );

		assertTrue( countUnlimited < countUs );

	}

	private int countUntilFailed( final UndoStack<Object> stack, final long failNumber ) throws InterruptedException {
		final Object e = new Object();
		int count = 0;

		try {
			while( true ) {
				stack.add( e );
				count += 1;
				if( count > failNumber ) {
					break;
				}
			}
		}
		catch( final Throwable t ) {
			stack.clear();
			Runtime.getRuntime().gc();
			t.printStackTrace();
		}

		return count;
	}

}
