/*
 * User data class to get and set user data
 */
package WSU_Enrolment;

public class User {

	public String username;
	public String name;
	public String surname;
	public String profile;
	public float GPA;

	public User(String username){
		this.username=username;
	}

	public String getUsername(){
		return this.username;
	}

	public String getName(){
		return this.name;
	}

	public String getSurname(){
		return this.surname;
	}

	public String getProfile(){
		return this.profile;
	}

	public float getGPA() {
		return GPA;
	}

	public void setGPA(float gPA) {
		GPA = gPA;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

}