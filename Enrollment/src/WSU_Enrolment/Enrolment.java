package WSU_Enrolment;
//Importing java library
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
//Import DAO File
import WSU_Enrolment.DAO;

public class Enrolment extends JFrame {
	//Unique identifier for class
	private static final long serialVersionUID = 1L;
	// Variables declaration
	private JPanel contentPane;
	private JTextField tfUnitId; //UnitId
	private JTextField tfGroupN; //GroupId
	private User currentUser; 	 //User logged in the system
	private JFrame pMenu; 		 //previous panel - menu.java
	private JTextArea boardOne;  //board Units Enrolled
	private JTextArea boardTwo;  //board Units Available


	// Constructor
	public Enrolment(JFrame menu, User currentUser) {
		this.currentUser = currentUser;
		initComponents();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Centralize screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		pMenu = menu;
		updateLoginDate();
	}
	//Set Login Date
	public void updateLoginDate() {
		try {
			//Connect to database
			DAO dao = new DAO();
			Date now = new Date();
			String format = "yyyy/MM/dd";
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			dao.connect();
			// Update User Login Info
			dao.executeSQL("UPDATE ESU_User SET lastLogin='"+formatter.format(now)+"' WHERE username='"+currentUser.username+"'");
			//Close Connection
			dao.closeConnection();
		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}

	//Connect to the Database and Update the name and board in the GUI, and user's last login in the Database
	private void connectToDB() {
		try {
			//Connect to database
			DAO dao = new DAO();
			ResultSet rs1 = null, rs2 = null;
			String board1="", board2="";
			int tCredits=0;
			dao.connect();
			//get Units Student Enrolled
			rs1 = dao.getResultSet("SELECT u.unitID, u.groupId, u.unitName, u.professorName, u.credits"
					+ ", u.location, u.weekDay, u.hourStart, u.hourEnd"
					+ " FROM ESU_UNIT u INNER JOIN ESU_ENROL e ON u.unitID = e.unitID"
					+ " WHERE e.studentUserName = '"+currentUser.getUsername()+"' AND e.unitID = u.unitID AND e.groupId = u.groupId");
			rs2 = dao.getResultSet("SELECT u.unitID, u.groupId, u.unitName, u.professorName, u.credits"
					+ ", u.studentsEnrolled, u.location, u.weekDay, u.hourStart, u.hourEnd, u.requirement, u.GPAmin,"
					+ "(SELECT DISTINCT p.unitName FROM ESU_UNIT p WHERE p.unitID = u.requirement) AS requirementName"
					+ " FROM ESU_Unit u LEFT JOIN (SELECT unitID, groupId, studentUserName FROM ESU_ENROL WHERE studentUserName ='"+currentUser.getUsername()+"') e "
					+ "ON u.unitID = e.unitID AND u.groupId = e.groupId WHERE e.studentUserName IS NULL ");
			//Update the Boards: Units Enrolled and Units Available
			while(rs1.next()){
				board1 = board1 + "UNIT ID: "+rs1.getString("u.unitID") +" / Group: "+rs1.getString("u.groupId")+"\n"
						+ rs1.getString("u.unitName")+", Credits: "+ rs1.getString("u.credits")
						+"\n"+ rs1.getString("u.professorName")+"\n"+ rs1.getString("u.weekDay") +": "
						+ rs1.getString("u.hourStart")+" - "+ rs1.getString("u.hourEnd") +"\nLocation: "+rs1.getString("u.location")+"\n\n";
				tCredits += Integer.parseInt(rs1.getString("u.credits"));
			}
			board1 += "TOTAL: " + tCredits + " Credits";
			if (tCredits == 0) {
				board1 = "You did not enrol in a unit yet";
			}
			while(rs2.next()){
				int spotsLeft = 25 - Integer.parseInt(rs2.getString("u.studentsEnrolled"));
				board2 = board2 + "UNIT ID: "+rs2.getString("u.unitID") +" / Group: "+rs2.getString("u.groupId")+"\n"
						+ rs2.getString("u.unitName")+", Credits: "+ rs2.getString("u.credits")
						+"\n"+ rs2.getString("u.professorName")+"\n"+ rs2.getString("u.weekDay")
						+": "+ rs2.getString("u.hourStart")+" - "+ rs2.getString("u.hourEnd")+"\nLocation: "+rs2.getString("u.location")
						+"\nRequirement: "+ rs2.getString("requirementName")+"\nGPA min: "+ rs2.getString("u.GPAmin") +"\t Free spots: "+ spotsLeft +"\n\n";
			}
			boardOne.setText(board1);
			boardTwo.setText(board2);
			//Close Connection
			dao.closeConnection();
		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}


	private void updateBoards() {

		try {
			//Connect to database
			DAO dao = new DAO();
			//Result set declaration
			ResultSet rs1 = null, rs2 = null;
			String board1="", board2="";
			int tCredits=0;
			dao.connect();
			//get Units Student Enrolled
			rs1 = dao.getResultSet("SELECT u.unitID, u.groupId, u.unitName, u.professorName, u.credits"
					+ ", u.location, u.weekDay, u.hourStart, u.hourEnd"
					+ " FROM ESU_UNIT u INNER JOIN ESU_ENROL e ON u.unitID = e.unitID"
					+ " WHERE e.studentUserName = '"+currentUser.getUsername()+"' AND e.unitID = u.unitID AND e.groupId = u.groupId");
			//get Units Student is NOT Enrolled
			rs2 = dao.getResultSet("SELECT u.unitID, u.groupId, u.unitName, u.professorName, u.studentsEnrolled, u.credits"
					+ ", u.location, u.weekDay, u.hourStart, u.hourEnd, u.requirement, u.GPAmin,"
					+ "(SELECT DISTINCT p.unitName FROM ESU_UNIT p WHERE p.unitID = u.requirement) AS requirementName"
					+ " FROM ESU_Unit u LEFT JOIN (SELECT unitID, groupId, studentUserName FROM ESU_ENROL WHERE studentUserName ='"+currentUser.getUsername()+"') e "
					+ "ON u.unitID = e.unitID AND u.groupId = e.groupId WHERE e.studentUserName IS NULL");
			//Update the Boards: Units Enrolled and Units Available
			while(rs1.next()){
				board1 = board1 + "UNIT ID: "+rs1.getString("u.unitID") +" / Group: "+rs1.getString("u.groupId")+"\n"
						+ rs1.getString("u.unitName")+", Credits: "+ rs1.getString("u.credits")
						+"\n"+ rs1.getString("u.professorName")+"\n"+ rs1.getString("u.weekDay") +": "
						+ rs1.getString("u.hourStart")+" - "+ rs1.getString("u.hourEnd") +"\nLocation: "+rs1.getString("u.location")+"\n\n";
				tCredits += Integer.parseInt(rs1.getString("u.credits"));
			}
			board1 += "TOTAL: " + tCredits + " Credits";
			while(rs2.next()){
				int spotsLeft = 25 - Integer.parseInt(rs2.getString("u.studentsEnrolled"));
				board2 = board2 + "UNIT ID: "+rs2.getString("u.unitID") +" / Group: "+rs2.getString("u.groupId")+"\n"
						+ rs2.getString("u.unitName")+", Credits: "+ rs2.getString("u.credits")
						+"\n"+ rs2.getString("u.professorName")+"\n"+ rs2.getString("u.weekDay")
						+": "+ rs2.getString("u.hourStart")+" - "+ rs2.getString("u.hourEnd")+"\nLocation: "+rs2.getString("u.location")
						+"\nRequirement: "+ rs2.getString("requirementName")+"\nGPA min: "+ rs2.getString("u.GPAmin") +"\tFree spots: "+spotsLeft+"\n\n";
			}
			boardOne.setText(board1);
			boardOne.setCaretPosition(0);
			boardTwo.setText(board2);
			boardTwo.setCaretPosition(0);
			//Close Connection
			dao.closeConnection();
		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}

	//Confirm enrollment in Unit/Group
	private void confirmEnrolment(String unitIDS, String groupNS) throws ParseException {
		try {
			if (unitIDS.isEmpty() || groupNS.isEmpty()){
				JOptionPane.showMessageDialog(this,"You did not specify the unit and/or group","Error",JOptionPane.ERROR_MESSAGE);
				return;
			}
			int unitId = Integer.parseInt(unitIDS);
			int groupN = Integer.parseInt(groupNS);
			boolean exit = false;
			boolean update = false;

			Object[] options = { "Yes", "No" };
			int option = JOptionPane.showOptionDialog(this, "Are you sure you want to Enrol in Unit "+unitId+" group "+groupN+"?","Confirmation",
					JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
			if (option == 0) {
				DAO dao = new DAO();

				dao.connect();
				ResultSet rs1 = null;
				//Check if Student is already enrolled in the subject and/or group
				rs1 = dao.getResultSet("SELECT studentUserName FROM ESU_Enrol WHERE studentUserName = '"+currentUser.getUsername()+"' AND unitID = "+unitId+"");
				if (rs1.next() || exit) {
					exit=true;
					JOptionPane.showMessageDialog(this,"You are enrolled already in this Unit"
							+ "\nIf you want to change group, cancel the Enrolment \n in the other group and Enrol again","Error",JOptionPane.ERROR_MESSAGE);
				}
				//check if student had the minimum GPA required
				if(!exit) { 
					rs1 = dao.getResultSet("SELECT GPAmin FROM ESU_Unit WHERE unitID = '"+unitId+"' AND groupId = "+groupN+"");
					if (rs1.next() && (rs1.getFloat("GPAmin")> currentUser.GPA)) {
						exit = true;
						JOptionPane.showMessageDialog(this,"Unfortunately, you did not reach the minimum GPA to enrol in this Unit/Group","Error",JOptionPane.ERROR_MESSAGE);
					}
				}
				//check if student completed the unit required as requirement
				if(!exit) { 
					rs1 = dao.getResultSet("SELECT DISTINCT requirement FROM ESU_Unit WHERE unitID = "+unitId+" AND requirement <> 0 ");
					if (rs1.next()) {
						String req = rs1.getString("requirement");
						if (req != null){
							rs1 = dao.getResultSet("SELECT status FROM ESU_History WHERE unitID = "+req+" AND username = '"+currentUser.getUsername()+"'");
							if (!rs1.next() || ((rs1.getString("status")).compareTo("completed") != 0 )){
								exit = true;
								JOptionPane.showMessageDialog(this,"Unfortunately, you do not have the requirements unit to enrol in this Unit/Group","Error",JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
				//check if group is already full
				if(!exit) {
					rs1 = dao.getResultSet("SELECT studentsEnrolled FROM ESU_Unit WHERE unitID = "+unitId+" AND groupId = "+groupN+"");
					if (rs1.next() && (rs1.getInt("studentsEnrolled")>=25)){
						exit = true;
						JOptionPane.showMessageDialog(this,"Unfortunately, this group is already full","Error",JOptionPane.ERROR_MESSAGE);
					}
				}
				//check if unit exists
				if(!exit) {
					rs1 = dao.getResultSet("SELECT unitID FROM ESU_Unit WHERE unitID = "+unitId+" AND groupId = "+groupN+"");
					if (!rs1.next()){
						exit = true;
						JOptionPane.showMessageDialog(this,"This unit/group does not exists","Error",JOptionPane.ERROR_MESSAGE);
					}
				}
				//check if unit clash schedule with another unit
				if(!exit) {
					rs1 = dao.getResultSet("SELECT * FROM ESU_Unit u2 INNER JOIN "
							+ "(SELECT u.unitID, u.weekDay, u.hourStart, u.hourEnd FROM ESU_Enrol e "
							+ "INNER JOIN ESU_Unit u ON e.unitID = u.unitID AND e.groupId = u.groupId "
							+ "WHERE e.studentUserName = '"+currentUser.getUsername()+"') u3 "
							+ "ON u2.weekDay = u3.weekDay AND u2.hourStart = u3.hourStart AND u2.hourEnd = u3.hourEnd "
							+ "WHERE u2.unitID = "+unitId+" AND u2.groupId = "+groupN+"");
					if (rs1.next()){
						exit = true;
						JOptionPane.showMessageDialog(this,"Unfortunately, you already enroled in an unit with the same schedule","Error",JOptionPane.ERROR_MESSAGE);
					}
				}
				//enroll student
				if(!exit) {
					dao.executeSQL("INSERT INTO ESU_Enrol VALUES ("+unitId+","+groupN+",'"+currentUser.getUsername()+"') ");
					dao.executeSQL("UPDATE ESU_Unit SET studentsEnrolled = (studentsEnrolled + 1) WHERE unitID = "+unitId+" AND groupId = "+groupN+"");
					JOptionPane.showMessageDialog(this,"You are enrolled in unitID: "+unitId+"/ group: "+groupN,"Confirmation",JOptionPane.INFORMATION_MESSAGE);
					update = true;
				}
				//Close Connection
				dao.closeConnection();
				//Update boards after enrollment canceled
				if(update) { updateBoards(); }
			} 
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this,"Enter Valid the unit and/or group","Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}

	//Cancel enrollment in Unit/Group
	private void cancelEnrolment(String unitIDS, String groupNS) {
		if (unitIDS.isEmpty() || groupNS.isEmpty()){
			JOptionPane.showMessageDialog(this,"You did not specify the unit and/or group","Error",JOptionPane.ERROR_MESSAGE);
		}
		else {
			int unitId = Integer.parseInt(unitIDS);
			int groupN = Integer.parseInt(groupNS);
			boolean update = false;

			Object[] options = { "Yes", "No" };
			int option = JOptionPane.showOptionDialog(this, "Are you sure you want to cancel your Enrollment in Unit "+unitId+" group "+groupN+"?","Confirmation",
					JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
			if (option == 0) {
				DAO dao = new DAO();
				try {
					dao.connect();
					ResultSet rs1 = null;
					rs1 = dao.getResultSet("SELECT studentUserName FROM ESU_Enrol WHERE studentUserName = '"+currentUser.username+"' AND unitID = "+unitId+" AND groupId = "+groupN);
					if (rs1.next()) {
						dao.executeSQL("DELETE FROM ESU_Enrol WHERE studentUserName = '"+currentUser.username+"' AND unitID = "+unitId+" AND groupId = "+groupN);
						dao.executeSQL("UPDATE ESU_Unit SET studentsEnrolled = (studentsEnrolled - 1) WHERE unitID = "+unitId+" AND groupId = "+groupN);
						JOptionPane.showMessageDialog(this,"Your enrolment was canceled!","Confirmed",JOptionPane.INFORMATION_MESSAGE);
						update = true;
					}
					else
						JOptionPane.showMessageDialog(this,"You are not enrolled in this Unit and Group!","Error",JOptionPane.ERROR_MESSAGE);
					//Close Connection
					dao.closeConnection();  
					//Update boards after enrollment canceled
					if(update) { updateBoards(); }
				}
				catch(Exception e){
					// Print SQl exception
					e.printStackTrace();
				}
			}	
		}
	}
	// Go back to previous menu
	public void goBackMenu() {
		this.setVisible(false);
		pMenu.setVisible(true);
	}
	// Check for current user profile
	public void checkProfile() {
		this.dispose();
		JFrame profileStudent = new ProfileStudent(pMenu, currentUser);
		profileStudent.setVisible(true);
	}
	//Logout The Current User
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

		tfUnitId = new JTextField();
		tfUnitId.setBounds(208, 498, 129, 26);
		contentPane.add(tfUnitId);
		

		tfGroupN = new JTextField();
		tfGroupN.setBounds(461, 498, 34, 26);
		contentPane.add(tfGroupN);

		JLabel lblEnrolment = new JLabel();
		lblEnrolment.setText("ENROLMENT");
		lblEnrolment.setFont(new Font("Dialog", Font.PLAIN, 36));

		JLabel lblGroupNumber = new JLabel();
		lblGroupNumber.setText("Group number:");
		lblGroupNumber.setBounds(349, 503, 100, 16);
		contentPane.add(lblGroupNumber);

		JLabel lblTypeTheUnit = new JLabel();
		lblTypeTheUnit.setText("Unit ID:");
		lblTypeTheUnit.setBounds(155, 503, 60, 16);
		contentPane.add(lblTypeTheUnit);

		JLabel lblUnitsEnroled = new JLabel("Units Enroled");
		lblUnitsEnroled.setForeground(new Color(0, 0, 139));
		lblUnitsEnroled.setFont(new Font("Lucida Grande", Font.ITALIC, 16));

		JLabel lblUnitsAvailableTo = new JLabel("Units Available to Enrol");
		lblUnitsAvailableTo.setForeground(new Color(0, 0, 128));
		lblUnitsAvailableTo.setFont(new Font("Lucida Grande", Font.ITALIC, 16));

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

		JButton btnHome = new JButton("Home");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBackMenu();
			}
		});

		JButton btnProfile = new JButton("Profile");
		btnProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkProfile();
			}
		});

		JButton btnEnrolment = new JButton("Enrolment");
		btnEnrolment.setEnabled(false);
		btnEnrolment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		JButton btnUnits = new JButton("Units");
		btnUnits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame checkUnits = new CheckUnits();
				checkUnits.setVisible(true);
			}
		});

		JButton btnCancelEnrolment = new JButton("Cancel Enrolment");
		btnCancelEnrolment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String unitId = tfUnitId.getText();
				String groupN = tfGroupN.getText();
				cancelEnrolment(unitId, groupN);
			}
		});
		btnCancelEnrolment.setBounds(616, 498, 143, 29);
		contentPane.add(btnCancelEnrolment);

