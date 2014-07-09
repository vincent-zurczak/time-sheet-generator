# Time Sheet Generator

A simple Java project that generates time sheet skeletons.  
The output is a single PDF file, with a page per week.

## Usage

``` properties
# To generate the time sheet for the current week
run.sh

# To generate the time sheets from a week 4 until this week
run.sh 4
	
# To generate the time sheets from week 5 until week 7
run.sh 4 7
```

## Configuration

* Rename *conf/conf-sample.properties* into *conf/conf.properties*.
* Update the fields. 

## Roadmap

* Support the connection to an OBM instance to retrieve holidays and so on...
* Pre-fill generated files with this information.
* Add other features to make life easier.

## License

This (tiny) project is licensed under the terms of the [Apache license (version 2.0)](http://www.apache.org/licenses/LICENSE-2.0).  
Note that this project relies on [iText PDF](http://itextpdf.com), which is available under the terms of the AGPL.
