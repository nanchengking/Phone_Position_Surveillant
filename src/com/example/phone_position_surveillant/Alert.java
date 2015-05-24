package com.example.phone_position_surveillant;

import android.content.Context;
import android.media.MediaPlayer;

public class Alert {
	
	private static Alert alert=null;
	private static MediaPlayer mp;
	
	/**
	 * ���ڻ�ȡһ��Alert����
	 * @param context ��ȡAlert��������Ҫ��������
	 * @return alert ���ڲ�������
	 */
	public static synchronized	Alert getInstance(Context context){
		alert=new Alert(context);
		return alert;
	}

	private Alert(Context context) {
		mp = MediaPlayer.create(context, R.raw.music);
	}

	/**
	 * �������֣���������
	 */
	public void playMusic() {	
			mp.start();
	}

}
