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
 * sensorManager ���������� listener ����������
 * 
 * @author Administrator
 *
 */
public class MoveDetectiveService extends Service {

	boolean isMoveed = false;
	float xValue;
	float yValue;
	float zValue;
	private SensorManager sensorManager;
	// һ�����ڰ󶨵�binder
	private AccelatorBinder mBinder = new AccelatorBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		Sensor sensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// ע�ᴫ����
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
			// �õ���������ļ��ٶ�
			float mxValue = Math.abs(event.values[0]);
			float myValue = Math.abs(event.values[1]);
			float mzValue = Math.abs(event.values[2]);
			xValue = mxValue;
			yValue = myValue;
			zValue = mzValue;

			if (mxValue > 15 || myValue > 15 || mzValue > 16) {
				// ��Ϊ�ֻ������˻ζ�
				Toast.makeText(MoveDetectiveService.this, "�ֻ����ƶ�",
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
	 * һ���ڲ��࣬binder�������ڻ�ܹ��ͷ���֮��ͨ�ţ��ܹ�ͨ��getAccelatorMeaasge�������õ�һ�����ٶ�����
	 * 
	 * @author Administrator
	 *
	 */
	class AccelatorBinder extends Binder {
		private boolean misMoveed;
		float[] accelatorValues = new float[3];

		public AccelatorBinder() {
			super();
			misMoveed = isMoveed;
			accelatorValues[0] = xValue;
			accelatorValues[1] = yValue;
			accelatorValues[2] = zValue;
		}

		public boolean isMoved() {
			return misMoveed;
		}
	}

}
