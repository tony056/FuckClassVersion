package com.example.fuckclassversion;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class FriendActivity extends ActionBarActivity{
	
	ListView listView;
	List<String> friendList;
	ArrayAdapter<String> arrayAdapter;
	Context context = this;
	ParseUser currentUser;
	ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.friendview);
		currentUser = ParseUser.getCurrentUser();
		//remoteTask execute
		
	}
	
	private void dialogBuilder(){
//		final Dialog dialog = new Dialog(context);
//		dialog.setContentView(R.layout.addfrienddialog);
//		dialog.setTitle("ADD FRIEND");
//		Button btnAddFriend = (Button) dialog.findViewById(R.id.buttonAdd);
//		Button btnCancel = (Button) dialog.findViewById(R.id.buttonCancel);
//		
//		btnAddFriend.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				EditText friendIdEditText = (EditText) dialog.findViewById(R.id.editTextFriednId);
//				addFriend(friendIdEditText.getText().toString());
//				dialog.dismiss();
//			}
//
//		});
//		
//		btnCancel.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
//		
//		dialog.show();
	}
	
	private void addFriend(final String targetUser) {
		Toast.makeText(this, "adding", Toast.LENGTH_SHORT).show();
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", targetUser);
		query.findInBackground(new FindCallback<ParseUser>() {
			
			@Override
			public void done(List<ParseUser> objects, ParseException e) {
				if(e == null){
					if(objects.size() == 1){
						ParseUser friend = objects.get(0);
						Log.e("DONE", "get user");
						ParseRelation<ParseUser> relation = currentUser.getRelation("Friend");
						relation.add(friend);
						currentUser.saveInBackground(new SaveCallback() {
							
							@Override
							public void done(ParseException e) {
								updateData();
							}
						});
					}
				}else{
					Log.e("Error", e.getMessage());
				}
				
			}
		});
		
	}
	
	private void sendPush(String target){
		ParseObject message = new ParseObject("Message");
		message.put("from", currentUser.getUsername());
		message.put("to", target);
		message.saveInBackground();
		Toast.makeText(context, "Message sent!", Toast.LENGTH_SHORT).show();
	}
	
	private void updateData(){
		friendList.clear();
		friendList.add("+");
		ParseRelation<ParseUser> relation = currentUser.getRelation("Friend");
		ParseQuery<ParseUser> query = relation.getQuery();
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> objects, ParseException e) {
				if(objects.size() > 0){
					for(ParseUser obj : objects){
						friendList.add(obj.getUsername());
					}
					
				}
				arrayAdapter.notifyDataSetChanged();
			}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed(){
		finish();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		mProgressDialog.dismiss();
	}
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(FriendActivity.this);
			// Set progressdialog title
			mProgressDialog.setTitle("FUCK!");
			// Set progressdialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progressdialog
			mProgressDialog.show();
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			friendList = new ArrayList<String>();
			if(ParseUser.getCurrentUser().getUsername() == null){
				friendList.add("+");
				
			}else {
				
				friendList.add("+");
				ParseRelation relation = ParseUser.getCurrentUser().getRelation("Friend");
				ParseQuery<ParseUser> query = relation.getQuery();
				query.findInBackground(new FindCallback<ParseUser>() {
					@Override
					public void done(List<ParseUser> objects, ParseException e) {
						if(objects.size() > 0){
							for(ParseUser obj : objects){
								friendList.add(obj.getUsername());
							}
							
						}
						arrayAdapter.notifyDataSetChanged();
					}
				});
			
				
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
//			listView = (ListView) findViewById(R.id.listViewFriend);
//			arrayAdapter = new ArrayAdapter<String>(getBaseContext(),
//					R.layout.mylistview, R.id.textItem, friendList);
//			listView.setAdapter(arrayAdapter);
//			mProgressDialog.dismiss();
//			listview on itme click			
		}
	}
}
