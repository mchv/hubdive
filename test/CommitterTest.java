import java.util.Collection;

import org.junit.Test;


import models.Committer;
import models.GitHubDive;
import models.HubDive;
import models.Project;
import play.test.UnitTest;
import util.connection.ConnectionException;


public class CommitterTest extends UnitTest {

	private HubDive dive = HubDiveFactory.instance;
	
	@Test
	public void retrieveManyCommitters() throws ConnectionException {
        Project project = new Project("mock", "mock", "http://github.com/schacon/simplegit", 0, 0);
        Collection<String> committers = dive.getPublicCommitters(project);
        assertNotNull(committers);
        assertFalse(committers.isEmpty());
        assertTrue(committers.size() > 1);
	}

	
	@Test
	public void retrieveCommittersFromOrganizationRepo() throws ConnectionException {
        Project project = new Project("mock", "mock", "https://github.com/ObeoNetwork/Xtext-viewpoint-integration", 0 , 0);
        Collection<String> committers = dive.getPublicCommitters(project);
        assertNotNull(committers);
        assertFalse(committers.isEmpty());
        /* there are public 13 members in ObeoNetwork, this may change*/
        assertEquals(13, committers.size());
	}
	
}
