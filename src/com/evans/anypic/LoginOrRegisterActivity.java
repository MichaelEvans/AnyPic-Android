package com.evans.anypic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.evans.model.Photo;
import com.evans.model.User;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

public class LoginOrRegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_or_register);
//		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//		if(cm.getAllNetworkInfo() != null && cm.getAllNetworkInfo().length > 0){
//			if(!cm.getAllNetworkInfo()[0].isConnected()){
//				Intent intent = new Intent(LoginOrRegisterActivity.this, StreamActivity.class);
//				Log.d("CM", "CM " + cm.getAllNetworkInfo()[0]);
//			    startActivity(intent);
//			    //finish();
//			}
//		}
		ParseObject.registerSubclass(Photo.class);
		ParseObject.registerSubclass(User.class);
		ParseObject.registerSubclass(com.evans.model.Activity.class);
		Parse.initialize(this, "BII77Lso2wqjLSThScrTLqUTq6DQ8hhPMxX9C6sn", "0wRgjMrQNw4HPCWzwDrlAKytmwTy50luntP5o960");
		PushService.subscribe(this, "", LoginOrRegisterActivity.class);
		ParseAnalytics.trackAppOpened(getIntent());
		
		final ParseUser user = ParseUser.getCurrentUser();
		if(user != null){
			Log.i("LoginActivity", "We're in!");
			Intent intent = new Intent(LoginOrRegisterActivity.this, StreamActivity.class);
		    startActivity(intent);
		    finish();
		}
		
		
		// Show the Up button in the action bar.
		Button goToLogin = (Button) findViewById(R.id.button1);
		goToLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginOrRegisterActivity.this, LoginActivity.class);
			    startActivity(intent);
			    if(user != null){
			    	finish();
			    }
			}
		});
		Button goToRegister = (Button) findViewById(R.id.button2);
		goToRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginOrRegisterActivity.this, RegisterActivity.class);
			    startActivity(intent);
			    if(user != null){
			    	finish();
			    }
			}
		});
	}
}
