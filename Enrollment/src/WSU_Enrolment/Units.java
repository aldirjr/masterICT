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
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
//Import Dao File
import WSU_Enrolment.DAO;

public class Units extends JFrame {
	//Unique identifier for class
	private static final long serialVersionUID = 1L;
	// Variables declaration
	private JPanel contentPane;
	private String username;
	private User currentUser;

	// Constructor
	public Units(String username) {
		currentUser = new User(username);
		updateNewUserData();
		initComponents();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//Centralize screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.setVisible(true);
	}
	// Update the selected user data
	private void updateNewUserData() {
		try {
			//connect database
			DAO dao = new DAO();
			ResultSet rt = null;
			dao.connect();
			rt = dao.getResultSet("SELECT name, GPA FROM ESU_Students_Data WHERE username='"+currentUser.username+"'");
			if(rt.next()) {
				currentUser.name = rt.getString("name");
				currentUser.GPA = rt.getFloat("GPA");
			}
			//Close database connection
			dao.closeConnection();
		}catch (Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}
	// Unread Message selection and view
	public void numberMsgNRead(JTextField number, JLabel msgs) {
		try {
			//Connect database
			int newMsgs = 0;
			DAO dao = new DAO();
			ResultSet rt = null;
			dao.connect();
			rt = dao.getResultSet("SELECT * FROM ESU_Messages WHERE username='"+this.username+"' AND lida='0'");
			while(rt.next()){
				newMsgs=newMsgs+1;
			}
			
			//Close Connection
			dao.closeConnection();
			if (newMsgs == 1){
				msgs.setText("UNREAD MESSAGE");
			}
		}catch (Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}
	//Open Enrollment Screen
	public void openEnrolment() {
		//this.setVisible(false);
		JFrame enrolment=new Enrolment(this, currentUser);
		enrolment.setVisible(true);
	}
	//Logout current User and redirect them to Login screen
	public void logoff() {
		this.dispose();
		JFrame login=new Login();
		login.setVisible(true);
	}


	/** This method is called from within the constructor to
	 * initialize the Frame.
	 */
	public void initComponents() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 778, 562);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel lblAttentionItIs = new JLabel();
		lblAttentionItIs.setForeground(Color.RED);
		lblAttentionItIs.setText("ATTENTION! IT IS TIME TO ENROL IN UNITS FOR THE NEXT SESSION!");
		lblAttentionItIs.setBounds(319, 504, 432, 16);
		contentPane.add(lblAttentionItIs);

		JPanel pMenu = new JPanel();
		pMenu.setPreferredSize(new Dimension(778, 478));
		pMenu.setBackground(Color.LIGHT_GRAY);
		pMenu.setBounds(0, 0, 778, 478);
		contentPane.add(pMenu);

		JLabel label_2 = new JLabel();
		label_2.setText("MENU");
		label_2.setFont(new Font("Dialog", Font.PLAIN, 36));

		JLabel label_3 = new JLabel("Student board");
		label_3.setForeground(new Color(0, 0, 139));
		label_3.setFont(new Font("Lucida Grande", Font.ITALIC, 16));

		JLabel lName = new JLabel("New label");
		lName.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lName.setText(currentUser.getName());
		lName.setForeground(Color.WHITE);
		lName.setFont(new Font("Lucida Grande", Font.PLAIN, 16));

		JButton btnLogout = new JButton();
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logoff();
			}
		});
		btnLogout.setText("LOGOUT");

		JButton btnProfile = new JButton("Profile");

		JButton btnEnrolment = new JButton("Enrolment");
		btnEnrolment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openEnrolment();
			}
		});

		JButton btnUnits = new JButton("Units");

		JTextPane tpStudentBoard = new JTextPane();
		tpStudentBoard.setEditable(false);
		tpStudentBoard.setFont(new Font("Lucida Grande", Font.PLAIN, 14));

		GroupLayout gl_pMenu = new GroupLayout(pMenu);
		gl_pMenu.setHorizontalGroup(
				gl_pMenu.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pMenu.createSequentialGroup()
						.addGap(21)
						.addGroup(gl_pMenu.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_pMenu.createSequentialGroup()
										.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(label_3, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_pMenu.createSequentialGroup()
										.addGap(96)
										.addComponent(lName, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
										.addGap(385)
										.addComponent(btnLogout))
								.addGroup(gl_pMenu.createSequentialGroup()
										.addGroup(gl_pMenu.createParallelGroup(Alignment.TRAILING, false)
												.addComponent(btnProfile, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(btnEnrolment, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(btnUnits, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGap(30)
										.addComponent(tpStudentBoard, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)))
						.addGap(85))
				);
		gl_pMenu.setVerticalGroup(
				gl_pMenu.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pMenu.createSequentialGroup()
						.addGap(30)
						.addGroup(gl_pMenu.createParallelGroup(Alignment.BASELINE, false)
								.addComponent(btnLogout)
								.addComponent(lName, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
						.addGap(16)
						.addGroup(gl_pMenu.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_2)
								.addComponent(label_3))
						.addGroup(gl_pMenu.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_pMenu.createSequentialGroup()
										.addGap(18)
										.addComponent(btnProfile)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnEnrolment)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnUnits))
								.addGroup(gl_pMenu.createSequentialGroup()
										.addGap(6)
										.addComponent(tpStudentBoard, GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)))
						.addGap(21))
				);
		pMenu.setLayout(gl_pMenu);
	}
}
