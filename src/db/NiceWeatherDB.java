package db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NiceWeatherDB {
	private static final String DB_NAME = "NiceWeather_DB";
	private static NiceWeatherDB niceweatherDB;
	private SQLiteDatabase db;
	private final int version = 1;

	private NiceWeatherDB(Context context) {
		NiceWeatherSQLHelper helper = new NiceWeatherSQLHelper(context, DB_NAME, null, version);
		db = helper.getWritableDatabase();
	}

	public synchronized static NiceWeatherDB getInstance(Context context) {
		if (niceweatherDB == null) {
			niceweatherDB = new NiceWeatherDB(context);

		}
		return niceweatherDB;
	}

	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues content = new ContentValues();
			content.put("province_name", province.getProvinceName());
			content.put("province_code", province.getProvinceCode());
			content.put("id", province.getId());
			long tmp = db.insert("PROVINCE_TABLE", null, content);
			if (tmp == -1) {
				Log.d("NiceWeatherDB", "saveProvince insert Wrong");
			}
		}
	}

	public List<Province> loadProvinces() {
		ArrayList<Province> thearray = new ArrayList<Province>();
		Cursor cursor = db.query("PROVINCE_TABLE", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				String province_name = cursor.getColumnName(cursor.getColumnIndex("province_name"));
				String province_code = cursor.getColumnName(cursor.getColumnIndex("province_code"));
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				Province pro = new Province();
				pro.setId(id);
				pro.setProvinceName(province_name);
				pro.setProvinceCode(province_code);
				thearray.add(pro);
			} while (cursor.moveToNext());
		}
		return thearray;
	}

	public void saveCity(City city) {
		if (city != null) {
			ContentValues content = new ContentValues();
			content.put("id", city.getId());
			content.put("city_name", city.getCityName());
			content.put("city_code", city.getCityCode());
			content.put("province_id", city.getProvinceId());
			long result = db.insert("CITY_TABLE", null, content);
			if (result == -1) {
				Log.d("NiceWeatherDB", "saveCity Insert Wrong");
			}
		}
	}

	public List<City> loadCitys() {
		List<City> tmparray = new ArrayList<City>();
		Cursor cursor = db.query("CITY_TABLE", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setCityCode(cursor.getColumnName(cursor.getColumnIndex("city_code")));
				city.setCityName(cursor.getColumnName(cursor.getColumnIndex("city_code")));
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
				tmparray.add(city);
			} while (cursor.moveToNext());
		}
		return tmparray;
	}

	public void saveCountry(Country country) {
		if (country != null) {
			ContentValues values = new ContentValues();
			values.put("id", country.getId());
			values.put("country_name", country.getCountryName());
			values.put("country_code", country.getCountryCode());
			values.put("city_id", country.getCityId());
			long result=db.insert("COUNTRY_TABLE", null, values);
			if(result==-1)
			{
				Log.d("NiceWeatherDB", "In the saveCountry ,insert Wrong");
			}
		}
	}

	public List<Country> loadCountries() {
		List<Country> tmparray = new ArrayList<Country>();
		Cursor cursor=db.query("COUNTRY_TABLE", null, null, null, null, null,null, null);
		if(cursor.moveToFirst())
		{
			do
			{
				Country country=new Country();
				country.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
				country.setCountryCode(cursor.getColumnName(cursor.getColumnIndex("city_code")));
				country.setCountryName(cursor.getColumnName(cursor.getColumnIndex("city_name")));
				country.setId(cursor.getInt(cursor.getColumnIndex("id")));
				tmparray.add(country);
			}while(cursor.moveToNext());
		}
		return tmparray;

	}

}
