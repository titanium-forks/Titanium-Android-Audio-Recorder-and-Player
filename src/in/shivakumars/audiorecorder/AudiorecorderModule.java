/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package in.shivakumars.audiorecorder;

import java.io.File;
import java.io.IOException;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

@Kroll.module(name = "Audiorecorder", id = "in.shivakumars.audiorecorder")
public class AudiorecorderModule extends KrollModule {

	// Standard Debugging variables
	private static final String LCAT = "AudiorecorderModule";
	private static final boolean DBG = TiConfig.LOGD;
	private MediaRecorder mRecorder = null;
	private MediaPlayer mPlayer = null;
	private String audioStoragePath;
	private boolean recording = false;
	private boolean playing = false;
	private int type = 0;

	// You can define constants with @Kroll.constant, for example:
	// @Kroll.constant public static final String EXTERNAL_NAME = value;

	public AudiorecorderModule() {
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
		Log.d(LCAT, "inside onAppCreate");
		// put module init code that needs to run when the application is
		// created
	}

	private void playAudioFile(String path) {
		if (!playing) {
			playing = true;
			mPlayer = new MediaPlayer();
			try {
				mPlayer.setDataSource(path);
				mPlayer.prepare();
				mPlayer.start();
			} catch (IOException e) {
				Log.e(LCAT, "prepare() failed");
				Log.e(LCAT, e.toString());
			}
		}
	}

	private void stopPlaying() {
		if (playing) {
			playing = false;
			mPlayer.release();
			mPlayer = null;
		}
	}

	private void recordAudio(String fileName, boolean useSDCard) {
		if (!recording) {
			recording = true;
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			if (type == 0) {
				mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			} else if (type == 1) {
				mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			}
			if (useSDCard) {
				audioStoragePath = Environment.getExternalStorageDirectory()
						.getPath()
						+ "/"
						+ TiApplication.getAppCurrentActivity()
								.getPackageName() + "/" + fileName;
			} else {
				ContextWrapper cw = new ContextWrapper(
						TiApplication.getAppRootOrCurrentActivity());
				File directory = cw.getDir("audioDir", Context.MODE_PRIVATE);
				audioStoragePath = new File(directory, fileName)
						.getAbsolutePath();
			}

			if (!audioStoragePath.contains("mp4"))
				audioStoragePath += ".mp4";

			mRecorder.setOutputFile(audioStoragePath);

			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			try {
				mRecorder.prepare();
			} catch (IOException e) {
				Log.e(LCAT, "prepare() failed");
				Log.e(LCAT, e.toString());
			}

			mRecorder.start();
		}
	}

	private void stopRecordingAudio() {
		if (recording) {
			recording = false;
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
	}

	String file;

	// Methods
	@Kroll.method
	public void startRecording(String fileName, boolean canUseSDcard, int type) {
		Log.d(LCAT, "start Rec called");
		this.type = type;
		recordAudio(fileName, canUseSDcard);
		file = fileName;
	}

	@Kroll.method
	public String stopRecording() {
		Log.d(LCAT, "stop Rec called");
		stopRecordingAudio();
		return audioStoragePath;
	}

	@Kroll.method
	public void playAudio(String fileName) {
		Log.d(LCAT, "start Play called");
		playAudioFile(fileName);
	}

	@Kroll.method
	public void stopAudio() {
		Log.d(LCAT, "stop Play called");
		stopPlaying();
	}

	@Kroll.method
	public Boolean isPlayerPlaying() {
		// Log.d(LCAT, "stop Play called");
		return mPlayer.isPlaying();
	}

}
