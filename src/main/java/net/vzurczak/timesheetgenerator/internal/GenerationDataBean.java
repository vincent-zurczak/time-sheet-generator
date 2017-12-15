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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent Zurczak
 */
public class GenerationDataBean {

	public final List<File> signatures = new ArrayList<File> ();
	private String name, managerName, outputType;
	private int startWeek, endWeek, totalHours, year;


	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * @return the managerName
	 */
	public String getManagerName() {
		return this.managerName;
	}

	/**
	 * @param managerName the managerName to set
	 */
	public void setManagerName( String managerName ) {
		this.managerName = managerName;
	}

	/**
	 * @return the startWeek
	 */
	public int getStartWeek() {
		return this.startWeek;
	}

	/**
	 * @param startWeek the startWeek to set
	 */
	public void setStartWeek( int startWeek ) {
		this.startWeek = startWeek;
	}

	/**
	 * @return the endWeek
	 */
	public int getEndWeek() {
		return this.endWeek;
	}

	/**
	 * @param endWeek the endWeek to set
	 */
	public void setEndWeek( int endWeek ) {
		this.endWeek = endWeek;
	}

	/**
	 * @return the totalHours
	 */
	public int getTotalHours() {
		return this.totalHours;
	}

	/**
	 * @param totalHours the totalHours to set
	 */
	public void setTotalHours( int totalHours ) {
		this.totalHours = totalHours;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear( int year ) {
		this.year = year;
	}

	/**
	 * @return the outputType
	 */
	public String getOutputType() {
		return this.outputType;
	}

	/**
	 * @param outputType the outputType to set
	 */
	public void setOutputType( String outputType ) {
		this.outputType = outputType;
	}
}
