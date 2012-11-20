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
		drawRecourses(delegate.calcDailyResources());
	}
	
	public void openWindow() {
		drawRecourses(delegate.calcDailyResources());
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
	
  public void drawRecourses(int dailyResources[]) { // [day][resource]
	/*	double[] value= new double[5];
	  String[] languages = new String[5];
	  value[0] = 1;
	  languages[0] = "Visual Basic";
	
	  value[1] = 2;
	  languages[1] = "PHP";
	
	  value[2] = 3;
	  languages[2] = "C++";
	
	  value[3] = 4;
	  languages[3] = "C";
	
	  value[4] = 5;
	  languages[4] = "Java"; */
	  frmGraph.getContentPane().add(new ResourceChart(dailyResources, "Resource Usage"));
	}

}
