package com.evans.anypic;

import java.util.ArrayList;
import java.util.Random;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.widget.StaggeredGridView;
import android.support.v4.widget.StaggeredGridView.LayoutParams;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SGVAdapter extends BaseAdapter {

	LayoutInflater mInflater;
	StaggeredGridView mSGV;
	ArrayList<String> list;
	public SGVAdapter(Context ctx, StaggeredGridView sgv, ArrayList<String> imageList) {
		mInflater = LayoutInflater.from(ctx);
		mSGV = sgv;
		list = imageList;
	}

	@Override
	public int getCount() {
		return 5;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final LayoutParams lp;
		int maxSpan = mSGV.getColumnCount();
		int halfSpan = maxSpan / 2;
		final View v;
		switch (position) {
		case 0:
			v = mInflater.inflate(R.layout.element_item, parent, false);
			ImageView iv = (ImageView)v.findViewById(R.id.imageView1);
			//iv.setImageResource(R.drawable.icon);
			if(list.size() > position)
				UrlImageViewHelper.setUrlDrawable(iv, list.get(position));
			else
				UrlImageViewHelper.setUrlDrawable(iv, "https://www.google.com/images/srpr/logo3w.png");
				
				//iv.setImageBitmap(list.get(0));
			lp = new LayoutParams(v.getLayoutParams());
			lp.span = halfSpan;
			break;
		case 1:
		case 3:
		case 4:
			v = mInflater.inflate(R.layout.element_item, parent, false);
			lp = new LayoutParams(v.getLayoutParams());
			lp.span = halfSpan;
			break;
		default:
			v = mInflater.inflate(R.layout.element_item_large, parent, false);
			lp = new LayoutParams(v.getLayoutParams());
			lp.span = maxSpan;
			break;
		}
//		case 0:
//		case 29:
//			v = mInflater.inflate(R.layout.element_header, parent, false);
//			lp = new LayoutParams(v.getLayoutParams());
//			lp.span = mSGV.getColumnCount();
//			break;
////		case 8:
////		case 9:
////		case 18:
////		case 19:
////			v = mInflater.inflate(R.layout.element_item_small, parent, false);
////			lp = new LayoutParams(v.getLayoutParams());
////			lp.span = 1;
////			break;
//		case 10:
//		case 20:
//			v = mInflater.inflate(R.layout.element_item_large, parent, false);
//			lp = new LayoutParams(v.getLayoutParams());
//			lp.span = 4;
//			break;
//		default:
//			v = mInflater.inflate(R.layout.element_item, parent, false);
//			lp = new LayoutParams(v.getLayoutParams());
//			lp.span = 2;
//			break;
//		}
		v.setLayoutParams(lp);
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("SGVAdapter", "Image Clicked at Position #" + position);
				//TODO: Do you launch the new activity from here?
			}
		});
		return v;
	}

	public void setBitmaps(ArrayList<String> bitmapList) {
		list = bitmapList;
	}
}