import java.util.*;
//TODO: add boolean isValid that indicates if current project can be correctly executed (check graph!!!)
public class Project {
	private String name;
	private int usedResources = 1;
	private int[] resLimits;
	private Calendar startDate;
	private Calendar endDate;
	private int length = -1; //current length of the project
	private List<Activity> activities = new ArrayList<Activity>();
	private MainWindow view;
	private CriticalPath criticalPath;
	private ProjectCalendar projectCalendar;
	private ResourceManager resourceManager;
	
	private boolean isUnique(String activityName) {
		Iterator<Activity> it = activities.iterator();
		while(it.hasNext()){
			if (activityName.equals(it.next().getName()))
				return false;
		}
		return true;
	}
	
	public Project(String name, MainWindow view, int maxResources) {
		this.name = name;
		this.view = view;
		this.resLimits = new int[maxResources];
		Arrays.fill(this.resLimits, 4); //TODO for debug later INTEGER.MAXVALUE
		this.projectCalendar = new ProjectCalendar(this);
		this.resourceManager = new ResourceManager(this);
		criticalPath = new CriticalPath(this);
		// add start activity
		Activity a = addActivity("START", 0, null, null);
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
	public int getMaxNumOfResources() {
		return this.resLimits.length;
	}
	public int[] getResourceLimits() {
		return this.resLimits;
	}
	public Calendar getStartDate() {
		return this.startDate;
	}
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
		view.printDebugln("Set start date to " + startDate.getTime());
	}

