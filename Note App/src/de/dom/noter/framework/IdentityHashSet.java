package de.dom.noter.framework;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

public class IdentityHashSet<T> implements Set<T> {

	IdentityHashMap<T, T> map = new IdentityHashMap<T, T>();

	@Override
	public boolean add( final T t ) {
		return map.put( t, t ) != t;
	}

	@Override
	public boolean addAll( final Collection<? extends T> ts ) {
		boolean result = false;
		for( final T t : ts ) {
			result |= add( t );
		}
		return result;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean contains( final Object o ) {
		return map.containsKey( o );
	}

	@Override
	public boolean containsAll( final Collection<?> os ) {
		boolean result = true;
		for( final Object o : os ) {
			result &= contains( o );
		}
		return result;
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return map.keySet().iterator();
	}

	@Override
	public boolean remove( final Object o ) {
		return map.remove( o ) != null;
	}

	@Override
	public boolean removeAll( final Collection<?> os ) {
		boolean result = false;
		for( final Object o : os ) {
			result |= remove( o );
		}
		return result;
	}

	@Override
	public boolean retainAll( final Collection<?> os ) {
		boolean result = false;

		final Iterator<T> iter = iterator();
		while( iter.hasNext() ) {
			if( !os.contains( iter.next() ) ) {
				iter.remove();
				result = true;
			}
		}

		return result;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Object[] toArray() {
		return map.keySet().toArray();
	}

	@Override
	public <A> A[] toArray( final A[] a ) {
		return map.keySet().toArray( a );
	}

}
