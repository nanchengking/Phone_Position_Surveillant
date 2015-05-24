package com.example.phone_position_surveillant;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView positionView;
	private LocationManager locationManager;
	private String provider;
	List<String> providerList;
	private MoveDetectiveService.AccelatorBinder mBinder;
	private boolean isMoved;
	float[] mAccelatorValues=new float[3];
	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBinder = (MoveDetectiveService.AccelatorBinder) service;
			Log.d("Test", "check if the mbind is the same- "+mBinder.id);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/**
		 * 立马启动服务
		 */
		Intent startIntent = new Intent(this, MoveDetectiveService.class);
		startService(startIntent);
		/**
		 * 绑定服务
		 */
		Intent bindIntent = new Intent(this, MoveDetectiveService.class);
		bindService(bindIntent, connection, BIND_AUTO_CREATE);
		
		/**
		 * 获得Mbinder的数据
		 */
		/*Log.d("Test", mBinder.isBinderAlive()+"");
		isMoved = mBinder.isMoved();
		mAccelatorValues = mBinder.accelatorValues;*/
		
		//显示经纬度和加速度的texView
		positionView = (TextView) findViewById(R.id.location_view);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		providerList = locationManager.getProviders(true);
		/**
		 * 优先使用网络定位器
		 */
		if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
		} else if (providerList.contains(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
		} else {
			Toast.makeText(this, "No Location Provider to use",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		Location location = locationManager.getLastKnownLocation(provider);
		locationManager.requestLocationUpdates(provider, 2000,2,
				locationListener);
		if (location != null) {
			showLocation(location);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.star_service:
			
			break;
		case R.id.bind:
			
			break;
		case R.id.unbind:
			unbindService(connection);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locationManager != null) {
			locationManager.removeUpdates(locationListener);
		}
		unbindService(connection);
		Intent stopService = new Intent(this, MoveDetectiveService.class);
		stopService(stopService);

	}

	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// 位置发生了改变
			showLocation(location);
			Log.d("Test", location.toString() + 03);

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}
	};

	private void showLocation(Location location) {
		String currentPosition = "纬度是：" + location.getLatitude() + "\n"
				+ "经度是：" + location.getLongitude() + mAccelatorValues[0] + "\n"
				+ mAccelatorValues[1] + "\n" + mAccelatorValues[2];
		positionView.setText(currentPosition);

		int i = 0;
		Log.d("Test", "-" + (i++) + location.toString());

	}

}
