package dao;

import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.cj.jdbc.PreparedStatement;

public class RegistrationDao {

	public String registerUser(String userName, String email, String password) 
	{  
		String status = "";
		try
		{  
			Statement stmt = ConnectionFactory.getConnection().createStatement();  
			ResultSet rs = stmt.executeQuery("SELECT * FROM user_login where lower(username) = '"+userName.trim()+"'"); 
			if(rs.next()) {
				status = "Username already exists";
			}
			rs.close();
			stmt.close();
		}catch(Exception ex){
	         throw new RuntimeException("Error connecting to the database", ex);
		} 
		if(status.equalsIgnoreCase("")) {
			status = "Problems occured in creating an account";
			try
			{  
				PreparedStatement pstmt = (PreparedStatement) ConnectionFactory.getConnection().prepareStatement("INSERT INTO user_login VALUES ("
															+ "?,?,?,?);");  
				
				pstmt.setString(1, userName);
				pstmt.setString(2, password);
				pstmt.setString(3, email);
				pstmt.setString(4, "N");
				int i = pstmt.executeUpdate();
				pstmt.close();
				if(i==1) {
					status = "Success";
				}
			}catch(Exception ex){
		         throw new RuntimeException("Error connecting to the database", ex);
			}
		}
		return status;
	} 
}
