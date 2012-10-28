import java.util.*;

public class Project {
	private String name;
	private Date startDate;
	private Date endDate;
	private List<Activity> activities = new ArrayList<Activity>();
	private MainWindow view;
	private CriticalPath criticalPath;
	private ProjectCalendar projectCalendar;
	
	private boolean isUnique(String activityName) {
		Iterator<Activity> it = activities.iterator();
		while(it.hasNext()){
			if (activityName.equals(it.next().getName()))
				return false;
		}
		return true;
	}
	
	public Project(String name, MainWindow view) {
		this.name = name;
		this.view = view;
		this.projectCalendar = new ProjectCalendar();
		criticalPath = new CriticalPath(this);
		// add start activity
		Activity a = addActivity("START", 0, null, 0);
		a.setTimeMin(0);
		a.setTimeMax(0);
	}
	
	// getters and setters
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartDate() {
		return this.startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return this.endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Activity> getActivities() {
		return this.activities;
	}
	
	public MainWindow getView() {
		return this.view;
	}
	
	public CriticalPath getCriticalPath() {
		return this.criticalPath;
	}
	
	public ProjectCalendar getProjectCalendar() {
		return this.projectCalendar;
	}
	
	public Activity getActivityByName(String activityName) {
		Iterator<Activity> it = activities.iterator();
		while(it.hasNext()){
			Activity current = it.next();
			if (activityName.equals(current.getName())) {
				return current;
			}
		}
		return null;
	}

	public Activity addActivity(String activityName, int activityDuration, String[] predecessors, int activityResources) {
		Activity newActivity = null;
		if (this.isUnique(activityName)) {
			newActivity = new Activity(activityName, activityDuration, activityResources);
			// the predecessors of START are NULL
			if (predecessors != null) {
				if (predecessors.length == 0) {
					Activity current = getActivityByName("START");
					newActivity.addPredecessor(current);
					current.addSuccessor(newActivity);
				}
				else {
					for (int i = 0; i < predecessors.length; i++) {
						Activity current = getActivityByName(predecessors[i]);
						if (current != null) {
							newActivity.addPredecessor(current);
							current.addSuccessor(newActivity);
						}
						else {
							view.printDebugln("Cannot add predecessor relation to non-exisiting node " + predecessors[i]);
						}
					}
				}
			}
			activities.add(newActivity);
		}
		else {
			// activity name is not unique or activity already exists
			view.printDebugln("Cannot add activity " + activityName + " because the name already exists.");
		}
		return newActivity;
	}
	
	public void deleteActivityByName(String activityName) {
		Activity current = getActivityByName(activityName);
		if (current != null) {
			// delete successor references
			Iterator<Activity> predIter = current.getPredecessors().iterator();
			while (predIter.hasNext()) {
				predIter.next().getSuccessors().remove(current);
			}
			// delete predecessor references
			Iterator<Activity> succIter = current.getSuccessors().iterator();
			while (succIter.hasNext()) {
				succIter.next().getPredecessors().remove(current);
			}
			// delete element
			activities.remove(current);
		}
	}
	
	public void addArc(String from, String to) {
		Activity activityFrom = getActivityByName(from); 
		Activity activityTo = getActivityByName(to);
		if (activityFrom != null && activityTo != null) {
			activityFrom.addSuccessor(activityTo);
			activityTo.addPredecessor(activityFrom);
		}
		else
			view.printDebugln("Cannot add arc: at least one activity is currently not present.");
	}
	
	public void removeArc(String from, String to) {
		Activity activityFrom = getActivityByName(from); 
		Activity activityTo = getActivityByName(to);
		if (activityFrom != null && activityTo != null) {
			activityFrom.deleteSuccessor(activityTo);
			activityTo.deletePredecessor(activityFrom);
		}
		else
			view.printDebugln("Cannot delete arc: at least one activity is currently not present.");
	}
		
	public void printActivities() {
		// clear table
		view.getTableModel().setRowCount(0);
		Iterator<Activity> iter = activities.iterator();
		while (iter.hasNext()) {
			Activity a = iter.next();
			//print succ's and pred's
			Iterator<Activity> relationsIter = a.getPredecessors().iterator();
			String predecessors = "";
			while (relationsIter.hasNext()) {
				predecessors += relationsIter.next().getName() + " ";
			}
			// print row
			view.getTableModel().addRow(new Object[]{a.getName(), a.getDuration(), a.getTimeMin(), a.getTimeMax(), predecessors, a.getResources()});
		}
	}

}
