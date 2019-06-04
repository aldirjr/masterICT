package WSU_Enrolment;
//Importing java library
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
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

public class CheckUnits extends JFrame {
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
	private JButton btnCancel;
	private JTextField textLocation;
	private JLabel lblLocation;
	private JLabel lblStudentsEnrolled;
	private JTextField textSEnrolled;

	/**
	 * Create the frame.
	 */
	//Constructor
	public CheckUnits() {
		initComponents();
	}

	//Load the comboBox containing the Units available
	public void loadCombo() {
		try {
			//Connect To database
			DAO dao = new DAO();  
			ResultSet rs = null;
			String[] temp = new String[50];  
			int i=0, totalItems = 0;
			dao.connect();           
			//Update the Student Board and News Board
			rs = dao.getResultSet("SELECT DISTINCT unitID, groupId, unitName FROM ESU_Unit GROUP BY unitID, groupId, unitName");
			while(rs.next()){
				temp[i] = rs.getString("unitID") +"-"+rs.getString("groupId")+"-"+rs.getString("unitName");
				i++;
			}
			totalItems = i;
			unitAndId = new String[i+1];
			unitAndId[0] = "Select...";

			for(int j=0;j<i;j++) {
				unitAndId[j+1]=temp[j];
			}  
			//Close Connection
			dao.closeConnection();
			for (int l=0;l<(totalItems+1);l++) {
				comboBoxSelected.addItem(unitAndId[l]);
			}
		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
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
		textSEnrolled.setText("");
	}
	// Update Board data as per user request like insert update and delete
	public void updateBoard(String command) {

		DAO dao = new DAO();
		ResultSet rs = null;
		try {
			dao.connect();
			rs = dao.getResultSet("SELECT unitID,groupId,unitName,credits,requirement,GPAmin,professorName,studentsEnrolled,location,weekDay,hourStart,hourEnd FROM ESU_Unit WHERE unitID = "+unitSelected+" AND groupId ="+groupSelected+"");
			if (rs.next()) {
				textCode.setText(rs.getString("unitId"));
				textGroup.setText(rs.getString("groupId"));
				textName.setText(rs.getString("unitName"));
				textCredits.setText(rs.getString("credits"));
				textRequirement.setText(rs.getString("requirement"));
				textGPA.setText(rs.getString("GPAmin"));
				textProfessor.setText(rs.getString("professorName"));
				comboBoxSchedule.setSelectedItem(rs.getString("weekDay"));
				textHStarts.setText(rs.getString("hourStart"));
				textHFinishes.setText(rs.getString("hourEnd"));
				textLocation.setText(rs.getString("location"));
				textSEnrolled.setText(rs.getString("studentsEnrolled"));
			}
			//Close Connection
			dao.closeConnection();
		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}
	// Cancel edit screen
	public void goBack() {
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

		setBounds(100, 100, 420, 410);
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
		textCode.setEditable(false);
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
		textCredits.setEditable(false);
		textCredits.setColumns(10);
		textCredits.setBounds(243, 227, 31, 26);
		contentPane.add(textCredits);

		btnCancel = new JButton("OK");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});
		btnCancel.setBounds(309, 339, 93, 29);
		contentPane.add(btnCancel);

		comboBoxSelected = new JComboBox<String>();

		loadCombo();

		comboBoxSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				unitSelected = "";
				groupSelected = "";
				switch (comboBoxSelected.getSelectedIndex()) {
				case 0:
					clearFields();
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

		JLabel lblName = new JLabel("Name");
		lblName.setBounds(135, 110, 61, 16);
		contentPane.add(lblName);

		textName = new JTextField();
		textName.setEditable(false);
		textName.setColumns(10);
		textName.setBounds(125, 128, 277, 26);
		contentPane.add(textName);

		JLabel lblMinGpa = new JLabel("min GPA:");
		lblMinGpa.setBounds(131, 166, 61, 16);
		contentPane.add(lblMinGpa);

		textGPA = new JTextField();
		textGPA.setEditable(false);
		textGPA.setColumns(10);
		textGPA.setBounds(194, 161, 31, 26);
		contentPane.add(textGPA);

		JLabel lblUnitRequirement = new JLabel("Requirement:");
		lblUnitRequirement.setBounds(226, 166, 86, 16);
		contentPane.add(lblUnitRequirement);

		textRequirement = new JTextField();
		textRequirement.setEditable(false);
		textRequirement.setColumns(10);
		textRequirement.setBounds(316, 161, 86, 26);
		contentPane.add(textRequirement);

		JLabel lblProfessor = new JLabel("Professor");
		lblProfessor.setBounds(27, 210, 61, 16);
		contentPane.add(lblProfessor);

		comboBoxSchedule = new JComboBox<String>();
		comboBoxSchedule.setEnabled(false);
		comboBoxSchedule.setModel(new DefaultComboBoxModel<String>(new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"}));
		comboBoxSchedule.setBounds(17, 294, 145, 27);
		contentPane.add(comboBoxSchedule);

		JLabel lblSchedule = new JLabel("Schedule");
		lblSchedule.setBounds(27, 277, 61, 16);
		contentPane.add(lblSchedule);

		JLabel lblHourStart = new JLabel("Hour Starts");
		lblHourStart.setBounds(184, 277, 83, 16);
		contentPane.add(lblHourStart);

		textHStarts = new JTextField();
		textHStarts.setEditable(false);
		textHStarts.setColumns(10);
		textHStarts.setBounds(174, 295, 93, 26);
		contentPane.add(textHStarts);

		JLabel lblHourFinish = new JLabel("Hour Finishes");
		lblHourFinish.setBounds(279, 277, 90, 16);
		contentPane.add(lblHourFinish);

		textHFinishes = new JTextField();
		textHFinishes.setEditable(false);
		textHFinishes.setColumns(10);
		textHFinishes.setBounds(276, 295, 93, 26);
		contentPane.add(textHFinishes);

		textGroup = new JTextField();
		textGroup.setEditable(false);
		textGroup.setColumns(10);
		textGroup.setBounds(80, 161, 31, 26);
		contentPane.add(textGroup);

		JLabel lblGroup = new JLabel("Group:");
		lblGroup.setBounds(27, 166, 49, 16);
		contentPane.add(lblGroup);

		textProfessor = new JTextField();
		textProfessor.setEditable(false);
		textProfessor.setBounds(17, 227, 200, 26);
		contentPane.add(textProfessor);
		textProfessor.setColumns(10);

		textLocation = new JTextField();
		textLocation.setEditable(false);
		textLocation.setColumns(10);
		textLocation.setBounds(314, 227, 44, 26);
		contentPane.add(textLocation);

		lblLocation = new JLabel("Location:");
		lblLocation.setBounds(309, 210, 60, 16);
		contentPane.add(lblLocation);

		lblStudentsEnrolled = new JLabel("Students Enrolled:");
		lblStudentsEnrolled.setBounds(27, 344, 120, 16);
		contentPane.add(lblStudentsEnrolled);

		textSEnrolled = new JTextField();
		textSEnrolled.setEditable(false);
		textSEnrolled.setColumns(10);
		textSEnrolled.setBounds(150, 339, 38, 26);
		contentPane.add(textSEnrolled);

	}
}
