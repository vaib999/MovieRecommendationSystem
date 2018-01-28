package service;

import dao.UserMovieRatingDao;

public class UserMovieRatingService {

	public String updateUserMovieRating(String userName, String movieName, String rating) {
		UserMovieRatingDao userMovDao = new UserMovieRatingDao();
		return userMovDao.updateUserMovieRating(userName, movieName, rating);
	}
	
}
