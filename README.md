# Retrieving historical weather data from wunderground

This Java class automatically retrieves weather data from wunderground.

### Licence

None. Feel free to use it as you wish.

### Context

Weather Underground ([wunderground](http://www.wunderground.com/)) is a website that provides weather-related reports, forecasts, maps, etc. Measurements from personal weather stations can also be uploaded to the website and be made available. An API also exists for retrieving data from the service.

There is however no built-in functionality to retrieve historical data over long periods (e.g., a year), which means that you need to write a script, either using the API, or using another method. In the following, I describe such a method.

Martin Benning developed [wunderground-core](https://code.google.com/p/wunderground-core/), a Java API for weather data handling from wunderground weather stations. I used this API to build a Java class that retrieves historical data over a year from a specific weather station. Data includes temperature, wind speed, humidity, rain rate, etc., but unfortunately not solar radiation. Based on the examples provided at [wunderground-core](https://code.google.com/p/wunderground-core/), I created the class available here.

### Using the code

The parameters of the script are the following (see WUndergroundHistoData.java) in the source code:
- The selected year;
- A vector giving the number of days in a month, that only needs to be edited to account for leap years (February with 28 or 29 days);
- The selected country;
- The selected weather station ID, which can be obtained directly by browsing the [wunderground website](http://www.wunderground.com/) or by listing the available stations using the example code available from wunderground-core (or uncomment the corresponding code to display the full list). The default one is [IBELFORT2](http://www.wunderground.com/weatherstation/WXDailyHistory.asp?ID=IBELFORT2).

If everything is operational (you need to import the wunderground-core JAR library), results should display in the console. At the end of the script, results are also written to a CSV file in the results folder.

### Sample output

Sample measurements from the IBELFORT2 weather station.

### Limitations

- The selected method is certainly not the most efficient, but it does the job. 
- The script sometimes fails randomly due a connection timeout, I don’t know why.

### Contact

Robin Roche - robinroche.com
