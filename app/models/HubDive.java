package models;

import java.util.Collection;

import util.connection.ConnectionException;


public interface HubDive {

	 /**
	  * Search the project hosted by the hub from the given query
	  * @param query the query in natural language
	  * @return a collection of projects which matches the query. If there is none the collection will be empty.
	  * @throws ConnectionException if a connection problem occurs.
	  */
	 Collection<Project> searchProjects(String query) throws ConnectionException;
	
	 /**
	  * Get the project hosted by the hub from the given owner and with the given project name.
	  * @param owner project owner
	  * @param name project name
	  * @return the project if it could be retrieved, <code>null</code> otherwise
	  */
	 Project getProject(String owner, String name) throws ConnectionException;
	 
	 /**
	  * Get the list of public registered and declared committers for the given project.
	  * @param project the project
	  * @return a collection of committer logins. If there is none the collection will be empty
	  * @throws ConnectionException if a connection problem occurs.
	  */
	 Collection<String> getPublicCommitters(Project project) throws ConnectionException;

	 
	 /**
	  * Get the last 100 commits for the given project
	  * @param project the project
	  * @return a collection of commits. If there is none the collection will be empty
	  * @throws ConnectionException if a connection problem occurs
	  */
	 Collection<Commit> getLast100Commits(Project project) throws ConnectionException;	 
	 
	 /**
	  * Get the impact of a committer for the given project on the given commits. The impact is computed as the ratio of the committer commits on the total project commits.
	  * @param project
	  * @param committerLogin
	  * @param commits the commits to analyse
	  * @return a float between 0 and 100 
	  * @throws ConnectionException
	  */
	 float getImpact(Project project, String committerLogin, Collection<Commit> commits) throws ConnectionException;
	 
	 /**
	  * Get statistics about given commits.
	  * @return statistics
	  */
	 CommitStats getStats(Collection<Commit>commit);
	 
}
