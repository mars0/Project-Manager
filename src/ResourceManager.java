import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ResourceManager implements ActionListener{
	private Project project;
	private ResourceWindow rWindow;
	
	public ResourceManager(Project p) {
		this.project = p;
	}
	
  public void actionPerformed(ActionEvent e) {

  }
  
  public void openResourceWindow() {
  	if (this.rWindow == null)
  		this.rWindow = new ResourceWindow(this);
  	else {
  		rWindow.openWindow();
  	}
  }
}
