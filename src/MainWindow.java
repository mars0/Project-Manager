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
	private ProjectManager delegate;
	private JScrollPane scrollPane;
	private JButton criticalPathButton;
	private JScrollPane scrollPane_1;
	private JTextArea textArea;
	private JScrollPane scrollPane_2;
	private JTextArea debugOutput;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnEdit;
	private JMenu mnShow;
	private JMenuItem mntmCalendar;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSaveAs;
	private JButton addResourceButton;
	private JButton deleteResourceButton;
	private JButton editActivityButton;
	private JPanel panel_1;
	private JMenuItem mntmResources;
	private JButton btnValid;
	private JMenuItem mntmSetResources;
	private JMenuItem mntmSequence;
	private JMenuItem mntmEqualize;
	private JMenu mnOptimize;
	private JMenuItem mntmChangeStartDate;
	

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
		frmProjectManagerPro.setBounds(100, 100, 790, 575);
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
				ColumnSpec.decode("max(48dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(69dlu;default):grow"),
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
				RowSpec.decode("max(2dlu;default)"),}));
		
		tableModel = new DefaultTableModel() {
	    @Override
	    public boolean isCellEditable(int row, int column) {
	       //all cells false
	       return false;
	    }
		};
		tableModel.addColumn("Name"); 
		tableModel.addColumn("Duration");
		tableModel.addColumn("Start");
		tableModel.addColumn("End");
		tableModel.addColumn("Predecessors");
		tableModel.addColumn("Gantt");
		this.columnOffset = 6;
		tableModel.addColumn("R"+1);
		
		scrollPane = new JScrollPane();
		frmProjectManagerPro.getContentPane().add(scrollPane, "2, 2, 9, 1, fill, fill");
		
		table = new JTable(tableModel); 
		scrollPane.setViewportView(table);
		table.setFont(new Font("Courier New", Font.PLAIN, 14));
		//table.setAutoCreateColumnsFromModel(false); //???
		
		scrollPane_1 = new JScrollPane();
		frmProjectManagerPro.getContentPane().add(scrollPane_1, "12, 2, fill, fill");
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane_1.setViewportView(textArea);
		
		btnValid = new JButton("Valid Graph?");
		frmProjectManagerPro.getContentPane().add(btnValid, "2, 4");
		btnValid.setActionCommand("valid");
		btnValid.addActionListener(delegate);
		
		addActivityButton = new JButton("Add Activity");
		frmProjectManagerPro.getContentPane().add(addActivityButton, "4, 4");
		addActivityButton.setActionCommand("addActivity");
		addActivityButton.addActionListener(delegate);
		
		editActivityButton = new JButton("Edit Activity");
		frmProjectManagerPro.getContentPane().add(editActivityButton, "6, 4");
		editActivityButton.setActionCommand("editActivity");
		editActivityButton.addActionListener(delegate);
		
		deleteActivityButton = new JButton("Delete Activity");
		frmProjectManagerPro.getContentPane().add(deleteActivityButton, "8, 4");
		deleteActivityButton.setActionCommand("deleteActivity");
		deleteActivityButton.addActionListener(delegate);
		
		panel_1 = new JPanel();
		frmProjectManagerPro.getContentPane().add(panel_1, "10, 4, fill, fill");
		panel_1.setLayout(null);
		
		addResourceButton = new JButton("+");
		addResourceButton.setBounds(0, 3, 50, 23);
		panel_1.add(addResourceButton);
		addResourceButton.setActionCommand("addResource");
		
		deleteResourceButton = new JButton("-");
		deleteResourceButton.setBounds(46, 3, 50, 23);
		panel_1.add(deleteResourceButton);
		deleteResourceButton.setActionCommand("deleteResource");
		deleteResourceButton.addActionListener(delegate);
		addResourceButton.addActionListener(delegate);
		
		criticalPathButton = new JButton("Critical Path");
		frmProjectManagerPro.getContentPane().add(criticalPathButton, "12, 4");
		criticalPathButton.setActionCommand("criticalPath");
		criticalPathButton.addActionListener(delegate);
		
		JLabel lblName = new JLabel(" Name");
		frmProjectManagerPro.getContentPane().add(lblName, "2, 6");
		
		txtName = new JTextField();
		frmProjectManagerPro.getContentPane().add(txtName, "4, 6, fill, default");
		txtName.setColumns(10);
		
		scrollPane_2 = new JScrollPane();
		frmProjectManagerPro.getContentPane().add(scrollPane_2, "6, 6, 7, 7, fill, fill");
		
		debugOutput = new JTextArea();
		scrollPane_2.setViewportView(debugOutput);
		debugOutput.setForeground(Color.GREEN);
		debugOutput.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		debugOutput.setBackground(Color.BLACK);
		debugOutput.setEditable(false);
		
		JLabel lblDuration = new JLabel(" Duration");
		frmProjectManagerPro.getContentPane().add(lblDuration, "2, 8");
		
		txtDuration = new JTextField();
		txtDuration.setText("0");
		frmProjectManagerPro.getContentPane().add(txtDuration, "4, 8, fill, default");
		txtDuration.setColumns(10);
		
		JLabel lblPredecessors = new JLabel(" Predecessors");
		frmProjectManagerPro.getContentPane().add(lblPredecessors, "2, 10");
		
		txtPred = new JTextField();
		frmProjectManagerPro.getContentPane().add(txtPred, "4, 10, fill, default");
		txtPred.setColumns(10);
		
		JLabel lblRecursos = new JLabel(" Resources");
		frmProjectManagerPro.getContentPane().add(lblRecursos, "2, 12");
		
		txtResources = new JTextField();
		txtResources.setText("0");
		frmProjectManagerPro.getContentPane().add(txtResources, "4, 12, fill, default");
		txtResources.setColumns(10);
		
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
		
		mntmSetResources = new JMenuItem("Resource Limits");
		mnEdit.add(mntmSetResources);
		mntmSetResources.addActionListener(delegate);
		mntmSetResources.setActionCommand("setRes");
		
		mntmChangeStartDate = new JMenuItem("Start Date");
		mnEdit.add(mntmChangeStartDate);
		mntmChangeStartDate.addActionListener(delegate);
		mntmChangeStartDate.setActionCommand("changeDate");
		
		mnShow = new JMenu("Show");
		menuBar.add(mnShow);
		
		mntmCalendar = new JMenuItem("Calendar");
		mnShow.add(mntmCalendar);
		mntmCalendar.addActionListener(delegate);
	  mntmCalendar.setActionCommand("showCal");
	  
	  mntmResources = new JMenuItem("Resources");
	  mnShow.add(mntmResources);
		mntmResources.addActionListener(delegate);
	  mntmResources.setActionCommand("showRes");
	  
	  mnOptimize = new JMenu("Optimize");
	  menuBar.add(mnOptimize);
	  
	  mntmSequence = new JMenuItem("Sequence");
	  mnOptimize.add(mntmSequence);
	  mntmSequence.addActionListener(delegate);
	  mntmSequence.setActionCommand("sequence");
	  
	  mntmEqualize = new JMenuItem("Equalize");
	  mnOptimize.add(mntmEqualize);
	  mntmEqualize.addActionListener(delegate);
	  mntmEqualize.setActionCommand("equalize");
	}
}
