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

package net.vzurczak.timesheetgenerator.internal.generation;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

import org.odftoolkit.odfdom.dom.element.style.StyleMasterPageElement;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStylePageLayout;
import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.draw.FrameRectangle;
import org.odftoolkit.simple.draw.Image;
import org.odftoolkit.simple.meta.Meta;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.PageLayoutProperties;
import org.odftoolkit.simple.style.StyleTypeDefinitions.CellBordersType;
import org.odftoolkit.simple.style.StyleTypeDefinitions.FontStyle;
import org.odftoolkit.simple.style.StyleTypeDefinitions.HorizontalAlignmentType;
import org.odftoolkit.simple.style.StyleTypeDefinitions.SupportedLinearMeasure;
import org.odftoolkit.simple.style.StyleTypeDefinitions.VerticalAlignmentType;
import org.odftoolkit.simple.table.Table;
import org.odftoolkit.simple.text.Paragraph;

import com.itextpdf.text.DocumentException;

import net.vzurczak.timesheetgenerator.internal.GenerationDataBean;
import net.vzurczak.timesheetgenerator.internal.Utils;

/**
 * @author Vincent Zurczak
 */
public class OdtGenerator implements IDocumentGenerator {

	private static final double MAX_SIG_WIDTH = 5.6;

	/*
	 * (non-Javadoc)
	 * @see net.vzurczak.timesheetgenerator.internal.generation.IDocumentGenerator
	 * #createDocument(net.vzurczak.timesheetgenerator.internal.GenerationDataBean, java.util.Properties, java.lang.String, java.lang.String)
	 */
	@Override
	public void createDocument( GenerationDataBean bean, Properties scheduleProperties, String documentTitle, String documentName )
			throws Exception {

		// Create the document
		TextDocument doc = TextDocument.newTextDocument();

		Meta metadata = new Meta( doc.getMetaDom());
		metadata.setCreator( bean.getName());
		metadata.setTitle( documentTitle );
		metadata.setSubject( documentTitle );

		// Page orientation
		for( Iterator<StyleMasterPageElement> it = doc.getOfficeMasterStyles().getMasterPages(); it.hasNext(); ) {
			StyleMasterPageElement page = it.next();
			String pageLayoutName = page.getStylePageLayoutNameAttribute();
			OdfStylePageLayout pageLayoutStyle = page.getAutomaticStyles().getPageLayout( pageLayoutName );
			PageLayoutProperties pageLayoutProps = PageLayoutProperties.getOrCreatePageLayoutProperties( pageLayoutStyle );

			double tmp = pageLayoutProps.getPageWidth();
			pageLayoutProps.setPageWidth( pageLayoutProps.getPageHeight());
			pageLayoutProps.setPageHeight( tmp );
		}

		// Content
		for( int i=bean.getStartWeek(); i<=bean.getEndWeek(); i++ ) {
			addPageForWeek( i, doc, bean, scheduleProperties );
			if( i<= bean.getEndWeek())
				doc.addPageBreak();
		}

		// Save the document
		File outputFile = new File( "./odt/" + documentName + ".odt" );
		if( ! outputFile.getParentFile().exists()
				&& ! outputFile.getParentFile().mkdirs())
			throw new IOException( outputFile.getParentFile() + " could not be created." );

		doc.save( outputFile );
	}


