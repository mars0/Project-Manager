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
	private JSplitPane splitPane;
	private JButton buttonPrev;
	private JButton buttonNext;
	private JScrollPane scrollPane;
	private JSplitPane splitPane_1;
	private JButton btnAdd;
	private JButton btnDelete;
	private JLabel lblHolidays;
	private JButton btnStartDate;
	private JLabel lblStartDate;
	private JSplitPane splitPane_2;
	private JButton btnShow;

	/**
	 * Create the application.
	 */
	public CalendarView(ProjectCalendar del) {
		delegate = del;
		initialize();
		frmProjectCalendar.setVisible(true);
	}
	
	public JTable getTable() {
		return table;
	}

	public JLabel getLblMonth() {
		return lblMonth;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}
	
	public void openWindow() {
		frmProjectCalendar.setVisible(true);	
	}
	
	public void closeWindow() {
		frmProjectCalendar.setVisible(false);
	}
	
	public void renderCell() {
		for (int i=0; i<7; i++) {
		 tColumn = table.getColumnModel().getColumn(i);
		 CustomTableCellRenderer ctcr = new CustomTableCellRenderer(delegate);
		 ctcr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		 tColumn.setCellRenderer(ctcr);;
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmProjectCalendar = new JFrame();
		frmProjectCalendar.setTitle("Calendar");
		frmProjectCalendar.setBounds(100, 100, 411, 307);
		frmProjectCalendar.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(9dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(32dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(76dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(9dlu;default)"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(18dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(105dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(11dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(11dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(16dlu;default)"),}));
		
		String[] colText = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
		tableModel = new DefaultTableModel(null, colText) {
	    @Override
	    public boolean isCellEditable(int row, int column) {
	       //all cells false
	       return false;
	    }
		};
				
		lblMonth = new JLabel("");
		frmProjectCalendar.getContentPane().add(lblMonth, "3, 2, center, default");
		
		splitPane = new JSplitPane();
		frmProjectCalendar.getContentPane().add(splitPane, "5, 2, left, center");
		
		buttonPrev = new JButton("<<");
		splitPane.setLeftComponent(buttonPrev);
		buttonPrev.setActionCommand("prevMonth");
		buttonPrev.addActionListener(delegate);
		
		buttonNext = new JButton(">>");
		splitPane.setRightComponent(buttonNext);
		buttonNext.setActionCommand("nextMonth");
		buttonNext.addActionListener(delegate);
		
		scrollPane = new JScrollPane();
		frmProjectCalendar.getContentPane().add(scrollPane, "3, 4, 3, 1, fill, fill");
		
		table = new JTable(tableModel);
		scrollPane.setViewportView(table);
		table.setCellSelectionEnabled(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(25);
		table.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		
		lblStartDate = new JLabel("Start Date:");
		frmProjectCalendar.getContentPane().add(lblStartDate, "3, 6");
		
		lblHolidays = new JLabel("Holidays:");
		frmProjectCalendar.getContentPane().add(lblHolidays, "5, 6");
		
		splitPane_2 = new JSplitPane();
		frmProjectCalendar.getContentPane().add(splitPane_2, "3, 8, left, fill");
		
		btnStartDate = new JButton("Set");
		splitPane_2.setLeftComponent(btnStartDate);
		btnStartDate.setActionCommand("setStartDate");
		
		btnShow = new JButton("Show");
		splitPane_2.setRightComponent(btnShow);
		btnStartDate.addActionListener(delegate);
		btnShow.setActionCommand("showStart");
		btnShow.addActionListener(delegate);
		
		splitPane_1 = new JSplitPane();
		frmProjectCalendar.getContentPane().add(splitPane_1, "5, 8, left, center");
		
		btnAdd = new JButton("Add");
		splitPane_1.setLeftComponent(btnAdd);
		btnAdd.setActionCommand("addHoliday");
		btnAdd.addActionListener(delegate);
		
		btnDelete = new JButton("Del");
		splitPane_1.setRightComponent(btnDelete);
		btnDelete.setActionCommand("deleteHoliday");
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
	  	if (value.length() > 0) {
	  		if (delegate.isHoliday(value, null)) {
	  			cell.setBackground(Color.red);
	  		}
	  		else if (delegate.isStartDate(value)) {
	  			cell.setBackground(Color.green);
	  		}
	  		else if (delegate.isEndDate(value)) {
	  			cell.setBackground(Color.magenta);
	  		}
	  		else if (isSelected && !delegate.isDisabledDate(value)) {
	  			cell.setBackground(Color.blue);
	  		} 
	  		else
	  			cell.setBackground(Color.white);
	  		// check disable invalid start dates
	  		if (delegate.isDisabledDate(value)) {
	  			Font newFont = new Font(cell.getFont().getName(),Font.ITALIC,cell.getFont().getSize());
	  			cell.setFont(newFont);
	  			cell.setForeground(Color.lightGray);
	  		}
	  		else {
	  			cell.setForeground(Color.black);
	  		}
	  	}
	  	else {
	  		cell.setBackground(Color.white);
	  	}
	  	return cell;
		}
		}

}
