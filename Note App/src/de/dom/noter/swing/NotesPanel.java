package de.dom.noter.swing;

import java.awt.LayoutManager;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import de.dom.noter.framework.Timer;

public class NotesPanel extends JPanel {

	private static final long serialVersionUID = -5580939459033087664L;

	private Map<Long, NotePanel> notePanels;

	private final MainWindow mainWindow;

	private final Timer timer;

	public NotesPanel(final MainWindow mainWindow) {
		super();
		final LayoutManager layout = new BoxLayout( this, BoxLayout.PAGE_AXIS );
		setLayout( layout );
		this.mainWindow = mainWindow;
		timer = new Timer();
		timer.start();
		notePanels = new LinkedHashMap<Long, NotePanel>();
	}

	private void setPanels( final Map<Long, NotePanel> newPanels ) {
		final Iterator<NotePanel> newPanelsIterator = newPanels.values().iterator();
		int panelComponentIndex = getComponentCount() - 1;

		while( panelComponentIndex >= 0 && newPanelsIterator.hasNext() ) {
			final NotePanel newPanel = newPanelsIterator.next();
			final NotePanel oldPanel = (NotePanel) getComponent( panelComponentIndex );

			if( newPanel != oldPanel ) {
				add( newPanel, panelComponentIndex );
			}

			panelComponentIndex -= 1;
		}

		while( panelComponentIndex >= 0 ) {
			remove( panelComponentIndex );
			panelComponentIndex -= 1;
		}

		while( newPanelsIterator.hasNext() ) {
			add( newPanelsIterator.next(), 0 );
		}

		notePanels = newPanels;

		mainWindow.validate();
		mainWindow.repaint();
	}

	public void onNotesChanged( final Collection<Long> newNoteIds ) {
		setPanels( copyAndCreatePanels( newNoteIds ) );
	}

	private Map<Long, NotePanel> copyAndCreatePanels( final Collection<Long> newNoteIds ) {
		final Map<Long, NotePanel> newPanels = new LinkedHashMap<Long, NotePanel>();

		for( final long id : newNoteIds ) {
			NotePanel panel = notePanels.get( id );
			if( null == panel ) {
				panel = new NotePanel( mainWindow, timer, id );
			}
			newPanels.put( id, panel );
		}

		return newPanels;
	}

	public Map<Long, NotePanel> getNotePanels() {
		return Collections.unmodifiableMap( notePanels );
	}

}
