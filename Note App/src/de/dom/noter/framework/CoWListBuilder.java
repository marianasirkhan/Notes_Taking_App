package de.dom.noter.framework;

public class CoWListBuilder<E> {

	private static class CLBElement<E> extends CoWList<E> {

		private E head;
		private CoWList<E> tail;

		@Override
		public CoWList<E> add( final E e ) {
			return new CoWListImpl<E>( e, this );
		}

		@Override
		public E head() {
			return head;
		}

		@Override
		public CoWList<E> tail() {
			return tail;
		}

		@Override
		public boolean hasHead() {
			return true;
		}

		@Override
		public boolean hasTail() {
			return true;
		}

	}

	private CoWList<E> list;
	private CLBElement<E> last;

	private boolean isSealed;

	public CoWListBuilder() {
		list = CoWList.empty();
		last = null;
	}

	public CoWListBuilder<E> addFirst( final E e ) {
		checkUnsealed();
		final CLBElement<E> newList = new CLBElement<E>();
		newList.head = e;
		newList.tail = list;
		list = newList;
		if( last == null ) {
			last = newList;
		}
		return this;
	}

	public CoWListBuilder<E> addLast( final E e ) {
		checkUnsealed();
		final CLBElement<E> newLast = new CLBElement<E>();
		newLast.head = e;
		newLast.tail = CoWList.empty();
		if( null != last ) {
			last.tail = newLast;
		}
		last = newLast;
		if( list == CoWList.empty() ) {
			list = newLast;
		}
		return this;
	}

	public CoWListBuilder<E> addLast( final CoWList<E> listToAdd, final int size ) {
		checkUnsealed();
		int s = size;
		CoWList<E> current = listToAdd;
		while( s > 0 ) {
			addLast( current.head() );
			current = current.tail();
			s -= 1;
		}
		return this;
	}

	public CoWList<E> toCoWList() {
		seal();
		return list;
	}

	private void seal() {
		this.isSealed = true;
	}

	private void checkUnsealed() {
		if( isSealed ) {
			throw new IllegalStateException( "List already sealed." );
		}
	}

}
