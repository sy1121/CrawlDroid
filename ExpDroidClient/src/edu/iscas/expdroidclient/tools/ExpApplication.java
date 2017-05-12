package edu.iscas.expdroidclient.tools;

import android.app.Application;
import android.util.Log;

public class ExpApplication extends Application{
	private static final String TAG=ExpApplication.class.getSimpleName();
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		 Log.i(TAG,"onCreate");
	//	  CrashHandler crashHandler = CrashHandler.getInstance();
     //     crashHandler.init(getApplicationContext());
	}
}
