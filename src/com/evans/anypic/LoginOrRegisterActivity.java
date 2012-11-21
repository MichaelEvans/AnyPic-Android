package com.evans.anypic;

import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.PushService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class LoginOrRegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_or_register);
		
		Parse.initialize(this, "BII77Lso2wqjLSThScrTLqUTq6DQ8hhPMxX9C6sn", "0wRgjMrQNw4HPCWzwDrlAKytmwTy50luntP5o960");
		PushService.subscribe(this, "", LoginOrRegisterActivity.class);
		
		ParseUser user = ParseUser.getCurrentUser();
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
			    finish();
			}
		});
		Button goToRegister = (Button) findViewById(R.id.button2);
		goToRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginOrRegisterActivity.this, RegisterActivity.class);
			    startActivity(intent);
			    finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login_or_register, menu);
		return true;
	}

	

}
