package de.dom.noter.framework;

import junit.framework.TestCase;

public class CoWListBuilderTest extends TestCase {

	private CoWListBuilder<Object> clb;
	private Object e1;
	private Object e2;
	private Object e3;

	@Override
	protected void setUp() throws Exception {
		clb = new CoWListBuilder<Object>();
		e1 = new Object();
		e2 = new Object();
		e3 = new Object();
	}

	public void testEmptyList() throws Exception {
		assertEquals( CoWList.empty(), clb.toCoWList() );
	}

	public void testAddFirst1() throws Exception {
		clb.addFirst( e1 );
		assertEquals( CoWList.empty().add( e1 ), clb.toCoWList() );
	}

	public void testAddFirst2() throws Exception {
		clb.addFirst( e1 ).addFirst( e2 );
		assertEquals( CoWList.empty().add( e1 ).add( e2 ), clb.toCoWList() );
	}

	public void testAddLast1() throws Exception {
		clb.addLast( e1 );
		assertEquals( CoWList.empty().add( e1 ), clb.toCoWList() );
	}

	public void testAddLast2() throws Exception {
		clb.addLast( e1 ).addLast( e2 );
		assertEquals( CoWList.empty().add( e2 ).add( e1 ), clb.toCoWList() );
	}

	public void testAddMixed1() throws Exception {
		clb.addFirst( e1 ).addLast( e2 ).addFirst( e3 );
		assertEquals( CoWList.empty().add( e2 ).add( e1 ).add( e3 ), clb.toCoWList() );
	}

	public void testAddMixed2() throws Exception {
		clb.addLast( e1 ).addFirst( e2 ).addLast( e3 );
		assertEquals( CoWList.empty().add( e3 ).add( e1 ).add( e2 ), clb.toCoWList() );
	}

	public void testSealing() throws Exception {
		clb.toCoWList();
		try {
			clb.addFirst( e1 );
			fail();
		}
		catch( final IllegalStateException expected ) {
		}
		try {
			clb.addLast( e1 );
			fail();
		}
		catch( final IllegalStateException expected ) {
		}
	}

	public void testPartialBuildedList() throws Exception {
		final CoWList<Object> cl = clb.addFirst( e2 ).addLast( e1 ).toCoWList().add( e3 );
		assertEquals( CoWList.empty().add( e1 ).add( e2 ).add( e3 ), cl );
		assertEquals( CoWList.empty().add( e1 ).add( e2 ), cl.tail() );
		assertEquals( CoWList.empty().add( e1 ), cl.tail().tail() );
		assertEquals( CoWList.empty(), cl.tail().tail().tail() );
	}

}
