package de.dom.noter.mvc.model;

import java.util.Collection;

public interface NotesChangedListener {

	void onNotesChanged( Collection<Long> noteIds );

	void onNoteChanged( Long id );
}
