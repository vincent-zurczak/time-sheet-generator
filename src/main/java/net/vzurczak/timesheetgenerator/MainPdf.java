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
import java.util.Properties;

import net.vzurczak.timesheetgenerator.internal.GenerationDataBean;
import net.vzurczak.timesheetgenerator.internal.PdfGenerator;
import net.vzurczak.timesheetgenerator.internal.Utils;

/**
 * @author Vincent Zurczak
 */
public class MainPdf {

	/**
	 * The main program.
	 * @param args
	 */
	public static void main( String[] args ) {

		try {
			// Properties
			final Properties props = Utils.readPropertiesFile( new File( "./conf/conf.properties" ));
			final Properties scheduleProperties = Utils.readPropertiesFile( new File( "./conf/schedule.properties" ));

			// Prepare the generation
			GenerationDataBean bean = new GenerationDataBean();
			bean.setEndWeek( Integer.parseInt( props.getProperty( "week.end", "-1" )));
			bean.setStartWeek( Integer.parseInt(props.getProperty( "week.start", "1" )));
			bean.setYear( Integer.parseInt(props.getProperty( "year", "" )));
			bean.setName( props.getProperty( "your.name", "" ));
			bean.setManagerName( props.getProperty( "your.manager", "" ));

			String totalHoursAS = props.getProperty( "your.time", "-1" ).trim();
			if( ! totalHoursAS.isEmpty())
				bean.setTotalHours( Integer.parseInt( totalHoursAS ));

			// Signatures
			for( String s : props.getProperty( "signatures", "" ).split( "," )) {
				File f = new File( s.trim());
				if( f.exists())
					bean.signatures.add( f );
			}

			// Generate and save
			new PdfGenerator().createDocument( bean, scheduleProperties );

		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
}
