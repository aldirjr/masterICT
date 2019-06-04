package WSU_Enrolment;
//Importing java library
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
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
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//Import DAO File
import WSU_Enrolment.DAO;

public class MenuAdmin extends JFrame {
	//Unique identifier for class
	private static final long serialVersionUID = 1L;
	// Variables declaration
	private JPanel contentPane;
	private String username;
	private String[][] boardOne, boardTwo;
	private int sizeBoardOne, sizeBoardTwo;
	private User currentUser;
	JTextPane tpStudentBoard;
	JTextPane tpEventNews;

	// Constructor
	public MenuAdmin(String username) {
		currentUser = new User(username);
		boardOne = new String[10][3];
		boardTwo = new String[10][3];
		updateNewUserData();
		initComponents();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Centralize screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

		this.setVisible(true);
	}
	// Load the student information
	private void updateNewUserData() {
		try {
			DAO dao = new DAO();
			ResultSet rt = null;
			dao.connect();
			rt = dao.getResultSet("SELECT name, GPA FROM ESU_Students_Data WHERE username='"+currentUser.username+"'");
			if(rt.next()) {
				currentUser.name = rt.getString("name");
				currentUser.GPA = rt.getFloat("GPA");
			}
			dao.closeConnection();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	//Connect to the Database and Update the name and board in the GUI, and user's last login in the Database
	private void connectToDB() {
		try {
			// Connect Database
			DAO dao = new DAO();
			Date now = new Date();
			String format = "yyyy/MM/dd";
			SimpleDateFormat formatter = new SimpleDateFormat(format);

			ResultSet rs = null;
			String board1="", board2="";
			int i=0, j=0;

			dao.connect();
			//Update the last login user made
			dao.executeSQL("UPDATE ESU_User SET lastLogin='"+formatter.format(now)+"' WHERE username='"+this.username+"'");

			//Update the Student Board and News Board
			rs = dao.getResultSet("SELECT board, title, publication_date, text FROM ESU_Boards");
			while(rs.next()){
				if (rs.getString("board").equalsIgnoreCase("Student")) {
					board1 = board1 + rs.getString("title") +" / "+rs.getString("publication_date")+"\n"
							+ rs.getString("text")+"\n\n";
					boardOne[i][0]=rs.getString("title");
					boardOne[i][1]=rs.getString("publication_date");
					boardOne[i][2]=rs.getString("text");
					i++;

				}
				else {
					board2 = board2 + rs.getString("title") +" / "+rs.getString("publication_date")+"\n"
							+ rs.getString("text")+"\n\n";
					boardTwo[j][0]=rs.getString("title");
					boardTwo[j][1]=rs.getString("publication_date");
					boardTwo[j][2]=rs.getString("text");
					j++;
				}
				sizeBoardOne = i;
				sizeBoardTwo = j;
				tpStudentBoard.setText(board1);
				tpEventNews.setText(board2);
			}
			//Close the database Connection
			dao.closeConnection();
		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}
	
	// Open Current User Profile
	private void openStudentsProfile() {
		JFrame studentsProfile = new Profile(this, currentUser);
		studentsProfile.setVisible(true);
		this.setVisible(false);
	}
	// Logout the current User 
	private void logoff() {
		this.dispose();
		JFrame login=new Login();
		login.setVisible(true);
	}

	/** This method is called from within the constructor to
	 * initialize the Frame.
	 */
	public void initComponents() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

		JLabel label_4 = new JLabel("Events / NEWS");
		label_4.setForeground(new Color(0, 0, 128));
		label_4.setFont(new Font("Lucida Grande", Font.ITALIC, 16));

		JLabel lWelcome = new JLabel();
		lWelcome.setText("WELCOME");
		lWelcome.setFont(new Font("Lucida Grande", Font.PLAIN, 16));

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

		JButton btnStudents = new JButton("Students");
		btnStudents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openStudentsProfile();
			}
		});

