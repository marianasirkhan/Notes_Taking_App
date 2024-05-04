package de.dom.noter.swing.action;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import de.dom.noter.framework.FileHelper;
import de.dom.noter.mvc.controller.NotesController;
import de.dom.noter.mvc.controller.command.CommandControl;
import de.dom.noter.mvc.controller.command.UCImportFromFile;
import de.dom.noter.swing.FileChooser;
import de.dom.noter.swing.FileChooser.FileSelectionHandler;
import de.dom.noter.swing.FileChooser.Mode;
import de.dom.noter.swing.MainWindow;

public class ImportFromFileAction extends AbstractNoterAction {
	private final CommandControl commandControl;
	private final NotesController notesController;
	private final MainWindow mainWindow;

	private static final String N = SpringLayout.NORTH;
	private static final String S = SpringLayout.SOUTH;
	private static final String E = SpringLayout.EAST;
	private static final String W = SpringLayout.WEST;
	private static final String HC = SpringLayout.HORIZONTAL_CENTER;
	private static final String BL = SpringLayout.BASELINE;

	private ImportFromFileAction(final CommandControl commandControl, final NotesController notesController, final MainWindow mainWindow) {
		this.commandControl = commandControl;
		this.notesController = notesController;
		this.mainWindow = mainWindow;
		setName( "Import notes…" );
		setDescription( "Reads notes from a text file." );
	}

	@Override
	public void actionPerformed( final ActionEvent e ) {
		final File file = chooseFile();
		final ImportDialog dialog = new ImportDialog( mainWindow, file );
		dialog.setVisible( true );
	}

	public static Action create( final ActionData data ) {
		return new ImportFromFileAction( data.commandControl, data.notesController, data.mainWindow );
	}

	protected File chooseFile() {
		final File[] files = new File[1];

		final FileChooser chooser = new FileChooser( mainWindow, getName(), Mode.LOAD_FILE );
		chooser.setFilenameFilter( new FilenameFilter() {
			@Override
			public boolean accept( final File dir, final String name ) {
				return name.endsWith( ".txt" );
			}
		} );
		chooser.openDialog( new FileSelectionHandler() {
			@Override
			public void onFileSelected( final File file ) {
				files[0] = file;
			}
		} );

		return files[0];
	}

	class ImportDialog extends JDialog {
		private static final String DEFAULT_SEPARATOR_LINE = "---";
		private File selectedFile;
		private final JTextField filePath;
		private final JTextField separatorLine;
		private final JButton analyzeFile;

		public ImportDialog(final JFrame owner, final File selectedFile) {
			super( owner );
			setResizable( false );

			this.selectedFile = selectedFile;

			filePath = new JTextField();
			separatorLine = new JTextField();
			analyzeFile = new JButton();

			create();
		}

		private void create() {
			final Container contentPane = getContentPane();

			setModalityType( ModalityType.APPLICATION_MODAL );

			final JLabel filePathLabel = new JLabel( "File to import" );
			contentPane.add( filePathLabel );

			final Dimension textFieldSize = getTextFieldSize();

			configureFilePath( contentPane, textFieldSize );
			final JButton chooseFile = configureChooseFile( contentPane );
			final JLabel separatorLineLabel = configureSeparatorLineLabel( contentPane );
			configureSeparatorLine( contentPane, textFieldSize );
			configureAnalyzeFile( contentPane );
			final JButton importFile = configureImportFile( contentPane );
			final JButton cancel = configureCancel( contentPane );

			final SpringLayout sl = new SpringLayout();
			setLayout( sl );

			sl.putConstraint( N, filePathLabel, 10, N, contentPane );
			sl.putConstraint( E, filePathLabel, 0, E, separatorLineLabel );

			sl.putConstraint( BL, filePath, 0, BL, filePathLabel );
			sl.putConstraint( W, filePath, 5, E, filePathLabel );

			sl.putConstraint( BL, chooseFile, 0, BL, filePathLabel );
			sl.putConstraint( W, chooseFile, 5, E, filePath );

			sl.putConstraint( N, separatorLineLabel, 10, S, filePath );
			sl.putConstraint( W, separatorLineLabel, 10, W, contentPane );

			sl.putConstraint( BL, separatorLine, 0, BL, separatorLineLabel );
			sl.putConstraint( HC, separatorLine, 0, HC, filePath );

			sl.putConstraint( BL, analyzeFile, 0, BL, separatorLineLabel );
			sl.putConstraint( W, analyzeFile, 0, W, chooseFile );

			sl.putConstraint( N, importFile, 10, S, separatorLine );
			sl.putConstraint( E, importFile, -10, E, contentPane );

			sl.putConstraint( BL, cancel, 0, BL, importFile );
			sl.putConstraint( E, cancel, -5, W, importFile );

			sl.putConstraint( E, contentPane, 10, E, chooseFile );
			sl.putConstraint( S, contentPane, 10, S, cancel );

			pack();

			setSelectedFile( selectedFile );
		}

