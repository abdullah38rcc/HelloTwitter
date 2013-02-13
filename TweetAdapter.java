package com.example.hellotreemo;


import java.util.ArrayList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetAdapter extends ArrayAdapter<Tweet> {

	private ArrayList<Tweet> tweets;
	private AsyncImageLoader asyncImageLoader;
	
	public TweetAdapter(Context context, int textViewResourceId,
			ArrayList<Tweet> tweets) {
		super(context, textViewResourceId, tweets);
		this.tweets = tweets;
		asyncImageLoader = new AsyncImageLoader();
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
	View v = convertView;
	
	//inflate view from XML
	if (v == null) {
		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.tweet_item, null);
	}
	
	Tweet tweet = tweets.get(position);
	
	if (tweet != null){
		TextView username = (TextView) v.findViewById(R.id.username);
		TextView message = (TextView) v.findViewById(R.id.message);
		final ImageView imageView = (ImageView) v.findViewById(R.id.avatar);
		
		if (username != null){
			username.setText("@"+tweet.username);
		}
		if (message != null){
			message.setText(tweet.message);
		}
		if (imageView != null) {
			Drawable cachedImage = asyncImageLoader.loadDrawable(tweet.image_url, new ImageCallback() {
				    public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				        imageView.setImageDrawable(imageDrawable);
				    }
			});
			imageView.setImageDrawable(cachedImage);
		}
	}
	
	/*set text and images on view
	TextView username = (TextView) v.findViewById(R.id.username);
	username.setText("@"+tweet.username);
	
	TextView message = (TextView) v.findViewById(R.id.message);
	message.setText(tweet.message);
	
	final ImageView imageView = (ImageView) v.findViewById(R.id.avatar);
	Drawable cachedImage = asyncImageLoader.loadDrawable(tweet.image_url, new ImageCallback() {
		    public void imageLoaded(Drawable imageDrawable, String imageUrl) {
		        imageView.setImageDrawable(imageDrawable);
		    }
	});
	imageView.setImageDrawable(cachedImage);*/
	
	return v;
	
	}
	
}
