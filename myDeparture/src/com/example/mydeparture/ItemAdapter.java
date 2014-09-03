package com.example.mydeparture;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.ClipData.Item;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<item> {

	// declaring our ArrayList of items
	private ArrayList<item> objects;

	/* here we must override the constructor for ArrayAdapter
	* the only variable we care about now is ArrayList<Item> objects,
	* because it is the list of objects we want to display.
	*/
	public ItemAdapter(Context context, int textViewResourceId, ArrayList<item> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}

	/*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
	public View getView(int position, View convertView, ViewGroup parent){

		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_item, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		item i = objects.get(position);

		if (i != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			TextView c = (TextView) v.findViewById(R.id.cLabel);
			TextView cd = (TextView) v.findViewById(R.id.cData);
			TextView b = (TextView) v.findViewById(R.id.bLabel);
			TextView bd = (TextView) v.findViewById(R.id.bData);
			TextView t = (TextView) v.findViewById(R.id.tLabel);
			TextView td = (TextView) v.findViewById(R.id.tData);
			TextView d = (TextView) v.findViewById(R.id.dLabel);
			TextView dd = (TextView) v.findViewById(R.id.dData);

			// check to see if each individual textview is null.
			// if not, assign some text
			if (c != null){
				c.setText("Class: ");
			}
			if (cd != null){
				cd.setText(i.getClassName());
			}
			if (b != null){
				b.setText("Building: ");
			}
			if (bd != null){
				bd.setText(i.getBuild());
			}
			if (t != null){
				t.setText("Start Time: ");
			}
			if (td != null){
				td.setText(i.getTime());
			}
			if (d != null){
				d.setText("Days: ");
			}
			if (dd != null){
				dd.setText(i.getDays());
			}
		}

		// the view must be returned to our activity
		return v;

	}

}