package de.dom.noter.swing.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.Action;

import de.dom.noter.mvc.controller.NotesController;
import de.dom.noter.mvc.controller.command.CExportToFile;
import de.dom.noter.mvc.controller.command.CommandControl;
import de.dom.noter.swing.FileChooser;
import de.dom.noter.swing.FileChooser.FileSelectionHandler;
import de.dom.noter.swing.FileChooser.Mode;
import de.dom.noter.swing.MainWindow;

public class ExportToFileAction extends AbstractNoterAction {

	private final CommandControl commandControl;
	private final NotesController notesController;
	private final MainWindow mainWindow;

	private ExportToFileAction(final CommandControl commandControl, final NotesController notesController, final MainWindow mainWindow) {
		this.commandControl = commandControl;
		this.notesController = notesController;
		this.mainWindow = mainWindow;
		setName( "Export notes…" );
		setDescription( "Create a human readable file, wich contains all notes." );
	}

	@Override
	public void actionPerformed( final ActionEvent e ) {
		final FileChooser chooser = new FileChooser( mainWindow, getName(), Mode.SAVE_FILE );
		chooser.setFilename( "notes.txt" );
		chooser.setFilenameFilter( new FilenameFilter() {
			@Override
			public boolean accept( final File dir, final String name ) {
				return name.endsWith( ".txt" );
			}
		} );
		chooser.openDialog( new FileSelectionHandler() {
			@Override
			public void onFileSelected( final File file ) {
				commandControl.doCommand( new CExportToFile( notesController, file ) );
			}
		} );
	}

	public static Action create( final ActionData data ) {
		return new ExportToFileAction( data.commandControl, data.notesController, data.mainWindow );
	}

}
