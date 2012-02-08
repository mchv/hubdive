package models;

import java.util.Collection;

public class Project {

	private final String name;
	
	private final String owner;
	
	private final String url;
	
	private final int forks;
	
	private final int watchers;
	
	public Project(String name, String owner, String url, int forks, int watchers) {
		this.name =name;
		this.owner = owner;
		this.url = url;
		this.forks = forks;
		this.watchers = watchers;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getOwner() {
		return this.owner;
	}
	
	public String getURL() {
		return this.url;
	}
	
	public int getForks() {
		return this.forks;
	}
	
	public int getWatchers() {
		return this.watchers;
	}
	
}
