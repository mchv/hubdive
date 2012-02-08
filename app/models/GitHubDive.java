package models;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;



import play.Logger;
import util.connection.ConnectionException;
import util.connection.HttpClient;
import util.json.JSONArray;
import util.json.JSONException;
import util.json.JSONObject;


public class GitHubDive implements HubDive {

	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private HttpClient client = new HttpClient();
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<Project> searchProjects(String query) throws ConnectionException {

		String response = client.get("http://github.com/api/v2/json/repos/search/" + query);

		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray repositories= jsonObject.getJSONArray("repositories");			
			Collection<Project> projects = new ArrayList<Project>();
			for (int i=0 ; i < repositories.length() ; i++) {
				JSONObject repo = repositories.getJSONObject(i);
				
				String name = repo.getString("name");
				String owner = repo.getString("owner");
				String url = repo.getString("url");
				int forks = repo.getInt("forks");
				int watchers = repo.getInt("watchers");
				Project project = new Project(name, owner, url, forks, watchers);
				projects.add(project);
			}
			return projects;
		} catch (JSONException e) {
			throw new ConnectionException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public Project getProject(String owner, String name)  throws ConnectionException {
		
		String response = client.get("http://github.com/api/v2/json/repos/show/" + owner +"/" + name);

		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject repository = jsonObject.optJSONObject("repository");
			if (repository != null)  {
				String url = repository.getString("url");
				int forks = repository.getInt("forks");
				int watchers = repository.getInt("watchers");
				Project project = new Project(name, owner, url, forks, watchers);
				return project;
			}
			return null;
		} catch (JSONException e) {
			throw new ConnectionException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<String> getPublicCommitters(Project project) throws ConnectionException {

		String response = client.get(project.getURL() + "/network_meta");
		
		if (response.isEmpty()) {
			throw new ConnectionException("GitHub returns empty data");
		}
		
		try {	
			JSONObject jsonObject = new JSONObject(response);
			JSONArray users = jsonObject.getJSONArray("users");

			Collection<String> committerLogins = new ArrayList<String>();

			
			for (int i=0 ; i < users.length() ; i++) {
				JSONObject user = users.getJSONObject(i);
				final String login = user.getString("name");

				if (checkForOrganization(users.length())) {
					if (isAnOrganization(login)) {
						committerLogins.addAll(getCommitterLoginsForOrganization(login));
					} else {
						committerLogins.add(login);
					}
				} else {
					committerLogins.add(login);
				}
			}

			return committerLogins;

		} catch (JSONException e) {
			throw new ConnectionException(e);
		}
	}

	private boolean checkForOrganization(int numberOfUsers) {
			return numberOfUsers <= 2;
	}

	private boolean isAnOrganization(String login) {
		try {
			String response = client.get("https://github.com/api/v2/json/organizations/" + login);
			JSONObject jsonObject = new JSONObject(response);
			JSONObject org = jsonObject.optJSONObject("organization");
			return org!=null;
		} catch (ConnectionException e) {
			return false;
		} catch (JSONException e) {
			return false;
		}
	}

	private Collection<String> getCommitterLoginsForOrganization(String organization) throws ConnectionException {

		String response = client.get("http://github.com/api/v2/json/organizations/" + organization + "/public_members");

		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray users = jsonObject.getJSONArray("users");

			Collection<String> committerLogins = new ArrayList<String>();

			for (int i=0 ; i < users.length() ; i++) {
				JSONObject user = users.getJSONObject(i);
				final String login = user.getString("login");
				committerLogins.add(login);
			}

			return committerLogins;
		} catch (JSONException e) {
			throw new ConnectionException(e);
		}
	}

	public Collection<Commit> getLast100Commits(Project project) throws ConnectionException {
		String response = client.get(project.getURL() + "/network_meta");

		try {
			JSONObject jsonObject = new JSONObject(response);
			String nethash = jsonObject.getString("nethash");

			String commitsResponse = client.get(project.getURL() + "/network_data_chunk?nethash="+nethash);
			jsonObject =  new JSONObject(commitsResponse);
			JSONArray jsonCommits = jsonObject.getJSONArray("commits");

			Collection<Commit> commits = new ArrayList<Commit>();
			
			int len = Math.min(jsonCommits.length(), 100);
			
			for (int i=0 ; i < len ; i++) {
				
				JSONObject jsonCommit = jsonCommits.getJSONObject(i);

				String commitId = jsonCommit.getString("id");
				String committerLogin = jsonCommit.getString("login");
				String author = jsonCommit.getString("author");
				String message =jsonCommit.getString("message");
				String date = jsonCommit.getString("date");
				String gravatar = jsonCommit.optString("gravatar");

				Committer committer = null;
				if (!committerLogin.isEmpty()) {
					committer = new Committer(committerLogin, author, gravatar);
				} else {
					committer = searchRegisteredCommitter(author);
					if (committer == null) {
						committer = createUnregisteredCommitter(committerLogin);
					}		
				}

				if (committer != null) {
					Commit commit = new Commit(commitId, committer, message, date);
					commits.add(commit);
				} else {		
					Logger.info("something goes wrong pan ! -> " + date + "  " + commitId + "  " + committerLogin + "  "  + message);
					Logger.info(jsonCommit.toString());
				}
			}
			return commits;
		} catch (JSONException e) {
			throw new ConnectionException(e);
		}
	}

	/**
	 * Search for a registered committer from its hub user name
	 * @param username the user name
	 * @return <code>null</code> if the creation failed if the login is not valid.
	 */
	private Committer searchRegisteredCommitter(String username) throws ConnectionException {

		String response =  client.get("http://github.com/api/v2/json/user/search/" + encode(username));

		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray jsonUsers = jsonObject.getJSONArray("users");

			if (jsonUsers.length() > 0) {
				
				JSONObject object = jsonUsers.getJSONObject(0);
				final String login = object.getString("login");
				final String gravatarId = object.optString("gravatar_id");
				return new Committer(login, username, gravatarId);
			}
			return null;
		} catch (JSONException e) {
			throw new ConnectionException(e);
		}
	}

	/**
	 * The github API is sensitive to some chars in severals requests.
	 * This method replace sensitive chars to avoid denied access.
	 */
	private String encode(String toEncode) {
		return toEncode.replace(' ', '+').replace('.', '+').replace('@', '+');
	}
	
	private Committer createUnregisteredCommitter(String username) {
		return new Committer(username);
	}

	/**
	 * {@inheritDoc}
	 */
	public float getImpact(Project project, String committerLogin, Collection<Commit> commits) throws ConnectionException {
		return (((float) getNumberOfCommits(commits, committerLogin)/(float) commits.size())*((float)100));
	}

	private int getNumberOfCommits(Collection<Commit> commits, String committerLogin) {
		int sum = 0;
		for (final Commit commit : commits) {
			final Committer committer = commit.getCommitter();
			if (committer.isRegistered()) {
				if (committer.getLogin().equals(committerLogin))
					sum++;
			}
		}
		return sum;
	}

	@Override
	public CommitStats getStats(Collection<Commit> commits) {
		return new CommitStats(commits, GitHubDive.DATE_PATTERN);
	}

}
