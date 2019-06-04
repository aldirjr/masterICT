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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

//Import Dao File
import WSU_Enrolment.DAO;

public class Profile extends JFrame {
	//Unique identifier for class
	private static final long serialVersionUID = 1L;
	// Variables declaration
	private JPanel contentPane;
	private JTextField textStudentId; //UnitId
	private User currentUser; 	 //User logged in the system
	private JFrame pMenu; 		 //previous panel - menu.java
	private JTextField textUsername;
	private JTextField textName;
	private JTextField textSex;
	private JTextField textCity;
	private JTextField textEmail;
	private JTextField textGPA;
	private JTextField textCourse;
	private JTextField textStartDate;
	private JTextField textCredits;
	private JTextField textSurname;
	private JTextField textBirthdate;
	private JTextField textState;
	private JTextField textCountry;
	private JTextField textPassword;

	private String currentUsername;

	private JButton btnSave;
	private JLabel lblPassword;

	// Constructor
	public Profile(JFrame menu, User currentUser) {
		this.currentUser = currentUser;
		initComponents();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Centralize screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

		pMenu = menu;
		//menu.setVisible(false);
		//this.setVisible(true);
	}
	// Clear All TextField
	private void clearFields() {
		textUsername.setText("");
		textName.setText("");
		textSex.setText("");
		textCity.setText("");
		textEmail.setText("");
		textGPA.setText("");
		textStartDate.setText("");
		textCredits.setText("");
		textSurname.setText("");
		textBirthdate.setText("");
		textState.setText("");
		textCountry.setText("");
		textCourse.setText("");
	}
	// Enable All text fields
	private void enableFields() {
		textUsername.setEnabled(true);
		textName.setEnabled(true);
		textSex.setEnabled(true);
		textCity.setEnabled(true);
		textEmail.setEnabled(true);
		textGPA.setEnabled(true);
		textStartDate.setEnabled(true);
		textCredits.setEnabled(true);
		textSurname.setEnabled(true);
		textBirthdate.setEnabled(true);
		textState.setEnabled(true);
		textCountry.setEnabled(true);
		btnSave.setEnabled(true);
		textCourse.setEnabled(true);
	}
	//set frame for new student Register
	private void newStudent() {
		textPassword.setEnabled(true);
		lblPassword.setEnabled(true);
		clearFields();
		enableFields();
	}
	// Update Current Student Information
	private void updateStudent(){
		DAO dao = new DAO();
		try {
			dao.connect();
			if (currentUsername.isEmpty()) {
				//Insert new student data
				Date now = new Date();
				String format = "yyyy/MM/dd";
				SimpleDateFormat formatter = new SimpleDateFormat(format);
				dao.executeSQL("INSERT INTO ESU_User (username,password,profile,lastlogin)"
						+ " VALUES ('"+textUsername.getText()+"','"+textPassword.getText()+"','student','"+formatter.format(now)+"')");
				String sql = "INSERT INTO ESU_Students_Data (username,name,surname,birthday,sex,city,state,country,email,course,GPA,credits,startDate)"
						+ " VALUES ('"+textUsername.getText()+"','"+textName.getText()+"','"+textSurname.getText()+"','"+textBirthdate.getText()+"','"+textSex.getText()+"','"+textCity.getText()+"','"
						+ textState.getText()+"','"+textCountry.getText()+"','"+textEmail.getText()+"','"+textCourse.getText()+"','"+textGPA.getText()+"','"+textCredits.getText()+"','"+textStartDate.getText()+"')";
				System.out.println(sql);
				dao.executeSQL(sql);
				textPassword.setEnabled(false);
				lblPassword.setEnabled(false);
				// Show Confirmation Popup
				JOptionPane.showMessageDialog(this,"New student was registered in the Enrollment System","Confirmation",JOptionPane.INFORMATION_MESSAGE);
				currentUsername = textUsername.getText();
			}
			else {
				// Update Existing Student Details
				dao.executeSQL("UPDATE ESU_Students_Data SET username='"+textUsername.getText()+"',name='"+textName.getText()+"',surname='"+textSurname.getText()+"',birthday='"+textBirthdate.getText()+"',"
						+ "sex='"+textSex.getText()+"',city='"+textCity.getText()+"',state='"+ textState.getText()+"',country='"+textCountry.getText()+"',email='"+textEmail.getText()+"',"
						+ "course='"+textCourse.getText()+"',GPA='"+textGPA.getText()+"',credits='"+textCredits.getText()+"',startDate='"+textStartDate.getText()+"'"
						+ " WHERE username = '"+currentUsername+"'");
				JOptionPane.showMessageDialog(this,"Student information was updated","Confirmation",JOptionPane.INFORMATION_MESSAGE);
			}
			// Close Database Connection
			dao.closeConnection();
		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}
	// Load Student Details
	private void loadStudent(String studentId){
		try {
			//Connect Database
			DAO dao = new DAO();
			ResultSet rs = null;
			textPassword.setEnabled(false);
			lblPassword.setEnabled(false);
			dao.connect();
			//get Units Student Enrolled
			rs = dao.getResultSet("SELECT username,name,surname,birthday,sex,city,state,country,email,course,GPA,credits,startDate FROM ESU_Students_Data WHERE username='"+studentId+"'");
			if(rs.next()){
				textUsername.setText(rs.getString("username"));
				textName.setText(rs.getString("name"));
				textSurname.setText(rs.getString("surname"));
				textBirthdate.setText(rs.getString("birthday"));
				textSex.setText(rs.getString("sex"));
				textCity.setText(rs.getString("city"));
				textState.setText(rs.getString("state"));
				textCountry.setText(rs.getString("country"));
				textEmail.setText(rs.getString("email"));
				textCourse.setText(rs.getString("course"));
				textGPA.setText(rs.getString("GPA"));
				textCredits.setText(rs.getString("credits"));
				textStartDate.setText(rs.getString("startDate"));
				enableFields();
			}
			//Close Database Connection
			dao.closeConnection();
		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}
	// Enable Enrollment Frame
	public void goEnrolment() {
		JFrame enrolmentScreen = new Enrolment(pMenu, currentUser);
		this.setVisible(false);
		enrolmentScreen.setVisible(true);
	}
	// Take to previous menu
	public void goBackMenu() {
		this.setVisible(false);
		pMenu.setVisible(true);
	}
	// Logout the current User
	public void logoff() {
		this.setVisible(false);
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

		textStudentId = new JTextField();
		textStudentId.setBounds(320, 498, 129, 26);
		contentPane.add(textStudentId);

		JLabel lblEnrolment = new JLabel();
		lblEnrolment.setText("STUDENTS PROFILE");
		lblEnrolment.setFont(new Font("Dialog", Font.PLAIN, 36));

		JLabel lblTypeTheUnit = new JLabel();
		lblTypeTheUnit.setText("Student ID:");
		lblTypeTheUnit.setBounds(232, 503, 76, 16);
		contentPane.add(lblTypeTheUnit);

		JLabel lblUnitsEnroled = new JLabel("Information");
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

		JButton btnProfile = new JButton("Students");
		btnProfile.setEnabled(false);

		JButton btnUnits = new JButton("Units");
		btnUnits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame checkUnits = new EditUnits();
				checkUnits.setVisible(true);
			}
		});

