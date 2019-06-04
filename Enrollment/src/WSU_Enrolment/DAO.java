/*
 * Database Connection Class 
 */

package WSU_Enrolment;

//Importing java library
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DAO {
	//Variable Declaration
	private Connection conexion = null;
	private boolean conected = false;

	// Constructor
	public DAO() {
	}
	// Connect method to initialize connection with provided database
	public void connect() throws Exception {
		try {
			// Database Driver set
			Class.forName("com.mysql.jdbc.Driver");
			//To add database name(WSU_PP_FinalProject) password and username  
			conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/wsu_pp_finalproject",
					"root", "");
		} catch (Exception e) {
			// Print SQl exception
			e.printStackTrace();
		}
	}
	// Return Result Set from database
	public ResultSet getResultSet(String sql) throws Exception {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conexion.createStatement();
			rs = (ResultSet)stmt.executeQuery(sql);
		} catch(Exception e) {
			// Throw SQl exception
			throw e;
		}
		return rs;
	}
	// Execute SQL statement of database
	public boolean executeSQL(String sql) throws Exception{
		Statement stmt = null;
		boolean res = false;
		try {
			stmt = conexion.createStatement();
			res = stmt.execute(sql);
		} catch(Exception e) {
			// Throw SQl exception
			throw e;
		}
		// Return SQL query Result
		return res;
	}
	// Close the Database Connection
	public void closeConnection() throws Exception {
		try {
			if(conected){
				conexion.close();
				conexion = null;
				conected = false;
			}
		} catch(Exception e){
			// Throw SQl exception
			throw e;
		}
	}
}