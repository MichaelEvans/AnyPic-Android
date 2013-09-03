package com.evans.anypic;

import android.app.Application;

//I wish I didn't have to do this. Sadly, ParseObjects are not serializable, nor parcelable :(
public class AnyPicApplication extends Application {
	private static AnyPicApplication singleton;

	public AnyPicApplication getInstance(){
		return singleton;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
	}
}
