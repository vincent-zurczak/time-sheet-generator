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

/**
 * @author Vincent Zurczak
 */
public class Utils {

	/**
	 * Finds the calendar associated with the first day of this week.
	 * @param weekNumber the week number
	 * @return a calendar
	 */
	public static Calendar findCalendar( int weekNumber ) {

		final Calendar calendar = Calendar.getInstance();
		int year = calendar.get( Calendar.YEAR );

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
}
