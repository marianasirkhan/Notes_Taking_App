package de.dom.noter.framework;

public abstract class CoWList<E> {

	@SuppressWarnings({ "rawtypes" })
	public static final CoWList EMPTY = CoWListImpl.EMPTY;

	@SuppressWarnings("unchecked")
	public static <E> CoWList<E> empty() {
		return EMPTY;
	}

	public abstract CoWList<E> add( E e );

	public abstract boolean hasHead();

	public abstract E head();

	public abstract boolean hasTail();

	public abstract CoWList<E> tail();

	public CoWList<E> headList( final int size ) {
		return new CoWListBuilder<E>().addLast( this, size ).toCoWList();
	}

	@Override
	public String toString() {
		return "[" + head() + ", " + tail() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hasHead() && head() != null) ? head().hashCode() : 0);
		result = prime * result + ((hasTail() && tail() != null) ? tail().hashCode() : 0);
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
		if( !(obj instanceof CoWList<?>) ) {
			return false;
		}

		final CoWList<?> other = (CoWList<?>) obj;

		if( hasHead() != other.hasHead() ) {
			return false;
		}
		if( head() == null ) {
			if( other.head() != null ) {
				return false;
			}
		}
		else if( !head().equals( other.head() ) ) {
			return false;
		}

		if( hasTail() != other.hasTail() ) {
			return false;
		}
		if( tail() == null ) {
			if( other.tail() != null ) {
				return false;
			}
		}
		else if( !tail().equals( other.tail() ) ) {
			return false;
		}

		return true;
	}

}