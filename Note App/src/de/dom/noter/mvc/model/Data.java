package de.dom.noter.mvc.model;

import java.util.Random;

public class Data {

	private static final Random RNG = new Random();

	private final long id;

	protected Data() {
		this( getNextId() );
	}

	protected Data(final Data orig) {
		this( orig.id );
	}

	protected Data(final long id) {
		this.id = id;
	}

	protected Data(final long newId, final Data orig) {
		this( newId );
	}

	public long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals( final Object obj ) {
		if( this == obj ) {
			return true;
		}
		if( obj == null ) {
			return false;
		}
		if( getClass() != obj.getClass() ) {
			return false;
		}
		final Data other = (Data) obj;
		if( id != other.id ) {
			return false;
		}
		return true;
	}

	protected static long getNextId() {
		return RNG.nextLong();
	}

}
