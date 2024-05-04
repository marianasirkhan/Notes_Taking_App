package de.dom.noter.framework;

import java.util.NoSuchElementException;

public class CoWListImpl<E> extends CoWList<E> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static final CoWListImpl EMPTY = new CoWListImpl( null, null ) {
		@Override
		public Object head() {
			throw new NoSuchElementException();
		};

		@Override
		public CoWList tail() {
			throw new NoSuchElementException();
		}

		@Override
		public boolean hasHead() {
			return false;
		}

		@Override
		public boolean hasTail() {
			return false;
		}

		@Override
		public String toString() {
			return "[EMPTY]";
		};
	};

	private final E head;
	private final CoWList<E> tail;

	CoWListImpl(final E head, final CoWList<E> tail) {
		this.head = head;
		this.tail = tail;
	}

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