		JButton btnUnits = new JButton("Units");
		btnUnits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame editUnits = new EditUnits();
				editUnits.setVisible(true);
			}
		});

		tpStudentBoard = new JTextPane();
		tpStudentBoard.setEditable(false);
		tpStudentBoard.setFont(new Font("Lucida Grande", Font.PLAIN, 14));

		tpEventNews = new JTextPane();
		tpEventNews.setEditable(false);
		tpEventNews.setFont(new Font("Lucida Grande", Font.PLAIN, 14));

		connectToDB();

		JButton btnEdit = new JButton("edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame editBoard = new EditBoard(boardOne,sizeBoardOne, 1);
				editBoard.setVisible(true);
			}
		});

		JButton btnEdit2 = new JButton("edit");
		btnEdit2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame editBoard = new EditBoard(boardTwo,sizeBoardTwo, 2);
				editBoard.setVisible(true);
			}
		});

		JButton btnUpdateBoards = new JButton("refresh boards");
		btnUpdateBoards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectToDB();
			}
		});

		GroupLayout gl_pMenu = new GroupLayout(pMenu);
		gl_pMenu.setHorizontalGroup(
				gl_pMenu.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pMenu.createSequentialGroup()
						.addGap(21)
						.addGroup(gl_pMenu.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_pMenu.createSequentialGroup()
										.addComponent(lWelcome)
										.addGap(18)
										.addComponent(lName, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
										.addGap(385)
										.addComponent(btnLogout))
								.addGroup(gl_pMenu.createSequentialGroup()
										.addGroup(gl_pMenu.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_pMenu.createParallelGroup(Alignment.TRAILING, false)
														.addComponent(btnStudents, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(btnUnits, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
												.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
										.addGap(21)
										.addGroup(gl_pMenu.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_pMenu.createSequentialGroup()
														.addComponent(label_3, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(btnEdit))
												.addGroup(gl_pMenu.createSequentialGroup()
														.addComponent(tpStudentBoard, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(ComponentPlacement.RELATED)))
										.addGroup(gl_pMenu.createParallelGroup(Alignment.TRAILING)
												.addGroup(gl_pMenu.createSequentialGroup()
														.addGap(43)
														.addGroup(gl_pMenu.createParallelGroup(Alignment.LEADING)
																.addGroup(gl_pMenu.createSequentialGroup()
																		.addComponent(label_4, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(btnEdit2, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
																.addComponent(tpEventNews, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)))
												.addGroup(gl_pMenu.createSequentialGroup()
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(btnUpdateBoards)))))
						.addGap(43))
				);
		gl_pMenu.setVerticalGroup(
				gl_pMenu.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pMenu.createSequentialGroup()
						.addGap(30)
						.addGroup(gl_pMenu.createParallelGroup(Alignment.BASELINE, false)
								.addComponent(lWelcome)
								.addComponent(btnLogout)
								.addComponent(lName, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
						.addGap(16)
						.addGroup(gl_pMenu.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_pMenu.createParallelGroup(Alignment.BASELINE)
										.addComponent(label_2)
										.addComponent(label_3))
								.addGroup(gl_pMenu.createSequentialGroup()
										.addGroup(gl_pMenu.createParallelGroup(Alignment.TRAILING)
												.addComponent(btnEdit)
												.addComponent(label_4, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
												.addComponent(btnEdit2))
										.addPreferredGap(ComponentPlacement.RELATED)))
						.addGroup(gl_pMenu.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_pMenu.createSequentialGroup()
										.addGap(18)
										.addComponent(btnStudents)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnUnits))
								.addGroup(gl_pMenu.createSequentialGroup()
										.addGap(6)
										.addGroup(gl_pMenu.createParallelGroup(Alignment.TRAILING, false)
												.addComponent(tpStudentBoard, Alignment.LEADING)
												.addComponent(tpEventNews, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnUpdateBoards)))
						.addGap(12))
				);
		pMenu.setLayout(gl_pMenu);
	}
}
