package com.example.hellotreemo;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HelloTreemo extends ListActivity {
/** Called when the activity is first created. */
	
	public ListView lv;
	public ImageView image;
	public Button rbutton;
	public Button sbutton;
	public EditText textResult;
	public String searchTerm;
	public CharSequence searchEntry;
	public ArrayList<Tweet> myTweets;
	public ArrayList<Tweet> update;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
      
		super.onCreate(savedInstanceState);
	      setContentView(R.layout.main);
	      
	      searchTerm = "AndroidDev";
	      
	      myTweets = fetchTwitterTimeline(searchTerm);
	      
	      lv = (ListView) findViewById(R.id.twitterList);
	      lv.setAdapter(new TweetAdapter(this, R.layout.tweet_item, myTweets));
	      lv.setOnItemClickListener(new OnItemClickListener() {
	      	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	      		
	      		new DownloadUserInfoTask(position, myTweets).execute();
	      		
	      	}
	      	
  	    	});
	      
	      rbutton = (Button) findViewById(R.id.buttonRefresh);
	      rbutton.setOnClickListener(new View.OnClickListener() {
	      	@Override
	      	public void onClick(View v) {
	      		refreshFeed(searchTerm);
	      	}
	      });
	      
	      
	      sbutton = (Button) findViewById(R.id.buttonSearch);
	      sbutton.setOnClickListener(new View.OnClickListener() {
	      	@Override
	      	public void onClick(View v) {
	      		String searchTest = textResult.getText().toString().replace(" ", ",");
	      		if (searchTest.equals("")) {
	      			Toast empty = Toast.makeText(HelloTreemo.this, "Whoops! You didn't search for anything! Please retry!", Toast.LENGTH_SHORT);
	      			empty.show();
	      		} else {
	      			searchTerm = searchTest;
	      			refreshFeed(searchTerm);
	      		}
	      		if (searchTest.startsWith("@")) {
		      		searchTerm = searchTest.substring(1);
		      		refreshFeed("from:"+searchTerm);
		      	}
	      		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
      			imm.hideSoftInputFromWindow(textResult.getWindowToken(), 0);
      			textResult.setText("");
	      	}
	      });
	      
	      
	      TextView pretitle = (TextView) findViewById(R.id.textPre);
	      pretitle.setText("twitter search for:");
	      
	      TextView title = (TextView) findViewById(R.id.textTitle);
	      title.setText("\""+searchTerm+"\"");
	      
	      
	      textResult = (EditText) findViewById(R.id.text_result);
	      textResult.requestFocus();
	}
	
	public class DownloadUserInfoTask extends AsyncTask<Void, Void, User> {

		public int position;
		public ProgressDialog pd;
		public ArrayList<Tweet> newList;
		private static final int USER_INFO = 1010;
		
		public DownloadUserInfoTask (int position, ArrayList<Tweet> newList){
			this.position = position;
			this.newList = newList;
		}
		
		protected void onPreExecute() {
			pd = new ProgressDialog(HelloTreemo.this);
			pd.setMessage("  Loading User Details...");
			pd.show();
		}
		
		protected User doInBackground(Void... params) {
			Tweet tweet = newList.get(position);
			User usertweet = fetchUserInfoOnce(tweet.username);
			return usertweet;
		}
		
		protected void onPostExecute(User result) {
			pd.dismiss();
			Intent intent = new Intent(HelloTreemo.this, UserDetails.class);
      		intent.putExtra("name", result.name);
      		intent.putExtra("username", result.screen_name);
      		intent.putExtra("location", result.location);
      		intent.putExtra("bio", result.description);
      		intent.putExtra("picture", result.profile_image_url);
      		startActivityForResult(intent, USER_INFO);
		}
	}
	
	public void refreshFeed(String searchTerm){
		
		update = fetchTwitterTimeline(searchTerm);
	      
	      lv = (ListView) findViewById(R.id.twitterList);
	      lv.setAdapter(new TweetAdapter(this, R.layout.tweet_item, update));
	      lv.setOnItemClickListener(new OnItemClickListener() {
	      	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	      		new DownloadUserInfoTask(position, update).execute();
	      	}
  	    	});
	      
	      TextView title = (TextView) findViewById(R.id.textTitle);
	      title.setText("\""+searchTerm.replace(",", " ")+"\"");
		
	}
	
	/*public ArrayList<Tweet> fetchTwitterTimeline(String searchItem) {
		
		ArrayList<Tweet> listItems = new ArrayList<Tweet>();
		try {
			URL url = new URL("http://search.twitter.com/search.json?q="+searchItem+"&rpp=10&include_entities=true&result_type=recent");
			HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
			int response = httpconn.getResponseCode();
			if(response == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
				String json = reader.readLine();
				// Instantiate a JSON Array from the request response
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
				      System.out.println("LOOK HEEERRRREEE!!!!!!");
				      System.out.println(mytweet.toString());
			      }
				
			} else {
				Log.e("SAMES", "INCORRECT RETURN CODE: "+response);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return listItems;
	}*/
	
	public ArrayList<Tweet> fetchTwitterTimeline(String searchItem) {
		 
		ArrayList<Tweet> listItems = new ArrayList<Tweet>();
		
		try {
			//Increase default timeout length
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 50000);
			HttpConnectionParams.setSoTimeout(httpParameters, 20000);
			// Create a new HTTP Client
			DefaultHttpClient defaultClient = new DefaultHttpClient(httpParameters);
			// Setup the get request
			HttpGet httpGetRequest = new HttpGet("http://search.twitter.com/search.json?q="+searchItem+"&rpp=10&include_entities=true&result_type=recent");
			// Execute the request in the client
			HttpResponse httpResponse = defaultClient.execute(httpGetRequest);
			// Grab the response
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
			String json = reader.readLine();
			// Instantiate a JSON Array from the request response
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
			      System.out.println("LOOK HEEERRRREEE!!!!!!");
			      System.out.println(mytweet.toString());
		      }
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return listItems;
	}
	
	public User fetchUserInfoOnce(String username) {
		 
		User myuser = null;
		
		try {
			// Create a new HTTP Client
			DefaultHttpClient defaultClient = new DefaultHttpClient();
			// Setup the get request
			HttpGet httpGetRequest = new HttpGet("https://api.twitter.com/1/users/show.json?screen_name="+username+"&include_entities=true");
			// Execute the request in the client
			HttpResponse httpResponse = defaultClient.execute(httpGetRequest);
			// Grab the response
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
			String json = reader.readLine();
			// Instantiate a JSON Object from the request response
			JSONObject oneObject = new JSONObject(json);
			// Retrieve USER data and put into USER object
			myuser = new User(
			      	oneObject.getString("name"),
			      	oneObject.getString("screen_name"),
			      	oneObject.getString("location"),
			      	oneObject.getString("description"),
			      	oneObject.getString("profile_image_url")
			      );
			// LOGCAT TEST
			System.out.println("LOOK HEEERRRREEE!!!!!!");
		      System.out.println(myuser.toString());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return myuser;
	}
	
}