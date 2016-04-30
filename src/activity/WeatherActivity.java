package activity;

import com.example.niceweather.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import util.HttpCallbackListener;
import util.HttpSendRequest;
import util.Utility;

public class WeatherActivity extends Activity implements OnClickListener {

	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;
	private TextView weatherDesp;
	private TextView tmp1Text;
	private TextView tmp2Text;
	private TextView currentDateText;
	private TextView publishTimeText;
	private ImageButton switchCity;
	private ImageButton refreshWeather;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		this.weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		this.cityNameText = (TextView) findViewById(R.id.city_name);
		publishTimeText = (TextView) findViewById(R.id.publish_text);
		this.weatherDesp = (TextView) findViewById(R.id.weather_desp);
		this.tmp1Text = (TextView) findViewById(R.id.temp1);
		this.tmp2Text = (TextView) findViewById(R.id.temp2);
		this.currentDateText = (TextView) findViewById(R.id.current_date);
		switchCity = (ImageButton) findViewById(R.id.switchButton);
		refreshWeather = (ImageButton) findViewById(R.id.refreshWeather);
		String countryCode = getIntent().getStringExtra("country_code");
		if (!TextUtils.isEmpty(countryCode)) {
			publishTimeText.setText("同步中");
			cityNameText.setVisibility(View.INVISIBLE);
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			queryWeatherCode(countryCode);
		} else {
			showWeather();
		}
		refreshWeather.setOnClickListener(this);
		switchCity.setOnClickListener(this);
	}

	private void showWeather() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String tmp1=prefs.getString("city_name", "");
		Log.d("Debug", tmp1);
		cityNameText.setText(prefs.getString("city_name", ""));
		this.currentDateText.setText(prefs.getString("currentTime", ""));
		this.tmp1Text.setText(prefs.getString("tmp1", ""));
		this.tmp2Text.setText(prefs.getString("tmp2", ""));
		this.publishTimeText.setText("今天" + prefs.getString("publishtime", "") + "发布");
		this.weatherInfoLayout.setVisibility(View.VISIBLE);
		this.cityNameText.setVisibility(View.VISIBLE);
		this.weatherDesp.setText(prefs.getString("weatherDescrp", ""));

	}

	private void queryWeatherCode(String countryCode) {
		// TODO Auto-generated method stub
		String address = "http://www.weather.com.cn/data/list3/city" + countryCode + ".xml";
		queryFromServe(address, "countryCode");

	}

	private void queryFromServe(final String address, final String string) {
		// TODO Auto-generated method stub
		HttpSendRequest.HttpSendRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String message) {
				// TODO Auto-generated method stub
				if ("countryCode".equals(string)) {
					if (!TextUtils.isEmpty(address)) {
						String[] array = message.split("\\|");
						if (array != null && array.length == 2) {
							String cityId = array[1];
							queryWeatherInfo(cityId);
						}

					}
				} else if ("cityId".equals(string)) {
					Utility.handleWeatherResponse(WeatherActivity.this, message);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							showWeather();

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
						publishTimeText.setText("同步失败");
					}
				});

			}

		});

	}

	private void queryWeatherInfo(String cityId) {
		// TODO Auto-generated method stub
		String address = "http://www.weather.com.cn/data/cityinfo/" + cityId + ".html";
		queryFromServe(address, "cityId");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weather, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.switchButton:
			Intent intent = new Intent(this, Choose_Area.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refreshWeather:
			this.weatherDesp.setText("同步中...");
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
			String cityId = pref.getString("cityId", "");
			if (!TextUtils.isEmpty(cityId)) {
				queryWeatherInfo(cityId);
			}
			break;
		default:
			break;
		}

	}
}
