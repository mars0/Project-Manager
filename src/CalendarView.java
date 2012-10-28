import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JTable;
import javax.swing.table.*;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.awt.*;

public class CalendarView {

	private JFrame frmProjectCalendar;
	private ProjectCalendar delegate;
	private JTable table;
	private TableColumn tColumn;
	private DefaultTableModel tableModel;
	private JLabel lblMonth;
	private JLabel lblYear;
	private JSplitPane splitPane;
	private JButton buttonPrev;
	private JButton buttonNext;
	private JScrollPane scrollPane;
	private JSplitPane splitPane_1;
	private JButton btnAdd;
	private JButton btnDelete;
	private JLabel lblHolidays;
	private JButton btnSetStartDate;
	private JLabel lblStartDate;

	/**
	 * Create the application.
	 */
	public CalendarView(ProjectCalendar del) {
		delegate = del;
		initialize();
		frmProjectCalendar.setVisible(true);
	}
	
	public JLabel getLblMonth() {
		return lblMonth;
	}

	public JLabel getLblYear() {
		return lblYear;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}
	
	public void openWindow() {
		frmProjectCalendar.setVisible(true);	
	}
	
	public void renderCell() {
		for (int i=0; i<7; i++) {
		 tColumn = table.getColumnModel().getColumn(i);
		 tColumn.setCellRenderer(new CustomTableCellRenderer(delegate));
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmProjectCalendar = new JFrame();
		frmProjectCalendar.setTitle("Calendar");
		frmProjectCalendar.setBounds(100, 100, 432, 298);
		frmProjectCalendar.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(32dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(26dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(8dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(76dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(0dlu;default)"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(18dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(99dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(11dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(11dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		tableModel = new DefaultTableModel(); 
		tableModel.addColumn("Mon"); 
		tableModel.addColumn("Tue");
		tableModel.addColumn("Wed");
		tableModel.addColumn("Thu");
		tableModel.addColumn("Fri");
		tableModel.addColumn("Sat");
		tableModel.addColumn("Sun");
		
		lblMonth = new JLabel("Month");
		frmProjectCalendar.getContentPane().add(lblMonth, "4, 2");
		
		lblYear = new JLabel("Year");
		frmProjectCalendar.getContentPane().add(lblYear, "6, 2");
		
		splitPane = new JSplitPane();
		frmProjectCalendar.getContentPane().add(splitPane, "10, 2, center, center");
		
		buttonPrev = new JButton("<-");
		splitPane.setLeftComponent(buttonPrev);
		buttonPrev.setActionCommand("prevMonth");
		buttonPrev.addActionListener(delegate);
		
		buttonNext = new JButton("->");
		splitPane.setRightComponent(buttonNext);
		buttonNext.setActionCommand("nextMonth");
		buttonNext.addActionListener(delegate);
		
		scrollPane = new JScrollPane();
		frmProjectCalendar.getContentPane().add(scrollPane, "4, 4, 7, 1, fill, fill");
		
		table = new JTable(tableModel);
		scrollPane.setViewportView(table);
		table.setCellSelectionEnabled(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		lblStartDate = new JLabel("Start Date:");
		frmProjectCalendar.getContentPane().add(lblStartDate, "4, 6");
		
		lblHolidays = new JLabel("Holidays:");
		frmProjectCalendar.getContentPane().add(lblHolidays, "10, 6");
		
		btnSetStartDate = new JButton("Set");
		frmProjectCalendar.getContentPane().add(btnSetStartDate, "4, 8");
		
		splitPane_1 = new JSplitPane();
		frmProjectCalendar.getContentPane().add(splitPane_1, "10, 8, center, center");
		
		btnAdd = new JButton("Add");
		splitPane_1.setLeftComponent(btnAdd);
		btnAdd.addActionListener(delegate);
		
		btnDelete = new JButton("Delete");
		splitPane_1.setRightComponent(btnDelete);
		btnDelete.addActionListener(delegate);
	}
	
	public class CustomTableCellRenderer extends DefaultTableCellRenderer{
		private static final long serialVersionUID = 1L;
		private ProjectCalendar delegate;
		
		public CustomTableCellRenderer(ProjectCalendar del) {
			super();
			delegate = del;
		}
		
		public Component getTableCellRendererComponent (JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
	  	Component cell = super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
	  	String value = obj.toString();
	  	boolean isHoliday = delegate.isHoliday(value);
	  	if (isHoliday) {
	  		cell.setBackground(Color.red);
	  	}
	  	else if (isSelected && value.length() > 0) {
	  		cell.setBackground(Color.blue);
  	  } 
	  	else {
	  		cell.setBackground(Color.white);
	  	}
	  	return cell;
		}
		}

}
