package com.evans.model;

import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseUser{
/*displayName : String
email : String
profilePictureMedium : File
profilePictureSmall : File
facebookId : String
facebookFriends : Array
channel : String*/
	
	public String getDisplayName(){
		return getString("displayName");
	}
	public String getEmail(){
		return getString("email");
	}
	public String getFacebookId(){
		return getString("facebookId");
	}
	public String getChannel(){
		return getString("channel");
	}
	public List<String> getFacebookFriends(){
		return getList("facebookFriends");
	}
	public ParseFile getProfilePictureSmall(){
		return getParseFile("profilePictureSmall");
	}
	public ParseFile getProfilePictureMedium(){
		return getParseFile("profilePictureMedium");
	}
	
	public void setDisplayName(String displayName){
		put("displayName", displayName);
	}
	public void setEmail(String email){
		put("email", email);
	}
	public void setFacebookId(String facebookId){
		put("facebookId", facebookId);
	}
	public void setChannel(String channel){
		put("channel", channel);
	}
	public void setFacebookFriends(List<String> facebookFriends){
		put("facebookFriends", facebookFriends);
	}
	public void setProfilePictureSmall(ParseFile picture){
		put("profilePictureSmall", picture);
	}
	public void setProfilePictureMedium(ParseFile picture){
		put("profilePictureMedium", picture);
	}
}
