//import java.awt.EventQueue;
import java.awt.*;
import javax.swing.*;
//import java.awt.Canvas;
//import java.awt.BorderLayout;


public class ResourceWindow {

	private JFrame frmGraph;
  private ResourceManager delegate;
	/**
	 * Create the application.
	 */
	public ResourceWindow(ResourceManager del) {
		this.delegate = del;
		initialize();
		frmGraph.setVisible(true);
    
   /* JScrollPane scrollPane = new JScrollPane();
    frmGraph.getContentPane().add(scrollPane, BorderLayout.CENTER);
    
    Canvas canvas = new Canvas();
    scrollPane.setViewportView(canvas);*/
    
	}
	
	public void openWindow() {
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

}
