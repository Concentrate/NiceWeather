package util;

public interface HttpCallbackListener {
	void onFinish(String message);
	void onError(String message);

}
