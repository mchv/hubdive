import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

import org.junit.Test;


import models.Commit;
import models.Committer;
import models.GitHubDive;
import models.HubDive;
import models.Project;
import play.test.UnitTest;
import util.connection.ConnectionException;


public class CommitTest extends UnitTest {

	private HubDive dive = HubDiveFactory.instance;
	
	@Test
	public void retrieveAllCommits() throws ConnectionException {
        Project project = new Project("mock", "mock", "http://github.com/schacon/simplegit", 0, 0);
        Collection<Commit> commits = dive.getLast100Commits(project);
        assertNotNull(commits);
        assertFalse(commits.isEmpty());
        /*34 seems to be the number of commit for the project, with all forks*/ 
        assertEquals(39, commits.size()); 
	}

	@Test
	public void retrieveAllCommitsFromAnOrganizationRepo() throws ConnectionException {
        Project project = new Project("mock", "mock", "https://github.com/ObeoNetwork/Xtext-viewpoint-integration", 0, 0);
        Collection<Commit> commits = dive.getLast100Commits(project);
        assertNotNull(commits);
        assertFalse(commits.isEmpty());
        /* 15 is the total number of commits from mchv */
        assertEquals(15, commits.size());
	}
	
	@Test
	public void RetrieveCommitsWithNotSupportedUsername() throws ConnectionException {
        /* 'sebasdevelopment@gmx.com' is one of the author, an address could ne be searched*/
		Project project = new Project("mock", "mock", "http://github.com/Pumpuli/multitheftauto", 0, 0);
        dive.getLast100Commits(project);
        /*if a connection exception was not raised the test succeed */
	}
	
	@Test
	public void commitsFilledCorrectly() throws ConnectionException {
        Project project = new Project("fake", "fake", "http://github.com/schacon/simplegit", 0, 0);
        Collection<Commit> commits = dive.getLast100Commits(project);
        
        Commit[] commitArray = (Commit[]) commits.toArray(new Commit[commits.size()]);
        assertEquals("a11bef06a3f659402fe7563abf99ad00de2209e6", commitArray[0].getId());
        assertEquals("schacon", commitArray[0].getCommitter().getLogin());
        assertEquals("first commit", commitArray[0].getMessage());
        assertEquals("2008-03-15 10:31:28", commitArray[0].getDate());
        
	}	
	
	@Test
	public void impactOfCommitsWithSeveralCommiters() throws ConnectionException {
        Project project = new Project("mock", "mock", "http://github.com/schacon/simplegit", 0, 0);
        Committer committer = new Committer("schacon", "mock", "mock");
        Collection<Commit> commits = dive.getLast100Commits(project);
        float impact = dive.getImpact(project, committer.getLogin(), commits);
        /* 64 is the percentage of commits of shacon*/
        assertEquals(64,00, impact);
	}
	
	@Test
	public void impactOfCommitsOfNotCommiter() throws ConnectionException {
        Project project = new Project("mock", "mock", "http://github.com/schacon/simplegit", 0, 0);
        Committer committer = new Committer("mchv", "mock", "mock");
        Collection<Commit> commits = dive.getLast100Commits(project);
        float impact = dive.getImpact(project, committer.getLogin(), commits);
        assertEquals(0,00, impact);
	}
	
	@Test
	public void impactOfCommitsWithOneCommitter() throws ConnectionException {
        Project project = new Project("mock", "mock", "https://github.com/ObeoNetwork/Xtext-viewpoint-integration", 0, 0);
        Committer committer = new Committer("mchv", "mock", "mock");
        Collection<Commit> commits = dive.getLast100Commits(project);
        float impact = dive.getImpact(project, committer.getLogin(), commits);
        assertEquals(100,00, impact);
	}

	@Test
	public void impactOfCommitsWithoutCommitLogin() throws ConnectionException {
        Project project = new Project("mock", "mock", "http://github.com/Obeo/fr.obeo.performance", 0, 0);
        Committer committer = new Committer("pcdavid", "mock", "mock");
        Collection<Commit> commits = dive.getLast100Commits(project);
        float impact = dive.getImpact(project, committer.getLogin(), commits);
        /* pcdavid made some commits */
        assertTrue(impact > 0);
	}

	@Test
	public void impactOfCommitsFromNotPublicCommitters() throws ConnectionException {
        Project project = new Project("mock", "mock", "http://github.com/Obeo/fr.obeo.performance", 0, 0);
        Committer committer = new Committer("cbrun", "mock", "mock");
        Collection<Commit> commits = dive.getLast100Commits(project);
        float impact = dive.getImpact(project, committer.getLogin(), commits);
        /* cbrun made some commits */
        assertTrue(impact > 0);
	}
	
	@Test
	public void impactOfCommitsFromUnregisteredCommitters() throws ConnectionException {
        Project project = new Project("mock", "mock", "http://github.com/Obeo/fr.obeo.performance", 0, 0);
        Collection<Commit> commits = dive.getLast100Commits(project);
        float impact = dive.getImpact(project, "Maxime Porhel", commits);
        /* maxime made some commits, but the impacts is computed only for registered users */
        assertEquals(0,00, impact);
	}
	
	
	
	
}


