package com.evans.anypic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.evans.model.Photo;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CaptureActivity extends Activity {
	private Uri mImageUri;
	private int CAMERA_REQUEST = 1;
	private ImageView imageView;
	private Bitmap bitmap;
	private Button upload;
	private EditText caption;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		imageView = (ImageView) findViewById(R.id.image_preview);
		upload = (Button) findViewById(R.id.upload_button);
		caption = (EditText) findViewById(R.id.image_caption);
		upload.setEnabled(false);
		Log.i("CaptureActivity", "About to capture image");
		//		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		mImageUri = Uri.fromFile(new File(getExternalFilesDir(null),
				"tmp_avatar_" + "Anypic" + ".jpg"));
		System.out.println(mImageUri.toString());

		//
		//		cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageUri);                     
		//		cameraIntent.putExtra("return-data", true);
		//		startActivityForResult(cameraIntent, CAMERA_REQUEST); 
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		startActivityForResult(cameraIntent, CAMERA_REQUEST);

		upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView.setEnabled(false);
				final Photo image = new Photo();
				image.setACL(new ParseACL(ParseUser.getCurrentUser()));
				image.setUser(ParseUser.getCurrentUser());
				image.put("caption", caption.getText().toString());
				byte[] bytes = null;
				try {
					File file = new File(getExternalFilesDir(null),
							"tmp_avatar_" + "Anypic" + ".jpg");
					/*bytes = FileUtils.readFileToByteArray(new File(getExternalFilesDir(null),
							"tmp_avatar_" + "Anypic" + ".jpg"));
							*/
					Bitmap bitmap = loadPrescaledBitmap(file.getAbsolutePath());
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					bytes = baos.toByteArray();
					
					final ParseFile fileImage = new ParseFile("file", bytes); 
					/*new ParseFile("image", new File(getExternalFilesDir(null),
							"tmp_avatar_" + "Anypic" + ".jpg"));*/
					final ParseFile thumbnailImage = new ParseFile("thumbnail", createThumbnail(new File(getExternalFilesDir(null),
							"tmp_avatar_" + "Anypic" + ".jpg")));
					//					thumbnailImage.saveInBackground(new SaveCallback() {
					//						
					//						@Override
					//						public void done(ParseException arg0) {
					// TODO Auto-generated method stub
					fileImage.saveInBackground(new SaveCallback() {

						@Override
						public void done(ParseException arg0) {
							// TODO Auto-generated method stub
							image.setImage(fileImage);
							//image.put("thumbnailFile", thumbnailImage);
							image.saveInBackground(new SaveCallback() {

								@Override
								public void done(ParseException arg0) {
									// TODO Auto-generated method stub
									finish();
								}
							});
						}
					});
					//						}
					//					});


				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	//	public void grabImage(ImageView imageView) {
	//		this.getContentResolver().notifyChange(mImageUri, null);
	//		ContentResolver cr = this.getContentResolver();
	//		Bitmap bitmap;
	//		try {
	//			bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr,
	//					mImageUri);
	//			imageView.setImageBitmap(bitmap);
	//		} catch (Exception e) {
	//			Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
	//			Log.d("CaptureActivity", "Failed to load", e);
	//		}
	//	}

	//	@Override
	//	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	//		if (resultCode == RESULT_OK) {
	//			// ... some code to inflate/create/find appropriate ImageView to
	//			// place grabbed image
	//			//this.grabImage(imageView);
	//		}
	//		super.onActivityResult(requestCode, resultCode, intent);
	//	}

	private byte[] createThumbnail(File fileName){
		byte[] imageData = null;

		try 
		{

			final int THUMBNAIL_SIZE = 1024;
			FileInputStream fis = new FileInputStream(fileName);
			Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

			Float width = new Float(imageBitmap.getWidth());
			Float height = new Float(imageBitmap.getHeight());
			Float ratio = width/height;
			imageBitmap = Bitmap.createScaledBitmap(imageBitmap, (int)(THUMBNAIL_SIZE * ratio), THUMBNAIL_SIZE, false);

			ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			imageData = baos.toByteArray();

		}
		catch(Exception ex) {
			Log.e("Crop", "thumbnail exception: ");
			ex.printStackTrace();
		}
		return imageData;
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//		if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
		//			try {
		//				// We need to recyle unused bitmaps
		//				if (bitmap != null) {
		//					bitmap.recycle();
		//				}
		//				InputStream stream = getContentResolver().openInputStream(data.getData());
		//				bitmap = BitmapFactory.decodeStream(stream);
		//				stream.close();
		//				imageView.setImageBitmap(bitmap);
		//			} catch (FileNotFoundException e) {
		//				e.printStackTrace();
		//			} catch (IOException e) {
		//				e.printStackTrace();
		//			}
		//		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
			//Bitmap photo = (Bitmap) data.getExtras().get("data");
			File f = new File(getExternalFilesDir(null),
					"tmp_avatar_" + "Anypic" + ".jpg");
			Bitmap photo = BitmapFactory.decodeFile(f.getAbsolutePath());
			Log.d("ANDROID_CAMERA","Picture taken!!!" + f.getAbsolutePath());
			Bitmap scaled = decodeFile(f.getAbsoluteFile());

			//			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			//			bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object   
			//			byte[] b = baos.toByteArray();  

			//Bitmap scaled = Bitmap.createScaledBitmap(photo, 2048, 2048, true);
			//imageView.setScaleType(ScaleType.CENTER_INSIDE);
			imageView.setImageBitmap(scaled);

			upload.setEnabled(true);
		} 
	}

	private Bitmap loadPrescaledBitmap(String filename) throws IOException {
		// Facebook image size
		final int IMAGE_MAX_SIZE = 1024;

		File file = null;
		FileInputStream fis;

		BitmapFactory.Options opts;
		int resizeScale;
		Bitmap bmp;

		file = new File(filename);

		// This bit determines only the width/height of the bitmap without loading the contents
		opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		fis = new FileInputStream(file);
		BitmapFactory.decodeStream(fis, null, opts);
		fis.close();

		// Find the correct scale value. It should be a power of 2
		resizeScale = 1;

		if (opts.outHeight > IMAGE_MAX_SIZE || opts.outWidth > IMAGE_MAX_SIZE) {
			resizeScale = (int)Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(opts.outHeight, opts.outWidth)) / Math.log(0.5)));
		}

		// Load pre-scaled bitmap
		opts = new BitmapFactory.Options();
		opts.inSampleSize = resizeScale;
		fis = new FileInputStream(file);
		bmp = BitmapFactory.decodeStream(fis, null, opts);

		fis.close();

		return bmp;
	} 

	//decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f){
		try {
			//Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f),null,o);

			//The new size we want to scale to
			final int REQUIRED_SIZE=2048;

			//Find the correct scale value. It should be the power of 2.
			int scale=1;
			while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
				scale*=2;

			//Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize=scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {}
		return null;
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
