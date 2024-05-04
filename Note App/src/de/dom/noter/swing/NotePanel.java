package de.dom.noter.swing;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.dom.noter.framework.Timer;
import de.dom.noter.framework.Timer.TimerListener;
import de.dom.noter.mvc.controller.NoteController;
import de.dom.noter.mvc.controller.command.UCSetContent;
import de.dom.noter.mvc.controller.command.UCSetTitle;
import de.dom.noter.mvc.model.Note;
import de.dom.noter.mvc.view.NoteView;
import de.dom.noter.swing.action.AbstractNoterAction;

public class NotePanel extends JPanel implements NoteView {

	private static final int TYPING_PAUSE = 500;

	private static final long serialVersionUID = -28761231700267765L;

	private NoteController noteController;

	private final JTextField labelTitle;
	final TimerListener doSetTitleAction = new TimerListener() {
		@Override
		public void onTimerFired() {
			mainWindow.doCommand( new UCSetTitle( noteController, labelTitle.getText() ) );
		}
	};

	private final JTextArea areaContent;
	final TimerListener doSetContentAction = new TimerListener() {
		@Override
		public void onTimerFired() {
			mainWindow.doCommand( new UCSetContent( noteController, areaContent.getText() ) );
		}
	};

	private final Timer timer;

	private final long id;

	private final MainWindow mainWindow;

	public NotePanel(final MainWindow mainWindow, final Timer timer, final long id) {
		super();
		this.mainWindow = mainWindow;
		this.timer = timer;
		this.id = id;

		labelTitle = new JTextField();
		areaContent = new JTextArea();

		final GroupLayout layout = new GroupLayout( this );
		setLayout( layout );

		layout.setAutoCreateGaps( true );
		layout.setAutoCreateContainerGaps( true );

		labelTitle.setFont( new Font( "SansSerif", Font.BOLD, 16 ) );
		labelTitle.setSize( new Dimension( 100, 20 ) );
		labelTitle.addKeyListener( new KeyListener() {

			@Override
			public void keyTyped( final KeyEvent arg0 ) {
				timer.fireTimer( TYPING_PAUSE, doSetTitleAction );
			}

			@Override
			public void keyReleased( final KeyEvent arg0 ) {
			}

			@Override
			public void keyPressed( final KeyEvent arg0 ) {
			}
		} );

		labelTitle.addFocusListener( new FocusListener() {

			@Override
			public void focusLost( final FocusEvent e ) {
				timer.fireTimer( 0, doSetTitleAction );
			}

			@Override
			public void focusGained( final FocusEvent e ) {
			}
		} );

		labelTitle.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed( final ActionEvent e ) {
				areaContent.requestFocusInWindow();
			}
		} );

		final AbstractNoterAction removeAction = new AbstractNoterAction() {
			{
				putValue( Action.NAME, "–" );
			}

			@Override
			public void actionPerformed( final ActionEvent e ) {
				noteController.removeNote();
			}
		};
		final JButton removeButton = new JButton( removeAction );

		final JPanel buttonBar = new JPanel();
		buttonBar.add( removeButton );
		add( buttonBar );

		areaContent.setFont( new Font( "Monospaced", Font.PLAIN, 12 ) );
		areaContent.addKeyListener( new KeyListener() {

			@Override
			public void keyTyped( final KeyEvent arg0 ) {
				timer.fireTimer( TYPING_PAUSE, doSetContentAction );
			}

			@Override
			public void keyReleased( final KeyEvent arg0 ) {
			}

			@Override
			public void keyPressed( final KeyEvent arg0 ) {
			}
		} );

		areaContent.addFocusListener( new FocusListener() {

			@Override
			public void focusLost( final FocusEvent e ) {
				timer.fireTimer( 0, doSetContentAction );
			}

			@Override
			public void focusGained( final FocusEvent e ) {
			}
		} );

		layout.setHorizontalGroup( layout.createParallelGroup()
				.addGroup( layout.createSequentialGroup().addComponent( removeButton ).addComponent( labelTitle ) ).addComponent( areaContent ) );
		layout.setVerticalGroup( layout.createSequentialGroup().addGroup( layout.createParallelGroup().addComponent( removeButton ).addComponent( labelTitle ) )
				.addComponent( areaContent ) );

	}

	@Override
	public void onNoteChanged( final Note newNote ) {
		final String title = newNote.getTitle();
		setTitle( title == null ? "<Titel>" : title );
		setContent( newNote.getContent() );
	}

	private void setContent( final String content ) {
		if( !areaContent.getText().equals( content ) ) {
			areaContent.setText( content );
		}
	}

	private void setTitle( final String title ) {
		if( !labelTitle.getText().equals( title ) ) {
			labelTitle.setText( title );
		}
	}

	@Override
	public void setController( final NoteController newNoteController ) {
		noteController = newNoteController;
	}

	Timer getTimer() {
		return timer;
	}

	public long getId() {
		return id;
	}

	public void selectTitle() {
		labelTitle.requestFocusInWindow();
	}

}
