package de.dom.noter.framework;

import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import de.dom.noter.main.Noter;

public class Persistence {

	private static final String DEFAULT_USER_DATA_FOLDER = System.getProperty( "user.home" ) + "/Library/" + Noter.getApplicationQualifiedName();
	private static final String KEY_USER_DATA_FOLDER = "UserDataFolder";

	private File userDataFolder;

	public Persistence() {
		setUserDataFolder( new File( getUserPrefAsString( Persistence.class, KEY_USER_DATA_FOLDER, DEFAULT_USER_DATA_FOLDER ) ) );
	}

	public void setUserDataFolder( final File folder ) {
		userDataFolder = folder;
		ensureFolder( folder );
		setUserPref( Persistence.class, KEY_USER_DATA_FOLDER, folder.getAbsolutePath() );
	}

	private File ensureFolder( final File folder ) {
		if( folder.exists() ) {
			if( !folder.isDirectory() ) {
				throw new IllegalArgumentException( "Given path " + folder.getAbsolutePath() + " is not a directory." );
			}
			if( !folder.canWrite() ) {
				throw new IllegalArgumentException( "Can't write into directory " + folder.getAbsolutePath() );
			}
		}
		else {
			if( folder.mkdirs() == false ) {
				throw new IllegalArgumentException( "Can't create directory " + folder.getAbsolutePath() );
			}
		}

		return folder;
	}

	public File getDataFile( final String filename ) {
		return new File( userDataFolder, filename );
	}

	public String getUserPrefAsString( final Class<?> cls, final String key ) {
		return getUserPrefAsString( cls, key, null );
	}

	public String getUserPrefAsString( final Class<?> cls, final String key, final String defaultValue ) {
		return getPreferences( cls ).get( key, defaultValue );
	}

	public int getUserPrefAsInt( final Class<?> cls, final String key ) {
		return getUserPrefAsInt( cls, key, Integer.MIN_VALUE );
	}

	public int getUserPrefAsInt( final Class<?> cls, final String key, final int defaultValue ) {
		return getPreferences( cls ).getInt( key, defaultValue );
	}

	public void setUserPref( final Class<?> cls, final String key, final String value ) {
		final Preferences prefs = getPreferences( cls );
		prefs.put( key, value );
		flush( prefs );
	}

	public void setUserPref( final Class<?> cls, final String key, final int value ) {
		final Preferences prefs = getPreferences( cls );
		prefs.putInt( key, value );
		flush( prefs );
	}

	private Preferences getPreferences( final Class<?> cls ) {
		final Preferences prefs = Preferences.userNodeForPackage( cls );
		return prefs;
	}

	private void flush( final Preferences prefs ) {
		try {
			prefs.flush();
		}
		catch( final BackingStoreException ignore ) {
			ignore.printStackTrace();
		}
	}

}
