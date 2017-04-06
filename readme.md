# Time Sheet Generator

A simple Java project that generates time sheet skeletons.  
The output is a single PDF file, with a page per week.

> I created this project for myself.  
> I generally run it from Eclipse. So, usability is reduced to its minimum.


## Usage

* Fill-in the configuration properties (**conf/conf.properties**).
* Run the **net.vzurczak.timesheetgenerator.MainScheduleProperties** class to pre-fill your schedule (*conf/schdule.properties*).
* Run the **net.vzurczak.timesheetgenerator.MainPdf** class and get your PDF under */pdf*.


## Configuration

* Rename *conf/conf.sample.properties* into **conf/conf.properties**.
* Update the file content. 


## Compilation

Make sure you have Maven installed and run...

```bash
mvn clean package
```

You will then find a ZIP file under the **target** directory.  
Extract it anywhere you want and run its content.


## License

This (tiny) project is licensed under the terms of the [Apache license (version 2.0)](http://www.apache.org/licenses/LICENSE-2.0).  
Note that this project relies on [iText PDF](http://itextpdf.com), which is available under the terms of the AGPL.
