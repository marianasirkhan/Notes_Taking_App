package de.dom.noter.mvc.controller.command;

public abstract class Command {

	boolean isSealed;

	public Command() {
		isSealed = false;
	}

	final public boolean perform() {
		checkNotSealed();
		final boolean changed = performInternal();
		seal();
		return changed;
	}

	final protected void seal() {
		isSealed = true;
	}

	final protected void checkSealed() {
		if( !isSealed ) {
			throw new IllegalArgumentException( "Command is not sealed." );
		}
	}

	final protected void checkNotSealed() {
		if( isSealed ) {
			throw new IllegalArgumentException( "Command is sealed." );
		}
	}

	/**
	 * @return true, if something was changed
	 */
	abstract protected boolean performInternal();

}
