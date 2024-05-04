package de.dom.noter.swing;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FilenameFilter;

public class FileChooser {

	public static enum Mode {
		LOAD_FILE, SAVE_FILE, CHOOSE_DIR;
	}

	public static interface FileSelectionHandler {
		void onFileSelected( File file );
	}

	private final FileDialog dialog;
	private final Mode mode;

	public FileChooser(final Frame parent, final String title, final Mode mode) {
		this.mode = mode;
		dialog = new FileDialog( parent, title );
		setMode( mode );
	}

	private void setMode( final Mode mode ) {
		switch( mode ) {
			case LOAD_FILE:
				dialog.setMode( FileDialog.LOAD );
				break;

			case SAVE_FILE:
				dialog.setMode( FileDialog.SAVE );
				break;

			case CHOOSE_DIR:
				dialog.setMode( FileDialog.LOAD );
				dialog.setFilenameFilter( new FilenameFilter() {
					@Override
					public boolean accept( final File dir, final String name ) {
						return new File( dir, name ).isDirectory();
					}
				} );
				break;

			default:
				throw new IllegalArgumentException( mode.toString() );
		}
	}

	/**
	 * @param handler
	 *            to handle the selected file
	 * @return true, if the selection handler was called; false otherwise
	 */
	public boolean openDialog( final FileSelectionHandler handler ) {
		showDialog();
		return evaluateResult( handler );
	}

	private void showDialog() {
		if( mode == Mode.CHOOSE_DIR ) {
			System.setProperty( "apple.awt.fileDialogForDirectories", "true" );
		}
		dialog.setVisible( true );
		if( mode == Mode.CHOOSE_DIR ) {
			System.setProperty( "apple.awt.fileDialogForDirectories", "false" );
		}
	}

	private boolean evaluateResult( final FileSelectionHandler listener ) {
		if( null != dialog.getFile() ) {
			final File file = new File( dialog.getDirectory(), dialog.getFile() );
			listener.onFileSelected( file );
			return true;
		}
		else {
			return false;
		}
	}

	public void setFilename( final String filename ) {
		dialog.setFile( filename );
	}

	public void setDirectory( final String dirname ) {
		dialog.setDirectory( dirname );
	}

	public void setFile( final File file ) {
		setFilename( file.getName() );
		setDirectory( file.getParent() );
	}

	public void setFilenameFilter( final FilenameFilter filter ) {
		dialog.setFilenameFilter( filter );
	}
}
