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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author Vincent Zurczak
 */
public class PdfGenerator {

	final SimpleDateFormat sdf = new SimpleDateFormat( "EEEE d MMMM yyyy" );


	/**
	 * Creates a PDF document.
	 * @param bean a generation bean (not null)
	 * @param scheduleProperties the details of the schedule (not null)
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public void createDocument( GenerationDataBean bean, Properties scheduleProperties )
	throws Exception {

		// File name
		StringBuilder sb = new StringBuilder();
		sb.append( "Feuille-De-Temps--s" );
		sb.append( String.format( "%02d", bean.getStartWeek()));
		sb.append( "--s" );
		sb.append( String.format( "%02d", bean.getEndWeek()));
		sb.append( "--" );
		sb.append( bean.getYear());
		sb.append( ".pdf" );

		// Create the document
		File outputFile = new File( "./pdf/" + sb.toString());
		final Document doc = new Document( PageSize.A4.rotate());
		PdfWriter.getInstance( doc, new FileOutputStream( outputFile ));

		doc.open();
		doc.addAuthor( bean.getName());
		doc.addCreator( bean.getName());

		String s;
		if( bean.getEndWeek() - bean.getStartWeek() > 1 )
			s = "Feuilles de Temps - Semaines " + bean.getStartWeek() + " à " + bean.getEndWeek();
		else
			s = "Feuille de Temps - Semaine " + bean.getStartWeek();

		doc.addTitle( s );
		doc.addSubject( s );

		// Add pages
		for( int i=bean.getStartWeek(); i<=bean.getEndWeek(); i++ )
			addPageForWeek( i, doc, bean, scheduleProperties );

		// That's it!
		doc.close();
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
	 */
	private void addPageForWeek( int weekNumber, Document doc, GenerationDataBean bean, Properties scheduleProperties  )
	throws DocumentException, MalformedURLException, IOException {

		doc.newPage();
		final Font boldFont = FontFactory.getFont( FontFactory.HELVETICA_BOLD );
		final Font normalFont = FontFactory.getFont( FontFactory.HELVETICA );
		Calendar calendar = Utils.findCalendar( weekNumber, bean.getYear());

		// Title
		Paragraph paragraph = new Paragraph( "Bordereau de Déclaration des Temps", boldFont );
		paragraph.setAlignment( Element.ALIGN_CENTER );
		doc.add( paragraph );

		doc.add( new Paragraph( " " ));
		doc.add( new Paragraph( " " ));


		// Meta: week
		final PdfPTable metaTable = new PdfPTable( 1 );

		paragraph = new Paragraph();
		paragraph.add( new Chunk( "Semaine : ", boldFont ));
		paragraph.add( new Chunk( String.valueOf( weekNumber ), normalFont ));

		PdfPCell c = new PdfPCell( paragraph );
		c.setBorder( Rectangle.NO_BORDER );
		metaTable.addCell( c );

		// Meta: date
		Calendar endOfWeekCalendar = ((Calendar) calendar.clone());
		endOfWeekCalendar.add( Calendar.DATE, 4 );
		String formattedDate = new SimpleDateFormat( "dd/MM/yyyy" ).format( endOfWeekCalendar.getTime());

		paragraph = new Paragraph();
		paragraph.add( new Chunk( "Date : ", boldFont ));
		paragraph.add( new Chunk( formattedDate, normalFont ));

		c = new PdfPCell( paragraph );
		c.setBorder( Rectangle.NO_BORDER );
		metaTable.addCell( c );

		doc.add( metaTable );
		doc.add( new Paragraph( " " ));
		doc.add( new Paragraph( " " ));


		// Signatures
		final PdfPTable signaturesTable = new PdfPTable( 2 );
		paragraph = new Paragraph();
		paragraph.add( new Chunk( "Nom : ", boldFont ));
		paragraph.add( new Chunk( bean.getName(), normalFont ));

		c = new PdfPCell( paragraph );
		c.setBorder( Rectangle.NO_BORDER );
		signaturesTable.addCell( c );

		paragraph = new Paragraph();
		paragraph.add( new Chunk( "Responsable : ", boldFont ));
		paragraph.add( new Chunk( bean.getManagerName(), normalFont ));

		c = new PdfPCell( paragraph );
		c.setBorder( Rectangle.NO_BORDER );
		signaturesTable.addCell( c );

		c = new PdfPCell( new Paragraph( "Signature : ", boldFont ));
		c.setBorder( Rectangle.NO_BORDER );
		signaturesTable.addCell( c );

		c = new PdfPCell( new Paragraph( "Signature : ", boldFont ));
		c.setBorder( Rectangle.NO_BORDER );
		signaturesTable.addCell( c );

		doc.add( signaturesTable );

		// Signature image
		if( ! bean.signatures.isEmpty()) {
			int random = new Random().nextInt( bean.signatures.size());
			Image img = Image.getInstance( bean.signatures.get( random ).toURI().toURL());
			img.scaleToFit( 200, 100 );
			img.setIndentationLeft( 25 + (random * random * 14) % 51 );
			doc.add( img );
		} else {
			doc.add( new Paragraph( " " ));
		}

		doc.add( new Paragraph( " " ));
		doc.add( new Paragraph( " " ));
		doc.add( new Paragraph( " " ));


		// Calendar
		final PdfPTable timeTable = new PdfPTable( 7 );
		timeTable.addCell( new PdfPCell());

		for( int i=0; i<5; i++ ) {
			final String date = this.sdf.format( calendar.getTime());
			timeTable.addCell( newCell( date, 10 ));
			calendar.add( Calendar.DATE, 1 );
		}

		timeTable.addCell( newCell( "Total", 10 ));
		timeTable.addCell( newCell( "Heures Effectuées", 20 ));

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

			timeTable.addCell( newCell( value, 20 ));
			calendar.add( Calendar.DATE, 1 );
		}

		if( total > bean.getTotalHours())
			throw new IOException( "Too many hours, you were supposed to do " + bean.getTotalHours() + " hours..." );
		else if( ! daysOff && total != bean.getTotalHours())
			throw new IOException( "Wrong schedule, you were supposed to do EXACTLY " + bean.getTotalHours() + " hours..." );

		timeTable.addCell( newCell( total + " h", 20 ));
		timeTable.completeRow();
		doc.add( timeTable );
	}


	private PdfPCell newCell( String content, int padding ) {

		PdfPCell c = new PdfPCell( new Phrase( content ));
		c.setHorizontalAlignment( Element.ALIGN_CENTER );
		c.setPaddingTop( padding );
		c.setPaddingBottom( padding );
		c.setPaddingLeft( padding / 2f );
		c.setPaddingRight( padding / 2f );

		return c;
	}
}
