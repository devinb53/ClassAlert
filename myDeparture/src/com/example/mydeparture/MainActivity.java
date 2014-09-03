package com.example.mydeparture;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.appcompat.R.integer;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class MainActivity extends ActionBarActivity {
	
	// Initialize all variables and views
	Button setAlarm;
	EditText className;
	EditText startTime;
	EditText origin;
	Spinner building;
	CheckBox monday;
	CheckBox tuesday;
	CheckBox wednesday;
	CheckBox thursday;
	CheckBox friday;
	private ArrayList<item> m_parts =  new ArrayList<item>();
	ArrayList<String> buildings = new ArrayList<String>();
	private Runnable viewParts;
	private ItemAdapter m_adapter;
	String cl;
	String build;
	String time;
	String days;
	ListView lv;
	ArrayAdapter<String> adapter;
	sqlite db;
	String originAddress;
	String originlatlong;
	String destination;
	String traveltime;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Set values of varialbes and views when app loads
		setAlarm = (Button)findViewById(R.id.btnSetAlarm);
		origin = (EditText)findViewById(R.id.origin);
		className = (EditText)findViewById(R.id.txtClass);
		startTime = (EditText)findViewById(R.id.txtTime);
		building = (Spinner)findViewById(R.id.spinBuilding);
		monday = (CheckBox)findViewById(R.id.cbxMonday);
		tuesday = (CheckBox)findViewById(R.id.cbxTuesday);
		wednesday = (CheckBox)findViewById(R.id.cbxWednesday);
		thursday = (CheckBox)findViewById(R.id.cbxThursday);
		friday = (CheckBox)findViewById(R.id.cbxFriday);
		lv = (ListView)findViewById(R.id.list);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, buildings);
		building = (Spinner)findViewById(R.id.spinBuilding);
		getBuildings();
		building.setAdapter(adapter);
		db = new sqlite(this);
		context = this;
	}
	
	// Hardcoded Drexel Buildings with latitude and longitude
	public void getBuildings()
	{
		adapter.add("Choose Building");
		adapter.add("Armory (39.956700, -75.188598)");
		adapter.add("Bossone (39.956658, -75.188698)");
		adapter.add("CAT (39.954725, -75.185998)");
		adapter.add("Curtis Hall (39.953994, -75.186947)");
		adapter.add("Disque Hall (39.954151, -75.188140)");
		adapter.add("Hagerty Library (39.955143, -75.189930)");
		adapter.add("Korman Center (39.955018, -75.189565)");
		adapter.add("Law Building (39.955143, -75.189930)");
		adapter.add("LeBow Engineering (39.954725, -75.185998)");
		adapter.add("Lebow Hall (39.957222, -75.189826)");
		adapter.add("MacAlister Hall (39.953994, -75.186947)");
		adapter.add("Main Building (39.953994, -75.186947)");
		adapter.add("Mandell Theater (39.953994, -75.186947)");
		adapter.add("One Drexel Plaza (39.955526, -75.184375)");
		adapter.add("Papadakis (39.953902, -75.189768)");
		adapter.add("Paul Peck Center (39.953902, -75.189768)");
		adapter.add("Randell Hall (39.953994, -75.186947)");
		adapter.add("Rush Building (39.956818, -75.189590)");
		adapter.add("Stratton Hall (39.954151, -75.188140)");
		adapter.add("University Crossings (39.956050, -75.187775)");
		adapter.add("URBN Center (39.957131, -75.192929)");
	}
	
	// Set alarms when button is clicked
	public void setAlarms(View v)
	{
		// Go through each item
		for (int i = 0; i < m_parts.size(); i++)
		{
			item current = m_parts.get(i);
			String alldays = current.getDays();
			String[] dayArray = alldays.split(",");
			// Go through each day
			for (int k = 0; k < dayArray.length; k++)
			{
				// Get item info
				String className = current.getClassName();
				String building = current.getBuild();
				String startTime = current.getTime();
				String traveltime = current.getTravelTime();
				String[] hourmin = startTime.split(":");
				// Parse time
				int hour = Integer.parseInt(hourmin[0]);
				int min = Integer.parseInt(hourmin[1]);
				// Get time minus travel time
				int travelMin = Integer.parseInt(String.valueOf(traveltime.charAt(0)));
				int hourMinusTravel = getHourMinusTravel(hour, min);
				int minMinusTravel = getMinMinusTravel(min, travelMin);
				// Get array of days
				String day = dayArray[k];
				int dayInt = getDayInt(day);
				
				// Create instance of calendar
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				cal.set(Calendar.DAY_OF_WEEK, dayInt);
				
				// Create and add new alarm intent
				Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
				alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, "Time to leave for " + className + "!!!");
				alarmIntent.putExtra(AlarmClock.EXTRA_DAYS, cal.DAY_OF_WEEK);
				alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hourMinusTravel);
				alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, minMinusTravel);
				startActivity(alarmIntent);
				
			}
		}
	}
	
	// Get minutes minus the travel time
	public int getMinMinusTravel(int min, int travelmin)
	{
		if (min == 0)
			return (60 - travelmin);
		else
			return (min - travelmin);
	}
	
	// Get hour minus travel time
	public int getHourMinusTravel(int hour, int min)
	{
		if (min == 0)
		{
			if (hour == 1)
				return 12;
			else
				return (hour - 1);
		}
		else
			return hour;
	}
	
	// Get int value of calendar day
	public int getDayInt(String day)
	{
		if (day.equals("Monday"))
			return Calendar.MONDAY;
		else if (day.equals("Tuesday"))
			return Calendar.TUESDAY;
		else if (day.equals("Wednesday"))
			return Calendar.WEDNESDAY;
		else if (day.equals("Thursday"))
			return Calendar.THURSDAY;
		else
			return Calendar.FRIDAY;
	}
	
	// Add class to items
	public void addClass(View v)
	{
		// Get item values
		cl = className.getText().toString();
		build = building.getSelectedItem().toString();
		time = startTime.getText().toString();
		originAddress = origin.getText().toString().replace(" ", "+");
		days = "";
		if (monday.isChecked())
			days = days + "Monday,";
		if (tuesday.isChecked())
			days = days + "Tuesday,";
		if (wednesday.isChecked())
			days = days + "Wednesday,";
		if (thursday.isChecked())
			days = days + "Thursday,";
		if (friday.isChecked())
			days = days + "Friday,";
		days = days.substring(0, days.length() - 1);
		destination = build.substring(build.indexOf("(") + 1, build.indexOf(")"));
		Log.i("destination", destination);
		destination = destination.replace(" ", "%20");
		Log.i("destination", destination);
		
		// Get latitude and longitiude of origin for googld distance matrix
		new getLatLong().execute();
		// Get travel time from origin to destination
		new getTravelTime().execute();

		
		// Add to sqlite database
		//db.addClass(new item(cl, build, time, days, traveltime));
		// Add to listview
		
		clearAll();
		
		
	}
	
	// Clear all views when user adds class
	public void clearAll()
	{
		origin.setText("");
		className.setText("");
		building.setSelection(0);
		startTime.setText("");
		monday.setChecked(false);
		tuesday.setChecked(false);
		wednesday.setChecked(false);
		thursday.setChecked(false);
		friday.setChecked(false);
	}

	// Make network call to google distance matrix api and get travel time
	private class getTravelTime extends AsyncTask<Void, Void, JsonObject>
	{
		JsonObject distance;
		protected JsonObject doInBackground(Void... params) {
			try
			{
				// Network call
				String sURL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" 
				+ originlatlong + "&destinations=" + destination
				+ "&mode=walking&language=fr-FR&key=AIzaSyCjOJAQEGUc13h6fUp8LCRYtV30TUFUD28";
				Log.i("getTravelTime", sURL);
				URL url;
				url = new URL(sURL);
				HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
				connection.connect();
						
				// Get json
				JsonParser jp = new JsonParser();
				JsonElement root = jp.parse(new InputStreamReader((InputStream) connection.getContent()));
				distance = root.getAsJsonObject();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return distance;
		}
		
		protected void onPostExecute(JsonObject result) {
			// Parse through json and get the travel time
			JsonArray rows = result.get("rows").getAsJsonArray();
			JsonObject obj = rows.get(0).getAsJsonObject();
			JsonArray elements = obj.get("elements").getAsJsonArray();
			JsonObject obj2 = elements.get(0).getAsJsonObject();
			JsonObject duration = obj2.get("duration").getAsJsonObject();
			traveltime = duration.get("text").getAsString();
			Log.i("getTravelTime", traveltime);
			// Add new item to array
			m_parts.add(new item(cl, build, time, days, traveltime));
			// Set array to item adapter
			m_adapter = new ItemAdapter(context, R.layout.list_item, m_parts);
			// Set item adapter to listview
	        lv.setAdapter(m_adapter);
		}	
	}
	
	// Make network call to get latitude and longitude of origin
	private class getLatLong extends AsyncTask<Void, Void, JsonObject>
	{
		JsonObject latlong;
		protected JsonObject doInBackground(Void... params) {
			try
			{
				// network call
				String sURL = "https://maps.googleapis.com/maps/api/geocode/json?address=" 
				+ originAddress + "&key=" + "AIzaSyCjOJAQEGUc13h6fUp8LCRYtV30TUFUD28";
				URL url;
				url = new URL(sURL);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.connect();
						
				// Get json
				JsonParser jp = new JsonParser();
				JsonElement root = jp.parse(new InputStreamReader((InputStream) connection.getContent()));
				latlong = root.getAsJsonObject();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return latlong;
		}
		
		protected void onPostExecute(JsonObject result) {
			// Parse through json and get latitude and longitude
			JsonArray results = result.get("results").getAsJsonArray();
			JsonObject obj = results.get(0).getAsJsonObject();
			JsonObject geometry = obj.get("geometry").getAsJsonObject();
			String originlat = geometry.get("location").getAsJsonObject().get("lat").getAsString();
			Log.i("getLatLong", originlat);
			String originlng = geometry.get("location").getAsJsonObject().get("lng").getAsString();
			Log.i("getLatLong", originlng);
			originlatlong = originlat + ",%20" + originlng;
		}	
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

