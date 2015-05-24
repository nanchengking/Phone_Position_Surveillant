package com.example.phone_position_surveillant;

import android.content.Context;
import android.media.MediaPlayer;

public class Alert {

	public MediaPlayer mp;

	public Alert(Context context) {
		mp = MediaPlayer.create(context, R.raw.music);
	}

	public void playMusic() {
		if (!mp.isPlaying()) {
			mp.start();
		}
	}

}
