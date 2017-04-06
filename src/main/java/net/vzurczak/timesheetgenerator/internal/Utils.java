/**
 * Copyright 2014 - Vincent Zurczak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.vzurczak.timesheetgenerator.internal;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Properties;

/**
 * @author Vincent Zurczak
 */
public class Utils {

	/**
	 * Finds the calendar associated with the first day of this week.
	 * @param weekNumber the week number
	 * @param wantedYear the wanted year
	 * @return a calendar
	 */
	public static Calendar findCalendar( int weekNumber, int wantedYear ) {

		final Calendar calendar = Calendar.getInstance();
		int year = wantedYear < 0 ? calendar.get( Calendar.YEAR ) : wantedYear;

		calendar.clear();
		calendar.set( Calendar.WEEK_OF_YEAR, weekNumber );
		calendar.set( Calendar.YEAR, year );
		while( calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY )
		    calendar.add( Calendar.DATE, -1 );

		return calendar;
	}


	/**
	 * @return the current week
	 */
	public static int findCurrentWeek() {
		return Calendar.getInstance().get( Calendar.WEEK_OF_YEAR );
	}


	/**
	 * Reads properties from a file.
	 * @param file a properties file
	 * @return a {@link Properties} instance
	 * @throws IOException if reading failed
	 */
	public static Properties readPropertiesFile( File file ) throws IOException {

		Properties result = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream( file );
			result.load( in );

		} finally {
			if( in != null )
				in.close();
		}

		return result;
	}


	/**
	 * Writes a string into a file.
	 *
	 * @param s the string to write (not null)
	 * @param outputFile the file to write into
	 * @throws IOException if something went wrong
	 */
	public static void writeStringInto( String s, File outputFile ) throws IOException {

		InputStream in = new ByteArrayInputStream( s.getBytes( "UTF-8" ));
		OutputStream os = new FileOutputStream( outputFile );
		try {
			byte[] buf = new byte[ 1024 ];
			int len;
			while((len = in.read( buf )) > 0) {
				os.write( buf, 0, len );
			}

		} finally {
			os.close ();
		}
	}
}
