import javax.swing.*;

public class ResourceWindow {

	private JFrame frmGraph;
  private ResourceManager delegate;
	/**
	 * Create the application.
	 */
	public ResourceWindow(ResourceManager del) {
		this.delegate = del;
		initialize();
		drawRecourses(delegate.calcDailyResources(), delegate.getProject().getResourceLimits());
	}
	
	public void openWindow() {
		drawRecourses(delegate.calcDailyResources(), delegate.getProject().getResourceLimits());
		frmGraph.setVisible(true);	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGraph = new JFrame();
		frmGraph.setTitle("Resource Usage");
		frmGraph.setBounds(100, 100, 450, 300);
	}
	
  public void drawRecourses(int dailyResources[][], int resLimit[]) { // [day][resource]
  	frmGraph.getContentPane().removeAll();
	  frmGraph.getContentPane().add(new ResourceChart(dailyResources, resLimit, "Resource Usage"));
	}

}
