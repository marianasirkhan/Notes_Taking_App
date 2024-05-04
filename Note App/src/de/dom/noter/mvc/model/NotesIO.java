package de.dom.noter.mvc.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.dom.noter.framework.FileHelper;
import de.dom.noter.framework.Persistence;

public class NotesIO implements NotesChangedListener {

	private static final String NL = "\n";
	private static final String PRE_TITLE = ">>> ";
	private static final String POST_TITLE = " <<<";
	private static final String PRE_ID = "###";
	private static final String POST_ID = "###";

	private static final String IDS_FILENAME = "ids.list";
	private static final String NOTE_EXTENSION = ".note";
	public static final String DEFAULT_NOTES_SEPARATOR = "---";

	private final Model model;
	private final Persistence persistence;

	public NotesIO(final Model model, final Persistence p) {
		this.model = model;
		persistence = p;
	}

	// ---------------------------------------------------------------------------- top level read methods

	public void readModelFromFiles() {
		final Collection<Long> noteIds = readListOfNoteIds();
		createModelFromNotes( noteIds );
	}

	public static Collection<Long> importNotesFromFile( final File notesFile, final Model model, final String separator ) {
		BufferedReader r = null;
		try {
			r = FileHelper.getBufferedUtf8FileReader( notesFile );
			return importNotesFromReader( model, r, separator );
		}
		catch( final IOException e ) {
			throw new RuntimeException( e );
		}
		finally {
			FileHelper.closeIgnoreException( r );
		}
	}

	// ---------------------------------------------------------------------------- intermediate read methods

	private Collection<Long> readListOfNoteIds() {
		final File idsFile = getIdsFile();
		if( !idsFile.exists() || !idsFile.canRead() ) {
			return Collections.emptyList();
		}

		BufferedReader r = null;
		try {
			r = FileHelper.getBufferedUtf8FileReader( idsFile );
			return readListOfNoteIdsFromReader( r );
		}
		catch( final IOException e ) {
			throw new RuntimeException( e );
		}
		finally {
			FileHelper.closeIgnoreException( r );
		}
	}

	private void createModelFromNotes( final Collection<Long> noteIds ) {
		for( final long id : noteIds ) {
			model.setNote( readNoteFromFile( id, getNoteFile( id ) ) );
		}
	}

	private Note readNoteFromFile( final long id, final File noteFile ) {
		if( !noteFile.exists() || !noteFile.canRead() ) {
			return null;
		}

		BufferedReader r = null;

		try {
			r = FileHelper.getBufferedUtf8FileReader( noteFile );
			return readNoteFromReader( r );
		}
		catch( final IOException e ) {
			throw new RuntimeException( e );
		}
		finally {
			FileHelper.closeIgnoreException( r );
		}
	}

	// ---------------------------------------------------------------------------- Reader methods

	static Collection<Long> readListOfNoteIdsFromReader( final BufferedReader r ) throws IOException {
		final ArrayList<Long> ids = new ArrayList<Long>();
		while( r.ready() ) {
			ids.add( Long.parseLong( r.readLine() ) );
		}
		return ids;
	}

	static Note readNoteFromReader( final BufferedReader r ) throws IOException {
		return readNoteFromReader( r, null );
	}

	static Collection<Long> importNotesFromReader( final Model m, final BufferedReader r, final String separator ) throws IOException {
		final ArrayList<Long> ids = new ArrayList<Long>();
		while( r.ready() ) {
			Note note = readNoteFromReader( r, separator );
			if( m.hasNote( note.getId() ) ) {
				note = note.createNewId();
			}
			m.setNote( note );
			ids.add( note.getId() );
		}

		return Collections.unmodifiableList( ids );
	}

