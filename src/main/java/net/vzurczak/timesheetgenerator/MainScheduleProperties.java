/**
 * Copyright 2017 Linagora, Université Joseph Fourier, Floralis
 *
 * The present code is developed in the scope of the joint LINAGORA -
 * Université Joseph Fourier - Floralis research program and is designated
 * as a "Result" pursuant to the terms and conditions of the LINAGORA
 * - Université Joseph Fourier - Floralis research program. Each copyright
 * holder of Results enumerated here above fully & independently holds complete
 * ownership of the complete Intellectual Property rights applicable to the whole
 * of said Results, and may freely exploit it in any manner which does not infringe
 * the moral rights of the other copyright holders.
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import net.vzurczak.timesheetgenerator.internal.GenerationDataBean;
import net.vzurczak.timesheetgenerator.internal.Utils;

/**
 * @author Vincent Zurczak - Linagora
 */
public class MainScheduleProperties {

	/**
	 * The main program.
	 * @param args
	 */
	public static void main( String[] args ) {

		try {
			// Prepare the generation
			GenerationDataBean bean = Utils.parseConfiguration( new File( "./conf/conf.properties" ));

			// Let's go...
			StringBuilder sb = new StringBuilder();
			for( int i=bean.getStartWeek(); i<=bean.getEndWeek(); i++ ) {
				sb.append( "# Week " );
				sb.append( i );
				sb.append( "\n" );

				// Little hack: insert default values (exclude Friday)
				int random = new Random().nextInt( 4 );
				// End of little hack

				Calendar calendar = Utils.findCalendar( i, bean.getYear());
				for( int j=0; j<5; j++ ) {
					String key = new SimpleDateFormat( "dd_MM_yyyy" ).format( calendar.getTime());
					sb.append( key );
					sb.append( " = " );
					sb.append( j == random ? 8 : 7 );
					sb.append( "\n" );
					calendar.add( Calendar.DATE, 1 );
				}

				sb.append( "\n" );
			}

			// Let's write it
			Utils.writeStringInto( sb.toString(), new File( "./conf/schedule.properties" ));

		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
}
