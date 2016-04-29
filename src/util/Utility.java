package util;

import android.text.TextUtils;
import db.City;
import db.Country;
import db.NiceWeatherDB;
import db.Province;

public class Utility {

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
