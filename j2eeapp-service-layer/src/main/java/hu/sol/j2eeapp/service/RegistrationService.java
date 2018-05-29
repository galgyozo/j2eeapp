package hu.sol.j2eeapp.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.sol.j2eeapp.bean.Task;
import hu.sol.j2eeapp.bean.User;
import hu.sol.j2eeapp.dao.TaskDao;
import hu.sol.j2eeapp.dao.UserDao;

@Service
@Transactional
public class RegistrationService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private TaskDao taskDao;

	public void RegisterUser(User user) {
		try {
			userDao.createUser(user);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void RegisterTask(Task task) {
		try {
			taskDao.createTask(task);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