	/**
	 * Adds a page for a given week.
	 * @param i the week number
	 * @param doc the document to update
	 * @param bean a generation bean (not null)
	 * @param scheduleProperties
	 * @throws DocumentException
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	private void addPageForWeek( int weekNumber, TextDocument doc, GenerationDataBean bean, Properties scheduleProperties  )
			throws MalformedURLException, IOException, URISyntaxException {

		Calendar calendar = Utils.findCalendar( weekNumber, bean.getYear());
		Font boldFont = new Font( "Arial", FontStyle.BOLD, 12, Color.BLACK );

		// Title
		Paragraph paragraph = doc.addParagraph( "Bordereau de Déclaration des Temps" );
		paragraph.setFont( boldFont );
		paragraph.setHorizontalAlignment( HorizontalAlignmentType.CENTER );

		doc.addParagraph( " " );
		doc.addParagraph( " " );


		// Meta: week and date
		Calendar endOfWeekCalendar = ((Calendar) calendar.clone());
		endOfWeekCalendar.add( Calendar.DATE, 4 );
		String formattedDate = new SimpleDateFormat( "dd/MM/yyyy" ).format( endOfWeekCalendar.getTime());

		Table metaTable = doc.addTable( 2, 2 );
		metaTable.getRowByIndex( 0 ).getCellByIndex( 0 ).setDisplayText( "Semaine : " );
		metaTable.getRowByIndex( 0 ).getCellByIndex( 0 ).setFont( boldFont );
		metaTable.getRowByIndex( 0 ).getCellByIndex( 1 ).setDisplayText( String.valueOf( weekNumber ));

		metaTable.getRowByIndex( 1 ).getCellByIndex( 0 ).setDisplayText( "Date : " );
		metaTable.getRowByIndex( 1 ).getCellByIndex( 0 ).setFont( boldFont );
		metaTable.getRowByIndex( 1 ).getCellByIndex( 1 ).setDisplayText( formattedDate );

		metaTable.getColumnByIndex( 0 ).setWidth( 25 );
		invisibleBorders( metaTable );

		doc.addParagraph( "" );
		doc.addParagraph( "" );


		// Signatures
		Table signatureTable = doc.addTable( 2, 4 );
		signatureTable.getRowByIndex( 0 ).getCellByIndex( 0 ).setDisplayText( "Nom : " );
		signatureTable.getRowByIndex( 0 ).getCellByIndex( 0 ).setFont( boldFont );
		signatureTable.getRowByIndex( 0 ).getCellByIndex( 1 ).setDisplayText( bean.getName());

		signatureTable.getRowByIndex( 1 ).getCellByIndex( 0 ).setDisplayText( "Signature : " );
		signatureTable.getRowByIndex( 1 ).getCellByIndex( 0 ).setFont( boldFont );

		signatureTable.getRowByIndex( 0 ).getCellByIndex( 2 ).setDisplayText( "Responsable : " );
		signatureTable.getRowByIndex( 0 ).getCellByIndex( 2 ).setFont( boldFont );
		signatureTable.getRowByIndex( 0 ).getCellByIndex( 3 ).setDisplayText( bean.getManagerName());

		signatureTable.getRowByIndex( 1 ).getCellByIndex( 2 ).setDisplayText( "Signature : " );
		signatureTable.getRowByIndex( 1 ).getCellByIndex( 2 ).setFont( boldFont );

		signatureTable.getColumnByIndex( 0 ).setWidth( 25 );
		signatureTable.getColumnByIndex( 2 ).setWidth( 35 );
		invisibleBorders( signatureTable );


		// Signature image
		Table signatureImageTable = doc.addTable( 1, 5 );
		signatureImageTable.getColumnByIndex( 0 ).setWidth( 25 );	// Aligned with the column above ("nom")
		signatureImageTable.getColumnByIndex( 1 ).setWidth( 30 );	// The column to contain the signature
		signatureImageTable.getColumnByIndex( 2 ).setWidth( 70 );	// Padding to (almost) reach the centered column above
		signatureImageTable.getColumnByIndex( 3 ).setWidth( 35 );	// Aligned (almost) with the column above ("responsable")
		invisibleBorders( signatureImageTable );

		Random randomGenerator = new Random();
		if( ! bean.signatures.isEmpty()) {
			int random = randomGenerator.nextInt( bean.signatures.size());
			Paragraph imageParagraph = signatureImageTable.getRowByIndex( 0 ).getCellByIndex( 1 ).addParagraph( "" );
			Image image = Image.newImage(
					imageParagraph,
					bean.signatures.get( random ).toURI());

			// Scale the image so that it is not too big
			random = randomGenerator.nextInt();
			double width = MAX_SIG_WIDTH - (random * random * 37) % 3;
			double ratio = image.getRectangle().getWidth() / width;
			double newHeight = image.getRectangle().getHeight() / ratio;

			image.setRectangle( new FrameRectangle( 0, 0, width, newHeight, SupportedLinearMeasure.CM ));
		}

		// TODO: add a place holder for the manager's signature
		//Paragraph imageParagraph = signatureImageTable.getRowByIndex( 0 ).getCellByIndex( 1 ).addParagraph( "" );
		//Image image = Image.newImage( imageParagraph, new URI( "https://www.google.fr/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png" ));
		//image.getOdfElement().setAttribute( "xlink:href", "https://www.google.fr/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png" );

		doc.addParagraph( "" );
		doc.addParagraph( "" );
		doc.addParagraph( "" );


		// Calendar
		Table calendarTable = doc.addTable( 2, 7 );
		calendarTable.getRowByIndex( 1 ).setHeight( 30, true );
		for( int i=0; i<calendarTable.getRowCount(); i++ ) {
			for( int j=0; j<calendarTable.getColumnCount(); j++ ) {
				calendarTable.getRowByIndex( i ).getCellByIndex( j ).setHorizontalAlignment( HorizontalAlignmentType.CENTER );
				calendarTable.getRowByIndex( i ).getCellByIndex( j ).setVerticalAlignment( VerticalAlignmentType.MIDDLE );
			}
		}

		for( int i=0; i<5; i++ ) {
			final String date = new SimpleDateFormat( "EEEE d MMMM yyyy" ).format( calendar.getTime());
			calendarTable.getRowByIndex( 0 ).getCellByIndex( i + 1 ).setDisplayText( date );
			calendar.add( Calendar.DATE, 1 );
		}

		calendarTable.getRowByIndex( 0 ).getCellByIndex( 6 ).setDisplayText( "Total" );
		calendarTable.getRowByIndex( 1 ).getCellByIndex( 0 ).setDisplayText( "Heures Effectuées" );

		int total = 0;
		boolean daysOff = false;

		calendar.add( Calendar.DATE, -5 );
		for( int i=0; i<5; i++ ) {
			String key = new SimpleDateFormat( "dd_MM_yyyy" ).format( calendar.getTime());
			String value = scheduleProperties.getProperty( key );

			if( ! value.matches( "\\d+" )) {
				value = "0\n\n" + value;
				daysOff = true;
			} else {
				total += Integer.parseInt( value );
			}

			calendarTable.getRowByIndex( 1 ).getCellByIndex( i + 1 ).setDisplayText( value );
			calendar.add( Calendar.DATE, 1 );
		}

		if( total > bean.getTotalHours())
			throw new IOException( "Too many hours, you were supposed to do " + bean.getTotalHours() + " hours..." );
		else if( ! daysOff && total != bean.getTotalHours())
			throw new IOException( "Wrong schedule, you were supposed to do EXACTLY " + bean.getTotalHours() + " hours..." );

		calendarTable.getRowByIndex( 1 ).getCellByIndex( 6 ).setDisplayText( "pom" + total + " h" );
	}


	private void invisibleBorders( Table table ) {

		for( int i=0; i<table.getRowCount(); i++ ) {
			for( int j=0; j<table.getColumnCount(); j++ ) {
				table.getRowByIndex( i ).getCellByIndex( j ).setBorders( CellBordersType.NONE, null );
			}
		}
	}
}
