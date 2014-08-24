package com.example.departure;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.temboo.Library.CloudMine.ObjectStorage.ObjectGet;
import com.temboo.Library.CloudMine.ObjectStorage.ObjectGet.ObjectGetInputSet;
import com.temboo.Library.CloudMine.ObjectStorage.ObjectGet.ObjectGetResultSet;
import com.temboo.Library.CloudMine.ObjectStorage.ObjectSet;
import com.temboo.Library.CloudMine.ObjectStorage.ObjectSet.ObjectSetInputSet;
import com.temboo.Library.CloudMine.ObjectStorage.ObjectSet.ObjectSetResultSet;
import com.temboo.core.TembooSession;


public class LocalCalendar {
	private ArrayList<Event> classes;
	public LocalCalendar(){
		classes = new ArrayList<Event>();
	}
	public Event addEvent(String name, String location, int time, boolean[] days){
		Event temp = new Event(name,location,time,days);
		classes.add(temp);
		Sort();
		return temp;
	}
	public void loadCalendar(String cloudKey, String cloudUserToken, String cloudApp, String tembAcctName, String tembAppKeyName, String tembAppKeyValue) throws Exception{
		TembooSession session = new TembooSession(tembAcctName, tembAppKeyName, tembAppKeyValue);
		// Instantiate the Choreo
		ObjectGet objectGetChoreo = new ObjectGet(session);
		// Get an InputSet object for the choreo
		ObjectGetInputSet objectGetInputs = objectGetChoreo.newInputSet();
		// Set inputs
		objectGetInputs.set_APIKey(cloudKey);
		objectGetInputs.set_SessionToken(cloudUserToken);
		objectGetInputs.set_ApplicationIdentifier(cloudApp);
		// Execute Choreo
		ObjectGetResultSet objectGetResults = objectGetChoreo.execute(objectGetInputs);
		JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(objectGetResults.get_Response());
        //Get the tweets as an array
        JsonObject rootobj = root.getAsJsonObject();
        JsonArray cal = rootobj.get("success").getAsJsonObject().get("calendar").getAsJsonArray();
        JsonArray day;
        JsonObject event;
        String name, location;
        int time;
        boolean[] days;
        for(int x = 0;x<cal.size();x++){
        	event = cal.get(x).getAsJsonObject();
        	name = event.get("name").getAsString();
        	location = event.get("location").getAsString();
        	time = event.get("time").getAsInt();
        	day = event.get("days").getAsJsonArray();
        	days = new boolean[day.size()];
        	for(int y = 0;y<day.size();y++){
        		days[y] = day.get(y).getAsJsonObject().get("day").getAsBoolean();
        	}
        	addEvent(name,location,time, days);
        }
	}
	public void save(String cloudKey, String cloudUserToken, String cloudApp, String tembAcctName, String tembAppKeyName, String tembAppKeyValue) throws Exception{
		TembooSession session = new TembooSession(tembAcctName, tembAppKeyName, tembAppKeyValue);
		// Instantiate the Choreo
		ObjectSet objectSetChoreo = new ObjectSet(session);

		// Get an InputSet object for the choreo
		ObjectSetInputSet objectSetInputs = objectSetChoreo.newInputSet();

		// Set inputs
		objectSetInputs.set_Data(toJson());
		objectSetInputs.set_APIKey(cloudKey);
		objectSetInputs.set_SessionToken(cloudUserToken);
		objectSetInputs.set_ApplicationIdentifier(cloudApp);

		// Execute Choreo
		ObjectSetResultSet objectSetResults = objectSetChoreo.execute(objectSetInputs);
	}
	public String toJson(){
		String json = "{\"calendar\":[";
		for(int x =0;x<classes.size();x++){
			json+=classes.get(x).toJson();
			if(x<classes.size()-1)
				json+=",";
		}
		json+="]}";
		return json;
	}
	public void Sort(){
		Event temp;
		for(int x=0;x<classes.size();x++){
			for(int y= x+1;y<classes.size();y++){
				if(classes.get(y).getTime()<classes.get(x).getTime()){
					temp = classes.get(x);
					classes.set(x,classes.get(y));
					classes.set(y,temp);
				}
			}
		}
			
	}
	public void removeEvent(Event rem){
		classes.remove(rem);
	}
}
