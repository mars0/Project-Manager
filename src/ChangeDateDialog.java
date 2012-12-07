import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;


public class ChangeDateDialog extends JDialog implements ActionListener{

	private final JPanel contentPanel = new JPanel();
	private Map<String,Integer> oldStarts;
	private ProjectCalendar delegate;

	/**
	 * Create the dialog.
	 */
	public ChangeDateDialog(ProjectCalendar del, int offset, boolean feasable, Map<String,Integer> oldStarts) {
		setTitle("Save Changes?");
		this.delegate = del;
		this.oldStarts = oldStarts;
		setBounds(100, 100, 372, 170);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblAreYouSure = new JLabel("Are you sure that you want to save the changes?");
		lblAreYouSure.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		lblAreYouSure.setBounds(17, 16, 333, 16);
		contentPanel.add(lblAreYouSure);
		
		JLabel lblChangesInProject = new JLabel("Changes in project duration: ");
		lblChangesInProject.setBounds(17, 44, 191, 16);
		contentPanel.add(lblChangesInProject);
		
		JLabel lblFeasableForResource = new JLabel("Respecting resource limits: ");
		lblFeasableForResource.setBounds(17, 72, 190, 16);
		contentPanel.add(lblFeasableForResource);
		
		String offsetS = ((Integer)offset).toString();
		if (offset > 0) 
			offsetS = "+" + offsetS;
		else if (offset == 0) 
			offsetS = "-";
		JLabel lblOffset = new JLabel(offsetS);
		lblOffset.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblOffset.setBounds(210, 44, 61, 16);
		contentPanel.add(lblOffset);
		
		String answer;
		if (feasable) answer = "YES";
		else answer = "NO";
		JLabel lblFeasable = new JLabel(answer);
		lblFeasable.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblFeasable.setBounds(210, 72, 61, 16);
		contentPanel.add(lblFeasable);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("changeOK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("changeCancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("changeOK".equals(e.getActionCommand())) {
			
		}
		else if ("changeCancel".equals(e.getActionCommand())) {
			delegate.revertChanges(oldStarts);
		}
		this.dispose();
	}
}
