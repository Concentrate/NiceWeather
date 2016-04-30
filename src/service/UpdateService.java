package service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import receiver.AutoUpdateReceiver;
import util.HttpCallbackListener;
import util.HttpSendRequest;
import util.Utility;

public class UpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		new Thread(new Runnable()
				{

					@Override
					public void run() {
						// TODO Auto-generated method stub
						updateWeather();
						
					}
			
				}).start();
		AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
		long anHour=8*60*60*1000;
		long traggerAtTime=SystemClock.elapsedRealtime()+anHour;
		Intent i=new Intent(this,AutoUpdateReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,traggerAtTime,pi);
		return super.onStartCommand(intent, flags, startId);
	}

	protected void updateWeather() {
		// TODO Auto-generated method stub
		SharedPreferences pres=PreferenceManager.getDefaultSharedPreferences(this);
		String cityId=pres.getString("cityId","");
		String address = "http://www.weather.com.cn/data/cityinfo/" +
				cityId + ".html";
		HttpSendRequest.HttpSendRequest(address, new HttpCallbackListener()
				{

					@Override
					public void onFinish(String message) {
						// TODO Auto-generated method stub
						Utility.handleWeatherResponse(UpdateService.this, message);
						
						
					}

					@Override
					public void onError(String message) {
						// TODO Auto-generated method stub
						
					}
			
				});
				
		
		
	}



	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}
	

}