	public Calendar getEndDate() {
		return this.endDate;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public void resetDates() {
		this.endDate = null;
		this.startDate = null;
	}
	
	public int getUsedResources() {
		return this.usedResources;
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
	
	public ResourceManager getResourceManager() {
		return this.resourceManager;
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
	
	// sets the working start day of every activity to the earliest start day
	public void resetActivityStartDays() {
		Iterator<Activity> it = activities.iterator();
		while (it.hasNext()) {
			Activity a = it.next();
			a.setStartDay(a.getTimeMin());
		}
	}
	
	public void setActivityDates() {
		// check if time table is valid
		if (startDate != null && endDate != null) { //TODO: check isValid...
			Iterator<Activity> it = activities.iterator();
			while (it.hasNext()) {
				Activity a = it.next();
				a.setStartDate(projectCalendar.getNextWorkDay(startDate, a.getStartDay()));
				a.setEndDate(projectCalendar.getNextWorkDay(startDate,a.getStartDay()+a.getDuration()));
			}
		}
	}
	
	public Calendar calculateEndDate() {
		Activity end = this.getActivityByName("END");
		if(end != null) {
			this.length = end.getStartDay(); 
			this.endDate = projectCalendar.calculateEndDate(startDate, this.length);
			return this.endDate;
		}
		return null;
	}

	public Activity addActivity(String activityName, int activityDuration, String predecessors[], int activityResources[]) {
		Activity newActivity = null;
		if (this.isUnique(activityName)) {
			newActivity = new Activity(activityName, activityDuration, activityResources, this.getMaxNumOfResources());
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
	
	public void updatePredecessors(Activity a, String[] newPreds) {
		List<Activity> oldPreds = new ArrayList<Activity>(a.getPredecessors());
		for (int i=0; i<newPreds.length; i++) {
			if (a.inList(oldPreds, newPreds[i])) {
				// no update
				oldPreds.remove(this.getActivityByName(newPreds[i]));
			}
			else {
				// add new arcs
				addArc(newPreds[i], a.getName());
			}
		}
		Iterator<Activity> it = oldPreds.iterator();
		while(it.hasNext()){
			// delete old arcs
			removeArc(it.next().getName(), a.getName());
		}
	}
	
	private void addArc(String from, String to) {
		Activity activityFrom = getActivityByName(from); 
		Activity activityTo = getActivityByName(to);
		if (activityFrom != null && activityTo != null) {
			activityFrom.addSuccessor(activityTo);
			activityTo.addPredecessor(activityFrom);
		}
		else
			view.printDebugln("Cannot add arc: at least one activity is currently not present.");
	}
	
	private void removeArc(String from, String to) {
		Activity activityFrom = getActivityByName(from); 
		Activity activityTo = getActivityByName(to);
		if (activityFrom != null && activityTo != null) {
			activityFrom.deleteSuccessor(activityTo);
			activityTo.deletePredecessor(activityFrom);
		}
		else
			view.printDebugln("Cannot delete arc: at least one activity is currently not present.");
	}
	
	public void addResource() {
		if(usedResources < this.resLimits.length) {
			usedResources++;
			view.addResource(usedResources);
		}
		else {
			view.printDebugln("Cannot add further resources.");
		}
	}
	
	public void deleteResource() {
		if(usedResources > 1) {
			view.deleteResource(usedResources);
			usedResources--;
		}
		else {
			view.printDebugln("The project needs at least one resource.");
		}
	}
		
	public boolean isValidSubGraph(Activity start, List<Activity> visitedNodes) {
		boolean res = true;
		Iterator<Activity> it = start.getSuccessors().iterator();
		List<Activity> vNodes;
		
		if (visitedNodes != null)
			vNodes = new ArrayList<Activity>(visitedNodes);
		else
			vNodes = new ArrayList<Activity>();
		vNodes.add(start);
		
		// only the end node is allowed to have no successors
		if (start.getSuccessors().size() == 0) {
			if ("END".equals(start.getName()))
				return true;
			else 
				return false;
		}
		
		// iterate through all successors
		while (it.hasNext()) {
			Activity succ = it.next();
			if ("END".equals(succ.getName()) && start.getSuccessors().size() != 1) {
				return false;
			}
			if (!(vNodes.contains(succ))) {
				res = res && isValidSubGraph(succ, vNodes);
			}
			else {
				return false;
			}
		}
		return res;
	}
		
	public void printActivities() {
		// clear table
		int currentRow = 0;
		view.getTableModel().setRowCount(currentRow);
		Iterator<Activity> iter = activities.iterator();
		while (iter.hasNext()) {
			Activity a = iter.next();
			//print succ's and pred's
			Iterator<Activity> relationsIter = a.getPredecessors().iterator();
			String predecessors = "";
			while (relationsIter.hasNext()) {
				predecessors += relationsIter.next().getName() + " ";
			}
			// get start and end
			Calendar calStart = a.getStartDate();
			Calendar calEnd = a.getEndDate();
			String start, end;
			if (calStart == null || calEnd == null) {
				start = "-";
				end = "-";
			}
			else {
				start = calStart.get(Calendar.DAY_OF_MONTH) + "/" + (calStart.get(Calendar.MONTH)+1) + "/" + calStart.get(Calendar.YEAR);
				end = calEnd.get(Calendar.DAY_OF_MONTH) + "/" + (calEnd.get(Calendar.MONTH)+1) + "/" + calEnd.get(Calendar.YEAR);
 			}
			// build Gantt for current activity
			String ganttLine = "";
			for (int t=0; t<this.length; t++){
				if (t >= a.getStartDay() && t < a.getStartDay()+a.getDuration())
					ganttLine += "X";
				else if (t >= a.getTimeMin() && t < a.getTimeMax()+a.getDuration())
					ganttLine += "-";
				else 
					ganttLine += " ";
			}
			// print row
			view.getTableModel().addRow(new Object[]{a.getName(), a.getDuration(), start, end, predecessors, ganttLine});
			for(int i=0; i<this.getMaxNumOfResources() && (i+view.getColumnOffset())<view.getTableModel().getColumnCount(); i++) {
				view.getTableModel().setValueAt(a.getResource(i), currentRow, i+view.getColumnOffset());
			}
			currentRow++;
		}
	}

}
