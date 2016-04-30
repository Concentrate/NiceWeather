package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import db.City;
import db.Country;
import db.NiceWeatherDB;
import db.Province;

public class Utility {

	public static void handleWeatherResponse(Context context,String response)
	{
		try {
			JSONObject object=new JSONObject(response);
			JSONObject weatherInfo=object.getJSONObject("weatherinfo");
			String city=weatherInfo.getString("city");
			String cityId=weatherInfo.getString("cityid");
			String tmp1=weatherInfo.getString("temp2");
			String tmp2=weatherInfo.getString("temp1");
			String weatherDescrp=weatherInfo.getString("weather");
			String publishtime=weatherInfo.getString("ptime");
			saveWeatherInfo(context,city,cityId,tmp1,tmp2,weatherDescrp,publishtime);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	private static void saveWeatherInfo(Context context, String city, String cityId,String tmp1, String tmp2,
			String weatherDescrp, String publishtime) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putString("city_name", city);
		editor.putBoolean("city_selected", true);
		editor.putString("tmp1", tmp1);
		editor.putString("tmp2", tmp2);
		editor.putString("weatherDescrp", weatherDescrp);
		editor.putString("currentTime", sdf.format(new Date()));
		editor.putString("publishtime", publishtime);
		editor.putString("cityId", cityId);
		editor.commit();
		
	}
	public synchronized static boolean HandleProvinceResponse(NiceWeatherDB db, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvince = response.split(",");
			if (allProvince != null && allProvince.length > 0) {
				for (String p : allProvince) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					db.saveProvince(province);

				}
				return true;
			}
		}
		return false;
	}

	public synchronized static boolean HandleCityResponse(NiceWeatherDB db, String response, int ProvinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCity = response.split(",");
			if (allCity != null && allCity.length > 0) {
				for (String c : allCity) {
					String[] array = c.split("\\|");
					City city = new City();
					if(array.length>=2)
					{
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					}
					city.setProvinceId(ProvinceId);
					db.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}

	public synchronized static boolean HandleCountryResponse(NiceWeatherDB db, String response, int cityid) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCountry = response.split(",");
			if (allCountry != null && allCountry.length > 0) {
				for (String country : allCountry) {
					String[] array = country.split("\\|");
					Country t = new Country();
					if(array.length>=2)
					{
						t.setCountryCode(array[0]);
						t.setCountryName(array[1]);
						
					}
					t.setCityId(cityid);
					db.saveCountry(t);
				}
				return true;
			}
		}
		return false;
	}
}
