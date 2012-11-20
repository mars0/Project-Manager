import javax.swing.JFrame;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.*;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class MainWindow {
  
	private int columnOffset = 0;
	private JFrame frmProjectManagerPro;
	private JTextField txtName;
	private JTextField txtDuration;
	private JTextField txtPred;
	private JTextField txtResources;
	private JTable table;
	private DefaultTableModel tableModel;
	private JButton addActivityButton;
	private JButton deleteActivityButton;
	private JButton addArcButton;
	private JButton removeArcButton;
	private ProjectManager delegate;
	private JScrollPane scrollPane;
	private JTextField txtFrom;
	private JTextField txtTo;
	private JButton criticalPathButton;
	private JScrollPane scrollPane_1;
	private JTextArea textArea;
	private JPanel panel;
	private JScrollPane scrollPane_2;
	private JTextArea debugOutput;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnEdit;
	private JMenu mnView;
	private JMenuItem mntmShow;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSaveAs;
	private JLabel lblAdddeleteResource;
	private JButton addResourceButton;
	private JButton deleteResourceButton;
	private JButton editActivityButton;
	private JPanel panel_1;
	

	public MainWindow(ProjectManager del) {
		this.delegate = del;
		initialize();
		frmProjectManagerPro.setVisible(true);
	}
	
	public int getColumnOffset() {
		return this.columnOffset;
	}

	public JTextField getTxtName() {
		return txtName;
	}


	public JTextField getTxtDuration() {
		return txtDuration;
	}


	public JTextField getTxtPred() {
		return txtPred;
	}


	public JTextField getTxtResources() {
		return txtResources;
	}
	
	public JTable getTable() {
		return table;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JTextField getTxtFrom() {
		return txtFrom;
	}

	public JTextField getTxtTo() {
		return txtTo;
	}

	public JTextArea getTextArea() {
		return textArea;
	}
	
	public void printDebug(String s) {
		debugOutput.append(s);
		debugOutput.setCaretPosition(debugOutput.getDocument().getLength());
	}
	
	public void printDebugln(String s) {
		debugOutput.append(s + "\n");
		debugOutput.setCaretPosition(debugOutput.getDocument().getLength());
	}
	
	public void addResource(int i) {
		tableModel.addColumn("R"+i);
	}

	public void deleteResource(int i) {
		//-1, because starting counting at 0
    TableColumn tcol = table.getColumnModel().getColumn(this.columnOffset+i-1);
    table.getColumnModel().removeColumn(tcol);
    //decrement column count 
    tableModel.setColumnCount(tableModel.getColumnCount()-1);
	}
	
	public void addActivityButtonToSave(boolean enable) {
		if (enable == true) {
			addActivityButton.setText("Save");
			addActivityButton.setForeground(Color.red);
		}
		else {
			addActivityButton.setText("Add Activity");
			addActivityButton.setForeground(Color.black);
		}
	}
	
	public void editActivityButtonToAbort(boolean enable) {
		if (enable == true) {
			editActivityButton.setText("Abort");
			editActivityButton.setForeground(Color.red);
		}
		else {
			editActivityButton.setText("Edit Activity");
			editActivityButton.setForeground(Color.black);
		}
	}
	
	public void clearInputFields() {
		this.txtName.setText("");
		this.txtDuration.setText("0");
	  this.txtPred.setText("");
		this.txtResources.setText("0");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmProjectManagerPro = new JFrame();
		frmProjectManagerPro.setTitle("Project Manager PRO");
		frmProjectManagerPro.setBounds(100, 100, 757, 575);
		frmProjectManagerPro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProjectManagerPro.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(40dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(54dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(61dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(20dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(65dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(70dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(1dlu;default)"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(172dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(18dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(17dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(17dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(14dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(14dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(40dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(2dlu;default)"),}));
		
		tableModel = new DefaultTableModel(); 
		tableModel.addColumn("Name"); 
		tableModel.addColumn("Duration");
		tableModel.addColumn("Min.");
		tableModel.addColumn("Max.");
		tableModel.addColumn("Predecessors");
		this.columnOffset = 5;
		tableModel.addColumn("R"+1);
		
		scrollPane = new JScrollPane();
		frmProjectManagerPro.getContentPane().add(scrollPane, "2, 2, 9, 1, fill, fill");
		
		table = new JTable(tableModel); 
		scrollPane.setViewportView(table);
		//table.setAutoCreateColumnsFromModel(false); //???
		
		scrollPane_1 = new JScrollPane();
		frmProjectManagerPro.getContentPane().add(scrollPane_1, "12, 2, fill, fill");
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane_1.setViewportView(textArea);
		
		JLabel lblName = new JLabel(" Name");
		frmProjectManagerPro.getContentPane().add(lblName, "2, 4");
		
		txtName = new JTextField();
		frmProjectManagerPro.getContentPane().add(txtName, "4, 4, fill, default");
		txtName.setColumns(10);
		
		JLabel lblAdddeleteArc = new JLabel("Add/Delete Arc");
		frmProjectManagerPro.getContentPane().add(lblAdddeleteArc, "6, 4");
		
		editActivityButton = new JButton("Edit Activity");
		frmProjectManagerPro.getContentPane().add(editActivityButton, "10, 4");
		editActivityButton.setActionCommand("editActivity");
	  editActivityButton.addActionListener(delegate);
		
		criticalPathButton = new JButton("Critical Path");
		frmProjectManagerPro.getContentPane().add(criticalPathButton, "12, 4");
		criticalPathButton.setActionCommand("criticalPath");
		criticalPathButton.addActionListener(delegate);
		
		JLabel lblDuration = new JLabel(" Duration");
		frmProjectManagerPro.getContentPane().add(lblDuration, "2, 6");
		
		txtDuration = new JTextField();
		txtDuration.setText("0");
		frmProjectManagerPro.getContentPane().add(txtDuration, "4, 6, fill, default");
		txtDuration.setColumns(10);
		
		txtFrom = new JTextField();
		frmProjectManagerPro.getContentPane().add(txtFrom, "6, 6");
		txtFrom.setText("FROM");
		txtFrom.setColumns(10);
		
		deleteActivityButton = new JButton("Delete Activity");
		frmProjectManagerPro.getContentPane().add(deleteActivityButton, "10, 6");
		deleteActivityButton.setActionCommand("deleteActivity");
		deleteActivityButton.addActionListener(delegate);
		
		JLabel lblPredecessors = new JLabel(" Predecessors");
		frmProjectManagerPro.getContentPane().add(lblPredecessors, "2, 8");
		
		txtPred = new JTextField();
		frmProjectManagerPro.getContentPane().add(txtPred, "4, 8, fill, default");
		txtPred.setColumns(10);
		
		txtTo = new JTextField();
		frmProjectManagerPro.getContentPane().add(txtTo, "6, 8");
		txtTo.setText("TO");
		txtTo.setColumns(10);
		
		lblAdddeleteResource = new JLabel("Add/Delete Resource");
		frmProjectManagerPro.getContentPane().add(lblAdddeleteResource, "10, 8");
		
		JLabel lblRecursos = new JLabel(" Resources");
		frmProjectManagerPro.getContentPane().add(lblRecursos, "2, 10");
		
		txtResources = new JTextField();
		txtResources.setText("0");
		frmProjectManagerPro.getContentPane().add(txtResources, "4, 10, fill, default");
		txtResources.setColumns(10);
		
		panel = new JPanel();
		frmProjectManagerPro.getContentPane().add(panel, "6, 10, fill, fill");
		panel.setLayout(null);
		
		addArcButton = new JButton("+");
		addArcButton.setBounds(6, 0, 60, 29);
		panel.add(addArcButton);
		addArcButton.setActionCommand("addArc");
		
		removeArcButton = new JButton("-");
		removeArcButton.setBounds(74, 0, 60, 29);
		panel.add(removeArcButton);
		removeArcButton.setActionCommand("removeArc");
		removeArcButton.addActionListener(delegate);
		addArcButton.addActionListener(delegate);
		
		panel_1 = new JPanel();
		frmProjectManagerPro.getContentPane().add(panel_1, "10, 10, fill, fill");
		panel_1.setLayout(null);
		
		addResourceButton = new JButton("+");
		addResourceButton.setBounds(6, 0, 60, 29);
		panel_1.add(addResourceButton);
		addResourceButton.setActionCommand("addResource");
		
		deleteResourceButton = new JButton("-");
		deleteResourceButton.setBounds(76, 0, 60, 29);
		panel_1.add(deleteResourceButton);
		deleteResourceButton.setActionCommand("deleteResource");
		deleteResourceButton.addActionListener(delegate);
		addResourceButton.addActionListener(delegate);
		
		addActivityButton = new JButton("Add Activity");
		frmProjectManagerPro.getContentPane().add(addActivityButton, "4, 12");
		addActivityButton.setActionCommand("addActivity");
		addActivityButton.addActionListener(delegate);
		
		scrollPane_2 = new JScrollPane();
		frmProjectManagerPro.getContentPane().add(scrollPane_2, "2, 14, 11, 1, fill, fill");
		
		debugOutput = new JTextArea();
		debugOutput.setForeground(Color.GREEN);
		debugOutput.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		debugOutput.setBackground(Color.BLACK);
		debugOutput.setEditable(false);
		scrollPane_2.setViewportView(debugOutput);
		
		menuBar = new JMenuBar();
		frmProjectManagerPro.setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		
		mntmSaveAs = new JMenuItem("Save as..");
		mnFile.add(mntmSaveAs);
		
		mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mnView = new JMenu("Calendar");
		menuBar.add(mnView);
		
		mntmShow = new JMenuItem("Show");
		mnView.add(mntmShow);
		mntmShow.addActionListener(delegate);
	  mntmShow.setActionCommand("showCal");
	}
}
