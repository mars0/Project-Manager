import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;


public class MaxResourceSetter extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private ResourceManager delegate;
	
	public void openWindow(String currentLimits) {
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		textField.setText(currentLimits);
	}
	
	public JTextField getTextField() {
		return this.textField;
	}
	
	public void close() {
		this.setVisible(false);
		this.dispose();
	}

	/**
	 * Create the dialog.
	 */
	public MaxResourceSetter(ResourceManager del) {
		this.delegate = del;
		setTitle("Set Max. Available Resources");
		setBounds(100, 100, 260, 139);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(23, 44, 214, 28);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblModifyResourceLimits = new JLabel("Modify Resource Limits here:");
		lblModifyResourceLimits.setBounds(36, 16, 187, 16);
		contentPanel.add(lblModifyResourceLimits);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(delegate);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(delegate);
				buttonPane.add(cancelButton);
			}
		}
	}
}
