/**
 * Copyright (c) www.longdw.com
 */
package com.lihua.music.interfaces;

import java.util.List;

import com.lihua.music.model.MusicInfo;

public interface IQueryFinished {
	
	public void onFinished(List<MusicInfo> list);

}
