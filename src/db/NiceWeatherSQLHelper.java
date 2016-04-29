package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NiceWeatherSQLHelper extends SQLiteOpenHelper {

	private static final String PROVINCE_TABLE="create table province"
			+ "(id integer primary key autoincrement,"
			+ "province_name text,"
			+ "province_code text"
			+ ")";
	private  static final String CITY_TABLE="create table city"
			+ "(id integer primary key autoincrement,"
			+ "city_name text,"
			+ "city_code text,"
			+ "province_id integer)";
	private static final String COUNTRY_TABLE="create table country"
			+ "(id integer primary key autoincrement,"
			+ "country_name text,"
			+ "country_code text,"
			+ "city_id integer)";
	public NiceWeatherSQLHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL(PROVINCE_TABLE);
		arg0.execSQL(CITY_TABLE);
		arg0.execSQL(this.COUNTRY_TABLE);
		Log.d("NiceWeatherSQLHelper", "OnCreate method have been executed");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		

	}

}
