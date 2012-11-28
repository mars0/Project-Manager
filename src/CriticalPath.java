import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


public class CriticalPath {
	
	private int length;
	private Project project;
	
	public CriticalPath(Project p) {
		this.length = -1;
		this.project = p;
	}
	
	public int getLength() {
		return this.length;
	}
	
	private void initTimes() {
		Iterator<Activity> it = project.getActivities().iterator();
		while(it.hasNext()) {
			Activity a = it.next();
			if (!"START".equals(a.getName())){
				a.setTimeMin(-1);
				a.setTimeMax(-1);
			}
		}
	}
	
	private boolean minTimesDone(List<Activity> aList) {
		Iterator<Activity> it = aList.iterator();
		while(it.hasNext()) {
			if (it.next().getTimeMin() < 0)
				return false;
		}
		return true;
	}
	
	private boolean maxTimesDone(List<Activity> aList) {
		Iterator<Activity> it = aList.iterator();
		while(it.hasNext()) {
			if (it.next().getTimeMax() < 0)
				return false;
		}
		return true;
	}
	
	
	private void forwardPass(Activity a) {
		List<Activity> predecessors = a.getPredecessors();
		
		// check if we should compute this node
		if (minTimesDone(predecessors) && a.getTimeMin() < 0) {
			//search for maximum
			Iterator<Activity> it = predecessors.iterator();
			int max = -1;
			while(it.hasNext()) {
				Activity current = it.next();
				if ((current.getDuration()+current.getTimeMin()) > max)
					max = current.getDuration()+current.getTimeMin();
			}
			a.setTimeMin(max);
			// recursive invocation for successors
			it = a.getSuccessors().iterator();
			while(it.hasNext()) {
				forwardPass(it.next());
			}
		}
	}
	
	private void backwardPass(Activity a) {
		List<Activity> successors = a.getSuccessors();
		
		// check if we should compute this node
		if (maxTimesDone(successors) && a.getTimeMax() < 0) {
			//search for minimum;
			Iterator<Activity> it = successors.iterator();
			int min = Integer.MAX_VALUE;
			while(it.hasNext()) {
				Activity current = it.next();
				if ((current.getTimeMax()-a.getDuration()) < min)
					min = current.getTimeMax()-a.getDuration();
			}
			a.setTimeMax(min);
			// recursive invocation for predecessors
			it = a.getPredecessors().iterator();
			while(it.hasNext()) {
				backwardPass(it.next());
			}
		}
	}
	
	private void computeFloats() {
		Iterator<Activity> it = project.getActivities().iterator();
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
				project.getView().getTextArea().append(path + " " + this.length +"\n");
			Iterator<Activity> it = a.getSuccessors().iterator();
			while(it.hasNext()) {
				printCriticalPath(it.next(), path);
			}
		}
	}
	
	private void printFloats() {
		// print total float and free float
		Iterator<Activity> iter = project.getActivities().iterator();
		project.getView().getTextArea().append("\n" + "total/free float: " + "\n");
		while(iter.hasNext()) {
			Activity c = iter.next();
			// print only computed values
			if (c.getTotalFloat() > -1)
				project.getView().getTextArea().append(c.getName() + ": " + c.getTotalFloat() + " " + c.getFreeFloat() + "\n");
		}
	}
	
	public boolean computeCriticalPath() {
		Activity s = project.getActivityByName("START");
		Activity e = project.getActivityByName("END");
		boolean success = false; 
		// check if graph is complete
		if (project.isValidSubGraph(s, null)) {
			// init min and max times to -1
			initTimes(); 
			// call forwardPass for successors of START
			Iterator<Activity> it = s.getSuccessors().iterator();
			while(it.hasNext()) {
				forwardPass(it.next());
			}
			e.setTimeMax(e.getTimeMin());
			// call backwardPass for predecessors of END
			it = e.getPredecessors().iterator();
			while(it.hasNext()) {
				backwardPass(it.next());
			}
			computeFloats();
			// set length of critical path
			this.length = project.getActivityByName("END").getTimeMax();
			
			// set start date and activity dates
			if (project.getStartDate() == null)
				project.setStartDate(project.getProjectCalendar().getNextWorkDay(Calendar.getInstance(),0));
      project.resetActivityStartDays();
			project.setActivityDates();
      project.calculateEndDate();
			
			// clear text area and print new results
			project.getView().getTextArea().setText("");
			printCriticalPath(s, "");
			printFloats();
			success = true;
		}
		else {
			this.length = -1;
			project.resetDates();
		}
		return success;
	}

}
