package de.dom.noter.mvc.model;

import junit.framework.TestCase;
import de.dom.noter.mvc.model.Data;

public class DataTest extends TestCase {

	public void testUid() throws Exception {
		final Data d1 = new Data();
		final Data d2 = new Data();

		assertFalse( d1.equals( d2 ) );
		assertFalse( d1.getId() == d2.getId() );
	}

	public void testEquals() throws Exception {
		final Data d = new Data();
		assertFalse( d.equals( null ) );
		assertFalse( d.equals( new Object() ) );
		assertFalse( d.equals( new Data() ) );
		assertTrue( d.equals( d ) );
	}

}
