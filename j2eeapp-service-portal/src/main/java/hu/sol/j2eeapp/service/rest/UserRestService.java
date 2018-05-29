package hu.sol.j2eeapp.service.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.sol.j2eeapp.bean.User;
import hu.sol.j2eeapp.service.DeleteService;
import hu.sol.j2eeapp.service.GetService;
import hu.sol.j2eeapp.service.RegistrationService;
import hu.sol.j2eeapp.service.UpdateService;

@RestController
@RequestMapping(path = "/rest")
public class UserRestService {

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private GetService getService;

	@Autowired
	private UpdateService updateService;

	@Autowired
	private DeleteService deleteService;

	@CrossOrigin
	@RequestMapping(path = "/getUserList", method = RequestMethod.GET)
	public List<User> getUserList() {
		return getService.getUserList();

	}

	@CrossOrigin
	@RequestMapping(path = "/getUserById", method = RequestMethod.GET)
	public User getUserById(@RequestParam("id") Long id) {
		return getService.getUserById(id);
	}

	@CrossOrigin
	@RequestMapping(path = "/createUser", method = RequestMethod.POST)
	public void createUser(User user) {
		registrationService.RegisterUser(user);
	}

	@CrossOrigin
	@RequestMapping(path = "/updateUser", method = RequestMethod.POST)
	public void updateUser(User user) {
		updateService.updateUser(user);
	}

	@CrossOrigin
	@RequestMapping(path = "/deleteUser", method = RequestMethod.POST)
	public String deleteUser(long id) {
		return deleteService.deleteUser(id);
	}
}
