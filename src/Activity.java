import java.util.*;

public class Activity implements Comparable<Activity>{
	// private attributes
	private String name;
	private int duration;
	private int resources[];
	private int timeMin;
	private int timeMax;
	private int startDay; // the working day this activity starts
	private int freeFloat;
	private int totalFloat;
	private Calendar startDate;
	private Calendar endDate;
	private List<Activity> predecessors = new ArrayList<Activity>();
	private List<Activity> successors = new ArrayList<Activity>();
	
	public Activity(String aName, int aDuration, int[] aResources, int maxResources) {
		//this.pCal = pCalendar;
		this.name = aName;
		this.duration = aDuration;
		this.resources = new int[maxResources];
		if(aResources != null) System.arraycopy(aResources, 0, this.resources, 0, maxResources);
		this.timeMin = -1;
		this.timeMax = -1;
		this.startDay = -1;
		this.totalFloat = -1;
		this.freeFloat = -1;
	}
	
	public int compareTo(Activity c) {
		if ((this.getStartDay() + this.getDuration()) < (c.getStartDay() + c.getDuration()))
			return -1;
		else if ((this.getStartDay() + this.getDuration()) == (c.getStartDay() + c.getDuration()))
			return 0;
		else 
			return 1;
	}

	// getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void setStartDay(int day) {
		this.startDay = day;
	}
	
	public int getStartDay() {
		return this.startDay;
	}
	
	// get resource i
	public int getResource(int i) {
		if (i < this.resources.length)
			return resources[i];
		else 
			return 0;
	}
  // set resource i to value
	public void setResource(int i, int value) {
		if (i < this.resources.length && value >= 0)
			this.resources[i] = value;
	}

	public int getTimeMin() {
		return timeMin;
	}

	public void setTimeMin(int timeMin) {
		this.timeMin = timeMin;
	}

	public int getTimeMax() {
		return timeMax;
	}

	public void setTimeMax(int timeMax) {
		this.timeMax = timeMax;
	}

	public int getFreeFloat() {
		return freeFloat;
	}

	public void setFreeFloat(int freeFloat) {
		this.freeFloat = freeFloat;
	}

	public int getTotalFloat() {
		return totalFloat;
	}

	public void setTotalFloat(int totalFloat) {
		this.totalFloat = totalFloat;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	
	public List<Activity> getPredecessors() {
		return predecessors;
	}
	

	public List<Activity> getSuccessors() {
		return successors;
	}
	
	public boolean inList(List<Activity> activityList, String activityName) {
		Iterator<Activity> it = activityList.iterator();
		while(it.hasNext()){
			if(activityName.equals(it.next().getName()))
				return true;
		}
		return false;
	}

	public void addPredecessor(Activity pred) {
		if (!inList(predecessors, pred.getName()))
		  predecessors.add(pred);
	}
	
	public void addSuccessor(Activity succ) {
		if (!inList(successors, succ.getName()))
			successors.add(succ);
	}
	
	public void deletePredecessor(Activity pred) {
		predecessors.remove(pred);
	}
	public void deleteSuccessor(Activity succ) {
		successors.remove(succ);
	}

}
