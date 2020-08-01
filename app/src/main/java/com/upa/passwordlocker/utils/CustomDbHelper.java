package com.upa.passwordlocker.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CustomDbHelper {

	public static final String DBNAME = "dataofstudent";

	public static final String TABLE_NAME = "UTKAPRANI";

	public static final String KEY_ROW_ID = "_id";

	public static final String KEY_WEBSITE = "Website";
	public static final String KEY_USERNAME = "Username";
	public static final String KEY_PASSWORD = "Password";

	public static final int DB_VERSION = 2;

	private DBHelper dbHelper;
	private SQLiteDatabase ourDatabase;

	private final Context ourContext;

	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DBNAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_WEBSITE
					+ " TEXT NOT NULL, " + KEY_USERNAME+" TEXT NOT NULL, " + KEY_PASSWORD + " TEXT NOT NULL);"
			);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			if(oldVersion == 1) {

				String[] columns = new String[] { KEY_ROW_ID, KEY_WEBSITE, KEY_USERNAME, KEY_PASSWORD };

				Cursor c = db.query(TABLE_NAME, columns, null, null, null, null, null);

				int iWebsite = c.getColumnIndex(KEY_WEBSITE);
				int iUsername = c.getColumnIndex(KEY_USERNAME);
				int iPassword = c.getColumnIndex(KEY_PASSWORD);

				StringBuilder data= new StringBuilder();

				for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					data.append(c.getString(iWebsite)).append(" ").append(c.getString(iUsername)).append(" ").append(c.getString(iPassword)).append(" ");
				}

				c.close();

				String[] arr = data.toString().split(" ");

				db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
				db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_WEBSITE +
						" TEXT NOT NULL, " + KEY_USERNAME + " TEXT NOT NULL, " + KEY_PASSWORD + " TEXT NOT NULL);"
				);

				for (int j = 0; j < arr.length; j = j + 3) {

					try {
						arr[j + 1]=AESHelper.encrypt(arr[j + 1]);
						arr[j + 2]=AESHelper.encrypt(arr[j + 2]);
					}
					catch (Exception e) {

					}

					ContentValues cv=new ContentValues();

					cv.put(KEY_WEBSITE, arr[j]);
					cv.put(KEY_USERNAME, arr[j + 1]);
					cv.put(KEY_PASSWORD, arr[j + 2]);

					db.insert(TABLE_NAME, null, cv);
				}
			}
		}
	}

	public CustomDbHelper(Context c){
		ourContext=c;
	}

	public void open() throws SQLException{
		dbHelper = new DBHelper(ourContext);
		ourDatabase = dbHelper.getWritableDatabase();
	}

	public void close() throws SQLException{
		dbHelper.close();
	}

	public void createEntry(String website, String username, String password) throws SQLException{

		ContentValues cv = new ContentValues();

		cv.put(KEY_WEBSITE, website);
		cv.put(KEY_USERNAME, username);
		cv.put(KEY_PASSWORD, password);

		ourDatabase.insert(TABLE_NAME, null, cv);
	}

	public void deleteEntry(int pos) {

		String whereClause = KEY_ROW_ID + "=?";
		String[] whereArgs = new String[] {String.valueOf(pos)};

		ourDatabase.beginTransaction();
		ourDatabase.delete(TABLE_NAME, whereClause, whereArgs);
		ourDatabase.setTransactionSuccessful();
		ourDatabase.endTransaction();

		refreshTable();
	}

	public void editTable(int pos, String website,String username,String password) {

		String whereClause = "_id" + "=?";
		String[] whereArgs = new String[] {String.valueOf(pos)};

		ContentValues cv = new ContentValues();

		cv.put(KEY_WEBSITE, website);
		cv.put(KEY_USERNAME, username);
		cv.put(KEY_PASSWORD, password);

		ourDatabase.beginTransaction();
		ourDatabase.update(TABLE_NAME, cv, whereClause, whereArgs);
		ourDatabase.setTransactionSuccessful();
		ourDatabase.endTransaction();

		refreshTable();
	}

	public void refreshTable() {

		String[] columns = new String[] { KEY_ROW_ID, KEY_WEBSITE, KEY_USERNAME, KEY_PASSWORD };

		Cursor c = ourDatabase.query(TABLE_NAME, columns, null, null, null, null, null);

		int iWebsite = c.getColumnIndex(KEY_WEBSITE);
		int iUsername = c.getColumnIndex(KEY_USERNAME);
		int iPassword = c.getColumnIndex(KEY_PASSWORD);

		StringBuilder data = new StringBuilder();
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			data.append(c.getString(iWebsite)).append(" ").append(c.getString(iUsername)).append(" ").append(c.getString(iPassword)).append(" ");
		}

		c.close();

		String[] arr = data.toString().split(" ");

		ourDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		ourDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " ( " + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_WEBSITE
				+ " TEXT NOT NULL, " + KEY_USERNAME + " TEXT NOT NULL, " + KEY_PASSWORD + " TEXT NOT NULL);"
		);

		for (int j = 0; j < arr.length; j=j+3) {
			createEntry(arr[j],arr[j+1],arr[j+2]);
		}
	}

	public String getData() {

		String[] columns = new String[] { KEY_ROW_ID, KEY_WEBSITE, KEY_USERNAME, KEY_PASSWORD };

		Cursor c = ourDatabase.query(TABLE_NAME, columns, null, null, null, null, null);

		int iWebsite = c.getColumnIndex(KEY_WEBSITE);
		int iUsername = c.getColumnIndex(KEY_USERNAME);
		int iPassword = c.getColumnIndex(KEY_PASSWORD);

		StringBuilder result= new StringBuilder();
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result.append(c.getString(iWebsite)).append("\n").append(c.getString(iUsername)).append("\n").append(c.getString(iPassword)).append("\n");
		}

		c.close();

		return result.toString();
	}

}
