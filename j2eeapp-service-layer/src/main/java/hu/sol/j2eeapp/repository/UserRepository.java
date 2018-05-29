package hu.sol.j2eeapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.sol.j2eeapp.bean.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public User findByUserNameAndPassword(String userName, String password);

}
