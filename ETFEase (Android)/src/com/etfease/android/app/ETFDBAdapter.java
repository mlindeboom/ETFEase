package com.etfease.android.app;

import java.util.Arrays; 
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.etfease.datasource.ETF;

/** Database Adapter for persisting Todo Items */
public class ETFDBAdapter {
	private static final String DATABASE_NAME = "etf.db";
	private static final String DATABASE_TABLE = "etfhistory"; 
	private static final int DATABASE_VERSION = 2;

	public static final String KEY_ID = "_id";
	public static final String KEY_SYMBOL = "symbol";
	public static final String KEY_NAME = "name";
	public static final String KEY_CHANGE_HISTORY = "change_history";
	public static final String KEY_PRICE_HISTORY = "price_history";
	public static final String KEY_PRICE_DATE = "price_date_history";
	public static final String SMA200_HISTORY = "sma200_history";
	public static final String SMA5_HISTORY = "sma5_history";

	public static final int SYMBOL_COLUMN = 1;
	public static final int NAME_COLUMN = 2;
	public static final int CHANGE_HISTORY_COLUMN = 3;
	public static final int PRICE_HISTORY_COLUMN = 4;
	public static final int PRICE_DATE_COLUMN = 5;
	public static final int SMA200_COLUMN = 6;
	public static final int SMA5_COLUMN = 7;

	private SQLiteDatabase db;
	private final Context context;
	private ETFDBOpenHelper dbHelper;

	public ETFDBAdapter(Context _context) {
		context = _context;
		dbHelper = new ETFDBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/** Close the database */
	public void close() {
		db.close();
	}

	/** Open the database */
	public void open() throws SQLiteException {  
		try {
			db = dbHelper.getWritableDatabase();
		}
		catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase();
		}	  
	}

	/** Insert a new ETF */
	public long insertETF(ETF _etf) {
		// Create a new row of values to insert.
		ContentValues newETFValues = new ContentValues();

		// Assign values for each row.
		newETFValues.put(KEY_SYMBOL, _etf.getSymbol());
		newETFValues.put(KEY_NAME, _etf.getName());
		newETFValues.put(KEY_CHANGE_HISTORY, Join(floatToString(_etf.getChangeHistory()), ",") );
		newETFValues.put(KEY_PRICE_HISTORY, Join(floatToString(_etf.getPriceHistory()), ",") );
		newETFValues.put(KEY_PRICE_DATE, Join(_etf.getPriceDate(), ",") );
		newETFValues.put(SMA200_HISTORY, Join(floatToString(_etf.getSma200History()), ",") );
		newETFValues.put(SMA5_HISTORY, Join(floatToString(_etf.getSma5History()), ",") );

		// Insert the row.
		return db.insert(DATABASE_TABLE, null, newETFValues);
	}

	/** Remove a ETF based on its index */
	public boolean removeETF(long _rowIndex) {
		return db.delete(DATABASE_TABLE, KEY_ID + "=" + _rowIndex, null) > 0;
	}

	/** Update a ETF */
	public boolean updateETF(ETF _etf) {
		ContentValues newETFValues = new ContentValues();
		newETFValues.put(KEY_SYMBOL, _etf.getSymbol());
		newETFValues.put(KEY_NAME, _etf.getName());
		newETFValues.put(KEY_CHANGE_HISTORY, Join(floatToString(_etf.getChangeHistory()), ",") );
		newETFValues.put(KEY_PRICE_HISTORY, Join(floatToString(_etf.getPriceHistory()), ",") );
		newETFValues.put(KEY_PRICE_DATE, Join(_etf.getPriceDate(), ",") );
		newETFValues.put(SMA200_HISTORY, Join(floatToString(_etf.getSma200History()), ",") );
		newETFValues.put(SMA5_HISTORY, Join(floatToString(_etf.getSma5History()), ",") );
		return db.update(DATABASE_TABLE, newETFValues, KEY_SYMBOL + "='" + _etf.getSymbol() + "'", null) > 0;
	}

