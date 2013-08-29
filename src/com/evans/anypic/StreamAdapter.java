package com.evans.anypic;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.evans.model.Photo;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;

public class StreamAdapter extends ArrayAdapter<Photo>{
	private List<Photo> mPhotos;
	private Context mContext;
	public StreamAdapter(Context context, List<Photo> objects) {
		super(context, R.layout.stream_item, objects);
		mPhotos = objects;
		mContext = context;
	}
	
	@Override
	public int getCount(){
		return mPhotos.size();
	}
	@Override
	public Photo getItem (int position){
		return mPhotos.get(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if(convertView == null){
			LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
			convertView = mLayoutInflater.inflate(R.layout.stream_item, null);
		}
		
		Photo photo = getItem(position);
		
		final ParseImageView imageView = (ParseImageView) convertView.findViewById(R.id.image);
		imageView.setParseFile(photo.getImage());
		//imageView.setPlaceholder()
		imageView.loadInBackground();
		
//		descriptionView.setText(task.getDescription());
//		
//		if(task.isCompleted()){
//			descriptionView.setPaintFlags(descriptionView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//		}else{
//			descriptionView.setPaintFlags(descriptionView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
//		}
		
		return convertView;
	}

}
