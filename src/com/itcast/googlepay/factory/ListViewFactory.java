package com.itcast.googlepay.factory;

import com.itcast.googlepay.utils.UIUtils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;

public class ListViewFactory {
	
	public static ListView getListView() {
		ListView listView = new ListView(UIUtils.getContext());
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		return listView;
	}
}
