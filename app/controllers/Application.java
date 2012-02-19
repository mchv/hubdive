package controllers;

import play.*;
import play.mvc.*;
import util.Maps;
import util.connection.ConnectionException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.Map.Entry;

import models.*;

public class Application extends Controller {

	
	private static HubDive dive = new GitHubDive();
	
    public static void index() {
        render();
    }
    
    /*
     * Search projects with a  query
     */
    public static void search(String query) {
    		try {
				Collection<Project> projects =dive.searchProjects(query);
				render(query, projects);	
		} catch (ConnectionException e) {
			connectionError(e);
		}
    }

    /*
     * Show analytics 
     */
    public static void show(String owner, String name) {
    		
    		try {
    			
        			Project project = dive.getProject(owner, name);
    			
    				Collection<String> committerLogins = dive.getPublicCommitters(project);
				Collection<Commit> commits = dive.getLast100Commits(project);
								
				Collection<Committer> committers = new LinkedHashSet<Committer>();
				for (final String login : committerLogins) {
					for (final Commit commit : commits) {
						final Committer committer = commit.getCommitter();
						if (login.equals(committer.getLogin())) {
							committers.add(committer);
							break;
						}
					}
					committers.add(new Committer(login, "", ""));
				}
				
				Map<Committer, Float> impactByCommitter = new HashMap<Committer, Float>();
				for (Committer committer : committers) {
					impactByCommitter.put(committer, dive.getImpact(project, committer.getLogin(), commits));
				}
				Collection<Entry<Committer, Float>> impacts = Maps.sortByDecreasingValue(impactByCommitter);
				CommitStats stats = dive.getStats(commits);
				
				/* name seems to be a reserved keyword from play!*/
				String projectOwner = owner;
				render(project, projectOwner, name, impacts, stats);
				
			} catch (ConnectionException e) {
				connectionError(e);
			}
    		
    		
    }
    
    
    
    private static void connectionError(ConnectionException e) {
	
    		String error = "Oops !";
    		String message = null;
    		
    		if(e.hasErrorCode()) {
    			final int errorCode = e.getErrorCode();
    			if (errorCode == 403) {
    				message = "We have reached API time limitation. Try again in few seconds";
    			} else {
    				message = "We got the following error code: " + errorCode;
    			}
    		} else {
    			Throwable cause = e.getCause();
    			if (cause instanceof MalformedURLException) {
    				message = "It seems we have an internal error...";
    			} else if (cause instanceof IOException) {
    				message = "It seems that the hub is not available. Check your connection";    				
    			} else {
                    if (cause != null) {
                        message = cause.getMessage();
                        cause.printStackTrace();                        
                    } else {
                        e.printStackTrace();
                    }

    			}
    		}
		renderTemplate("errors/connection.html", error, message);
    }
    
}