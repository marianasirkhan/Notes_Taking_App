package de.dom.noter.swing.action;

import java.awt.Toolkit;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

public abstract class AbstractNoterAction extends AbstractAction {

	private static final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

	public static Action create( final ActionData data ) {
		throw new UnsupportedOperationException( "Override this method!" );
	}

	protected void setName( final String name ) {
		putValue( Action.NAME, name );
	}

	protected String getName() {
		return (String) getValue( Action.NAME );
	}

	protected void setDescription( final String description ) {
		putValue( Action.SHORT_DESCRIPTION, description );
		putValue( Action.LONG_DESCRIPTION, description );
	}

	protected void setShortcut( final char keyCode ) {
		setShortcut( keyCode, 0 );
	}

	/**
	 * @param keyCode
	 *            the capital letter
	 * @param additionalModifiers
	 *            The modifiers consist of any combination of:
	 *            <ul>
	 *            <li>java.awt.event.InputEvent.SHIFT_DOWN_MASK
	 *            <li>java.awt.event.InputEvent.CTRL_DOWN_MASK
	 *            <li>java.awt.event.InputEvent.META_DOWN_MASK
	 *            <li>java.awt.event.InputEvent.ALT_DOWN_MASK
	 *            <li>java.awt.event.InputEvent.ALT_GRAPH_DOWN_MASK
	 *            </ul>
	 * 
	 *            Note that CTRL and META are used as SHORTCUT modifiers on Windows and MacOS, and therefore should be avoided.
	 * 
	 */
	protected void setShortcut( final char keyCode, final int additionalModifiers ) {
		final KeyStroke shortcutStroke = KeyStroke.getKeyStroke( keyCode, SHORTCUT_MASK | additionalModifiers );
		putValue( Action.ACCELERATOR_KEY, shortcutStroke );
	}
}
