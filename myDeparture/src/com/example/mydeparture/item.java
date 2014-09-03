package com.example.mydeparture;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.os.Build;

public class item {
	private String classname;
	private String building;
	private String time;
	private String days;
	private String origin;
	private String traveltime;

	public item(){

	}

	// Initialize new item
	public item(String c, String b, String t, String d, String tr){
		this.classname = c;
		this.building = b;
		this.time = t;
		this.days = d;
		this.traveltime = tr;
	}

	// Get class
	public String getClassName() {
		return classname;
	}

	// Set class
	public void setClass(String cl) {
		this.classname = cl;
	}

	// Get building
	public String getBuild() {
		return building;
	}

	// Set building
	public void setBuild(String build) {
		this.building = build;
	}

	// Get Time
	public String getTime() {
		return time;
	}

	// Set Time
	public void setTime(String t) {
		this.time = t;
	}
	
	// Get days
	public String getDays() {
		return days;
	}
	
	// Set days
	public void setDay(String day) {
		this.days = day;
	}
	
	// Get Travel Time
	public String getTravelTime()
	{
		return traveltime;
	}

	// Set Travel Tie
	public void setTravelTime(String tt) {
		this.traveltime = tt;
	}

}