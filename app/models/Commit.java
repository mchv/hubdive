package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import play.Logger;

public class Commit {

	private final String id;
	
	private final Committer committer;
	
	private final String message;
	
	private final String date;
	
	public Commit(String id, Committer committer, String message, String date) {
		this.id = id;
		this.committer = committer;
		this.message = message;
		this.date = date;
	}
	
	public String getId() {
		return this.id;
	}
	
	public Committer getCommitter() {
		return this.committer;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public String getDate() {
		return this.date;
	}
		
}
