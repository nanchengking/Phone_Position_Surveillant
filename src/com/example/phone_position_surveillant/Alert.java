package com.example.phone_position_surveillant;

import android.content.Context;
import android.media.MediaPlayer;

public class Alert {
	
	private static Alert alert=null;
	private static MediaPlayer mp;
	
	/**
	 * 用于获取一个Alert单例
	 * @param context 获取Alert单例所需要的上下文
	 * @return alert 用于播放音乐
	 */
	public static synchronized	Alert getInstance(Context context){
		alert=new Alert(context);
		return alert;
	}

	private Alert(Context context) {
		mp = MediaPlayer.create(context, R.raw.music);
	}

	/**
	 * 播放音乐（警告音）
	 */
	public void playMusic() {	
			mp.start();
	}

}
