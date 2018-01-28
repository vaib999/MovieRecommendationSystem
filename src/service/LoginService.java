package service;

import dao.LoginDao;

public class LoginService {

	public String loginUser(String userName, String password) {
		LoginDao loginDao = new LoginDao();
		return loginDao.loginUser(userName, password);
	}
	
}
