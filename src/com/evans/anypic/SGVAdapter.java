package com.evans.anypic;

import java.util.Random;

import android.content.Context;
import android.support.v4.widget.StaggeredGridView;
import android.support.v4.widget.StaggeredGridView.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SGVAdapter extends BaseAdapter {

	LayoutInflater mInflater;
	StaggeredGridView mSGV;

	public SGVAdapter(Context ctx, StaggeredGridView sgv) {
		mInflater = LayoutInflater.from(ctx);
		mSGV = sgv;
	}

	@Override
	public int getCount() {
		return 30;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	Random r = new Random();

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutParams lp;
		final View v;
		switch (position) {
		case 0:
		case 29:
			v = mInflater.inflate(R.layout.element_header, parent, false);
			lp = new LayoutParams(v.getLayoutParams());
			lp.span = mSGV.getColumnCount();
			break;
		case 8:
		case 9:
		case 18:
		case 19:
			v = mInflater.inflate(R.layout.element_item_small, parent, false);
			lp = new LayoutParams(v.getLayoutParams());
			lp.span = 1;
			break;
		case 10:
		case 20:
			v = mInflater.inflate(R.layout.element_item_large, parent, false);
			lp = new LayoutParams(v.getLayoutParams());
			lp.span = 4;
			break;
		default:
			v = mInflater.inflate(R.layout.element_item, parent, false);
			lp = new LayoutParams(v.getLayoutParams());
			lp.span = 2;
			break;
		}
		v.setLayoutParams(lp);
		return v;
	}
}