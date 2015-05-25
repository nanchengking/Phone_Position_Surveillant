package com.example.phone_position_surveillant;

import java.util.List;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapView;

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

	private boolean isServiceAlive = false;
	private TextView positionView;
	private LocationManager locationManager;
	private String provider;
	List<String> providerList;
	private AccelatorBinder mBinder;
	private boolean isMoved;

	/*
	 * 百度的东西哦哦哦
	 */
	private BMapManager baiduManager;
	private MapView mapView;

	float[] mAccelatorValues = new float[3];
	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBinder = (AccelatorBinder) service;
			Log.d("Test", "check if the mbind is the same- "
					+ mBinder.accelatorValues[1]);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * 获得baiduMapManager
		 */
		baiduManager = new BMapManager(this);
		baiduManager.init("MUChAgwIZd6RvtWm8Fx1fIu3", null);

		setContentView(R.layout.activity_main);

		// 再一次记住，控件初始化一定要在setContent之前——
		mapView = (MapView) findViewById(R.id.map_view);

		positionView = (TextView) findViewById(R.id.location_view);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		providerList = locationManager.getProviders(true);
		/**
		 * 优先使用网络定位器，我有什么办法呢， GPS初始化那么慢，劳资一次没成功现实过
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
		locationManager.requestLocationUpdates(provider, 2000, 1,
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
			/*
			 * 立马启动服务
			 */
			Intent startIntent = new Intent(this, MoveDetectiveService.class);
			startService(startIntent);
			/*
			 * 绑定服务
			 */
			Intent bindIntent = new Intent(this, MoveDetectiveService.class);
			bindService(bindIntent, connection, BIND_AUTO_CREATE);
			isServiceAlive = true;
			break;
		case R.id.stop_service:
			unbindService(connection);
			Intent stopService = new Intent(this, MoveDetectiveService.class);
			stopService(stopService);
			isServiceAlive = false;
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {

		mapView.destroy();
		if (baiduManager != null) {
			baiduManager.destroy();
			baiduManager = null;
		}

		if (isServiceAlive) {
			if (locationManager != null) {
				locationManager.removeUpdates(locationListener);
			}
			unbindService(connection);
			Intent stopService = new Intent(this, MoveDetectiveService.class);
			stopService(stopService);
		}

		super.onDestroy();

	}

	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// 位置发生了改变
			showLocation(location);

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
				+ "经度是：" + location.getLongitude() + "\n"
				+ mBinder.accelatorValues[0] + "\n"
				+ mBinder.accelatorValues[1] + "\n"
				+ mBinder.accelatorValues[2];
		positionView.setText(currentPosition);

	}

	@Override
	protected void onResume() {
		mapView.onResume();
		if (baiduManager != null) {
			baiduManager.start();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		mapView.onPause();
		if (baiduManager != null) {
			baiduManager.stop();
		}
		super.onPause();
	}

}
