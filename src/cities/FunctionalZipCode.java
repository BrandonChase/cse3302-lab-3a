package cities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FunctionalZipCode {
	static final String INPUT_FILENAME = "L03a zip_code_database.csv";
	static final String OUTPUT_FILENAME = "Problem_3a_output.txt";
	static boolean SHOW_INFO; // Used in lambda stream.forEach() to handle logic for only showing county and city info on first zip type entry
	
	public static void main(String[] args) {
		try {
			//-----Get Data-----
			ArrayList<zipCodeClass> zipCodes = new ArrayList<>();
			populateListFromFile(zipCodes);
				
			//-----Write First Column-----
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(OUTPUT_FILENAME));
			bWriter.write("County\tCity\tZip Type\tCity Pop\tFirst Zip\tNo. Zips\tCounty Pop");
			bWriter.newLine();
			
			//-----Write Data-----
			zipCodes.stream()
					 .map(zipCode -> zipCode.getCountyName()) //convert to stream of county names
					 .distinct()
					 .sorted()
					 .forEach(county -> { //go though each distinct county in alphabetical order
						 zipCodes.stream()
						 		 .filter(zipCode -> zipCode.getCountyName().equals(county)) //filter to stream of zipCodes in current county
						 		 .map(zipCode -> zipCode.getCityName()) //convert to stream of city names
						 		 .distinct()
						 		 .sorted()
						 		 .forEach(city -> { //go through each city in each county in alphabetical order
						 			 SHOW_INFO=true;
						 			 zipCodes.stream()
							 		 .filter(zipCode -> zipCode.getCountyName().equals(county) && zipCode.getCityName().equals(city)) //filter to stream of zipCodes in current county and city
							 		 .map(zipCode -> zipCode.getTypeZip()) //convert to stream of zip types
							 		 .distinct()
							 		 .sorted()
							 		 .forEach(zipType -> {//go through each zip type in current county / city pair
							 			try {
							 				if(SHOW_INFO) { //boolean defaults to false
								 				//-----County Name-----
								 				bWriter.write(county);
								 				bWriter.write("\t");
												 
												//-----City Name-----
								 				bWriter.write(city);
								 				bWriter.write("\t");
							 				} else {
							 					bWriter.write("\t");
							 					bWriter.write("\t");
							 				}
							 				
							 				//-----Zip Type-----
							 				bWriter.write(zipType);
							 				bWriter.write("\t");												 
											 
							 				//-----City Population-----
							 				bWriter.write(NumberFormat.getNumberInstance(Locale.US).format(
													 		zipCodes.stream()
														            .filter(zipCode -> zipCode.getCountyName().equals(county) && zipCode.getCityName().equals(city) && zipCode.getTypeZip().equals(zipType))
														            .mapToInt(zipCode -> zipCode.getEstPop())
														            .sum()));
							 				bWriter.write("\t");
											
							 				//-----First Zip-----
							 				bWriter.write(String.valueOf(zipCodes.stream()
														          		.filter(zipCode -> zipCode.getCountyName().equals(county) && zipCode.getCityName().equals(city) && zipCode.getTypeZip().equals(zipType))
														          		.mapToInt(zipCode -> zipCode.getZipCode())
														          		.min()
														          		.getAsInt()));
							 				bWriter.write("\t");
							 				
							 				
											 //-----Number of Zip Codes-----
											 bWriter.write(String.valueOf(zipCodes.stream()
													 					 .filter(zipCode -> zipCode.getCountyName().equals(county) && zipCode.getCityName().equals(city) && zipCode.getTypeZip().equals(zipType))
														           		 .count()));
											 bWriter.write("\t");
											 
											 if(SHOW_INFO) {//boolean defaults to false
												 //-----County Population-----
												 bWriter.write(NumberFormat.getNumberInstance(Locale.US).format(
													 		zipCodes.stream()
														            .filter(zipCode -> zipCode.getCountyName().equals(county))
														            .mapToInt(zipCode -> zipCode.getEstPop())
														            .sum()));
												 bWriter.write("\t");
												 
												 SHOW_INFO = false; //disable showing info for subsequent rows in same county+city combination
											 } else {
												 bWriter.write("\t");
											 }
											 
											 bWriter.newLine();
							 			 } catch(Exception e) {
							 				System.out.println(e.getMessage());
							 			 }
							 		 });
						 		 });
					 });
					 
			bWriter.close();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Gets data from a .csv file about Texas zip codes and puts it into an ArrayList
	static void populateListFromFile(ArrayList<zipCodeClass> zipCodes) throws FileNotFoundException, IOException
	{
		String[] values;
		String line = "";
		BufferedReader bReader = new BufferedReader(new FileReader(INPUT_FILENAME));
		
		while((line = bReader.readLine()) != null)
		{
			values = line.split(",");
			zipCodes.add(new zipCodeClass(Integer.parseInt(values[0]), //zip code
										  values[1], //zip code type
										  values[2], //city name
										  values[3], //county name
										  Integer.parseInt(values[4]) //estimated population
										  ));				  
		}
		
		bReader.close();
	}
}
