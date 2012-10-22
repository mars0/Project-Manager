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
	
	public void computeCriticalPath() {
		Activity s = project.getActivityByName("START");
		Activity e = project.getActivityByName("END");
		// check if graph is complete
		if (s != null && e != null) {
			// search for start activity
			forwardPass(s);
			e.setTimeMax(e.getTimeMin());
			backwardPass(e);
			computeFloats();
			//set length of critical path
			this.length = project.getActivityByName("END").getTimeMax();
			// clear text area and print new results
			project.getView().getTextArea().setText("");
			printCriticalPath(s, "");
			printFloats();
		}
		else {
			project.getView().printDebugln("Cannot compute critical path: END-activity is missing.");
		}
	}

}
