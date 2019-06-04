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
import javax.swing.JTextPane;

public class EditBoard extends JFrame {
	//Unique identifier for class
	private static final long serialVersionUID = 1L;
	// Variable Declaration
	private JPanel contentPane;
	private JTextField textTitle;
	private JTextField textDate;
	private JTextPane textInformation;
	String[][]boardEntrances;
	int totalEntrances;
	String board;
	String titleToBeUpdated;
	JComboBox<String> comboBoxSelected;

	// Constructor
	public EditBoard(String[][] boardItems, int itemSize, int boardNumber) {
		boardEntrances = boardItems;
		totalEntrances = itemSize;
		if (boardNumber == 1) {
			board = "Student";
		}
		else
			board = "News";
		initComponents();
	}
	// Fill the board data in form text field
	public void fillForms(int item) {
		textTitle.setText(boardEntrances[item][0]);
		textDate.setText(boardEntrances[item][1]);
		textInformation.setText(boardEntrances[item][2]);
	}
	//Update Board data 
	public void updateBoard(String command) {
		try {
			// Connect Database
			DAO dao = new DAO();
			dao.connect();
			//Delete the board info
			if (command.equals("delete")){
				dao.executeSQL("DELETE FROM ESU_BOARDS WHERE title = '"+titleToBeUpdated+"'");
				dao.closeConnection();

			}
			else {
				//Create a new publication
				if (titleToBeUpdated.equals("new")) {
					dao.executeSQL("INSERT INTO ESU_BOARDS (board, title, text, publication_date) VALUES ('"+board+"','"+textTitle.getText()+"','"+textInformation.getText()+"','"+textDate.getText()+"')");      
					dao.closeConnection();
				}
				//Update the board for that publication
				else
				{
					dao.executeSQL("UPDATE ESU_BOARDS SET title='"+textTitle.getText()+"', publication_date='"+textDate.getText()+"', text='"+textInformation.getText()+"' WHERE title='"+titleToBeUpdated+"'");      
					dao.closeConnection();
				}
			}
			// Show Confirmation 
			JOptionPane.showMessageDialog(this,"The board has been updated","Updated",JOptionPane.INFORMATION_MESSAGE);
			textTitle.setText("");
			textDate.setText("");
			textInformation.setText("");
			comboBoxSelected.setSelectedIndex(0);
			updateNewState();
		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}

	private void updateNewState() {
		try {
			// Connect To database
			DAO dao = new DAO();
			ResultSet rs = null;
			int i=0;
			dao.connect();
			//Update the titles available
			rs = dao.getResultSet("SELECT title, publication_date, text FROM ESU_Boards WHERE board = '"+board+"'");
			while(rs.next()){        
				boardEntrances[i][0]=rs.getString("title");
				boardEntrances[i][1]=rs.getString("publication_date");
				boardEntrances[i][2]=rs.getString("text");
				i++;              	
				totalEntrances = i;
			}  
			//Close Database Connection
			dao.closeConnection();
			//Construct new selectbox
			String[] loadCombo = new String[totalEntrances+2];
			loadCombo[0] = "Select...";
			loadCombo[1] = "New";
			for (int j=0; j < totalEntrances; j++) {
				loadCombo[j+2] = boardEntrances[j][0];
			}
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(loadCombo);
			// Update SelectBox
			comboBoxSelected.setModel(model);
		}
		catch(Exception e){
			// Print SQl exception
			e.printStackTrace();
		}
	}
	// Cancel the Edit Pop up
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

		JLabel label = new JLabel();
		label.setText("Edit board");
		label.setFont(new Font("Dialog", Font.PLAIN, 36));
		label.setBounds(99, 6, 188, 43);
		contentPane.add(label);

		textTitle = new JTextField();
		textTitle.setEnabled(false);
		textTitle.setColumns(10);
		textTitle.setBounds(17, 128, 262, 26);
		contentPane.add(textTitle);

		JLabel label_1 = new JLabel("Title");
		label_1.setBounds(27, 110, 61, 16);
		contentPane.add(label_1);

		JLabel label_2 = new JLabel("Date");
		label_2.setBounds(27, 166, 61, 16);
		contentPane.add(label_2);

		textDate = new JTextField();
		textDate.setEnabled(false);
		textDate.setColumns(10);
		textDate.setBounds(17, 184, 130, 26);
		contentPane.add(textDate);

		JLabel label_3 = new JLabel("Information");
		label_3.setBounds(27, 222, 93, 16);
		contentPane.add(label_3);

		JButton button = new JButton("Cancel");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelEdit();
			}
		});
		button.setBounds(194, 367, 93, 29);
		contentPane.add(button);

		JButton button_1 = new JButton("Save");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateBoard("save");
			}
		});
		button_1.setBounds(290, 367, 93, 29);
		contentPane.add(button_1);

		textInformation = new JTextPane();
		textInformation.setEnabled(false);
		textInformation.setBounds(17, 247, 385, 116);
		contentPane.add(textInformation);


		String[] loadCombo = new String[totalEntrances+2];
		loadCombo[0] = "Select...";
		loadCombo[1] = "New";
		for (int i=0; i < totalEntrances; i++) {
			loadCombo[i+2] = boardEntrances[i][0];
		}

		comboBoxSelected = new JComboBox<String>(loadCombo);

		comboBoxSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (comboBoxSelected.getSelectedIndex()) {
				case 0:
					break;
				case 1:
					textTitle.setEnabled(true);
					textDate.setEnabled(true);
					textInformation.setEnabled(true);
					titleToBeUpdated = "new";
					break;
				default:
					fillForms(comboBoxSelected.getSelectedIndex()-2);
					// -2 because first 2 entrances are: Select and New;
					textTitle.setEnabled(true);
					textDate.setEnabled(true);
					textInformation.setEnabled(true);
					titleToBeUpdated = (String) comboBoxSelected.getSelectedItem();
					break;
				}

			}
		});
		comboBoxSelected.setBounds(17, 61, 270, 27);
		contentPane.add(comboBoxSelected);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateBoard("delete");
			}
		});
		btnDelete.setBounds(99, 367, 93, 29);
		contentPane.add(btnDelete);

	}
}
