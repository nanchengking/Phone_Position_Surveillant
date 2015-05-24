package com.example.phone_position_surveillant;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * sensorManager 传感器管理 listener 传感器监听
 * 
 * @author Administrator
 *
 */
public class MoveDetectiveService extends Service {

	boolean isMoveed = false;
	float xValue=0;
	float yValue=0;
	float zValue=0;
	private SensorManager sensorManager;
	// 一个用于绑定的binder
    private AccelatorBinder mBinder = new AccelatorBinder();

	@Override
	public IBinder onBind(Intent intent) {
		mBinder.id=007;
		Log.d("Test","returned mbind--" +mBinder.isMoved()+"--"+mBinder.getInterfaceDescriptor());
		return mBinder;		
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		Sensor sensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// 注册传感器
		sensorManager.registerListener(listener, sensor,
				sensorManager.SENSOR_DELAY_NORMAL);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (sensorManager != null) {
			sensorManager.unregisterListener(listener);
		}
	}

	private SensorEventListener listener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// 得到三个方向的加速度
			float mxValue = Math.abs(event.values[0]);
			float myValue = Math.abs(event.values[1]);
			float mzValue = Math.abs(event.values[2]);
			xValue = mxValue;
			yValue = myValue;
			zValue = mzValue;

			if (mxValue > 15 || myValue > 15 || mzValue > 16) {
				// 认为手机出现了晃动
				Toast.makeText(MoveDetectiveService.this, "手机被移动",
						Toast.LENGTH_SHORT).show();
				isMoveed = true;
				int i = 0;
				Log.d("Test", "--" + (i++) + "\n" + xValue + "\n" + yValue
						+ "\n" + zValue);

			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};

	/**
	 * 一个内部类，binder，用来在活动能够和服务之间通信，能够通过getAccelatorMeaasge（）来得到一个加速度数组
	 * 
	 * @author Administrator
	 *
	 */
	class AccelatorBinder extends Binder {
		private boolean misMoveed;
		int id=0;
		float[] accelatorValues = new float[3];

		public AccelatorBinder() {
			super();
			misMoveed = isMoveed;
			accelatorValues[0] = xValue;
			accelatorValues[1] = yValue;
			accelatorValues[2] = zValue;
			id=1234567890;
			Log.d("Test", "Binder is generated!");
		}

		public boolean isMoved() {
			return misMoveed;
		}
	}

}
