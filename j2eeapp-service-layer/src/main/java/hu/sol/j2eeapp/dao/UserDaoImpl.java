package hu.sol.j2eeapp.dao;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.sol.j2eeapp.bean.User;
import hu.sol.j2eeapp.repository.UserRepository;

@Service
public class UserDaoImpl implements UserDao {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> getUserList() {
		return Lists.newArrayList(userRepository.findAll());
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public User findByUserNameAndPassword(String userName, String password) {
		return userRepository.findByUserNameAndPassword(userName, password);
	}

	@Override
	public User updateUser(User user) {
		User updatedUser = userRepository.findById(user.getId()).orElse(null);

		if (updatedUser == null) {
			throw new RuntimeException();
		} else {
			updatedUser.setName(user.getName());
			updatedUser.setEmail(user.getEmail());
			updatedUser.setUserName(user.getUserName());
			updatedUser.setPassword(user.getPassword());
			updatedUser.setAdmin(user.isAdmin());
			return updatedUser;
		}

	}

	@Override
	public User deleteUser(Long id) {
		User deletedUser = userRepository.findById(id).orElse(null);

		if (deletedUser == null) {
			throw new RuntimeException();
		} else {
			userRepository.delete(deletedUser);
			return deletedUser;
		}
	}

	@Override
	public User createUser(User user) {
		User createdUser = user;
		return userRepository.save(createdUser);
	}

}
