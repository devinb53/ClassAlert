package com.example.departure;
public class Event {
	private String name, location;
	private int time;
	private boolean[] days;
	public Event(){
		name ="";
		location="";
		time=0;
		days = new boolean[7];
		for(int x = 0;x<days.length;x++)
			days[x]=false;
	}
	public Event(String nam, String loc, int tim, boolean[] day){
		name=nam;
		location=loc;
		time=tim;
		days=day;
	}
	public String getName(){
		return name;
	}
	public void setName(String nam){
		name=nam;
	}
	public String getLocation(){
		return location;
	}
	public void setLocation(String loc){
		location=loc;
	}
	public int getTime(){
		return time;
	}
	public void setTime(int tim){
		time=tim;
	}
	public boolean[] getDays(){
		return days;
	}
	public void setDays(boolean[] day){
		days=day;
	}
	public String toJson(){
		String json="{\"name\":\""+name+"\",\"location\":\""+location
				+"\",\"time\":"+time+",\"days\":[";
		for(int x = 0;x<days.length;x++){
			json+="{\"day\":"+days[x]+"}";
			if(x<days.length-1)
				json+=",";
		}
		json+="]}";
		return json;
	}
}

