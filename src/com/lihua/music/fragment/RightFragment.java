/**
 * Copyright (c) www.longdw.com
 */
package com.lihua.music.fragment;

import com.lihua.music.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author tacker(335682638@qq.com)
 *
 */
public class RightFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.viewpager_trans_layout, container, false);
	}

}
