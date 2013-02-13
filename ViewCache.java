package com.example.hellotreemo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewCache {
	 
	    private View baseView;
	    private TextView textView;
	    private ImageView imageView;
	 
	    public ViewCache(View baseView) {
	        this.baseView = baseView;
	    }
	 
	    public TextView getMessageTextView() {
	        if (textView == null) {
	            textView = (TextView) baseView.findViewById(R.id.message);
	        }
	        return textView;
	    }
	    
	    public TextView getUsernameTextView() {
		        if (textView == null) {
		            textView = (TextView) baseView.findViewById(R.id.username);
		        }
		        return textView;
		    }
	 
	    public ImageView getImageView() {
	        if (imageView == null) {
	            imageView = (ImageView) baseView.findViewById(R.id.avatar);
	        }
	        return imageView;
	    }
	}
