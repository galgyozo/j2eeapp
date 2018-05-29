package hu.sol.j2eeapp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.sol.j2eeapp.bean.Task;
import hu.sol.j2eeapp.bean.User;
import hu.sol.j2eeapp.dao.TaskDao;
import hu.sol.j2eeapp.dao.UserDao;

@Service
@Transactional
public class GetService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private TaskDao taskDao;

	public List<User> getUserList() {
		return userDao.getUserList();

	}

	public User getUserById(Long id) {
		return userDao.getUserById(id);
	}

	public User findByUserNameAndPassword(String userName, String password) {
		return userDao.findByUserNameAndPassword(userName, password);
	}

	public List<Task> getTaskList() {
		return taskDao.getTaskList();

	}

	public List<Task> getUserTaskList(Long userId) {
		return taskDao.findTaskByUserId(userId);
	}

//	public List<Task> getUserTaskListDone(Long userId, Boolean done) {
//		return taskDao.findTaskByUserIdDone(userId, done);
//	}

	public Task getTaskById(Long id) {
		return taskDao.getTaskById(id);
	}
}
