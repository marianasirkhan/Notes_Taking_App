package de.dom.noter.swing;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.dom.noter.swing.action.ActionType;

public class MainMenu extends JMenuBar {

	private static final long serialVersionUID = -7777765930343767590L;

	private final MainWindow mainWindow;

	public MainMenu(final MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		createAndAddFileMenu();
		createAndAddEditMenu();
		createAndAddNotesMenu();
	}

	private void createAndAddFileMenu() {
		final JMenu menu = new JMenu( "File" );
		add( menu );

		menu.add( new JMenuItem( mainWindow.getAction( ActionType.IMPORT_FROM_FILE ) ) );
		menu.add( new JMenuItem( mainWindow.getAction( ActionType.EXPORT_TO_FILE ) ) );
	}

	private void createAndAddEditMenu() {
		final JMenu menu = new JMenu( "Edit" );
		add( menu );

		menu.add( new JMenuItem( mainWindow.getAction( ActionType.UNDO ) ) );
		menu.add( new JMenuItem( mainWindow.getAction( ActionType.REDO ) ) );
	}

	private void createAndAddNotesMenu() {
		final JMenu menu = new JMenu( "Notes" );
		add( menu );

		menu.add( new JMenuItem( mainWindow.getAction( ActionType.CREATE_NEW_NOTE ) ) );
	}

}
