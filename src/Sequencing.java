import java.util.*;


public class Sequencing {
	
	//private int[][] availableResources;
	private Project project;
	
	public Sequencing(Project p) {
		this.project = p;	
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
		int counter = 0;
		while (duration > 0) {
			int[] res = availableRes.get(start);
			counter++;
			if (res != null) {
				for (int r=0; r<project.getUsedResources(); r++) {
					if (res[r]-a.getResource(r) < 0) {
						start += counter;
						counter = 0;
						duration = a.getDuration();
						break;
					}
				}
				duration--;
			}
		}
		return start;
	}
	
	private void serialize(List<Activity> elected, List<Activity> completed, Map<Integer,int[]> availableRes) {
    Activity current = leastLFT(elected);
		elected.remove(current);
		int startDay = latestFinishingTime(current.getPredecessors());
		startDay = nextAvailable(current, startDay, availableRes);
		current.setStartDay(startDay);
		completed.add(current);
		
		//update resources
		for (int r=0; r<project.getUsedResources(); r++) {
			availableRes.get(startDay)[r] -= current.getResource(r);
		}
		int[] lastUsage = project.getResourceLimits();
		for (int i=startDay; i<startDay+current.getDuration(); i++) {
			if (availableRes.get(startDay) != null) {
				for (int r=0; r<project.getUsedResources(); r++) {
					lastUsage = availableRes.get(startDay);
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
		availableRes.put(0, project.getResourceLimits());
		if (project.getCriticalPath().computeCriticalPath()) {
			serialize(project.getActivityByName("START").getSuccessors(), new ArrayList<Activity>(), availableRes);
		}
 	}

}
