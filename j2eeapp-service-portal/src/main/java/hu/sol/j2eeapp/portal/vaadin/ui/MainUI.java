package hu.sol.j2eeapp.portal.vaadin.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

import hu.sol.j2eeapp.service.DeleteService;
import hu.sol.j2eeapp.service.GetService;
import hu.sol.j2eeapp.service.RegistrationService;
import hu.sol.j2eeapp.service.UpdateService;

@SpringUI
public class MainUI extends UI {

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private GetService getService;

	@Autowired
	private UpdateService updateService;

	@Autowired
	private DeleteService deleteService;

	@Override
	protected void init(VaadinRequest request) {
		
		getUI().getLoadingIndicatorConfiguration().setFirstDelay(0);
		
		getSession().setAttribute("get", getService);
		getSession().setAttribute("registration", registrationService);
		getSession().setAttribute("update", updateService);
		getSession().setAttribute("delete", deleteService);

		new Navigator(this, this);
		getNavigator().addView(LoginView.NAME, LoginView.class);
		getNavigator().addView(MainView.NAME, MainView.class);
		getNavigator().addViewChangeListener(new ViewChangeListener() {

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {

				boolean isLoggedIn = getSession().getAttribute("user") != null;
				boolean isLoginView = event.getNewView() instanceof LoginView;

				if (!isLoggedIn && !isLoginView) {

					getNavigator().navigateTo(LoginView.NAME);
					return false;

				} else if (isLoggedIn && isLoginView) {
					
					getNavigator().navigateTo(MainView.NAME);
					return false;
				}

				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {

			}
		});
	}
}
