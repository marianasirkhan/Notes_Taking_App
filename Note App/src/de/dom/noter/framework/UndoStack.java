package de.dom.noter.framework;

import java.util.NoSuchElementException;

public class UndoStack<E> {

	private CoWList<E> undoable;
	private CoWList<E> redoable;
	private int undoableSize;
	private int redoableSize;
	private final int capacity;
	private int undoableSizeInternal;

	public UndoStack() {
		this( Integer.MAX_VALUE );
	}

	public UndoStack(final int capacity) {
		this.capacity = capacity;
		clear();
	}

	public void clear() {
		undoable = CoWListImpl.empty();
		redoable = CoWListImpl.empty();
		undoableSize = 0;
		redoableSize = 0;
		undoableSizeInternal = 0;
	}

	public UndoStack<E> add( final E e ) {
		redoable = CoWListImpl.empty();
		undoable = undoable.add( e );
		redoableSize = 0;
		undoableSize += 1;
		undoableSizeInternal += 1;
		if( undoableSize > capacity ) {
			undoableSize = capacity;
		}
		cleanupIfNecessary();
		return this;
	}

	private void cleanupIfNecessary() {
		if( (undoableSizeInternal >>> 2) > undoableSize ) {
			undoable = undoable.headList( undoableSize );
			undoableSizeInternal = undoableSize;
		}
	}

	public E getNextUndoElement() {
		checkUndoElement();
		final E e = undoable.head();
		undoable = undoable.tail();
		redoable = redoable.add( e );
		undoableSize -= 1;
		redoableSize += 1;
		undoableSizeInternal -= 1;
		return e;
	}

	private void checkUndoElement() {
		if( !hasUndoElement() ) {
			throw new NoSuchElementException();
		}
	}

	public E getNextRedoElement() {
		checkRedoElement();
		final E e = redoable.head();
		redoable = redoable.tail();
		undoable = undoable.add( e );
		undoableSize += 1;
		redoableSize -= 1;
		undoableSizeInternal += 1;
		return e;
	}

	private void checkRedoElement() {
		if( !hasRedoElement() ) {
			throw new NoSuchElementException();
		}
	}

	public int size() {
		return undoableSize + redoableSize;
	}

	public boolean hasRedoElement() {
		return redoableSize > 0;
	}

	public boolean hasUndoElement() {
		return undoableSize > 0;
	}

	public E peekNextRedoElement() {
		checkRedoElement();
		return redoable.head();
	}

	public E peekNextUndoElement() {
		checkUndoElement();
		return undoable.head();
	}

}
