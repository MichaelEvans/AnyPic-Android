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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class StreamActivity extends FragmentActivity implements
ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	StaggeredGridView mSGV;
	SGVAdapter mAdapter;
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

		mSGV = (StaggeredGridView) findViewById(R.id.grid);
		mSGV.setColumnCount(4);
//		mAdapter = new SGVAdapter(StreamActivity.this, mSGV);
//		mSGV.setAdapter(mAdapter);
		//Gonna need a progress bar.
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseQuery query = new ParseQuery("Photo");
		query.whereEqualTo("ACL", currentUser.getACL());
		final ArrayList<String> bitmapList = new ArrayList<String>();
		System.out.println("Bitmap List " + bitmapList.size());
		mAdapter = new SGVAdapter(StreamActivity.this, mSGV, bitmapList);
		mSGV.setAdapter(mAdapter);
		
//		mAdapter.notifyDataSetChanged();
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> scoreList, ParseException e) {
				
				if (e == null) {
					Log.d("score", "Retrieved " + scoreList.size() + " scores");
					
					for(ParseObject object : scoreList){
						
						ParseFile imageFile = (ParseFile)object.get("imageFile");
						bitmapList.add(imageFile.getUrl());
						System.out.println(bitmapList);
						mAdapter.setBitmaps(bitmapList);
						mAdapter.notifyDataSetChanged();
						
//						imageFile.getDataInBackground(new GetDataCallback() {
//							public void done(byte[] data, ParseException e) {
//								if (e == null) {
//									Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
//									Log.d("Stream", "Bitmap" + bMap.toString());
//									System.out.println(bMap);
//									//test.setImageBitmap(bMap);
//									//bitmapList.add(bMap);
////									mSGV.
//								} else {
//									// something went wrong
//									System.out.println("Something went wrong.");
//								}
//							}
//						});
					}
					
//					mAdapter.setBitmaps(bitmapList);

					System.out.println("Adding View");
					//nowLayout.addView(iv);
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});
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

}
