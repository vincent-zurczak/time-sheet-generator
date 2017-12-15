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
import net.vzurczak.timesheetgenerator.internal.Utils;
import net.vzurczak.timesheetgenerator.internal.generation.IDocumentGenerator;
import net.vzurczak.timesheetgenerator.internal.generation.OdtGenerator;
import net.vzurczak.timesheetgenerator.internal.generation.PdfGenerator;

/**
 * @author Vincent Zurczak
 */
public class MainDocumentGenerator {

	/**
	 * The main program.
	 * @param args
	 */
	public static void main( String[] args ) {

		try {
			// Load the schedule properties
			final Properties scheduleProperties = Utils.readPropertiesFile( new File( "./conf/schedule.properties" ));

			// Load the generation's parameters
			GenerationDataBean bean = Utils.parseConfiguration( new File( "./conf/conf.properties" ));

			// Build the title and document name
			String title;
			if( bean.getEndWeek() - bean.getStartWeek() > 1 )
				title = "Feuilles de Temps - Semaines " + String.format( "%02d", bean.getStartWeek()) + " à " + String.format( "%02d", bean.getEndWeek());
			else
				title = "Feuille de Temps - Semaine " + String.format( "%02d", bean.getStartWeek());

			String documentName = title.replace( " à ", "  s" ).replace( " Semaines ", " s" ).replace( " ", "-" ).replaceAll( "-{3,}", "--" ).trim();
			documentName += "--" + bean.getYear();
			title = title.replace( " 0", " " );

			// Generate and save
			IDocumentGenerator gen = "pdf".equalsIgnoreCase( bean.getOutputType()) ? new PdfGenerator() : new OdtGenerator();
			gen.createDocument( bean, scheduleProperties, title, documentName );

			System.out.println( "Fini !" );

		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
}
