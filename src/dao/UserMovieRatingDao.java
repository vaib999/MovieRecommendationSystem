package dao;

import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.cj.jdbc.PreparedStatement;

public class UserMovieRatingDao {

	public String updateUserMovieRating(String userName, String movieName, String rating)
	{  
		String status = "";
		boolean updateRating = true;
		Integer userPreRating = 0;
		Integer currentUserRating = Integer.parseInt(rating);
		
		try
		{  
			Statement stmt = ConnectionFactory.getConnection().createStatement();  
			ResultSet rs = stmt.executeQuery("SELECT rating FROM user_movie_rating where lower(username)='"+userName.trim()+"' AND "
											+ "movie_name='"+movieName+"'"); 
			if(rs.next()) {
				userPreRating = rs.getInt("rating");
				if(userPreRating == currentUserRating) {
					updateRating = false;
				}
			}
			rs.close();
			stmt.close();

			
			if(updateRating) {
				Statement stmt2 = ConnectionFactory.getConnection().createStatement();
				stmt2.executeUpdate("DELETE FROM user_movie_rating where lower(username)='"+userName.trim()+"' AND "
													+ "movie_name='"+movieName+"'"); 
				stmt2.close();
				
				PreparedStatement pstmt = (PreparedStatement) ConnectionFactory.getConnection().
												prepareStatement("INSERT INTO user_movie_rating VALUES (?,?,?)");  
				pstmt.setString(1, userName);
				pstmt.setString(2, movieName);
				pstmt.setInt(3, currentUserRating);
				pstmt.executeUpdate();
				pstmt.close();
				
				Double existingMovieRating = null;
				Integer existingUsersRated = null;
				Statement stmt3 = ConnectionFactory.getConnection().createStatement();  
				ResultSet rs3 = stmt3.executeQuery("SELECT rating, num_users FROM movie_rating WHERE movie_name='"+	movieName+"'"); 
				if(rs3.next()) {
					existingMovieRating = rs3.getDouble("rating");
					existingUsersRated = rs3.getInt("num_users");
				}
				rs3.close();
				stmt3.close();
								
				Double overallRating = existingMovieRating*existingUsersRated;
				Double newOverallRating = overallRating - userPreRating + currentUserRating;
				if(userPreRating == 0) {
					existingUsersRated++;
				}
				Double newMovieRating = newOverallRating/(existingUsersRated);
				
				PreparedStatement pstmt2 = (PreparedStatement) ConnectionFactory.getConnection().
											prepareStatement("UPDATE movie_rating set rating=?, num_users=? WHERE movie_name=?");  
				pstmt2.setDouble(1, newMovieRating);
				pstmt2.setInt(2, existingUsersRated);
				pstmt2.setString(3, movieName);
				pstmt2.executeUpdate();
				pstmt2.close();
			}			
			status = userName+", Your recommendations saved in the system.";
		}catch(Exception ex){
			 status = "Unable to process the request."; 
	         throw new RuntimeException("Error connecting to the database", ex);
		}
		return status;
	} 
}
