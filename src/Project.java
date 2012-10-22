import java.util.*;

public class Project {
	private String name;
	private int duration;
	private Date startDate;
	private Date endDate;
	private List<Activity> activities = new ArrayList<Activity>();
	private MainWindow view;
	
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
		// add start activity
		Activity a = addActivity("START", 0, null, 0);
		a.setTimeMin(0);
		a.setTimeMax(0);
	}
	
	// getters and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Activity> getActivities() {
		return activities;
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
			if (predecessors != null) {
				if (predecessors.length == 0) {
					Activity current = getActivityByName("START");
					newActivity.addPredecessor(current);
					current.addSuccessor(newActivity);
				}
				else {
					for (int i = 0; i < predecessors.length; i++) {
						Activity current = getActivityByName(predecessors[i]);
						newActivity.addPredecessor(current);
						current.addSuccessor(newActivity);
						
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
	
	private void forwardPass(Activity a) {
		List<Activity> predecessors = a.getPredecessors();
		List<Activity> successors = a.getSuccessors();
		
		//search for maximum
		Iterator<Activity> it = predecessors.iterator();
		int max = -1;
		while(it.hasNext()) {
			Activity current = it.next();
			if ((current.getDuration()+current.getTimeMin()) > max)
				max = current.getDuration()+current.getTimeMin();
		}
		// check if we found a true maximum
		if (max > -1)
			a.setTimeMin(max);
		// recursive invocation for successors
		it = successors.iterator();
		while(it.hasNext()) {
			forwardPass(it.next());
		}
	}
	
	private void backwardPass(Activity a) {
		List<Activity> predecessors = a.getPredecessors();
		List<Activity> successors = a.getSuccessors();
		
		//search for minimum;
		Iterator<Activity> it = successors.iterator();
		int min = Integer.MAX_VALUE;
		while(it.hasNext()) {
			Activity current = it.next();
			if ((current.getTimeMax()-a.getDuration()) < min)
				min = current.getTimeMax()-a.getDuration();
		}
		// check if we found a true minimum
		if ( min < Integer.MAX_VALUE)
			a.setTimeMax(min);
		// recursive invocation for predecessors
		it = predecessors.iterator();
		while(it.hasNext()) {
			backwardPass(it.next());
		}
	}
	
	private void computeFloats() {
		Iterator<Activity> it = activities.iterator();
		while(it.hasNext()) {
			Activity current = it.next();
			//exclude start and end activities
			if (!("START".equals(current.getName()) || "END".equals(current.getName()))) {
				current.setTotalFloat(current.getTimeMax()-current.getTimeMin());
				Iterator<Activity> it2 = current.getSuccessors().iterator();
				int min = Integer.MAX_VALUE;
				while(it2.hasNext()){
					int calc = it2.next().getTimeMin()-current.getTimeMin()-current.getDuration();
					if(calc < min)
						min = calc; 
				}
				current.setFreeFloat(min);
			}
		}
	}
	
	private void printCriticalPath(Activity a, String path) {
		if (a.getTimeMin() == a.getTimeMax()) {
			//critical
			path += a.getName() + " ";
			//view.getTextArea().append(a.getName() + " ");
			if ("END".equals(a.getName()))
				view.getTextArea().append(path + "\n");
			Iterator<Activity> it = a.getSuccessors().iterator();
			while(it.hasNext()) {
				printCriticalPath(it.next(), path);
			}
		}
	}
	
	private void printFloats() {
		// print total float and free float
		Iterator<Activity> iter = activities.iterator();
		view.getTextArea().append("\n" + "total/free float: " + "\n");
		while(iter.hasNext()) {
			Activity c = iter.next();
			// print only computed values
			if (c.getTotalFloat() > -1)
				view.getTextArea().append(c.getName() + ": " + c.getTotalFloat() + " " + c.getFreeFloat() + "\n");
		}
	}
	
	public void computeCriticalPath() {
		Activity s = getActivityByName("START");
		Activity e = getActivityByName("END");
		// check if graph is complete
		if (s != null && e != null) {
			// search for start activity
			forwardPass(s);
			e.setTimeMax(e.getTimeMin());
			backwardPass(e);
			computeFloats();
			// clear text area and print new results
			view.getTextArea().setText("");
			printCriticalPath(s, "");
			printFloats();
		}
		else {
			view.printDebugln("Cannot compute critical path: END-activity is missing.");
		}
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
			/*System.out.println();
			System.out.print("SUCCESSORS: ");
			relationsIter = current.getSuccessors().iterator();
			while (relationsIter.hasNext()){
				System.out.print(relationsIter.next().getName() + " ");
			}
			System.out.println();*/
		}
	}
	
	

}
