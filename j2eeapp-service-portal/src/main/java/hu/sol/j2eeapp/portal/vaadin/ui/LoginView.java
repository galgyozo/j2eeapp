package hu.sol.j2eeapp.portal.vaadin.ui;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import hu.sol.j2eeapp.bean.User;
import hu.sol.j2eeapp.service.GetService;

@SpringView(name = LoginView.NAME)
public class LoginView extends CustomComponent implements View {

	public static final String NAME = "login";

	private User user;

	private TextField userName = new TextField("Username");
	private TextField password = new PasswordField("Password");
	private Button loginButton = new Button("Login");
	private GetService getService;

	public LoginView() {

	}

	private void init() {
		setSizeFull();

		Panel loginPanel = new Panel("Login");
		loginPanel.setSizeUndefined();
		loginPanel.setWidth("350px");
		loginPanel.setHeight("250px");

		final FormLayout loginLayout = new FormLayout();
		loginLayout.addComponent(userName);
		loginLayout.addComponent(password);
		loginLayout.addComponent(loginButton);
		loginButton.setStyleName(ValoTheme.BUTTON_PRIMARY);

		loginLayout.setCaption("Please login to access the application.");
		loginLayout.setMargin(true);

		userName.setRequiredIndicatorVisible(true);
		password.setRequiredIndicatorVisible(true);

		loginPanel.setContent(loginLayout);

		VerticalLayout viewLayout = new VerticalLayout(loginPanel);
		viewLayout.setSizeFull();
		viewLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		setCompositionRoot(viewLayout);

		loginButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				User loggedUser = getService.findByUserNameAndPassword(userName.getValue(), password.getValue());

				if (loggedUser != null) {

					getSession().setAttribute("user", loggedUser);
					getUI().getNavigator().navigateTo(MainView.NAME);

				} else {
					Notification.show("Invalid username or password", Notification.TYPE_ERROR_MESSAGE)
							.setPosition(Position.TOP_CENTER);
					password.setValue("");
					password.focus();

				}
			}
		});

	}

	@Override
	public void enter(ViewChangeEvent event) {
		Page.getCurrent().setTitle("Spring & Vaadin demo");

		getService = (GetService) getSession().getAttribute("get");

		init();
	}
}
