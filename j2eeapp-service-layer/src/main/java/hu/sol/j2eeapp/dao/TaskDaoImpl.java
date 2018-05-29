package hu.sol.j2eeapp.dao;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.sol.j2eeapp.bean.Task;
import hu.sol.j2eeapp.repository.TaskRepository;

@Service
public class TaskDaoImpl implements TaskDao {

	@Autowired
	private TaskRepository taskRepository;

	@Override
	public List<Task> getTaskList() {
		return Lists.newArrayList(taskRepository.findAll());
	}

	@Override
	public Task getTaskById(Long id) {
		return taskRepository.findById(id).orElse(null);
	}

	@Override
	public List findTaskByUserId(Long userId) {
		return taskRepository.findTaskByUserId(userId);
	}

//	@Override
//	public List findTaskByUserIdNotDone(Long userId, Boolean done) {
//		return taskRepository.findTaskByUserIdDone(userId, done);
//	}

	@Override
	public Task updateTask(Task task) {
		Task updatedTask = taskRepository.findById(task.getId()).orElse(null);

		if (updatedTask == null) {
			throw new RuntimeException();
		} else {
			updatedTask.setTaskName(task.getTaskName());
			updatedTask.setUserId(task.getUserId());
			updatedTask.setDescription(task.getDescription());
			updatedTask.setDone(task.isDone());
			return updatedTask;
		}

	}

	@Override
	public Task deleteTask(Long id) {
		Task deletedTask = taskRepository.findById(id).orElse(null);

		if (deletedTask == null) {
			throw new RuntimeException();
		} else {
			taskRepository.delete(deletedTask);
			return deletedTask;
		}
	}

	@Override
	public Task createTask(Task task) {
		Task createdTask = task;
		return taskRepository.save(createdTask);
	}

}