		JButton btnEnrol = new JButton("Enrol");
		btnEnrol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					confirmEnrolment(tfUnitId.getText(), tfGroupN.getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		btnEnrol.setBounds(507, 498, 97, 29);
		contentPane.add(btnEnrol);

		JScrollPane spUnitsAvailable = new JScrollPane();

		JScrollPane spUnitsEnroled = new JScrollPane();

		GroupLayout gl_panelEnrolment = new GroupLayout(panelEnrolment);
		gl_panelEnrolment.setHorizontalGroup(
				gl_panelEnrolment.createParallelGroup(Alignment.TRAILING)
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
										.addGap(18)
										.addGroup(gl_panelEnrolment.createParallelGroup(Alignment.LEADING)
												.addComponent(spUnitsEnroled, GroupLayout.PREFERRED_SIZE, 279, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblUnitsEnroled, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
										.addGap(29)
										.addGroup(gl_panelEnrolment.createParallelGroup(Alignment.LEADING)
												.addComponent(lblUnitsAvailableTo, GroupLayout.PREFERRED_SIZE, 191, GroupLayout.PREFERRED_SIZE)
												.addComponent(spUnitsAvailable, GroupLayout.PREFERRED_SIZE, 279, GroupLayout.PREFERRED_SIZE))))
						.addGap(598))
				);
		gl_panelEnrolment.setVerticalGroup(
				gl_panelEnrolment.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelEnrolment.createSequentialGroup()
						.addGap(30)
						.addGroup(gl_panelEnrolment.createParallelGroup(Alignment.LEADING)
								.addComponent(btnLogout)
								.addComponent(lblEnrolment))
						.addGap(18)
						.addGroup(gl_panelEnrolment.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUnitsEnroled, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUnitsAvailableTo, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panelEnrolment.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelEnrolment.createSequentialGroup()
										.addComponent(btnHome)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnProfile)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnEnrolment)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnUnits))
								.addGroup(gl_panelEnrolment.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_panelEnrolment.createParallelGroup(Alignment.LEADING)
												.addComponent(spUnitsEnroled, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE)
												.addComponent(spUnitsAvailable, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE)))))
				);

		JTextArea taUnitsAvailable = new JTextArea();
		taUnitsAvailable.setEditable(false);
		spUnitsAvailable.setViewportView(taUnitsAvailable);

		JTextArea taUnitsEnroled = new JTextArea();
		taUnitsEnroled.setEditable(false);
		spUnitsEnroled.setViewportView(taUnitsEnroled);
		panelEnrolment.setLayout(gl_panelEnrolment);

		boardOne = taUnitsEnroled;
		boardTwo = taUnitsAvailable;
		connectToDB();

	}
}
