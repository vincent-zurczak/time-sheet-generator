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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import net.vzurczak.timesheetgenerator.internal.GenerationDataBean;
import net.vzurczak.timesheetgenerator.internal.Utils;

/**
 * @author Vincent Zurczak
 */
public class UtilsTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();


	@Test
	public void testFindCurrentWeek() {

		// Basic checks
		int weekNumber = Utils.findCurrentWeek();
		Assert.assertTrue( weekNumber > 0 );
		Assert.assertTrue( weekNumber < 53 );

		// Make sure this week number is between J+0 and J-7
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get( Calendar.YEAR );
		calendar.clear();
		calendar.set( Calendar.YEAR, year );
		calendar.set( Calendar.WEEK_OF_YEAR, weekNumber );

		Calendar comparator = Calendar.getInstance();
		Assert.assertTrue( calendar.compareTo( comparator ) < 0 );

		comparator.add( Calendar.DATE, -7 );
		Assert.assertTrue( calendar.compareTo( comparator ) > 0 );
	}


	@Test
	public void testFindCalendar() {

		Calendar calendar = Utils.findCalendar( 41, -1 );
		Assert.assertEquals( Calendar.MONDAY, calendar.get( Calendar.DAY_OF_WEEK ));
		Assert.assertEquals( 41, calendar.get( Calendar.WEEK_OF_YEAR ));

		calendar = Utils.findCalendar( 1, -1 );
		Assert.assertEquals( Calendar.MONDAY, calendar.get( Calendar.DAY_OF_WEEK ));
		Assert.assertEquals( 1, calendar.get( Calendar.WEEK_OF_YEAR ));
	}


	@Test
	public void testParseConfiguration_withAllValues() throws Exception {

		Properties props = new Properties();
		props.setProperty( "week.end", "5" );
		props.setProperty( "week.start", "2" );
		props.setProperty( "year", "2015" );
		props.setProperty( "your.name", "me" );
		props.setProperty( "your.manager", "him or her" );

		File f = this.folder.newFile();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		props.store( os, null );
		Utils.writeStringInto( os.toString( "UTF-8" ), f );

		GenerationDataBean bean = Utils.parseConfiguration( f );
		Assert.assertNotNull( bean );
		Assert.assertEquals( 5, bean.getEndWeek());
		Assert.assertEquals( 2, bean.getStartWeek());
		Assert.assertEquals( 2015, bean.getYear());
		Assert.assertEquals( "me", bean.getName());
		Assert.assertEquals( "him or her", bean.getManagerName());
	}


	@Test
	public void testParseConfiguration_withOptionalValues() throws Exception {

		Properties props = new Properties();
		props.setProperty( "week.start", "2" );
		props.setProperty( "your.name", "me!" );
		props.setProperty( "your.manager", "him or her" );

		File f = this.folder.newFile();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		props.store( os, null );
		Utils.writeStringInto( os.toString( "UTF-8" ), f );

		GenerationDataBean bean = Utils.parseConfiguration( f );
		Assert.assertNotNull( bean );
		Assert.assertEquals( Utils.findCurrentWeek(), bean.getEndWeek());
		Assert.assertEquals( 2, bean.getStartWeek());
		Assert.assertEquals( new GregorianCalendar().get( Calendar.YEAR ), bean.getYear());
		Assert.assertEquals( "me!", bean.getName());
		Assert.assertEquals( "him or her", bean.getManagerName());
	}


	@Test
	public void testParseConfiguration_withOptionalValuesEmpty() throws Exception {

		Properties props = new Properties();
		props.setProperty( "week.end", "" );
		props.setProperty( "week.start", "2" );
		props.setProperty( "year", "" );
		props.setProperty( "your.name", "me!" );
		props.setProperty( "your.manager", "him or her" );

		File f = this.folder.newFile();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		props.store( os, null );
		Utils.writeStringInto( os.toString( "UTF-8" ), f );

		GenerationDataBean bean = Utils.parseConfiguration( f );
		Assert.assertNotNull( bean );
		Assert.assertEquals( Utils.findCurrentWeek(), bean.getEndWeek());
		Assert.assertEquals( 2, bean.getStartWeek());
		Assert.assertEquals( new GregorianCalendar().get( Calendar.YEAR ), bean.getYear());
		Assert.assertEquals( "me!", bean.getName());
		Assert.assertEquals( "him or her", bean.getManagerName());
	}
}
