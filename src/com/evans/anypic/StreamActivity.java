package com.evans.anypic;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.StaggeredGridView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.evans.model.Activity;
import com.evans.model.Photo;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class StreamActivity extends FragmentActivity implements
ActionBar.OnNavigationListener, OnItemClickListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	ListView mListView;
	private StreamAdapter mAdapter;
	//SGVAdapter mAdapter;
	private ParseUser mCurrentUser;
	//private NowLayout nowLayout;
	private ImageView test;

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stream);

		//nowLayout = (NowLayout) findViewById(R.id.nowLayout);
		//test = (ImageView) findViewById(R.id.txtView);
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
				// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
					getString(R.string.title_section1),
					getString(R.string.title_section2),
					getString(R.string.title_section3), }), this);

		mListView = (ListView) findViewById(R.id.grid);
		mAdapter = new StreamAdapter(this, new ArrayList<Photo>());
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
//		mAdapter = new SGVAdapter(StreamActivity.this, mSGV);
//		mSGV.setAdapter(mAdapter);
		//Gonna need a progress bar.
		mCurrentUser = ParseUser.getCurrentUser();
		// Query for the friends the current user is following
		
		
//		followingActivitiesQuery.whereEqualTo("ACL", currentUser.getACL());
//		final ArrayList<String> bitmapList = new ArrayList<String>();
//		System.out.println("Bitmap List " + bitmapList.size());
		
	}
	
	protected void onResume(){
		super.onResume();
		
		ParseQuery<Photo> photoQuery = buildParseQuery();
		photoQuery.findInBackground(new FindCallback<Photo>() {
			@Override
			public void done(List<Photo> photos, ParseException error) {
				Log.e("PhotoLoad", "Loaded " + photos.size() + " photos.");
				if(photos != null){
					mAdapter.clear();
					mAdapter.addAll(photos);
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	private ParseQuery<Photo> buildParseQuery() {
		ParseQuery<Activity> followingActivitiesQuery = ParseQuery.getQuery(Activity.class);
		followingActivitiesQuery.whereEqualTo("type", Activity.FOLLOW);
		followingActivitiesQuery.whereEqualTo("fromUser", mCurrentUser);
		
		// Using the activities from the query above, we find all of the photos taken by
	    // the friends the current user is following
		ParseQuery<Photo> photosFromFollowedUsersQuery = ParseQuery.getQuery(Photo.class);
		photosFromFollowedUsersQuery.whereMatchesKeyInQuery("user", "toUser", followingActivitiesQuery);
		photosFromFollowedUsersQuery.whereExists("image");
		
		ParseQuery<Photo> photosFromCurrentUserQuery = ParseQuery.getQuery(Photo.class);
		photosFromCurrentUserQuery.whereEqualTo("user", mCurrentUser);
		photosFromCurrentUserQuery.whereExists("image");
		
		ArrayList<ParseQuery<Photo>> queries = new ArrayList<ParseQuery<Photo>>();
		queries.add(photosFromFollowedUsersQuery);
		queries.add(photosFromCurrentUserQuery);
		ParseQuery<Photo> query = ParseQuery.or(queries);
		query.include("user");
		query.orderByDescending("createdAt");
		return query;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_stream, menu);
		return true;
	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case R.menu.activity_capture:
	// Intent i = new Intent(this, CaptureActivity.class);
	// startActivity(i);
	// return true;
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	// }

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_capture:
			Log.e("StreamActivity", "Clicked Capture");
			Intent i = new Intent(this, CaptureActivity.class);
			startActivity(i);
			break;
		case R.id.menu_logout:
			ParseUser.logOut();
			i = new Intent(this, LoginOrRegisterActivity.class);
			startActivity(i);
			finish();
			break;
		case R.id.menu_settings:
			i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.container, fragment).commit();
		return true;
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			//			textView.setText(Integer.toString(getArguments().getInt(
			//					ARG_SECTION_NUMBER)));
			return textView;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Photo photo = mAdapter.getItem(position);
		Intent i = new Intent(this, PhotoDetailsActivity.class);
		i.putExtra("photo", photo.getObjectId());
		startActivity(i);
	}

}
