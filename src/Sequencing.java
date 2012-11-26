import java.util.*;


public class Sequencing {
	
	private Project project;
	private int numberOfRes;
	
	public Sequencing(Project p) {
		this.project = p;
		this.numberOfRes = project.getMaxNumOfResources();
	}
	
	private Activity leastLFT(List<Activity> activities) {
		int min = Integer.MAX_VALUE;
		Activity res = null;
		Iterator<Activity> it = activities.iterator();
		while(it.hasNext()) {
			Activity a = it.next();
			if (a.getDuration()+a.getTimeMax() < min) {
				min = a.getDuration()+a.getTimeMax();
				res = a;
			}
		}
		return res;
	}
	
	private int latestFinishingTime(List<Activity> activities) {
		int time = 0;
		Iterator<Activity> it = activities.iterator();
		while(it.hasNext()) {
			Activity a = it.next();
			if (a.getStartDay()+a.getDuration() > time)
				time = a.getStartDay()+a.getDuration();
		}
		return time;
	}
	
	private int nextAvailable(Activity a, int start, Map<Integer,int[]> availableRes) {
		int duration = a.getDuration();
		int res[];
		boolean success = false;

		while (duration > 0) {
			if (availableRes.get(start) != null) {
				success = true;
				res = availableRes.get(start);
				for (int r=0; r<project.getUsedResources(); r++) {
					//System.out.println("Available: " + res[r]);
					if (res[r]-a.getResource(r) < 0) {
						//System.out.println("Confict: " + start);
						duration = a.getDuration();
						success = false;
						break;
					}
				}
			}
			start++;
			if (success == true) {
				duration--;
			}
		}
		//System.out.println(a.getName() + " starts at " + (start-a.getDuration()));
		return (start - a.getDuration());
	}
	
	private void serialize(List<Activity> e, List<Activity> c, Map<Integer,int[]> availableRes) {
		List<Activity> elected = new ArrayList<Activity>(e);
		List<Activity> completed = new ArrayList<Activity>(c);
    Activity current = leastLFT(elected);
		elected.remove(current);
		int startDay = latestFinishingTime(current.getPredecessors());
		//project.getView().printDebugln(current.getName() + " earliest start day: " + startDay);
		startDay = nextAvailable(current, startDay, availableRes);
		//project.getView().printDebugln(current.getName() + " start day: " + startDay);
		current.setStartDay(startDay);
		completed.add(current);

		//update resources
		int[] lastUsage = new int[this.numberOfRes];
		System.arraycopy(availableRes.get(startDay), 0, lastUsage, 0, this.numberOfRes);
		for (int i=startDay; i<startDay+current.getDuration(); i++) {
			if (availableRes.get(i) != null) {
				System.arraycopy(availableRes.get(i), 0, lastUsage, 0, this.numberOfRes);
				for (int r=0; r<project.getUsedResources(); r++) {
					availableRes.get(i)[r] -= current.getResource(r);
				}
			}
		}
		if (availableRes.get(startDay+current.getDuration()) == null) {
			availableRes.put(startDay+current.getDuration(), lastUsage);
		}
		
		// updated elected
		Iterator<Activity> it = current.getSuccessors().iterator();
		while(it.hasNext()) {
			Activity a = it.next();
			Iterator<Activity> it2 = a.getPredecessors().iterator();
			boolean add = true;
			while(it2.hasNext()) {
				if (!completed.contains(it2.next()))
					add = false;
			}
			if (add) elected.add(a); 
		}
		
		// recursive call
		if (elected.size() > 0)
			serialize(elected, completed, availableRes);
	}
	
	public void serialization() {
		Map<Integer,int[]> availableRes = new HashMap<Integer,int[]>();
		int resLimits[] = new int[this.numberOfRes];
		System.arraycopy(project.getResourceLimits(), 0, resLimits, 0, this.numberOfRes);
		availableRes.put(0, resLimits);
		Activity start = project.getActivityByName("START");
		start.setStartDay(0);
		if (project.getCriticalPath().computeCriticalPath()) {
			serialize(start.getSuccessors(), new ArrayList<Activity>(), availableRes);
			project.calculateEndDate();
			project.setActivityDates();
		}
 	}

}
