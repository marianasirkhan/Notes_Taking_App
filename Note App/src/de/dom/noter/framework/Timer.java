package de.dom.noter.framework;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Timer extends Thread {

	public static interface TimerListener {
		void onTimerFired();
	}

	private long nextSleepInterval;
	private boolean isRunning;
	private final Map<TimerListener, Long> listeners;
	private final Map<TimerListener, Long> newListeners;

	public Timer() {
		isRunning = false;
		listeners = new IdentityHashMap<TimerListener, Long>();
		newListeners = new IdentityHashMap<TimerListener, Long>();

		Runtime.getRuntime().addShutdownHook( new Thread() {
			@Override
			public void run() {
				end();
				notifyAllListeners();			
			}

			private void notifyAllListeners() {
				for( final TimerListener listener : listeners.keySet() ) {
					listener.onTimerFired();
				}
			}
		} );

	}

	@Override
	synchronized public void run() {
		isRunning = true;
		long lastNow = getTime();
		while( isRunning ) {
			final long now = getTime();
			waitForTimers( now - lastNow );
			lastNow = now;
			sleepAWhile();
		}
	}

	private void waitForTimers( final long timeElapsed ) {
		nextSleepInterval = Long.MAX_VALUE;

		listeners.putAll( newListeners );

		for( final Iterator<Entry<TimerListener, Long>> iter = listeners.entrySet().iterator(); iter.hasNext(); ) {
			final Entry<TimerListener, Long> entry = iter.next();

			final TimerListener listener = entry.getKey();
			final long toWait = entry.getValue();

			long newTimeToWait;
			if( newListeners.containsKey( listener ) ) {
				newTimeToWait = toWait;
				newListeners.remove( listener );
			}
			else {
				newTimeToWait = toWait - timeElapsed;
			}

			if( newTimeToWait <= 0 ) {
				iter.remove();
				listener.onTimerFired();
			}
			else {
				entry.setValue( newTimeToWait );
				if( newTimeToWait < nextSleepInterval ) {
					nextSleepInterval = newTimeToWait;
				}
			}
		}
	}

	private long getTime() {
		return System.currentTimeMillis();
	}

	private void sleepAWhile() {
		try {
			wait( nextSleepInterval );
		}
		catch( final InterruptedException ignore ) {
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	synchronized public void end() {
		isRunning = false;
		notify();
	}

	/**
	 * @param ms
	 *            time to wait in milliseconds
	 * @param listener
	 *            the listener to inform after the time
	 * @throws IllegalStateException
	 *             if the timer is not running
	 */
	synchronized public void fireTimer( final long ms, final TimerListener listener ) {
		if( isRunning ) {
			newListeners.put( listener, ms );
			notify();
		}
		else {
			throw new IllegalStateException( "Timer is not running!" );
		}
	}

}
