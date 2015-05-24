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
	private AccelatorBinder mBinder;
	private boolean isMoved;
	float[] mAccelatorValues=new float[3];
	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBinder = (AccelatorBinder) service;
			Log.d("Test", "check if the mbind is the same- "+mBinder.accelatorValues[1]);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/**
		 * ������������
		 */
		Intent startIntent = new Intent(this, MoveDetectiveService.class);
		startService(startIntent);
		/**
		 * �󶨷���
		 */
		Intent bindIntent = new Intent(this, MoveDetectiveService.class);
		bindService(bindIntent, connection, BIND_AUTO_CREATE);
		
		/**
		 * ���Mbinder������
		 */
		//Log.d("Test", "check binder again: is alive?-"+mBinder.isBinderAlive()+"-"+mBinder.id);
		
		
		//��ʾ��γ�Ⱥͼ��ٶȵ�texView
		positionView = (TextView) findViewById(R.id.location_view);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		providerList = locationManager.getProviders(true);
		/**
		 * ����ʹ�����綨λ��
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
		locationManager.requestLocationUpdates(provider, 2000,1,
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
			// λ�÷����˸ı�
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

	private  void  showLocation(Location location) {
		
		String currentPosition = "γ���ǣ�" + location.getLatitude() + "\n"
				+ "�����ǣ�" + location.getLongitude() + "\n"+ mBinder.accelatorValues[0]+ "\n"
				+ mBinder.accelatorValues[1] + "\n" + mBinder.accelatorValues[2];
		positionView.setText(currentPosition);

		int i = 0;
		Log.d("Test", "-" + (i++) + location.toString());

	}

}