	/** Return a Cursor to all the etf items */
	public Cursor getAllETFCursor() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ID, KEY_CHANGE_HISTORY, KEY_PRICE_HISTORY, KEY_PRICE_DATE, SMA200_HISTORY, SMA5_HISTORY}, null, null, null, null, null);
	}

	/** Return a Cursor to a specific row */
	public Cursor setCursorToETF(long _rowIndex) throws SQLException {
		Cursor result = db.query(true, DATABASE_TABLE, new String[] {KEY_ID, KEY_SYMBOL, KEY_NAME, KEY_CHANGE_HISTORY, KEY_PRICE_HISTORY, KEY_PRICE_DATE, SMA200_HISTORY, SMA5_HISTORY},
				KEY_ID + "=" + _rowIndex, null, null, null, null, null);

		if ((result.getCount() == 0) || !result.moveToFirst()) {
			throw new SQLException("No etf items found for row: " + _rowIndex);
		}
		return result;
	}

	/** Return a Todo Item based on its row index */
	public ETF getETF(String _symbol) throws SQLException {
		Cursor cursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ID, KEY_SYMBOL, KEY_NAME, KEY_CHANGE_HISTORY, KEY_PRICE_HISTORY, KEY_PRICE_DATE, SMA200_HISTORY, SMA5_HISTORY},
				KEY_SYMBOL + "='" + _symbol + "'", null, null, null, null, null);
		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			throw new SQLException("No to do item found for row: " + _symbol);
		}
		ETF etf = new ETF();

		String s = cursor.getString(SYMBOL_COLUMN);
		etf.setSymbol(s);
		
		s = cursor.getString(NAME_COLUMN);
		etf.setName(s);

		s = cursor.getString(CHANGE_HISTORY_COLUMN);
		etf.setChangeHistory(stringToFloat(s.split(",")));
		
		s = cursor.getString(PRICE_HISTORY_COLUMN);
		etf.setPriceHistory(stringToFloat(s.split(",")));

		s = cursor.getString(PRICE_DATE_COLUMN);
		etf.setPriceDate(s.split(","));
		
		s = cursor.getString(SMA200_COLUMN);
		etf.setSma200History(stringToFloat(s.split(",")));
		
		s = cursor.getString(SMA5_COLUMN); 
		etf.setSma5History(stringToFloat(s.split(",")));

		return etf;  
	}

	/** Static Helper class for creating, upgrading, and opening
	 * the database.
	 */
	private static class ETFDBOpenHelper extends SQLiteOpenHelper {

		public ETFDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		/** SQL Statement to create a new database */
		private static final String DATABASE_CREATE = "create table " + 
		DATABASE_TABLE + " (" + KEY_ID + " integer primary key autoincrement, " +
		KEY_SYMBOL + " text not null, " + KEY_NAME + " text not null, " +
		KEY_CHANGE_HISTORY + " text not null, " + KEY_PRICE_HISTORY + " text not null, " +
		KEY_PRICE_DATE + " text not null, " + SMA200_HISTORY + " text not null, " +
		SMA5_HISTORY + " text not null " + 
		");";

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
			Log.w("ETFDBAdapter", "Upgrading from version " + 
					_oldVersion + " to " +
					_newVersion + ", which will destroy all old data");

			// Drop the old table.
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			// Create a new one.
			onCreate(_db);
		}
	}


	private static String Join(String[] s, String delimiter)
	{
		return Join(Arrays.asList(s), delimiter);
	}

	private static String Join(List<String> coll, String delimiter)
	{
		if (coll.isEmpty())
			return "";

		StringBuilder sb = new StringBuilder();

		for (String x : coll)
			sb.append(x + delimiter);

		sb.delete(sb.length()-delimiter.length(), sb.length());

		return sb.toString();
	}  

	private static String[] floatToString(float[] f){
		String[] s= new String[f.length];
		for(int i=0;i<f.length;i++){
			s[i]=new Float(f[i]).toString();
		}
		return s;
	}

	private static float[] stringToFloat(String[] s){
		float[] f= new float[s.length];
		for(int i=0;i<s.length;i++){
			f[i]= Float.parseFloat(s[i]);
		}
		return f;
	}



}