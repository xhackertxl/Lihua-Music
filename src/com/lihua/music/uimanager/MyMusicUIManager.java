/**
 * Copyright (c) www.longdw.com
 */
package com.lihua.music.uimanager;

import com.lihua.music.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.lihua.music.activity.IConstants;
import com.lihua.music.activity.MainContentActivity;
import com.lihua.music.activity.MusicListSearchActivity;
import com.lihua.music.model.MusicInfo;
import com.lihua.music.service.ServiceManager;
import com.lihua.music.utils.MusicTimer;
import com.lihua.music.utils.MusicUtils;
import com.lihua.music.view.AlwaysMarqueeTextView;

/**
 * 处理歌曲列表页中的UI元素，包括底部view，SlidingDrawer等
 * 
 * @author tacker(335682638@qq.com)
 * 
 */
@SuppressLint("HandlerLeak")
public class MyMusicUIManager implements OnClickListener,
		OnSeekBarChangeListener, IConstants {

	private Activity mActivity;
	private View mView;
	private ServiceManager mServiceManager;
	private AlwaysMarqueeTextView mMusicNameTv, mArtistTv,mAlbumname;
	private TextView mPositionTv, mDurationTv , music_sum , now_position ;
	private ImageButton mPlayBtn, mPauseBtn, mNextBtn, mMenuBtn;
	private SeekBar mPlaybackProgress;
	public Handler mHandler;
	private Bitmap mDefaultAlbumIcon;
	private ImageView mHeadIcon;
	private ImageButton mBackBtn;



	public MyMusicUIManager(Activity a, ServiceManager sm, View view,
			UIManager manager) {
		this.mActivity = a;
		this.mView = view;

		this.mServiceManager = sm;
		initView();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				refreshSeekProgress(mServiceManager.position(),
						mServiceManager.duration());
			}
		};
	}
	
	public MyMusicUIManager(Activity a, ServiceManager sm, View view) {
		this.mActivity = a;
		this.mView = view;

		this.mServiceManager = sm;
		initView();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				refreshSeekProgress(mServiceManager.position(),
						mServiceManager.duration());
			}
		};
	}

	private void initView() {

		mBackBtn = (ImageButton) findViewById(R.id.backBtn);
		mMusicNameTv = (AlwaysMarqueeTextView) findViewById(R.id.musicname_tv2);
		mAlbumname = (AlwaysMarqueeTextView) findViewById(R.id.albumname);
		mArtistTv = (AlwaysMarqueeTextView) findViewById(R.id.artist_tv2);
		mPositionTv = (TextView) findViewById(R.id.position_tv2);
		mDurationTv = (TextView) findViewById(R.id.duration_tv2);
		mPlayModeTv = (TextView) findViewById(R.id.txt_play_mode);
		mPlayModeBtn = (ImageButton) findViewById(R.id.loop);
		mPrevBtn = (ImageButton) findViewById(R.id.btn_playPre2);
		mStopBtn = (ImageButton) findViewById(R.id.btn_stop2);
		mPlayBtn = (ImageButton) findViewById(R.id.btn_play2);
		mPauseBtn = (ImageButton) findViewById(R.id.btn_pause2);
		mNextBtn = (ImageButton) findViewById(R.id.btn_playNext2);				
		mMenuBtn = (ImageButton) findViewById(R.id.btn_menu2);
		
		
		music_sum = (TextView) findViewById(R.id.music_sum);
		now_position = (TextView) findViewById(R.id.now_position);
		
		mStopBtn.setOnClickListener(this);
		mPrevBtn.setOnClickListener(this);
		mPlayModeBtn.setOnClickListener(this);
		mBackBtn.setOnClickListener(this);
		mPlayBtn.setOnClickListener(this);
		mPauseBtn.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);
		mMenuBtn.setOnClickListener(this);

		mPlaybackProgress = (SeekBar) findViewById(R.id.playback_seekbar2);
		mPlaybackProgress.setOnSeekBarChangeListener(this);

		mDefaultAlbumIcon = BitmapFactory.decodeResource(
				mActivity.getResources(), R.drawable.albumart_mp_unknown);
		/*
		 * mDefaultAlbumIcon = new BitmapDrawable(mActivity.getResources(), b);
		 * // no filter or dither, it's a lot faster and we can't tell the //
		 * difference mDefaultAlbumIcon.setFilterBitmap(false);
		 * mDefaultAlbumIcon.setDither(false);
		 */

		mHeadIcon = (ImageView) findViewById(R.id.headicon_iv);
	}

	public void refreshSeekProgress(int curTime, int totalTime) {

		curTime /= 1000;
		totalTime /= 1000;
		int curminute = curTime / 60;
		int cursecond = curTime % 60;

		String curTimeString = String.format("%02d:%02d", curminute, cursecond);
		mPositionTv.setText(curTimeString);


		if(mPlaybackProgress.getMax() != totalTime)			
			mPlaybackProgress.setMax(totalTime);
		
		mPlaybackProgress.setProgress(curTime);
	}

	public void refreshUI(int curTime, int totalTime, MusicInfo music) {

		mCurrentMusicInfo = music;

		int tempCurTime = curTime;
		int tempTotalTime = totalTime;

		totalTime /= 1000;
		int totalminute = totalTime / 60;
		int totalsecond = totalTime % 60;
		String totalTimeString = String.format("%02d:%02d", totalminute,
				totalsecond);

		mDurationTv.setText(totalTimeString);

		mMusicNameTv.setText(music.musicName);
		mAlbumname.setText(music.album);
		mArtistTv.setText(music.artist);

		Bitmap bitmap = MusicUtils.getCachedArtwork(mActivity, music.albumId,
				mDefaultAlbumIcon);

		mHeadIcon.setBackgroundDrawable(new BitmapDrawable(mActivity
				.getResources(), bitmap));
				
		refreshSeekProgress(tempCurTime, tempTotalTime);
	}

	public void showPlay(boolean flag) {
		if (flag) {
			mPlayBtn.setVisibility(View.VISIBLE);
			mPauseBtn.setVisibility(View.GONE);
		} else {
			mPlayBtn.setVisibility(View.GONE);
			mPauseBtn.setVisibility(View.VISIBLE);
		}
	}

	public View findViewById(int id) {
		return mView.findViewById(id);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_playPre2:
			if (mCurrentMusicInfo == null) {
				return;
			}
			mServiceManager.prev();
			break;
		case R.id.btn_stop2:
			if (mCurrentMusicInfo == null) {
				return;
			}
			mServiceManager.stop();
			break;
		case R.id.btn_play2:
			mServiceManager.rePlay();
			break;
		case R.id.btn_pause2:
			mServiceManager.pause();
			break;
		case R.id.btn_playNext2:
			mServiceManager.next();
			break;
		case R.id.backBtn:
			// MainContentActivity.mSlidingMenu.showMenu();
			// mActivity.onBackPressed();
			//mUIManager.setCurrentItem();
			break;
		case R.id.btn_menu2:
			((MainContentActivity) mActivity).mSlidingMenu.showMenu(true);

		case R.id.loop:
			changeMode();
			break;
		}

	}

	private MusicInfo mCurrentMusicInfo;
	private ImageButton mPrevBtn, mStopBtn;
	private int mProgress;
	private boolean mPlayAuto = true;
	private MusicTimer mMusicTimer;

	public void setMusicTimer(MusicTimer musicTimer) {
		this.mMusicTimer = musicTimer;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (seekBar == mPlaybackProgress) {
			if (!mPlayAuto) {
				mProgress = progress;
				// mServiceManager.seekTo(progress);
				// refreshSeekProgress(mServiceManager.position(),
				// mServiceManager.duration());
			}
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		if (seekBar == mPlaybackProgress) {
			mPlayAuto = false;
			mMusicTimer.stopTimer();
			mServiceManager.pause();
		}
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (seekBar == mPlaybackProgress) {
			mPlayAuto = true;
			mServiceManager.seekTo(mProgress);
			refreshSeekProgress(mServiceManager.position(),
					mServiceManager.duration());
			mServiceManager.rePlay();
			mMusicTimer.startTimer();
		}
	}

	private TextView mPlayModeTv;
	private ImageButton mPlayModeBtn;
	private int mCurMode;
	private static final String modeName[] = { "全部循环", "顺序播放", "随机播放", "单曲循环" };
	
	// 播放模式
	public static final int MPM_LIST_LOOP_PLAY = 0; // 全部循环
	public static final int MPM_ORDER_PLAY = 1; // 顺序播放
	public static final int MPM_RANDOM_PLAY = 2; // 随机播放
	public static final int MPM_SINGLE_LOOP_PLAY = 3; // 单曲循环

	private void changeMode() {
		mCurMode++;
		if (mCurMode > MPM_SINGLE_LOOP_PLAY) {
			mCurMode = MPM_LIST_LOOP_PLAY;
		}
		mServiceManager.setPlayMode(mCurMode);
		initPlayMode();
	}

	private void initPlayMode() {
		mPlayModeTv.setText(modeName[mCurMode]);
	}
	
	public void setMusicList (String now , String num)
	{

		music_sum.setText(num);
		now_position.setText(now);
	}
	
	public void setMusicList (String now)
	{
		Integer text = mServiceManager.getMusicList().size();		
		music_sum.setText(text.toString());
		text  = mServiceManager.position();
		now_position.setText(now);
	}
}
