package de.dom.noter.mvc.view;

import de.dom.noter.mvc.controller.NoteController;
import de.dom.noter.mvc.model.NoteChangedListener;

public interface NoteView extends NoteChangedListener {

	void setController( final NoteController noteController );

}
