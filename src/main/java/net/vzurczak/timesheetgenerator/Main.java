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

package net.vzurczak.timesheetgenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.itextpdf.text.DocumentException;

/**
 * @author Vincent Zurczak
 */
public class Main {

	/**
	 * The main program.
	 * @param args
	 */
	public static void main( String[] args ) {

		// Check arguments
		int startWeek = -1;
		int endWeek = Utils.findCurrentWeek();
		if( args.length == 0 ) {
			startWeek = endWeek;

		} else if( args.length == 1 ) {
			startWeek = Integer.parseInt( args[ 0 ]);

		} else if( args.length == 2 ) {
			startWeek = Integer.parseInt( args[ 0 ]);
			endWeek = Integer.parseInt( args[ 1 ]);

		} else {
			System.err.println( "Ha ! Ha ! Ha ! C'est pas les bons paramètres." );
			System.exit( 1 );
		}

		// Make a basic check
		if( startWeek > endWeek ) {
			System.err.println( "La semaine de départ doit précéder la semaine de fin..." );
			System.exit( 1 );
		}

		// Properties
		final Properties props = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream( new File( "./conf/conf.properties" ));
			props.load( in );

		} catch( final IOException e ) {
			e.printStackTrace();

		} finally {
			try {
				if( in != null )
					in.close();

			} catch( final IOException e ) {
				// nothing
			}
		}

		// Prepare the generation
		GenerationDataBean bean = new GenerationDataBean();
		bean.setEndWeek( endWeek );
		bean.setStartWeek( startWeek );
		bean.setName( props.getProperty( "your.name", "" ));
		bean.setManagerName( props.getProperty( "your.manager", "" ));

		String totalHoursAS = props.getProperty( "your.time", "-1" ).trim();
		if( ! totalHoursAS.isEmpty())
			bean.setTotalHours( Integer.parseInt( totalHoursAS ));

		// Generate and save
		try {
			new PdfGenerator().createDocument( bean );

		} catch( FileNotFoundException e ) {
			e.printStackTrace();

		} catch( DocumentException e ) {
			e.printStackTrace();
		}
	}
}
