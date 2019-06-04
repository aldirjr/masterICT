package WSU_Enrolment;
//Importing java library
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class EditUnits extends JFrame {
	//Unique identifier for class
	private static final long serialVersionUID = 1L;
	//Variable Declaration
	private JPanel contentPane;
	private JComboBox<String> comboBoxSelected;
	private String unitSelected;
	private String groupSelected;
	private JTextField textCode;
	private JTextField textGroup;
	private JTextField textName;
	private JTextField textCredits;
	private JTextField textRequirement;
	private JTextField textGPA;
	private JTextField textProfessor;
	private JComboBox<String> comboBoxSchedule;
	private JTextField textHStarts;
	private JTextField textHFinishes;
	private String[] unitAndId;
	private JButton btnSave;
	private JButton btnCancel;
	private JLabel lblLocation;
	private JTextField textLocation;

	// Constructor
	public EditUnits() {
		initComponents();
	}

	// Fill The form on load
	public void fillForms() {
		updateBoard("load");
	}
	// Load unit details
	public void loadCombo() {		
		try {
			DAO dao = new DAO();  
			ResultSet rs = null;
			String[] temp = new String[50];  
			int i=0, totalItems = 0;
			int sizeBefore = comboBoxSelected.getItemCount();
			if (sizeBefore >0) {
				for (;sizeBefore>1;sizeBefore--) {
					comboBoxSelected.removeItemAt(sizeBefore-1);
				}
			}
			dao.connect();           
			//Update the Student Board and News Board
			rs = dao.getResultSet("SELECT DISTINCT unitID, groupId, unitName FROM ESU_Unit GROUP BY unitID, groupId, unitName");
			//rs.getRow();
			while(rs.next()){
				temp[i] = rs.getString("unitID") +"-"+rs.getString("groupId")+"-"+rs.getString("unitName");
				i++;
			}
			totalItems = i;
			unitAndId = new String[i+2];
			unitAndId[0] = "Select...";
			unitAndId[1] = "New";

			for(int j=0;j<i;j++) {
				unitAndId[j+2]=temp[j];
			}         
			dao.closeConnection();
			for (int l=0;l<(totalItems+2);l++) {
				comboBoxSelected.addItem(unitAndId[l]);
			}
			if (sizeBefore>0)
				comboBoxSelected.removeItemAt(1);

		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}
	// Disable All fields
	public void disableFields() {
		textCode.setEnabled(false);
		textGroup.setEnabled(false);
		textName.setEnabled(false);
		textCredits.setEnabled(false);
		textRequirement.setEnabled(false);
		textGPA.setEnabled(false);
		textProfessor.setEnabled(false);
		comboBoxSchedule.setEnabled(false);
		textHStarts.setEnabled(false);
		textHFinishes.setEnabled(false);
		textLocation.setEnabled(false);
	}
	// Enable all fields
	public void enableFields() {
		textCode.setEnabled(true);
		textGroup.setEnabled(true);
		textName.setEnabled(true);
		textCredits.setEnabled(true);
		textRequirement.setEnabled(true);
		textGPA.setEnabled(true);
		textProfessor.setEnabled(true);
		comboBoxSchedule.setEnabled(true);
		textHStarts.setEnabled(true);
		textHFinishes.setEnabled(true);
		textLocation.setEnabled(true);
	}
	// set empty value to all unit text field
	public void clearFields() {
		comboBoxSelected.setSelectedIndex(0);
		textCode.setText("");
		textGroup.setText("");
		textName.setText("");
		textCredits.setText("");
		textRequirement.setText("");
		textGPA.setText("");
		textProfessor.setText("");
		comboBoxSchedule.setSelectedIndex(0);
		textHStarts.setText("");
		textHFinishes.setText("");
		textLocation.setText("");
	}
	// Update Board data as per user request like insert update and delete
	public void updateBoard(String command) {
		try {
			//connect database
			DAO dao = new DAO();
			ResultSet rs = null;
			dao.connect();
			// delete the unit
			if (command.equals("delete")){
				if (textCode.getText().isEmpty() || textGroup.getText().isEmpty()) {
					JOptionPane.showMessageDialog(this,"The unit has not been set","Error",JOptionPane.ERROR_MESSAGE);
				}
				else {
					if (!dao.executeSQL("DELETE FROM ESU_Unit WHERE unitID="+unitSelected+" AND groupId = "+groupSelected+"")) {
						JOptionPane.showMessageDialog(this,"The unit and group chosen does not exists","Error",JOptionPane.ERROR_MESSAGE);
					}
					else {

						JOptionPane.showMessageDialog(this,"The unit has been deleted","Updated",JOptionPane.INFORMATION_MESSAGE);
						clearFields();
						disableFields();
						loadCombo();
					}
				}
			}
			else {
				//Create a new unit or update unit
				if (command.contentEquals("save")){
					//Save new unit information
					if (groupSelected.isEmpty()) {
						dao.executeSQL("INSERT INTO ESU_Unit (unitID,groupId,unitName,credits,requirement,GPAmin,professorname,studentsEnrolled,location,weekDay,hourStart,hourEnd)"
								+ " VALUES ("+textCode.getText()+","+textGroup.getText()+",'"+textName.getText()+"',"+textCredits.getText()+","+textRequirement.getText()+","
								+ ""+textGPA.getText()+",'"+textProfessor.getText()+"',0,"+textLocation.getText()+",'"+(String) comboBoxSchedule.getSelectedItem()+"','"+textHStarts.getText()+"','"+textHFinishes.getText()+"')");
						unitSelected = textCode.getText();
						groupSelected = textGroup.getText();
						loadCombo();
					}
					//Update unit information
					else{
						dao.executeSQL("UPDATE ESU_Unit SET unitID="+textCode.getText()+", groupId ="+textGroup.getText()+", unitName ='"+textName.getText()+"', credits ="+textCredits.getText()+""
								+ ", requirement ="+textRequirement.getText()+", GPAmin ="+textGPA.getText()+", professorName ='"+textProfessor.getText()+"', location = "+textLocation.getText()+""
								+ ", weekDay ='"+(String) comboBoxSchedule.getSelectedItem()+"'"
								+ ", hourStart ='"+textHStarts.getText()+"', hourEnd='"+textHFinishes.getText()+"' WHERE unitID="+unitSelected+" AND groupId = "+groupSelected+"");     
						JOptionPane.showMessageDialog(this,"The unit has been updated","Updated",JOptionPane.INFORMATION_MESSAGE);

					}
				}
				//Load unit information
				else
				{
					rs = dao.getResultSet("SELECT unitID,groupId,unitName,credits,requirement,GPAmin,professorname,location,weekDay,hourStart,hourEnd FROM ESU_Unit WHERE unitID = "+unitSelected+" AND groupId ="+groupSelected+"");
					if (rs.next()) {
						textCode.setText(rs.getString("unitId"));
						textGroup.setText(rs.getString("groupId"));
						textName.setText(rs.getString("unitName"));
						textCredits.setText(rs.getString("credits"));
						textRequirement.setText(rs.getString("requirement"));
						textGPA.setText(rs.getString("GPAmin"));
						textProfessor.setText(rs.getString("professorName"));
						textLocation.setText(rs.getString("location"));
						comboBoxSchedule.setSelectedItem(rs.getString("weekDay"));
						textHStarts.setText(rs.getString("hourStart"));
						textHFinishes.setText(rs.getString("hourEnd"));
						enableFields();
					}
				}
			}
			// Close the database connection
			dao.closeConnection();
		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}
	// Cancel edit screen
	public void cancelEdit() {
		this.dispose();
	}

	/** This method is called from within the constructor to
	 * initialize the Frame.
	 */
	public void initComponents() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		//Centralize screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

		setBounds(100, 100, 420, 430);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUnits = new JLabel();
		lblUnits.setHorizontalAlignment(SwingConstants.CENTER);
		lblUnits.setText("Units");
		lblUnits.setFont(new Font("Dialog", Font.PLAIN, 36));
		lblUnits.setBounds(99, 6, 188, 43);
		contentPane.add(lblUnits);

		textCode = new JTextField();
		textCode.setEnabled(false);
		textCode.setColumns(10);
		textCode.setBounds(17, 128, 93, 26);
		contentPane.add(textCode);

		JLabel lblCode = new JLabel("Code");
		lblCode.setBounds(27, 110, 61, 16);
		contentPane.add(lblCode);

		JLabel lblCredits = new JLabel("Credits:");
		lblCredits.setBounds(238, 210, 49, 16);
		contentPane.add(lblCredits);

		textCredits = new JTextField();
		textCredits.setEnabled(false);
		textCredits.setColumns(10);
		textCredits.setBounds(243, 227, 31, 26);
		contentPane.add(textCredits);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelEdit();
			}
		});
		btnCancel.setBounds(194, 367, 93, 29);
		contentPane.add(btnCancel);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateBoard("save");
			}
		});
		btnSave.setBounds(290, 367, 93, 29);
		contentPane.add(btnSave);

		comboBoxSelected = new JComboBox<String>();

		loadCombo();

		comboBoxSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				unitSelected = "";
				groupSelected = "";
				switch (comboBoxSelected.getSelectedIndex()) {
				case 0:
					break;
				case 1:
					enableFields();
					break;
				default:
					String[] tokens = ((String) comboBoxSelected.getSelectedItem()).split("-",5);
					unitSelected = tokens[0];
					groupSelected = tokens[1];
					updateBoard("load");
					break;
				}

			}
		});
		comboBoxSelected.setBounds(17, 61, 385, 27);
		contentPane.add(comboBoxSelected);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateBoard("delete");
			}
		});
		btnDelete.setBounds(99, 367, 93, 29);
		contentPane.add(btnDelete);

		JLabel lblName = new JLabel("Name");
		lblName.setBounds(135, 110, 61, 16);
		contentPane.add(lblName);

		textName = new JTextField();
		textName.setEnabled(false);
		textName.setColumns(10);
		textName.setBounds(125, 128, 277, 26);
		contentPane.add(textName);

		JLabel lblMinGpa = new JLabel("min GPA:");
		lblMinGpa.setBounds(131, 166, 61, 16);
		contentPane.add(lblMinGpa);

		textGPA = new JTextField();
		textGPA.setEnabled(false);
		textGPA.setColumns(10);
		textGPA.setBounds(194, 161, 31, 26);
		contentPane.add(textGPA);

		JLabel lblUnitRequirement = new JLabel("Requirement:");
		lblUnitRequirement.setBounds(226, 166, 86, 16);
		contentPane.add(lblUnitRequirement);

		textRequirement = new JTextField();
		textRequirement.setEnabled(false);
		textRequirement.setColumns(10);
		textRequirement.setBounds(316, 161, 86, 26);
		contentPane.add(textRequirement);

		JLabel lblProfessor = new JLabel("Professor");
		lblProfessor.setBounds(27, 210, 61, 16);
		contentPane.add(lblProfessor);

		comboBoxSchedule = new JComboBox<String>();
		comboBoxSchedule.setEnabled(false);
		comboBoxSchedule.setModel(new DefaultComboBoxModel<String>(new String[] {"Select...", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"}));
		comboBoxSchedule.setBounds(17, 294, 145, 27);
		contentPane.add(comboBoxSchedule);

		JLabel lblSchedule = new JLabel("Schedule");
		lblSchedule.setBounds(27, 277, 61, 16);
		contentPane.add(lblSchedule);

		JLabel lblHourStart = new JLabel("Hour Starts");
		lblHourStart.setBounds(184, 277, 83, 16);
		contentPane.add(lblHourStart);

		textHStarts = new JTextField();
		textHStarts.setEnabled(false);
		textHStarts.setColumns(10);
		textHStarts.setBounds(174, 295, 93, 26);
		contentPane.add(textHStarts);

		JLabel lblHourFinish = new JLabel("Hour Finishes");
		lblHourFinish.setBounds(279, 277, 90, 16);
		contentPane.add(lblHourFinish);

		textHFinishes = new JTextField();
		textHFinishes.setEnabled(false);
		textHFinishes.setColumns(10);
		textHFinishes.setBounds(276, 295, 93, 26);
		contentPane.add(textHFinishes);

		textGroup = new JTextField();
		textGroup.setEnabled(false);
		textGroup.setColumns(10);
		textGroup.setBounds(80, 161, 31, 26);
		contentPane.add(textGroup);

		JLabel lblGroup = new JLabel("Group:");
		lblGroup.setBounds(27, 166, 49, 16);
		contentPane.add(lblGroup);

		textProfessor = new JTextField();
		textProfessor.setEnabled(false);
		textProfessor.setBounds(17, 227, 200, 26);
		contentPane.add(textProfessor);
		textProfessor.setColumns(10);

		lblLocation = new JLabel("Location:");
		lblLocation.setBounds(316, 210, 67, 16);
		contentPane.add(lblLocation);

		textLocation = new JTextField();
		textLocation.setEnabled(false);
		textLocation.setColumns(10);
		textLocation.setBounds(320, 227, 41, 26);
		contentPane.add(textLocation);

	}
}