	static Note readNoteFromReader( final BufferedReader r, final String notesSeparator ) throws IOException {
		Note result = new Note();

		final StringBuilder content = new StringBuilder();
		int nullcount = 0;

		String nlSep = "";
		while( r.ready() ) {
			final String line = r.readLine();
			if( null == line ) {
				nullcount += 1;
				if( nullcount > 6 ) {
					break;
				}
				else {
					sleepAWhile( nullcount * 3 );
					continue;
				}
			}
			else if( line.trim().equalsIgnoreCase( notesSeparator ) ) {
				break;
			}
			else if( !result.hasTitle() && line.startsWith( PRE_ID ) && line.endsWith( POST_ID ) ) {
				try {
					final long id = Long.parseLong( line.substring( PRE_ID.length(), line.length() - POST_ID.length() ) );
					result = result.setId( id );
					continue;
				}
				catch( final NumberFormatException ignore ) {
				}
			}
			else if( !result.hasTitle() && line.startsWith( PRE_TITLE ) && line.endsWith( POST_TITLE ) ) {
				result = result.setTitle( line.substring( PRE_TITLE.length(), line.length() - POST_TITLE.length() ) );
				continue;
			}

			content.append( nlSep ).append( line );

			nlSep = NL;
			nullcount = 0;
		}

		final String contentString = content.toString();

		if( !result.hasTitle() ) {
			result = setTitleFromContent( result, contentString );
		}

		return result.setContent( contentString );
	}

	private static Note setTitleFromContent( Note result, final String content ) {
		final String trimmed = content.trim();
		final int firstNlInContent = trimmed.indexOf( NL );
		if( firstNlInContent >= 0 ) {
			result = result.setTitle( trimmed.substring( 0, firstNlInContent ) );
		}
		else {
			result = result.setTitle( trimmed.toString() );
		}
		return result;
	}

	// ---------------------------------------------------------------------------- top level write methods

	@Override
	public void onNotesChanged( final Collection<Long> noteIds ) {
		Writer w = null;
		try {
			w = FileHelper.getBufferedUtf8FileWriter( getIdsFile() );
			writeListOfNoteIdsToWriter( noteIds, w );
		}
		catch( final IOException e ) {
			throw new RuntimeException( e );
		}
		finally {
			FileHelper.closeIgnoreException( w );
		}
	}

	@Override
	public void onNoteChanged( final Long id ) {
		BufferedWriter w = null;
		try {
			w = FileHelper.getBufferedUtf8FileWriter( getNoteFile( id ) );
			writeNoteToWriter( model.getNote( id ), w );
		}
		catch( final IOException e ) {
			throw new RuntimeException( e );
		}
		finally {
			FileHelper.closeIgnoreException( w );
		}
	}

	public static void exportModel( final File notesFile, final Model model ) {
		BufferedWriter w = null;
		try {
			w = FileHelper.getBufferedUtf8FileWriter( notesFile );

			exportModelToWriter( w, model );
		}
		catch( final IOException e ) {
			throw new RuntimeException( e );
		}
		finally {
			FileHelper.closeIgnoreException( w );
		}
	}

	// ---------------------------------------------------------------------------- Writer methods
	static void writeListOfNoteIdsToWriter( final Collection<Long> ids, final Writer w ) throws IOException {
		for( final long id : ids ) {
			w.write( id + NL );
		}
	}

	static void writeNoteToWriter( final Note note, final Writer w ) throws IOException {
		w.write( PRE_ID + note.getId() + POST_ID + NL );

		if( note.hasTitle() ) {
			w.write( PRE_TITLE + note.getTitle() + POST_TITLE + NL );
		}

		if( note.hasContent() ) {
			w.write( note.getContent() + NL );
		}

	}

	static void exportModelToWriter( final Writer w, final Model m ) throws IOException {
		String sep = "";
		for( final long id : m.getNoteIds() ) {
			w.append( sep );
			sep = DEFAULT_NOTES_SEPARATOR + NL;

			final Note note = m.getNote( id );

			writeNoteToWriter( note, w );
		}
	}

	// ---------------------------------------------------------------------------- Helper methods

	private File getIdsFile() {
		return persistence.getDataFile( IDS_FILENAME );
	}

	private File getNoteFile( final long id ) {
		return persistence.getDataFile( Long.toHexString( id ) + NOTE_EXTENSION );
	}

	private static void sleepAWhile( final int timeToSleep ) {
		try {
			Thread.sleep( timeToSleep );
		}
		catch( final InterruptedException ignore ) {
		}
	}

}
