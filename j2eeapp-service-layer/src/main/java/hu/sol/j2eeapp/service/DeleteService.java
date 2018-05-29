package hu.sol.j2eeapp.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.sol.j2eeapp.dao.TaskDao;
import hu.sol.j2eeapp.dao.UserDao;

@Service
@Transactional
public class DeleteService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private TaskDao taskDao;

	public String deleteUser(long id) {
		try {
			userDao.deleteUser(id);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return "User deleted";
	}

	public String deleteTask(long id) {
		try {
			taskDao.deleteTask(id);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return "Task deleted";
	}

}
