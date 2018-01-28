package service;

import java.util.LinkedList;
import java.util.List;

import vo.MovieRating;
import dao.RecommendationDao;

public class RecommendationService {

	public List<MovieRating> fetchAllMoviesList() {
		RecommendationDao recDao = new RecommendationDao();
		return recDao.fetchAllMoviesList();
	}

	public List<MovieRating> fetchUserMoviesList(List<MovieRating> allMoviesList, String userName)
	{
		List<MovieRating> movieRatingList = new LinkedList<MovieRating> ();
		RecommendationDao recDao = new RecommendationDao();
		List<String> userMovieList = recDao.fetchUserMoviesList(userName);
		
		for(String userMovie: userMovieList) {
			for(MovieRating movies : allMoviesList) {
				if(movies.getMovieName().equalsIgnoreCase(userMovie)) {
					movieRatingList.add(movies);
				}
			}
		}
		return movieRatingList;
	}
}
