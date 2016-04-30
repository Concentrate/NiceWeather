package activity;

import java.util.ArrayList;
import com.example.niceweather.R;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import db.City;
import db.Country;
import db.NiceWeatherDB;
import db.Province;
import util.HttpCallbackListener;
import util.HttpSendRequest;
import util.Utility;

public class Choose_Area extends Activity {
	private final static int LEVEL_PROVINCE = 0;
	private final static int LEVEL_CITY = 1;
	private final static int LEVEL_COUNTRY = 2;
	private List<String> data_list = new ArrayList<String>();
	private List<Province> provinceList;
	private List<City> cityList;
	private List<Country> countryList;
	private TextView title;
	private ListView listview;
	private ProgressDialog progressdialog;
	private ArrayAdapter<String> adapter;
	private NiceWeatherDB niceweatherdb;
	private Province selectedProvince;
	private City selectedCity;
	private int currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(com.example.niceweather.R.layout.choose_layout);
		Intent secondIntent=getIntent();
		boolean isChooseAnotherOne=false;
		if(secondIntent!=null)
		{
			 isChooseAnotherOne=secondIntent.getBooleanExtra("from_weather_activity", false);
		}
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		if (pref.getBoolean("city_selected", false)&&!isChooseAnotherOne) {
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		title = (TextView) findViewById(R.id.text_view);
		listview = (ListView) findViewById(R.id.list_view);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data_list);
		listview.setAdapter(adapter);
		niceweatherdb = NiceWeatherDB.getInstance(this);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(arg2);
					queryCitys();

				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(arg2);
					queryCountries();
				} else if (currentLevel == LEVEL_COUNTRY) {
					String countryCode = countryList.get(arg2).getCountryCode();
					Intent intent = new Intent(Choose_Area.this, WeatherActivity.class);
					intent.putExtra("country_code", countryCode);
					startActivity(intent);
					finish();
				}
			}

		});
		queryProvinces();
	}

	private void queryProvinces() {
		// TODO Auto-generated method stub
		this.provinceList = niceweatherdb.loadProvinces();
		if (provinceList != null && provinceList.size() > 0) {
			data_list.clear();
			for (Province province : provinceList) {
				data_list.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listview.setSelection(0);
			title.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServe(null, "province");

		}

	}

	private void queryFromServe(final String code, final String type) {
		// TODO Auto-generated method stub
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		ShowProgressDialog();
		HttpSendRequest.HttpSendRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String message) {
				// TODO Auto-generated method stub
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.HandleProvinceResponse(niceweatherdb, message);

				} else if ("city".equals(type)) {
					result = Utility.HandleCityResponse(niceweatherdb, message, selectedProvince.getId());
				} else if ("country".equals(type)) {
					result = Utility.HandleCountryResponse(niceweatherdb, message, selectedCity.getId());
				}
				if (result) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							if ("province".equals(type))
								queryProvinces();
							else if ("city".equals(type))
								queryCitys();
							else if ("country".equals(type))
								queryCountries();

						}

					});
				}
			}

			@Override
			public void onError(String message) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(Choose_Area.this, "加载数据失败", Toast.LENGTH_SHORT).show();
					}

				});
			}
		});

	}

	private void closeProgressDialog() {
		// TODO Auto-generated method stub
		if (progressdialog != null)
			progressdialog.dismiss();

	}

	private void ShowProgressDialog() {
		// TODO Auto-generated method stub
		if (this.progressdialog == null) {
			progressdialog = new ProgressDialog(this);
			progressdialog.setMessage("正在加载数据");
			progressdialog.setCanceledOnTouchOutside(false);

		}
		progressdialog.show();

	}

	private void queryCountries() {
		// TODO Auto-generated method stub
		this.countryList = this.niceweatherdb.loadCountries(selectedCity.getId());
		if (countryList != null && countryList.size() > 0) {
			data_list.clear();
			for (Country tmp : countryList) {
				data_list.add(tmp.getCountryName());
			}
			adapter.notifyDataSetChanged();
			listview.setSelection(0);
			title.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTRY;
		} else {
			queryFromServe(selectedCity.getCityCode(), "country");
		}

	}

	private void queryCitys() {
		// TODO Auto-generated method stub
		this.cityList = this.niceweatherdb.loadCitys(selectedProvince.getId());
		if (cityList != null && cityList.size() > 0) {
			data_list.clear();
			for (City c : cityList) {
				data_list.add(c.getCityName());
			}
			adapter.notifyDataSetChanged();
			listview.setSelection(0);
			title.setText(selectedProvince.getProvinceName());
			this.currentLevel = LEVEL_CITY;
		} else {
			queryFromServe(selectedProvince.getProvinceCode(), "city");
		}

	}

	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTRY) {
			queryCitys();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			finish();
		}
	}

}
