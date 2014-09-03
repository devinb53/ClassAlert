package com.example.mydeparture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class sqlite extends SQLiteOpenHelper {
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ClassDB";
 
    public sqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }
    
    private static final String TABLE_CLASS = "classes";

    // Classess Table Columns names
    private static final String KEY_CLASS = "class";
    private static final String KEY_BUILDING = "building";
    private static final String KEY_TIME = "time";
    private static final String KEY_DAYS = "days";
    private static final String KEY_TRAVELTIME = "traveltime";

    private static final String[] COLUMNS = {KEY_CLASS,KEY_BUILDING,KEY_TIME,KEY_DAYS,KEY_TRAVELTIME};
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create class table
        String CREATE_CLASS_TABLE = "CREATE TABLE classes ( " +
                "class TEXT, " + 
                "building TEXT, " +
                "time TEXT, " +
                "days TEXT, " +
                "traveltime TEXT )";
 
        // create classess table
        db.execSQL(CREATE_CLASS_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older classess table if existed
        db.execSQL("DROP TABLE IF EXISTS classes");
 
        // create fresh classes table
        this.onCreate(db);
    }
    
    public void addClass(item classname)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_CLASS, classname.getClassName());
    	values.put(KEY_BUILDING, classname.getBuild());
    	values.put(KEY_TIME, classname.getTime());
    	values.put(KEY_DAYS, classname.getDays());
    	values.put(KEY_TRAVELTIME, classname.getTravelTime());
    	
    	db.insert(TABLE_CLASS, null, values);
    	
    	db.close();
    }
    
    public item getClassName(String className)
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	Cursor cursor = 
    			db.query(TABLE_CLASS,
    			COLUMNS,
    			" class = ?",
    			new String[] { String.valueOf(className) },
    			null,
    			null,
    			null,
    			null);
    	
    	if (cursor != null)
    		cursor.moveToFirst();
    	
    	item classfound = new item();
    	classfound.setClass(cursor.getString(0));
    	classfound.setBuild(cursor.getString(1));
    	classfound.setTime(cursor.getString(2));
    	classfound.setDay(cursor.getString(3));
    	classfound.setTravelTime(cursor.getString(4));
    	
    	Log.d("getClassName("+className+")", classfound.toString());
    	
    	return classfound;
    }
    
    public void deleteBook(item deleteclass)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	db.delete(TABLE_CLASS,
    			KEY_CLASS+" = ?",
    			new String[] { String.valueOf(deleteclass.getClass()) });
    	
    	db.close();
    	Log.d("deleteClass", deleteclass.toString());
    }
    
 
}