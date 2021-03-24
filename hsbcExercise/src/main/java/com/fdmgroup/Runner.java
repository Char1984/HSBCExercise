package com.fdmgroup;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Runner {
	
	private final static Logger Logger = LogManager.getLogger(Runner.class);

	public static void main(String[] args) {
		
		String urlForContent = "https://samples.openweathermap.org/data/2.5/box/city?bbox=12,32,15,37,10&appid=b6907d289e10d714a6e88b30761fae22";
		String stringFromUrl = getUrlContent(urlForContent);
		getNumberOfCitiesBeginningWithT(stringFromUrl);
	}
	
	private static void getNumberOfCitiesBeginningWithT(String string) {
		
		ObjectMapper mapper = new ObjectMapper();
		int counter = 0;
		
		try {
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			City[] cities = mapper.readValue(string, City[].class);
			for(City city : cities) {
				if(city.getName().startsWith("T")) counter++;
			}
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			Logger.error(e);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			Logger.error(e);
		}
		
		Logger.info("There are "+counter+" cities that begin with the letter T");
	
	}

	private static String getUrlContent(String theUrl) {
						
		String content = "";
		JSONObject jObject = new JSONObject();
		
		try {
			URL url = new URL(theUrl);
			URLConnection urlConnection = url.openConnection();
			JSONParser parser = new JSONParser();
			InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
			jObject = (JSONObject)parser.parse(inputStreamReader);
			content = jObject.get("list").toString();
			
		} catch (Exception e) {
			Logger.error(e);
		}
		return content;
	}

}
