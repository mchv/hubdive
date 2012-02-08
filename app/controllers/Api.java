package controllers;

import java.util.Collection;

import models.Commit;
import models.CommitStats;
import models.GitHubDive;
import models.HubDive;
import models.Project;
import play.Logger;
import play.mvc.Controller;
import util.connection.ConnectionException;

public class Api extends Controller {

	private static HubDive dive = new GitHubDive();
	
	public static void search(String query) {
		try {
			final Collection<Project> projects =dive.searchProjects(query);
			renderJSON(projects);	
		} catch (ConnectionException e) {
			error(e);
		}
	}
	
	public static void committers(String owner, String repository) {
		try {
			final Project project = dive.getProject(owner, repository);
			final Collection<String> committerLogins = dive.getPublicCommitters(project);
			renderJSON(committerLogins);	
		} catch (ConnectionException e) {
			error(e);
		}
	}
	
	public static void impact(String owner, String repository, String committer) {
		try {

			final Project project = dive.getProject(owner, repository);
			final Collection<Commit> commits = dive.getLast100Commits(project);
			float impact = dive.getImpact(project, committer, commits);
			renderJSON(impact);
		} catch (ConnectionException e) {
			error(e);
		}
	}
	
	
	public static void stats(String owner, String repository) {
		try {
			final Project project = dive.getProject(owner, repository);
			final Collection<Commit> commits = dive.getLast100Commits(project);
			CommitStats stats = dive.getStats(commits);
			renderJSON(stats);
		} catch (ConnectionException e) {
			error(e);
		}
	}
	
	
	
}
