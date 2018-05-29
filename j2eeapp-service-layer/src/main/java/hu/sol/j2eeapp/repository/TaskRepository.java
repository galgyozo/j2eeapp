package hu.sol.j2eeapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.sol.j2eeapp.bean.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	public List findTaskByUserId(Long userId);
	
//	public List findTaskByUserIdDone(Long userId, Boolean done);

}
