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

/**Alert 播放音乐的东西噢噢噢噢
 * sensorManager 传感器管理 listener 传感器监听
 * mBinder 绑定机制用的那个东西
 * RATE 加速度的分界线，一般大于9.8就好啦
 * @author Administrator
 *
 */
public class MoveDetectiveService extends Service {

	private final int RATE=11;
	private Alert alert;
	
	boolean isMoveed = false;
	private SensorManager sensorManager;
	// 一个用于绑定的binder
    private AccelatorBinder mBinder = AccelatorBinder.getInstence();

	@Override
	public IBinder onBind(Intent intent) {
		Log.d("Test","returned mbind--" +mBinder.isMoved()+"--"+mBinder.getInterfaceDescriptor());
		return mBinder;		
	}

	@Override
	public void onCreate() {
		super.onCreate();
		alert=Alert.getInstance(this);
		
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

			if (mxValue > RATE || myValue > RATE || mzValue > RATE) {
				mBinder.accelatorValues[0] = mxValue;
				mBinder.accelatorValues[1]= myValue;
				mBinder.accelatorValues[2] = mzValue;
				alert.playMusic();
				// 认为手机出现了晃动
				Toast.makeText(MoveDetectiveService.this, "手机被移动",
						Toast.LENGTH_SHORT).show();
				isMoveed = true;
				int i = 0;
				Log.d("Test", "--" + (i++) + "\n" + mBinder.accelatorValues[0]  + "\n" + mBinder.accelatorValues[1] 
						+ "\n" + mBinder.accelatorValues[2] );

			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};


}
