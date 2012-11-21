package com.evans.anypic;

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class CaptureActivity extends Activity {
	private Uri mImageUri;
	private int CAMERA_REQUEST = 0;
	private ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		imageView = (ImageView) findViewById(R.id.image_preview);
		Log.i("CaptureActivity", "About to capture image");
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		mImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
				"tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

		cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageUri);                     
		cameraIntent.putExtra("return-data", true);
		startActivityForResult(cameraIntent, CAMERA_REQUEST); 

	}

	public void grabImage(ImageView imageView) {
		this.getContentResolver().notifyChange(mImageUri, null);
		ContentResolver cr = this.getContentResolver();
		Bitmap bitmap;
		try {
			bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr,
					mImageUri);
			imageView.setImageBitmap(bitmap);
		} catch (Exception e) {
			Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
			Log.d("CaptureActivity", "Failed to load", e);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			// ... some code to inflate/create/find appropriate ImageView to
			// place grabbed image
			this.grabImage(imageView);
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_capture, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
