package hu.sol.j2eeapp.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import hu.sol.j2eeapp.bean.Task;

@Repository
@Transactional
public interface TaskDao {

	public List<Task> getTaskList();

	public Task getTaskById(Long id);
	
	public List findTaskByUserId(Long userId);
	
//	public List findTaskByUserIdDone(Long userId, Boolean done);

	public Task createTask(Task task);

	public Task updateTask(Task task);

	public Task deleteTask(Long id);
}
