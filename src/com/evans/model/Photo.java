package com.evans.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Photo")
public class Photo extends ParseObject{
	public ParseFile getImage(){
		return getParseFile("image");
	}
	public void setImage(ParseFile file){
		put("image", file);
	}
	
	public ParseFile getThumbnail(){
		return getParseFile("thumbnail");
	}
	public void setThumbnail(ParseFile file){
		put("thumbnail", file);
	}
	
	public ParseUser getUser(){
		return getParseUser("user");
	}
	public void setUser(ParseUser user){
		put("user", user);
	}
}
