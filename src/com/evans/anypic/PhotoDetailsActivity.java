package com.evans.anypic;

import java.util.List;

import com.evans.model.Photo;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class PhotoDetailsActivity extends Activity {

	EditText mCommentInput;
	ImageButton mCommentSubmit;
	Photo mPhoto;
	private String mPhotoId;
	ParseImageView mPhotoView;
	ListView mCommentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_details);
		// Show the Up button in the action bar.

		mPhotoId = getIntent().getStringExtra("photo");
		mCommentInput = (EditText) findViewById(R.id.comment_input);
		mCommentSubmit = (ImageButton) findViewById(R.id.comment_send);
		mPhotoView = (ParseImageView) findViewById(R.id.image);
		mCommentList = (ListView) findViewById(R.id.comment_list);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		ParseQuery<Photo> photoQuery = ParseQuery.getQuery(Photo.class);
		photoQuery.whereEqualTo("objectId", mPhotoId);
		photoQuery.getFirstInBackground(new GetCallback<Photo>() {
			@Override
			public void done(Photo photo, ParseException error) {
				Log.e("Details", "Error"  + photo);
				mPhoto = photo;
				mPhotoView.setParseFile(photo.getImage());
				mPhotoView.loadInBackground();
				ParseQuery<com.evans.model.Activity> query = queryComments(photo);
				query.findInBackground(new FindCallback<com.evans.model.Activity>() {
					@Override
					public void done(List<com.evans.model.Activity> comments, ParseException commentError) {
						
					}
				});
				mCommentSubmit.setEnabled(true);
				mCommentInput.setEnabled(true);
			}
		});

		mCommentSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				submitComment();
			}
		});
	}

	private void submitComment() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		String commentText = mCommentInput.getText().toString().trim();
		if(commentText.length() > 0 && mPhoto != null){ //photo.objectForKey("user")
			com.evans.model.Activity comment = new com.evans.model.Activity();
			comment.setContent(commentText);
			comment.setToUser(mPhoto.getUser());//TODO
			comment.setFromUser(currentUser);
			comment.setType(com.evans.model.Activity.COMMENT);
			comment.setPhoto(mPhoto);

			ParseACL acl = new ParseACL(currentUser);
			acl.setPublicReadAccess(true);
			comment.setACL(acl);

			//cache PAP

			comment.saveEventually();
		}
		mCommentInput.setText("");
		//reload comments
	}

	private ParseQuery<com.evans.model.Activity> queryComments(Photo photo) {
		ParseQuery<com.evans.model.Activity> query = ParseQuery.getQuery(com.evans.model.Activity.class);
		query.whereEqualTo("photo", photo);
		query.whereEqualTo("type", com.evans.model.Activity.COMMENT);
		query.include("user");
		query.orderByDescending("createdAt");

		return query;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_photo_details, menu);
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