		JButton btnNewStudent = new JButton("New student");
		btnNewStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentUsername = "";
				newStudent();
			}
		});
		btnNewStudent.setBounds(616, 498, 143, 29);
		contentPane.add(btnNewStudent);

		JButton btnCheckStudent = new JButton("Check Student");
		btnCheckStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentUsername = textStudentId.getText();
				loadStudent(currentUsername);
			}
		});

		btnCheckStudent.setBounds(461, 498, 143, 29);
		contentPane.add(btnCheckStudent);

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
														.addComponent(lblEnrolment, GroupLayout.PREFERRED_SIZE, 364, GroupLayout.PREFERRED_SIZE)
														.addGap(226)
														.addComponent(btnLogout))
												.addGroup(gl_panelEnrolment.createSequentialGroup()
														.addGroup(gl_panelEnrolment.createParallelGroup(Alignment.LEADING, false)
																.addComponent(btnHome, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(btnProfile, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
		lblPassword.setEnabled(false);
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setBounds(236, 30, 61, 16);
		panel.add(lblPassword);

		textPassword = new JTextField();
		textPassword.setEnabled(false);
		textPassword.setColumns(10);
		textPassword.setBounds(309, 25, 114, 26);
		panel.add(textPassword);

		JLabel lblCourse = new JLabel("Course");
		lblCourse.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCourse.setBounds(236, 269, 61, 16);
		panel.add(lblCourse);

		textCourse = new JTextField();
		textCourse.setEnabled(false);
		textCourse.setColumns(10);
		textCourse.setBounds(309, 264, 114, 26);
		panel.add(textCourse);
		panelEnrolment.setLayout(gl_panelEnrolment);

	}
}
