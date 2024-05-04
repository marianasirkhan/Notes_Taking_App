package de.dom.noter.framework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public final class FileHelper {

	public static BufferedWriter getBufferedUtf8FileWriter( final File file ) throws IOException {
		return new BufferedWriter( new OutputStreamWriter( new FileOutputStream( file ), "UTF-8" ) );
	}

	public static BufferedReader getBufferedUtf8FileReader( final File file ) throws IOException {
		return new BufferedReader( new InputStreamReader( new FileInputStream( file ), "UTF-8" ) );
	}

	public static void closeIgnoreException( final Writer w ) {
		try {
			w.close();
		}
		catch( final Exception igonre ) {
		}
	}

	public static void closeIgnoreException( final Reader r ) {
		try {
			r.close();
		}
		catch( final Exception igonre ) {
		}
	}

	public static String[] readFileAsUtf8StringArray( final File file ) throws IOException {
		BufferedReader reader = null;
		try {
			reader = getBufferedUtf8FileReader( file );
			final List<String> lines = new ArrayList<String>();
			while( reader.ready() ) {
				lines.add( reader.readLine() );
			}
			return lines.toArray( new String[lines.size()] );
		}
		finally {
			closeIgnoreException( reader );
		}
	}

}
