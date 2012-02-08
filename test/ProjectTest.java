import java.util.Collection;

import org.junit.Test;

import models.HubDive;
import models.Project;
import play.test.UnitTest;
import util.connection.ConnectionException;


public class ProjectTest extends UnitTest {

	private HubDive dive = HubDiveFactory.instance;
	
	@Test
	public void getProject() throws ConnectionException {
		Project project = dive.getProject("torvalds", "linux");
		assertNotNull(project);		
		assertTrue(project.getForks() > 0);
		assertTrue(project.getWatchers() > 0);	
	}
	
    @Test
    public void searchExistingProject() throws ConnectionException {
    		Collection<Project> results = dive.searchProjects("play");
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    public void searchNonExistingProject() throws ConnectionException {
        Collection<Project> results = dive.searchProjects("zzzzaaaawwwwwttrzejihkjssjnjk");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
    
    
    @Test
    public void searchExistingProjectWith2Words() throws ConnectionException {
        Collection<Project> results = dive.searchProjects("Xtext viewpoint");
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }
	
}


