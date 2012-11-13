import java.util.*;

public class Activity {
	// private attributes
	private String name;
	private int duration;
	private int resources[];
	private int timeMin;
	private int timeMax;
	private int freeFloat;
	private int totalFloat;
	private Date startDate;
	private Date endDate;
	private List<Activity> predecessors = new ArrayList<Activity>();
	private List<Activity> successors = new ArrayList<Activity>();
	
	private boolean inList(List<Activity> activityList, String activityName) {
		Iterator<Activity> it = activityList.iterator();
		while(it.hasNext()){
			if(activityName.equals(it.next().getName()))
				return true;
		}
		return false;
	}
	
	public Activity(String aName, int aDuration, int[] aResources, int maxResources) {
		//this.pCal = pCalendar;
		this.name = aName;
		this.duration = aDuration;
		this.resources = new int[maxResources];
		if(aResources != null) System.arraycopy(aResources, 0, this.resources, 0, maxResources);
		this.timeMin = -1;
		this.timeMax = -1;
		this.totalFloat = -1;
		this.freeFloat = -1;
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
	
	/*public int getResources() {
		return resources;
	}

	public void setResources(int resources) {
		this.resources = resources;
	}*/

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

	public Date getStartDate() {
		// what if startDate is null?
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		// what if endDate is null?
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public List<Activity> getPredecessors() {
		return predecessors;
	}

	public List<Activity> getSuccessors() {
		return successors;
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
