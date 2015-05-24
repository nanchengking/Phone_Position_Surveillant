package com.example.phone_position_surveillant;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

/**sensorManager  传感器管理
 * listener  传感器监听
 * @author Administrator
 *
 */
public class MoveDetectiveService extends Service{
	
	private SensorManager sensorManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		
		Sensor sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}
	

	private SensorEventListener listener=new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
		  //得到三个方向的加速度
		   float xValue=Math.abs(event.values[0]);
		   float yValue=Math.abs(event.values[1]);
		   float zValue=Math.abs(event.values[2]);	
		   
		   if(xValue>15||yValue>15||zValue>16){
			   //认为手机出现了晃动
			   Toast.makeText(MoveDetectiveService.this, "手机被移动", Toast.LENGTH_SHORT);
		   }		
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
			
		}
	};

}
