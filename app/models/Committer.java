package models;

import java.util.Collection;

import org.apache.commons.collections.FastHashMap;

public class Committer {

	private final String login;
	
	private final String username;
	
	private final String gravatarId;
	
	private boolean registered;
	
	public Committer(String login, String username, String gravatarId) {
		this.registered = true;
		this.login = login;
		this.username = username;
		this.gravatarId = gravatarId;
	}
	
	/**
	 * Create a committer <b>not</b> registered to the hub.
	 * @param username the username
	 */
	public Committer(String username) {
		this.registered = false;
		this.username = username;
		this.login = null;
		this.gravatarId = null;
	}
	
	/**
	 * Check if a committer is registered to the hub.
	 * @return <code>true</code> if the committer is registered, <code>false</code> otherwise
	 */
	public boolean isRegistered() {
		return registered;
	}
	
	public String getLogin() {
		return this.login;
	}

	public String getUsername() {
		return this.username;
	}
	
	public String getGravatarId() {
		return this.gravatarId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Committer other = (Committer) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}

	@Override
	public String toString() {
		/* useful for debug */
		return "login: " + login + " / username: " + username; 
	}
	
}
