package dao;

import java.sql.ResultSet;
import java.sql.Statement;

public class LoginDao {

	public String loginUser(String userName, String password) throws RuntimeException
	{  
		String status = "";
		try
		{  
			Statement stmt = ConnectionFactory.getConnection().createStatement();  
			ResultSet rs = stmt.executeQuery("SELECT * FROM user_login where lower(username) = '"+userName.trim()+"'"); 
			if(!rs.next()) {
				status = "Not a registered username, register first if not done so.";
			}
			rs.close();
			stmt.close();
		}catch(Exception ex){
	         throw new RuntimeException("Error connecting to the database", ex);
		} 
		if(status.equalsIgnoreCase("")) {
			try
			{  
				Statement stmt = ConnectionFactory.getConnection().createStatement(); 
				ResultSet rs = stmt.executeQuery("SELECT username FROM user_login where lower(username) = '"+userName.trim()+"'"
													+" and password ='"+ password+"'"); 
				if(rs.next()) {
					status = "Success";
				}else {
					status = "Incorrect passsword, resubmit again.";					
				}
				rs.close();
				stmt.close();
			}catch(Exception ex){
		         throw new RuntimeException("Error connecting to the database", ex);
			}
		}
		return status;
	} 
}
