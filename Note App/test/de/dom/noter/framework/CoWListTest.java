package de.dom.noter.framework;

import java.util.NoSuchElementException;

import junit.framework.TestCase;

public class CoWListTest extends TestCase {

	private CoWList<Object> cl;
	private Object e1;
	private Object e2;

	@Override
	protected void setUp() throws Exception {
		cl = CoWListImpl.empty();
		e1 = new Object();
		e2 = new Object();
	}

	public void testAdd() throws Exception {
		cl = cl.add( e1 ).add( e2 );
		assertEquals( e2, cl.head() );
		assertEquals( e1, cl.tail().head() );
		assertEquals( CoWList.EMPTY, cl.tail().tail() );
	}

	public void testEmpty() throws Exception {
		try {
			CoWList.EMPTY.head();
			fail();
		}
		catch( final NoSuchElementException expected ) {
		}
		try {
			CoWList.EMPTY.tail();
			fail();
		}
		catch( final NoSuchElementException expected ) {
		}
	}

	public void testHeadList() throws Exception {
		cl = cl.add( e1 ).add( e2 );
		final CoWList<Object> hl0 = cl.headList( 0 );
		final CoWList<Object> hl1 = cl.headList( 1 );
		final CoWList<Object> hl2 = cl.headList( 2 );

		assertEquals( CoWListImpl.empty(), hl0 );
		assertEquals( CoWListImpl.empty().add( e2 ), hl1 );
		assertEquals( cl, hl2 );

		try {
			cl.headList( 3 );
			fail();
		}
		catch( final NoSuchElementException expected ) {
		}
	}

	public void testHashCode() throws Exception {
		final CoWList<Object> c0 = cl;
		final CoWList<Object> c1 = c0.add( e1 );
		final CoWList<Object> c2 = c1.add( e2 );

		assertFalse( c0.hashCode() == c1.hashCode() );
		assertFalse( c0.hashCode() == c2.hashCode() );
		assertFalse( c1.hashCode() == c2.hashCode() );
	}

}
