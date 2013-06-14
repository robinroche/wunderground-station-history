/**
 * Class to retrieve historical weather data from wunderground
 * https://code.google.com/p/wunderground-core/wiki/Examples
 * @author Robin Roche
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import de.mbenning.weather.wunderground.api.domain.DataGraphSpan;
import de.mbenning.weather.wunderground.api.domain.DataSet;
import de.mbenning.weather.wunderground.api.domain.WeatherStation;
import de.mbenning.weather.wunderground.impl.services.HttpDataReaderService;
import de.mbenning.weather.wunderground.impl.services.WeatherStationService;


public class WUndergroundHistoData 
{

	// Variables
	static FileWriter writer = null;
	static ArrayList<String> writeList = new ArrayList<String>();
	static WeatherStation targetStation;
	static int year;
	
	
	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) 
	throws UnsupportedEncodingException, IOException, ParseException 
	{
		
		/* PARAMETERS */
		
		// Selected year
		year = 2011;
		
		// Days in a month, careful with leap years
		int[] daysInMonth = {31,28,31,30,31,30,31,31,30,31,30,31};
		
		// Weather station country
		String country = "France";
		
		// Selected weather station ID
		String stationId = "IBELFORT2";
		
		
		/* GET THE WEATHER STATION */		
		
		// Create an instance of WeatherStationService
		WeatherStationService weatherStationService = new WeatherStationService();

		// Find all weather stations
		List<WeatherStation> stations = weatherStationService.findAllWeatherStationsByCountry(country);

//		// Display all found stations
//		for (WeatherStation weatherStation : stations) {
//		    System.out.println(weatherStation.getStationId() + "\t" + "\t" + weatherStation.getCity() + 
//		         "\t" + weatherStation.getCountry());           
//		}
		
		// Look for the specific station
		targetStation = null;
		for (WeatherStation weatherStation : stations) {
			if(weatherStation.getStationId().equals(stationId))
			{
				System.out.println(weatherStation.getStationId() + "\t" + weatherStation.getCity() + "\t" + weatherStation.getCountry());
				targetStation = weatherStation;
			}
		}
		
		
		/* GET THE DATA FOR EACH DAY */	
		
		// For each month
		for(int i=1;i<=12;i++)
		{
			// For each day on this month
			for(int j=1;j<=daysInMonth[i-1];j++)
			{
				// Create an instance of HttpDataReaderService
				HttpDataReaderService dataReader = new HttpDataReaderService();

				// Set graphspan to day
				dataReader.setDataGraphSpan(DataGraphSpan.DAY);

				// Set the desired weather station
				dataReader.setWeatherStation(targetStation);

				// Set the desired month and year as DateTime
				dataReader.setWeatherDate(new DateTime(year, i, j, 0, 0, 0).toDate());

				// For each day and hour, display/save the measurements
				List<DataSet> daily = dataReader.readDataSets();
				for (DataSet dataSet : daily) {
					System.out.println(dataSet.getDateTime() + "\t" + dataSet.getTemperature() + "\t" + dataSet.getWindSpeedKmh());
					String newLine = "" + 
						dataSet.getDateTime() + "," + 
						dataSet.getTemperature() + "," +
						dataSet.getWindSpeedKmh() + "," + 
						dataSet.getWindDirection() + "," + 
						dataSet.getHumidity()+ "," +
						dataSet.getRainRateHourlyMm() + "," +
						dataSet.getPressurehPa() + "," + 
						dataSet.getDewPoint();
					writeList.add(newLine);
				}
			}
			
			// Wait 15 seconds between each month
			try 
			{
				Thread.sleep(15000);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}

		
		/* SAVE THE RESULTS TO A FILE */	
		
		// Save results to a CSV file
		saveResults();

	}
	

	/**
	 * Saves results in a CSV file
	 */
	private static void saveResults()
	{

		System.out.println("Saving results");

		// Check if the directory exists
		File resultsDir = new File("results");
		boolean resultsDirExists = resultsDir.exists();

		// If the results directory doesn't exist, create it
		if(!resultsDirExists)
		{
			resultsDir.mkdir();
		}

		try 
		{
			writer = new FileWriter("results/" + targetStation.getStationId() + "_" + year + ".csv", true);
			BufferedWriter out = new BufferedWriter(writer);

			for(int i=0;i<writeList.size();i++)
			{
				String writeStr = writeList.get(i) + System.getProperty("line.separator");
				out.write(writeStr,0,writeStr.length());
			}

			out.close();
			writer.close();
		}
		catch(IOException ex)
		{
			System.err.println("Writing failed");
			ex.printStackTrace();
		}
	}
}
