package dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import vo.MovieRating;

public class RecommendationDao {

	public List<MovieRating> fetchAllMoviesList() 
	{  
		List<MovieRating> movieRatingList = new LinkedList<MovieRating> ();
		try
		{  
			Statement stmt = ConnectionFactory.getConnection().createStatement();  
			ResultSet rs = stmt.executeQuery("SELECT movie_name, rating, num_users FROM movie_rating"); 
			while(rs.next()) {
				MovieRating movieRating = new MovieRating();
				movieRating.setMovieName(rs.getString("movie_name"));
				BigDecimal value = new BigDecimal(rs.getDouble("rating")).setScale(1, RoundingMode.HALF_UP); 
				movieRating.setRating(value);
				movieRating.setNumUsers(rs.getInt("num_users"));
				movieRatingList.add(movieRating);
			}
			rs.close();
			stmt.close();
		}catch(Exception ex){
	         throw new RuntimeException("Error connecting to the database", ex);
		}		
		return movieRatingList;
	}
	
	public List<String> fetchUserMoviesList(String userName)
	{
		List<String> userMovieList = new LinkedList<String> ();
		Set<String> usermovieSet = new LinkedHashSet<String>();
		try
		{  
			Statement stmt = ConnectionFactory.getConnection().createStatement();  
			ResultSet rs = stmt.executeQuery("SELECT movie_name FROM movie_recommendation_scala where username ='"+ userName +"'"); 
			while(rs.next()) {
				usermovieSet.add(rs.getString("movie_name"));
			}
			rs.close();
			stmt.close();
		}catch(Exception ex){
	         throw new RuntimeException("Error connecting to the database", ex);
		}	
		userMovieList.addAll(usermovieSet);
		return userMovieList;
	}
	
}
