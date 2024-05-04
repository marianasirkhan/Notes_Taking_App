package de.dom.noter.mvc.view;

import java.util.Map;

import de.dom.noter.mvc.controller.NotesController;
import de.dom.noter.mvc.model.NotesChangedListener;

public interface NotesView extends NotesChangedListener {

	void setController( final NotesController notesController );

	NotesController getController();

	Map<Long, ? extends NoteView> getNoteViews();

}
