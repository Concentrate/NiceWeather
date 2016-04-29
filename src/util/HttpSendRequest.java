package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpSendRequest {
	public static void HttpSendRequest(final String address, final HttpCallbackListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				URL theurl;
				HttpURLConnection connection = null;
				try {
					theurl = new URL(address);
					connection = (HttpURLConnection) theurl.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					StringBuilder message = new StringBuilder();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					String line;
					while ((line = reader.readLine()) != null)
						message.append(line);
					if (listener != null)
						listener.onFinish(message.toString());

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(listener!=null)
					listener.onError(e.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(listener!=null)
						listener.onError(e.toString());
				}finally
				{
					if(connection!=null)
						connection.disconnect();
				}

			}

		}).start();

	}

}
