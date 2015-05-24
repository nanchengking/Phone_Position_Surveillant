package com.example.phone_position_surveillant;

import android.os.Binder;
import android.util.Log;

public class AccelatorBinder extends Binder {

	static boolean misMoveed;
	
	static float[] accelatorValues = new float[3];
	
	private static AccelatorBinder mBinder=new AccelatorBinder();
	
	private AccelatorBinder(){
		
	}
	
	public static synchronized AccelatorBinder getInstence(){
		return mBinder;
	}

	public boolean isMoved() {
		return misMoveed;
	}
}
