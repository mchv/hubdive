import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

import models.Commit;
import models.CommitStats;
import models.Committer;
import models.GitHubDive;
import models.HubDive;

import org.junit.Test;

import play.test.UnitTest;


public class CommitStatsTest extends UnitTest{

	private HubDive dive = HubDiveFactory.instance;

	@Test
	public void zeroCommits() {
		CommitStats stats = dive.getStats(new ArrayList<Commit>());
		/* test succeed if the code do not fail*/
		assertTrue(stats.getCommitsPerUnitByUser().isEmpty());
	}
	
	@Test
	public void commitPerDayOneCommitter() {
		
		Collection<Commit> commits = new LinkedHashSet<Commit>();
		Committer plop = new Committer("plop", null, null);
		commits.add(new Commit("", plop, "", "2010-05-03 10:31:28"));
		commits.add(new Commit("", plop,  "", "2010-05-04 10:31:28"));
		commits.add(new Commit("", plop, "", "2010-05-12 10:31:28"));
		commits.add(new Commit("", plop, "", "2010-05-12 10:42:27"));
		CommitStats stats = dive.getStats(commits);
		assertEquals(Calendar.DAY_OF_MONTH, stats.getUnit()); 
		assertEquals(10, stats.getLength());
		
		Map<String, Integer[]> commitsPerUnitByUser = stats.getCommitsPerUnitByUser();
		/* only one user*/
		assertEquals(1,  commitsPerUnitByUser.size());
		/*one entry per day */
		Integer[] commitsFromPlop =  commitsPerUnitByUser.get("plop");
		assertEquals(10, commitsFromPlop.length);
		
		assertEquals(1, commitsFromPlop[0].intValue());
		assertEquals(1, commitsFromPlop[1].intValue());
		assertEquals(0, commitsFromPlop[2].intValue());
		assertEquals(0, commitsFromPlop[3].intValue());
		assertEquals(0, commitsFromPlop[4].intValue());
		assertEquals(0, commitsFromPlop[5].intValue());
		assertEquals(0, commitsFromPlop[6].intValue());
		assertEquals(0, commitsFromPlop[7].intValue());
		assertEquals(0, commitsFromPlop[8].intValue());
		assertEquals(2, commitsFromPlop[9].intValue());
	}
	
	@Test
	public void commitPerYearOneCommitter() {
		
		Collection<Commit> commits = new LinkedHashSet<Commit>();
		Committer plop = new Committer("plop", null, null);
		commits.add(new Commit("", plop, "", "2010-05-15 10:31:28"));
		commits.add(new Commit("", plop,  "", "2010-06-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-07-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2012-01-01 10:31:28"));
		
		CommitStats stats = dive.getStats(commits);
		assertEquals(Calendar.YEAR, stats.getUnit()); 
		assertEquals(3, stats.getLength());
		
		Map<String, Integer[]> commitsPerUnitByUser = stats.getCommitsPerUnitByUser();
		/* only one user*/
		assertEquals(1,  commitsPerUnitByUser.size());
		/*one entry per month */
		Integer[] commitsFromPlop =  commitsPerUnitByUser.get("plop");
		assertEquals(3, commitsFromPlop.length);
		
		assertEquals(2, commitsFromPlop[0].intValue());
		assertEquals(1, commitsFromPlop[1].intValue());
		assertEquals(1, commitsFromPlop[2].intValue());
	}
	
	@Test
	public void commitPerYearOneYearDiff() {
		
		Collection<Commit> commits = new LinkedHashSet<Commit>();
		Committer plop = new Committer("plop", null, null);
		commits.add(new Commit("", plop, "", "2010-08-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-01-15 10:31:28"));
		
		CommitStats stats = dive.getStats(commits);
		assertEquals(Calendar.YEAR, stats.getUnit()); 
		assertEquals(2, stats.getLength());
		assertEquals(2010, stats.getStart());
		
		Map<String, Integer[]> commitsPerUnitByUser = stats.getCommitsPerUnitByUser();
		/* only one user*/
		assertEquals(1,  commitsPerUnitByUser.size());
		/*one entry per year */
		Integer[] commitsFromPlop =  commitsPerUnitByUser.get("plop");
		assertEquals(2, commitsFromPlop.length);
		
		assertEquals(1, commitsFromPlop[0].intValue());
		assertEquals(1, commitsFromPlop[1].intValue());
	}
	
	
	@Test
	public void commitPerMonthOneCommitter() {
		
		Collection<Commit> commits = new LinkedHashSet<Commit>();
		Committer plop = new Committer("plop", null, null);
		commits.add(new Commit("", plop, "", "2011-01-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-02-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-03-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-04-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-05-15 10:31:28"));
		commits.add(new Commit("", plop,  "", "2011-06-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-07-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-08-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-09-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-10-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-11-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-12-15 10:31:28"));
		
		CommitStats stats = dive.getStats(commits);
		assertEquals(Calendar.MONTH, stats.getUnit()); 
		assertEquals(12, stats.getLength());
		
		Map<String, Integer[]> commitsPerUnitByUser = stats.getCommitsPerUnitByUser();
		/* only one user*/
		assertEquals(1,  commitsPerUnitByUser.size());
		/*one entry per month */
		Integer[] commitsFromPlop =  commitsPerUnitByUser.get("plop");
		assertEquals(12, commitsFromPlop.length);
		for (Integer num : commitsFromPlop) {
			assertEquals(1, num.intValue());
		}
	}
	
	@Test
	public void commitPerMonthThreeCommitters() {
		
		Collection<Commit> commits = new LinkedHashSet<Commit>();
		Committer plop = new Committer("plop", null, null);
		Committer foo = new Committer("foo", null, null);
		Committer bar = new Committer("bar", null, null);
		commits.add(new Commit("", plop, "", "2011-01-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-02-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-03-15 10:31:28"));
		commits.add(new Commit("", plop, "", "2011-04-15 10:31:28"));
		commits.add(new Commit("", foo, "", "2011-05-15 10:31:28"));
		commits.add(new Commit("", foo,  "", "2011-06-15 10:31:28"));
		commits.add(new Commit("", foo, "", "2011-07-15 10:31:28"));
		commits.add(new Commit("", foo, "", "2011-08-15 10:31:28"));
		commits.add(new Commit("", bar, "", "2011-09-15 10:31:28"));
		commits.add(new Commit("", bar, "", "2011-10-15 10:31:28"));
		commits.add(new Commit("", bar, "", "2011-11-15 10:31:28"));
		commits.add(new Commit("", bar, "", "2011-12-15 10:31:28"));
		
		CommitStats stats = dive.getStats(commits);
		assertEquals(Calendar.MONTH, stats.getUnit()); 
		assertEquals(12, stats.getLength());
		
		Map<String, Integer[]> commitsPerUnitByUser = stats.getCommitsPerUnitByUser();
		/* we get 3 user*/
		assertEquals(3,  commitsPerUnitByUser.size());
		/*one entry per month */
		Integer[] commitsFromPlop =  commitsPerUnitByUser.get("plop");
		assertEquals(12, commitsFromPlop.length);

		Integer[] commitsFromFoo =  commitsPerUnitByUser.get("foo");
		assertEquals(12, commitsFromFoo.length);

		Integer[] commitsFromBar =  commitsPerUnitByUser.get("bar");
		assertEquals(12, commitsFromBar.length);
		
	}
	
	
	
	
	
}
