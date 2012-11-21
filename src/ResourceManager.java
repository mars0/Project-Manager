import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;


public class ResourceManager implements ActionListener{
	private Project project;
	private ResourceWindow rWindow;
	
	public ResourceManager(Project p) {
		this.project = p;
	}
	
  public void actionPerformed(ActionEvent e) {

  }
  
  public int[] calcDailyResources() {
  	int res[];
  	int length = project.getLength();
  	if (length > 0) { 
  		res = new int[length];
  		for (int day=0; day<length; day++) {
  			Iterator<Activity> it = project.getActivities().iterator();
  			while (it.hasNext()) {
  				Activity a = it.next();
  				if (a.getStartDay() < day && day < a.getStartDay()+a.getDuration()) {
  					res[day] += a.getResource(0); //TODO for all resources 
  				}
  			}
  		}
  	}
  	else {
  		res = new int[0];
  	}
  	return res;
  }
  
  public void openResourceWindow() {
  	if (this.rWindow == null) {
  		this.rWindow = new ResourceWindow(this);
  	  rWindow.openWindow();
  	}
  	else {
  		rWindow.openWindow();
  	}
  }
}