		private JButton configureCancel( final Container contentPane ) {
			final JButton cancel = new JButton( new AbstractNoterAction() {
				{
					setName( "Cancel" );
					setDescription( "Do not import notes from a file." );
				}

				@Override
				public void actionPerformed( final ActionEvent e ) {
					ImportDialog.this.setVisible( false );
				}

			} );
			contentPane.add( cancel );
			return cancel;
		}

		private JButton configureImportFile( final Container contentPane ) {
			final JButton importFile = new JButton( new AbstractNoterAction() {
				{
					setName( "Import" );
					setDescription( "Import the notes from the file." );
				}

				@Override
				public void actionPerformed( final ActionEvent e ) {
					ImportDialog.this.setVisible( false );
					commandControl.doCommand( new UCImportFromFile( notesController, selectedFile, separatorLine.getText() ) );
				}

			} );
			contentPane.add( importFile );
			return importFile;
		}

		private void configureAnalyzeFile( final Container contentPane ) {
			analyzeFile.setAction( new AbstractNoterAction() {
				{
					setName( "Analyze" );
					setDescription( "Search a possible line, which separates notes." );
				}

				@Override
				public void actionPerformed( final ActionEvent e ) {
					separatorLine.setText( findSepLineInFile() );
					analyzeFile.setEnabled( false );
				}

			} );
			contentPane.add( analyzeFile );
		}

		private void configureSeparatorLine( final Container contentPane, final Dimension textFieldSize ) {
			separatorLine.setText( DEFAULT_SEPARATOR_LINE );
			separatorLine.setPreferredSize( textFieldSize );
			contentPane.add( separatorLine );
		}

		private JLabel configureSeparatorLineLabel( final Container contentPane ) {
			final JLabel separatorLineLabel = new JLabel( "Note separator" );
			contentPane.add( separatorLineLabel );
			return separatorLineLabel;
		}

		private JButton configureChooseFile( final Container contentPane ) {
			final JButton chooseFile = new JButton( new AbstractNoterAction() {
				{
					setName( "Choose file�" );
					setDescription( "Choose text file for import." );
				}

				@Override
				public void actionPerformed( final ActionEvent e ) {
					ImportDialog.this.setSelectedFile( chooseFile() );
				}
			} );
			contentPane.add( chooseFile );
			return chooseFile;
		}

		private void configureFilePath( final Container contentPane, final Dimension textFieldSize ) {
			filePath.setPreferredSize( textFieldSize );
			contentPane.add( filePath );
		}

		private Dimension getTextFieldSize() {
			final Dimension textFieldSize = filePath.getPreferredSize();
			textFieldSize.width = 400;
			return textFieldSize;
		}

		public File getSelectedFile() {
			return selectedFile;
		}

		public void setSelectedFile( final File selectedFile ) {
			this.selectedFile = selectedFile;
			if( null != getSelectedFile() ) {
				filePath.setText( getSelectedFile().getAbsolutePath() );
				analyzeFile.setEnabled( true );
				validate();
				repaint();
			}
			else {
				analyzeFile.setEnabled( false );
			}
		}

		private String findSepLineInFile() {
			String[] lines = new String[0];
			try {
				lines = FileHelper.readFileAsUtf8StringArray( selectedFile );
			}
			catch( final Exception e ) {
				return DEFAULT_SEPARATOR_LINE;
			}

			Arrays.sort( lines );

			String candidate = DEFAULT_SEPARATOR_LINE;
			int candidateCount = 0;

			String currentBest = candidate;
			int currentBestCount = candidateCount;

			for( String line : lines ) {
				line = line.trim();

				if( line.isEmpty() ) {
					continue;
				}

				if( line.equals( candidate ) ) {
					candidateCount += line.length();
					continue;
				}

				if( candidateCount > currentBestCount ) {
					currentBest = candidate;
					currentBestCount = candidateCount;
				}

				if( line.matches( "[-=xX.]+" ) ) {
					candidate = line;
					candidateCount = line.length();
				}
			}

			return currentBest;
		}
	}

}
