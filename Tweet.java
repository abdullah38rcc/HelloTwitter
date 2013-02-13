package com.example.hellotreemo;

public class Tweet {
	
	public String username;
	public String message;
	public String image_url;
	    
	public Tweet(String message, String username, String image_url) {    
		this.username = username;
		this.message = message;
		this.image_url = image_url;
	}
	
	@Override public String toString() {
		     return getClass().getName() + "[" +
		         "username: " + username + ", " +
		         "message: " + message + ", " +
		         "image url: " + image_url + "]";
		   }
	
}
