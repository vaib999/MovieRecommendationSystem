package service;

import dao.RegistrationDao;

public class RegistrationService {

	public String registerUser(String userName, String email, String password) {
		RegistrationDao regDao = new RegistrationDao();
		return regDao.registerUser(userName, email, password);
	}
	
}
