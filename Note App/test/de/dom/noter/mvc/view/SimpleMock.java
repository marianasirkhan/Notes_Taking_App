package de.dom.noter.mvc.view;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class SimpleMock {

	private static final Map<Object, Map<String, Integer>> CALLS = new IdentityHashMap<Object, Map<String, Integer>>();

	public static Object create( final Class<?> clazz ) throws Exception {
		final Object proxy = Proxy.newProxyInstance( clazz.getClassLoader(), new Class<?>[] { clazz }, getStdInvocationHandler() );

		CALLS.put( proxy, new HashMap<String, Integer>() );

		return proxy;
	}

	public static int getCalls( final Object mock, final String method ) {
		if( !CALLS.containsKey( mock ) ) {
			throw new IllegalArgumentException( "Mock object " + mock + " not found in " + CALLS.keySet() );
		}
		if( !CALLS.get( mock ).containsKey( method ) ) {
			throw new IllegalArgumentException( "Method " + method + " not found in " + CALLS.get( mock ).keySet() );
		}
		return CALLS.get( mock ).get( method );
	}

	private static InvocationHandler getStdInvocationHandler() {
		return new InvocationHandler() {
			@Override
			public Object invoke( final Object proxy, final Method method, final Object[] args ) throws Throwable {
				final Map<String, Integer> map = CALLS.get( proxy );
				final String key = method.getName();
				if( !map.containsKey( key ) ) {
					map.put( key, 0 );
				}
				final int count = map.get( key ) + 1;
				map.put( key, count );

				try {
					final Method objectMethod = Object.class.getMethod( method.getName(), method.getParameterTypes() );
					return objectMethod.invoke( this, args );
				}
				catch( final NoSuchMethodException noMethodFound ) {
					return null;
				}
			}
		};
	}
}
