package com.example.hellotreemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class UserDetails extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_details);
		
		//int prePosition = getIntent().getIntExtra("position", 0);
		//new DownloadUserInfoTask(prePosition).execute();
		
		User usertweet = new User(getIntent().getStringExtra("name"), getIntent().getStringExtra("username"), getIntent().getStringExtra("location"), getIntent().getStringExtra("bio"),getIntent().getStringExtra("picture"));

		ImageView image_url = (ImageView) findViewById(R.id.avatar);
		image_url.setImageDrawable(loadImageFromUrl(usertweet.profile_image_url));
		
		TextView name = (TextView) findViewById(R.id.name);
		name.setText(usertweet.name);
		
		TextView username = (TextView) findViewById(R.id.username);
		username.setText("@"+usertweet.screen_name);
		
		TextView location = (TextView) findViewById(R.id.location);
		location.setText(usertweet.location);
		
		TextView description = (TextView) findViewById(R.id.description);
		description.setText(usertweet.description);
	}
	
/*	public class DownloadUserInfoTask extends AsyncTask<Void, Void, User> {

		public int prePosition;
		public ProgressDialog pd;
		
		public DownloadUserInfoTask (int prePosition){
			this.prePosition = prePosition;	
		}
		
		protected void onPreExecute() {
			pd = new ProgressDialog(UserDetails.this);
			pd.setMessage("  Loading User Details...");
			pd.show();
			//Toast.makeText(UserDetails.this, "Loading User Details...", Toast.LENGTH_LONG).show();
		}
		
		protected User doInBackground(Void... params) {
			
			ArrayList<Tweet> tweets = fetchTwitterTimeline("@_TELEKINESIS");
			Tweet tweet = tweets.get(prePosition);
			ArrayList<User> userinfo = fetchUserInfo(tweet.username);
			User usertweet = userinfo.get(prePosition);
			return usertweet;
		}
		
		protected void onPostExecute(User result) {
			pd.dismiss();
			
			ImageView image_url = (ImageView) findViewById(R.id.avatar);
			image_url.setImageDrawable(loadImageFromUrl(result.profile_image_url));
			
			TextView name = (TextView) findViewById(R.id.name);
			name.setText(result.name);
			
			TextView username = (TextView) findViewById(R.id.username);
			username.setText("@"+result.screen_name);
			
			TextView location = (TextView) findViewById(R.id.location);
			location.setText(result.location);
			
			TextView description = (TextView) findViewById(R.id.description);
			description.setText(result.description);
		}
	}
	
	public ArrayList<Tweet> fetchTwitterTimeline(String searchItem) {
		 
		ArrayList<Tweet> listItems = new ArrayList<Tweet>();
		
		try {
			// Create a new HTTP Client
			DefaultHttpClient defaultClient = new DefaultHttpClient();
			// Setup the get request
			HttpGet httpGetRequest = new HttpGet("http://search.twitter.com/search.json?q="+searchItem+"&rpp=10&page=1");
			// Execute the request in the client
			HttpResponse httpResponse = defaultClient.execute(httpGetRequest);
			// Grab the response
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
			String json = reader.readLine();
			JSONObject obj = new JSONObject(json);
			JSONArray jArray = obj.getJSONArray("results");
			//feed into ArrayList
			for (int i = 0; i < jArray.length(); i++) {
			      JSONObject oneObject = jArray.getJSONObject(i);
			      Tweet mytweet = new Tweet(
			      	oneObject.getString("text"),
			      	oneObject.getString("from_user"), 
			      	oneObject.getString("profile_image_url")
			      );
			      listItems.add(mytweet);	      
		      }
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return listItems;
	}
	
	public ArrayList<User> fetchUserInfo(String username) {
		 
		ArrayList<User> listItems = new ArrayList<User>();
		
		try {
			// Create a new HTTP Client
			DefaultHttpClient defaultClient = new DefaultHttpClient();
			// Setup the get request
			HttpGet httpGetRequest = new HttpGet("https://api.twitter.com/1/statuses/user_timeline.json?screen_name="+username+"&count=10");
			// Execute the request in the client
			HttpResponse httpResponse = defaultClient.execute(httpGetRequest);
			// Grab the response
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
			String json = reader.readLine();
			// Instantiate a JSON Array from the request response
			JSONArray jArray = new JSONArray(json);
			//feed into ArrayList
			for (int i = 0; i < jArray.length(); i++) {
			      JSONObject oneObject = jArray.getJSONObject(i);
			      JSONObject subObject = oneObject.getJSONObject("user");
			      User myuser = new User(
			      	subObject.getString("name"),
			      	subObject.getString("screen_name"),
			      	subObject.getString("location"),
			      	subObject.getString("description"),
			      	subObject.getString("profile_image_url")
			      );
			      listItems.add(myuser);	      
		      }
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return listItems;
	} */
	
	public static Drawable loadImageFromUrl(String url) {
		InputStream inputStream;
		try {
			inputStream = new URL(url).openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return Drawable.createFromStream(inputStream, "src");
	}
}
