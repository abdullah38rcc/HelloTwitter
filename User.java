package com.example.hellotreemo;

public class User {
	
	public String name;
	public String screen_name;
	public String location;
	public String description;
	public String profile_image_url;
	
	public User (String name, String screen_name, String location, String description, String profile_image_url) {
		this.name = name;
		this.screen_name = screen_name;
		this.location = location;
		this.description = description;
		this.profile_image_url = profile_image_url;	
	}
	
	@Override public String toString() {
		     return getClass().getName() + "[" +
		         "name: " + name + ", " +
		         "screen name: " + screen_name + ", " +
		         "location: " + location + ", " +
		         "description: " + description + ", " +
		         "profile image: " + profile_image_url + "]";
		   }

}
