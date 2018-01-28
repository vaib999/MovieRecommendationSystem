package vo;

import java.math.BigDecimal;

public class MovieRating {

	private String movieName;
	
	private BigDecimal rating;
	
	private Integer numUsers;

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public Integer getNumUsers() {
		return numUsers;
	}

	public void setNumUsers(Integer numUsers) {
		this.numUsers = numUsers;
	}
	
}
