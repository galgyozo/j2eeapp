package hu.sol.j2eeapp.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import hu.sol.j2eeapp.bean.User;

@Repository
@Transactional
public interface UserDao {

	public List<User> getUserList();

	public User getUserById(Long id);
	
	public User findByUserNameAndPassword(String userName, String password);
	
	public User createUser(User user);

	public User updateUser(User user);
	
	public User deleteUser(Long id);
}
