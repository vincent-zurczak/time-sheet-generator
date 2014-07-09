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

import java.util.Calendar;

import junit.framework.Assert;
import net.vzurczak.timesheetgenerator.Utils;

import org.junit.Test;

/**
 * @author Vincent Zurczak
 */
public class UtilsTest {

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

		Calendar calendar = Utils.findCalendar( 41 );
		Assert.assertEquals( Calendar.MONDAY, calendar.get( Calendar.DAY_OF_WEEK ));
		Assert.assertEquals( 41, calendar.get( Calendar.WEEK_OF_YEAR ));

		calendar = Utils.findCalendar( 1 );
		Assert.assertEquals( Calendar.MONDAY, calendar.get( Calendar.DAY_OF_WEEK ));
		Assert.assertEquals( 1, calendar.get( Calendar.WEEK_OF_YEAR ));
	}
}
