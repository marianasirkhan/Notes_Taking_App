package de.dom.noter.mvc.model;

import java.io.BufferedReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

public class NotesIOTest extends TestCase {

	final String NL = "\n";
	final String START_TITLE = ">>> ";
	final String END_TITLE = " <<<";
	final String START_ID = "###";
	final String END_ID = "###";

	final long id = 0x1DL;
	final String title = "title";
	final String content1 = "content";
	final String content2 = "content2";
	final String content = content1 + NL + content2;

	final String idString = START_ID + id + END_ID;
	final String titleString = START_TITLE + title + END_TITLE;
	final String contentString = content;

	BufferedReader r;
	Writer w;

	@Override
	protected void setUp() throws Exception {
		w = new PipedWriter();
		r = new BufferedReader( new PipedReader( (PipedWriter) w ) );
	}

	@Override
	protected void tearDown() throws Exception {
		r.close();
		w.close();
	}

	public void testListOfNoteIds() throws Exception {
		final List<Long> ids = Arrays.asList( 1L, 2L, 3L );
		NotesIO.writeListOfNoteIdsToWriter( ids, w );
		assertEquals( ids, NotesIO.readListOfNoteIdsFromReader( r ) );
	}

	public void testEmptyListOfNoteIds() throws Exception {
		final List<Long> ids = Collections.emptyList();
		NotesIO.writeListOfNoteIdsToWriter( ids, w );
		assertEquals( ids, NotesIO.readListOfNoteIdsFromReader( r ) );
	}

	public void testNoteEmpty() throws Exception {
		final Note note = new Note();
		NotesIO.writeNoteToWriter( note, w );
		final Note noteFromReader = NotesIO.readNoteFromReader( r );
		assertEquals( note, noteFromReader );
	}

	public void testNote() throws Exception {
		final Note note = new Note().setTitle( "t" ).setContent( "c\n" );
		NotesIO.writeNoteToWriter( note, w );
		final Note noteFromReader = NotesIO.readNoteFromReader( r );

		assertEquals( note, noteFromReader );
	}

	public void testNoteEol() throws Exception {
		final Note note = new Note().setTitle( "t" ).setContent( "c\nd\n\re\r\nf" );
		NotesIO.writeNoteToWriter( note, w );
		final Note noteFromReader = NotesIO.readNoteFromReader( r );

		assertEquals( note, noteFromReader );
	}

	public void testModelEmpty() throws Exception {
		final Model model = new Model();
		NotesIO.exportModelToWriter( w, model );
		final Model modelFromReader = new Model();
		NotesIO.importNotesFromReader( modelFromReader, r, null );

		assertEquals( model, modelFromReader );
	}

	public void testModel1() throws Exception {
		final Model model = new Model();
		model.createNote();
		NotesIO.exportModelToWriter( w, model );
		final Model modelFromReader = new Model();
		NotesIO.importNotesFromReader( modelFromReader, r, NotesIO.DEFAULT_NOTES_SEPARATOR );

		assertEquals( model, modelFromReader );
	}

	public void testModel2() throws Exception {
		final Model model = new Model();
		model.createNote();
		model.createNote();
		NotesIO.exportModelToWriter( w, model );
		final Model modelFromReader = new Model();
		NotesIO.importNotesFromReader( modelFromReader, r, NotesIO.DEFAULT_NOTES_SEPARATOR );

		assertEquals( model, modelFromReader );
	}

	public void testModelReadDouble() throws Exception {
		final Model model = new Model();
		model.createNote();
		model.createNote();
		final Model modelFromReader = new Model();

		NotesIO.exportModelToWriter( w, model );
		NotesIO.importNotesFromReader( modelFromReader, r, NotesIO.DEFAULT_NOTES_SEPARATOR );

		NotesIO.exportModelToWriter( w, model );
		NotesIO.importNotesFromReader( modelFromReader, r, NotesIO.DEFAULT_NOTES_SEPARATOR );

		assertEquals( model.size() * 2, modelFromReader.size() );
	}

	public void testNoteFormatFull() throws Exception {
		final String txt = idString + NL + titleString + NL + contentString;
		final Note note = NotesIO.readNoteFromReader( new BufferedReader( new StringReader( txt ) ) );
		assertEquals( id, note.getId() );
		assertEquals( title, note.getTitle() );
		assertEquals( content, note.getContent() );
	}

	public void testNoteFormatMissingId() throws Exception {
		final String txt = titleString + NL + contentString;
		final Note note = NotesIO.readNoteFromReader( new BufferedReader( new StringReader( txt ) ) );
		assertEquals( title, note.getTitle() );
		assertEquals( content, note.getContent() );

		final Note note2 = NotesIO.readNoteFromReader( new BufferedReader( new StringReader( txt ) ) );
		assertFalse( note.getId() == note2.getId() );
	}

	public void testNoteFormatMissingTitle() throws Exception {
		final String txt = idString + NL + contentString;
		final Note note = NotesIO.readNoteFromReader( new BufferedReader( new StringReader( txt ) ) );
		assertEquals( id, note.getId() );
		assertEquals( content1, note.getTitle() );
		assertEquals( content, note.getContent() );
	}

	public void testNoteFormatMissingContent() throws Exception {
		final String txt = idString + NL + titleString;
		final Note note = NotesIO.readNoteFromReader( new BufferedReader( new StringReader( txt ) ) );
		assertEquals( id, note.getId() );
		assertEquals( title, note.getTitle() );
		assertFalse( note.hasContent() );
	}

	public void testNoteOnlyId() throws Exception {
		final String txt = idString;
		final Note note = NotesIO.readNoteFromReader( new BufferedReader( new StringReader( txt ) ) );
		assertEquals( id, note.getId() );
		assertFalse( note.hasTitle() );
		assertFalse( note.hasContent() );
	}

	public void testNoteFormatOnlyTitle() throws Exception {
		final String txt = titleString;
		final Note note = NotesIO.readNoteFromReader( new BufferedReader( new StringReader( txt ) ) );
		assertEquals( title, note.getTitle() );
		assertFalse( note.hasContent() );

		final Note note2 = NotesIO.readNoteFromReader( new BufferedReader( new StringReader( txt ) ) );
		assertFalse( note.getId() == note2.getId() );
	}

	public void testNoteFormatOnlyContent() throws Exception {
		final String txt = contentString;
		final Note note = NotesIO.readNoteFromReader( new BufferedReader( new StringReader( txt ) ) );
		assertEquals( content1, note.getTitle() );
		assertEquals( content, note.getContent() );

		final Note note2 = NotesIO.readNoteFromReader( new BufferedReader( new StringReader( txt ) ) );
		assertFalse( note.getId() == note2.getId() );
	}

	public void testNoteFormatSingleLineContent() throws Exception {
		final String txt = content1;
		final Note note = NotesIO.readNoteFromReader( new BufferedReader( new StringReader( txt ) ) );
		assertEquals( content1, note.getTitle() );
		assertEquals( content1, note.getContent() );

		final Note note2 = NotesIO.readNoteFromReader( new BufferedReader( new StringReader( txt ) ) );
		assertFalse( note.getId() == note2.getId() );
	}

	public void testNoteFormatContentStartsWithNL() throws Exception {
		final String txt = NL + content1;
		final Note note = NotesIO.readNoteFromReader( new BufferedReader( new StringReader( txt ) ) );
		assertEquals( content1, note.getTitle() );
		assertEquals( NL + content1, note.getContent() );

		final Note note2 = NotesIO.readNoteFromReader( new BufferedReader( new StringReader( txt ) ) );
		assertFalse( note.getId() == note2.getId() );
	}

}
