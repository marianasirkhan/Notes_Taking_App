package de.dom.noter.mvc.model;

public class Note extends Data {

	private static final String UNTITLED = new String( "<untitled>" );
	private static final String NO_CONTENT = new String( "" );

	private String title;
	private String content;

	public Note() {
		super();
		init( UNTITLED, NO_CONTENT );
	}

	public Note(final long id) {
		super( id );
		init( UNTITLED, NO_CONTENT );
	}

	private void init( final String titleString, final String contentString ) {
		title = titleString;
		content = contentString;
	}

	private Note(final Note note) {
		this( note.getId(), note );
	}

	private Note(final long newId, final Note note) {
		super( newId, note );
		init( note.title, note.content );
	}

	public Note setTitle( final String newTitle ) {
		final Note n = new Note( this );
		n.title = ((newTitle == null) || (newTitle.isEmpty())) ? UNTITLED : newTitle;
		return n;
	}

	public String getTitle() {
		return title;
	}

	public Note setContent( final String newContent ) {
		final Note n = new Note( this );
		n.content = ((newContent == null) || (newContent.isEmpty())) ? NO_CONTENT : newContent.replace( "\r", "" );
		return n;
	}

	public String getContent() {
		return content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals( final Object obj ) {
		if( this == obj ) {
			return true;
		}
		if( !super.equals( obj ) ) {
			return false;
		}
		final Note other = (Note) obj;

		if( hasContent() != other.hasContent() ) {
			return false;
		}
		else if( !content.equals( other.content ) ) {
			return false;
		}

		if( hasTitle() != other.hasTitle() ) {
			return false;
		}
		else if( !title.equals( other.title ) ) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return title + " (" + getId() + ")";
	}

	public boolean hasTitle() {
		return UNTITLED != title;
	}

	public boolean hasContent() {
		return NO_CONTENT != content;
	}

	public Note setId( final long newId ) {
		if( newId == getId() ) {
			return this;
		}
		else {
			return new Note( newId, this );
		}
	}

	public Note createNewId() {
		return new Note( getNextId(), this );
	}

}
