package de.dom.noter.framework;

import junit.framework.TestCase;
import de.dom.noter.framework.Timer.TimerListener;

public class TimerTest extends TestCase {

	private Timer timer;

	@Override
	protected void setUp() throws Exception {
		timer = new Timer();
	}

	public void testTimeNotRunning() throws Exception {
		try {
			timer.fireTimer( 100, new TimerListener() {
				@Override
				public void onTimerFired() {
				}
			} );
			fail();
		}
		catch( final IllegalStateException expected ) {
		}
	}

	public void testStartAndStop() throws Exception {
		assertFalse( timer.isRunning() );
		timer.start();
		sleepAWhile();
		assertTrue( timer.isRunning() );
		timer.end();
		sleepAWhile();
		assertFalse( timer.isRunning() );
	}

	public void testWatch() throws Exception {
		final int[] alerts = new int[] { 0, 0 };

		final TimerListener listener1 = new TimerListener() {
			@Override
			public void onTimerFired() {
				alerts[0] += 1;
			}
		};

		final TimerListener listener2 = new TimerListener() {
			@Override
			public void onTimerFired() {
				alerts[1] += 1;
			}
		};

		timer.start();

		Thread.sleep( 5 ); // listener1: 5

		timer.fireTimer( 100, listener1 );
		assertEquals( 0, alerts[0] );
		assertEquals( 0, alerts[1] );

		Thread.sleep( 50 ); // listener1: 55
		assertEquals( 0, alerts[0] );
		assertEquals( 0, alerts[1] );

		timer.fireTimer( 250, listener2 );

		Thread.sleep( 55 ); // listener1: 110, listener2: 55
		assertEquals( 1, alerts[0] );
		assertEquals( 0, alerts[1] );

		timer.fireTimer( 160, listener2 );

		Thread.sleep( 51 ); // listener2: 51
		assertEquals( 1, alerts[0] );
		assertEquals( 0, alerts[1] );

		Thread.sleep( 51 ); // listener2: 102
		assertEquals( 1, alerts[0] );
		assertEquals( 0, alerts[1] );

		Thread.sleep( 63 ); // listener2: 165
		assertEquals( 1, alerts[0] );
		assertEquals( 1, alerts[1] );

		Thread.sleep( 55 ); // listener2: 220
		assertEquals( 1, alerts[0] );
		assertEquals( 1, alerts[1] );

		Thread.sleep( 55 ); // listener2: 275
		assertEquals( 1, alerts[0] );
		assertEquals( 1, alerts[1] );

		timer.end();
		assertEquals( 1, alerts[0] );
		assertEquals( 1, alerts[1] );

	}

	public void testStartProblem() throws Exception {
		timer.start();
		Thread.sleep( 100 );
		timer.fireTimer( 50, new TimerListener() {
			@Override
			public void onTimerFired() {
			}
		} );
		Thread.sleep( 10 );
		timer.end();
	}

	private void sleepAWhile() throws InterruptedException {
		Thread.sleep( 5 );
	}

}
