package models;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import play.Logger;

public class CommitStats {

	private final String datePattern;
	
	private int unit;
	
	private int start;
	
	private int length;
	
	private int year;
	
	private int month;
	
	private final Map<String, Integer[]> commitsPerUnitByUser;
	
	public CommitStats(Collection<Commit> commits, String pattern) {
		
		this.datePattern = pattern;
		commitsPerUnitByUser = new HashMap<String, Integer[]>();
		
		if (!commits.isEmpty()) {
			Commit[] commitsArray = (Commit[]) commits.toArray(new Commit[commits.size()]);
			
			Commit firstCommit = commitsArray[0];
			Commit lastCommit = commitsArray[commitsArray.length-1];
			computeParameters(firstCommit.getDate(), lastCommit.getDate());

			
			final Calendar cal = Calendar.getInstance();
					
			for (Commit commit : commitsArray) {
					
					Date date = parse(commit.getDate());
					
					if (date !=null) {
						cal.setTime(date);
						
						int unitInstance = cal.get(getUnit());					
						final int index = unitInstance - getStart();					
						
						final Committer committer = commit.getCommitter();
						if (committer.isRegistered()) {
							String login = commit.getCommitter().getLogin();
							
							if (commitsPerUnitByUser.containsKey(login)) {
								addCommit(login, index);
							} else {
								Integer[] loginCommits = new Integer[getLength()];
								for (int i=0; i < loginCommits.length ; i++) {
									loginCommits[i] = Integer.valueOf(0);
								}
								commitsPerUnitByUser.put(login, loginCommits);
								addCommit(login, index);
							}
						}
					}
			}
		}
	}

	private void addCommit(String login, int index) {
		Integer[] loginCommits = commitsPerUnitByUser.get(login);
		loginCommits[index] = Integer.valueOf(loginCommits[index].intValue()+1);
	}
	
	private void computeParameters(String start, String end) {
		final Date startDate = parse(start);
		final Date endDate = parse(end);
		if (startDate != null && endDate != null) {
			
			final Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			int startYear = cal.get(Calendar.YEAR);
			int startMonth = cal.get(Calendar.MONTH);
			int startDay = cal.get(Calendar.DAY_OF_MONTH);
			
			cal.setTime(endDate);
			int endYear = cal.get(Calendar.YEAR);
			int endMonth = cal.get(Calendar.MONTH);
			int endDay = cal.get(Calendar.DAY_OF_MONTH);
			
			
			if (endYear != startYear) {
				this.unit = Calendar.YEAR;
				this.start = startYear;
				this.length = endYear - startYear +1;
			} else if(endMonth != startMonth) {
				this.unit = Calendar.MONTH;
				this.start = startMonth;
				this.length = endMonth - startMonth +1;
				this.year = endYear;
			} else {
				this.unit = Calendar.DAY_OF_MONTH;
				this.start = startDay;
				this.length = endDay - startDay +1;
				this.year = endYear;
				this.month = endMonth;
			}
		}
		
	}
	
	
	
	
	/**
	 * Parse the date with the given pattern.
	 * @param date the date to parse
	 * @return the date if it could be parsed, <code>null</code> otherwise
	 */
	private Date parse(String date) {
		final SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getInstance(); 
		formatter.applyPattern(datePattern);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			Logger.debug(date + " was not parsed correctly. Expected pattern was: " + datePattern);
			return null;
		}
	}
	
	/**
	 * Get the unit of time for the commit stats.
	 * @return {@link Calendar#YEAR}, {@link Calendar#MONTH}, or  {@link Calendar#DAY_OF_MONTH} if it could be computed
	 */
	public int getUnit() {
		return unit;
	}

	/**
	 * Get the the unit value of the first commit. For instance if unit is year and first commit was in 1950 it will return 1950.
	 */
	public int getStart() {
		return start;
	}
	
	/**
	 * Get the number of time unit.
	 * @return the number of time unit
	 * @see CommitStats#getUnit()
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Get the year of analyzed commits. This is useful when the unit is Month or Day.
	 * @return the year of commits.
	 */
	public int getYear() {
		return year;
	}
	
	/**
	 * Get the year of analyzed commits. This is useful when the unit is Month or Day.
	 * @return the year of commits.
	 */
	public String getMonth() {		
		return new DateFormatSymbols(Locale.ENGLISH).getMonths()[month];
	}
	
	
	/**
	 * Get commits per time unit by user. The number of time unit will be the same for all user and can be retrieved with {@link #getLength()}
	 * @return the commits stats raw data.
	 */
	public Map<String, Integer[]> getCommitsPerUnitByUser() {
		return commitsPerUnitByUser;
	}
	
	
}
