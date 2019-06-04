package WSU_Enrolment;
//Importing java library
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;

//Import Dao File
import WSU_Enrolment.DAO;

public class ProfileStudent extends JFrame {
	//Unique identifier for class
	private static final long serialVersionUID = 1L;
	// Variables declaration
	private JPanel contentPane;
	private User currentUser; 	 //User logged in the system
	private JFrame pMenu; 		 //previous panel - menu.java
	private JTextField textUsername;
	private JTextField textName;
	private JTextField textSex;
	private JTextField textCity;
	private JTextField textEmail;
	private JTextField textGPA;
	private JTextField textStartDate;
	private JTextField textCredits;
	private JTextField textSurname;
	private JTextField textBirthdate;
	private JTextField textState;
	private JTextField textCountry;
	private JTextField textCourse;
	private JButton btnSave;
	private JButton btnEdit;
	private JLabel lblPassword;
	private JPasswordField passwordField;
	private String currentPassword;

	// Constructor
	public ProfileStudent(JFrame menu, User currentUser) {
		this.currentUser = currentUser;
		// Load Frame
		initComponents();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Centralize screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		// Set Menu
		pMenu = menu;
		menu.setVisible(false);
		this.setVisible(true);
		//Load Student Information
		loadStudent(currentUser.username);

	}
	// Enable edit function for student details update
	private void editInformation(){
		// Confirm User Password
		String inputValue = JOptionPane.showInputDialog("Please insert your password");
		if (inputValue.equals(currentPassword)) {
			enableFields();
		}
		else {
			// Pop for Incorrect Password
			JOptionPane.showMessageDialog(this,"Your password does not matches!","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	// Enable Editable fields
	private void enableFields() {
		//textUsername.setEnabled(true);
		passwordField.setEnabled(true);
		textEmail.setEnabled(true);
		btnSave.setEnabled(true);
		btnEdit.setEnabled(false);
	}
	// Update Student table from text box
	private void updateStudent(){
		try {
			// Connect Database
			DAO dao = new DAO();
			dao.connect();
			String newPassword = new String(passwordField.getPassword());
			// Authenticate the user
			if (!currentPassword.equals(newPassword)) {
				String inputValue = JOptionPane.showInputDialog("Please confirm the new password");
				if (inputValue.equals(newPassword)) {
					dao.executeSQL("UPDATE ESU_User SET password ='"+newPassword+"' WHERE username = '"+currentUser.username+"'");
					dao.executeSQL("UPDATE ESU_Students_Data SET email='"+textEmail.getText()+"' WHERE username = '"+currentUser.username+"'");
					// Display Confirmation Message
					JOptionPane.showMessageDialog(this,"Your information was updated","Confirmation",JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					// Display Error Message
					JOptionPane.showMessageDialog(this,"Your new password does not matches!","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
			else {
				dao.executeSQL("UPDATE ESU_Students_Data SET email='"+textEmail.getText()+"' WHERE username = '"+currentUser.username+"'");
				// Display Confirmation Message
				JOptionPane.showMessageDialog(this,"Your information was updated","Confirmation",JOptionPane.INFORMATION_MESSAGE);
			}
			dao.closeConnection();
		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}
	//Load Student Details
	private void loadStudent(String studentId){
		try {
			//Connect Database
			DAO dao = new DAO();
			ResultSet rs1 = null, rs2 = null;
			dao.connect();
			//get Units Student Enrolled
			rs1 = dao.getResultSet("SELECT password FROM ESU_User WHERE username='"+studentId+"'");
			rs2 = dao.getResultSet("SELECT username,name,surname,birthday,sex,city,state,country,email,course,GPA,credits,startDate FROM ESU_Students_Data WHERE username='"+studentId+"'");
			if(rs2.next() && rs1.next()){
				currentPassword = rs1.getString("password");
				passwordField.setText(currentPassword);

				textUsername.setText(rs2.getString("username"));
				textName.setText(rs2.getString("name"));
				textSurname.setText(rs2.getString("surname"));
				textBirthdate.setText(rs2.getString("birthday"));
				textSex.setText(rs2.getString("sex"));
				textCity.setText(rs2.getString("city"));
				textState.setText(rs2.getString("state"));
				textCountry.setText(rs2.getString("country"));
				textEmail.setText(rs2.getString("email"));
				textGPA.setText(rs2.getString("GPA"));
				textCredits.setText(rs2.getString("credits"));
				textStartDate.setText(rs2.getString("startDate"));
				textCourse.setText(rs2.getString("course"));
			}
			// Close Database Connection
			dao.closeConnection();
		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}
	// Enable Enrollment frame
	public void goEnrolment() {
		this.setVisible(false);
		JFrame enrolmentScreen = new Enrolment(this, currentUser);
		enrolmentScreen.setVisible(true);
	}
	// GO for previous menu
	public void goBackMenu() {
		this.dispose();
		pMenu.setVisible(true);
	}
	// Logout The current user and redirect them to login page
	public void logoff() {
		this.dispose();
		JFrame login=new Login();
		login.setVisible(true);
	}

	/** This method is called from within the constructor to
	 * initialize the Frame.
	 */
	public void initComponents() {

		setBounds(100, 100, 778, 562);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblEnrolment = new JLabel();
		lblEnrolment.setText("PROFILE");
		lblEnrolment.setFont(new Font("Dialog", Font.PLAIN, 36));

		JLabel lblUnitsEnroled = new JLabel("Your Information");
		lblUnitsEnroled.setHorizontalAlignment(SwingConstants.CENTER);
		lblUnitsEnroled.setForeground(new Color(0, 0, 139));
		lblUnitsEnroled.setFont(new Font("Lucida Grande", Font.ITALIC, 16));

		JPanel panelEnrolment = new JPanel();
		panelEnrolment.setPreferredSize(new Dimension(778, 478));
		panelEnrolment.setBackground(Color.LIGHT_GRAY);
		panelEnrolment.setBounds(0, 0, 778, 478);
		contentPane.add(panelEnrolment);

		JButton btnLogout = new JButton();
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logoff();
			}
		});
		btnLogout.setText("LOGOUT");

		JButton btnHome = new JButton("Back");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBackMenu();
			}
		});

		JButton btnProfile = new JButton("Profile");
		btnProfile.setEnabled(false);

		JButton btnEnrolment = new JButton("Enrolment");
		btnEnrolment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goEnrolment();
			}
		});

		JButton btnUnits = new JButton("Units");
		btnUnits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame checkUnits = new CheckUnits();
				checkUnits.setVisible(true);
			}
		});

		JPanel panel = new JPanel();

		GroupLayout gl_panelEnrolment = new GroupLayout(panelEnrolment);
		gl_panelEnrolment.setHorizontalGroup(
				gl_panelEnrolment.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelEnrolment.createSequentialGroup()
						.addGroup(gl_panelEnrolment.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelEnrolment.createSequentialGroup()
										.addGap(21)
										.addGroup(gl_panelEnrolment.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panelEnrolment.createSequentialGroup()
														.addComponent(lblEnrolment, GroupLayout.PREFERRED_SIZE, 276, GroupLayout.PREFERRED_SIZE)
														.addGap(314)
														.addComponent(btnLogout))
												.addGroup(gl_panelEnrolment.createSequentialGroup()
														.addGroup(gl_panelEnrolment.createParallelGroup(Alignment.LEADING, false)
																.addComponent(btnHome, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(btnProfile, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(btnEnrolment, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(btnUnits, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
														.addGap(57)
														.addComponent(panel, GroupLayout.PREFERRED_SIZE, 533, GroupLayout.PREFERRED_SIZE))))
								.addGroup(gl_panelEnrolment.createSequentialGroup()
										.addGap(346)
										.addComponent(lblUnitsEnroled, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(59, Short.MAX_VALUE))
				);
		gl_panelEnrolment.setVerticalGroup(
				gl_panelEnrolment.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelEnrolment.createSequentialGroup()
						.addGap(30)
						.addGroup(gl_panelEnrolment.createParallelGroup(Alignment.LEADING)
								.addComponent(btnLogout)
								.addComponent(lblEnrolment))
						.addGap(12)
						.addComponent(lblUnitsEnroled, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panelEnrolment.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelEnrolment.createSequentialGroup()
										.addComponent(btnHome)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnProfile)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnEnrolment)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnUnits))
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE)))
				);
		panel.setLayout(null);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsername.setBounds(23, 30, 72, 16);
		panel.add(lblUsername);

		JLabel lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(23, 69, 72, 16);
		panel.add(lblName);

		JLabel lblSurname = new JLabel("Surname");
		lblSurname.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSurname.setBounds(236, 69, 61, 16);
		panel.add(lblSurname);

		JLabel lblBirthdate = new JLabel("Birthdate");
		lblBirthdate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBirthdate.setBounds(236, 112, 61, 16);
		panel.add(lblBirthdate);

		JLabel lblSex = new JLabel("Sex");
		lblSex.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSex.setBounds(23, 112, 72, 16);
		panel.add(lblSex);

		JLabel lblCity = new JLabel("City");
		lblCity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCity.setBounds(34, 150, 61, 16);
		panel.add(lblCity);

		JLabel lblState = new JLabel("State");
		lblState.setHorizontalAlignment(SwingConstants.RIGHT);
		lblState.setBounds(236, 150, 61, 16);
		panel.add(lblState);

		JLabel lblCountry = new JLabel("Country");
		lblCountry.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCountry.setBounds(362, 150, 61, 16);
		panel.add(lblCountry);

		JLabel lblEmail = new JLabel("E-mail");
		lblEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmail.setBounds(34, 190, 61, 16);
		panel.add(lblEmail);

		JLabel lblNewLabel = new JLabel("GPA");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(34, 231, 61, 16);
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Credits");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setBounds(236, 231, 61, 16);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Start date");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2.setBounds(34, 269, 61, 16);
		panel.add(lblNewLabel_2);

		textUsername = new JTextField();
		textUsername.setEnabled(false);
		textUsername.setBounds(107, 25, 117, 26);
		panel.add(textUsername);
		textUsername.setColumns(10);

		textName = new JTextField();
		textName.setEnabled(false);
		textName.setColumns(10);
		textName.setBounds(107, 64, 117, 26);
		panel.add(textName);

		textSex = new JTextField();
		textSex.setEnabled(false);
		textSex.setColumns(10);
		textSex.setBounds(107, 107, 117, 26);
		panel.add(textSex);

		textCity = new JTextField();
		textCity.setEnabled(false);
		textCity.setColumns(10);
		textCity.setBounds(107, 145, 117, 26);
		panel.add(textCity);

		textEmail = new JTextField();
		textEmail.setEnabled(false);
		textEmail.setColumns(10);
		textEmail.setBounds(107, 185, 316, 26);
		panel.add(textEmail);

		textGPA = new JTextField();
		textGPA.setEnabled(false);
		textGPA.setColumns(10);
		textGPA.setBounds(107, 226, 117, 26);
		panel.add(textGPA);

		textStartDate = new JTextField();
		textStartDate.setEnabled(false);
		textStartDate.setColumns(10);
		textStartDate.setBounds(107, 264, 117, 26);
		panel.add(textStartDate);

		textCredits = new JTextField();
		textCredits.setEnabled(false);
		textCredits.setColumns(10);
		textCredits.setBounds(309, 226, 52, 26);
		panel.add(textCredits);

		textSurname = new JTextField();
		textSurname.setEnabled(false);
		textSurname.setColumns(10);
		textSurname.setBounds(309, 64, 114, 26);
		panel.add(textSurname);

		textBirthdate = new JTextField();
		textBirthdate.setEnabled(false);
		textBirthdate.setColumns(10);
		textBirthdate.setBounds(309, 107, 114, 26);
		panel.add(textBirthdate);

		textState = new JTextField();
		textState.setEnabled(false);
		textState.setColumns(10);
		textState.setBounds(309, 145, 52, 26);
		panel.add(textState);

		textCountry = new JTextField();
		textCountry.setEnabled(false);
		textCountry.setColumns(10);
		textCountry.setBounds(435, 145, 72, 26);
		panel.add(textCountry);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateStudent();
			}
		});
		btnSave.setEnabled(false);
		btnSave.setBounds(429, 293, 98, 29);
		panel.add(btnSave);

		lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setBounds(236, 30, 61, 16);
		panel.add(lblPassword);

		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editInformation();
			}
		});
		btnEdit.setBounds(328, 293, 98, 29);
		panel.add(btnEdit);

		passwordField = new JPasswordField();
		passwordField.setEnabled(false);
		passwordField.setBounds(309, 27, 114, 21);
		panel.add(passwordField);

		JLabel lblCourse = new JLabel("Course");
		lblCourse.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCourse.setBounds(236, 269, 61, 16);
		panel.add(lblCourse);

		textCourse = new JTextField();
		textCourse.setEnabled(false);
		textCourse.setColumns(10);
		textCourse.setBounds(309, 264, 198, 26);
		panel.add(textCourse);
		panelEnrolment.setLayout(gl_panelEnrolment);

		JLabel label = new JLabel();
		label.setText("ATTENTION! IT IS TIME TO ENROL IN UNITS FOR THE NEXT SESSION!");
		label.setForeground(Color.RED);
		label.setBounds(319, 504, 432, 16);
		contentPane.add(label);

	}
}
