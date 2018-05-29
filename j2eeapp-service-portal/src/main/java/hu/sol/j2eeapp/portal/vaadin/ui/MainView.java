package hu.sol.j2eeapp.portal.vaadin.ui;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import hu.sol.j2eeapp.bean.User;
import hu.sol.j2eeapp.service.DeleteService;
import hu.sol.j2eeapp.service.GetService;
import hu.sol.j2eeapp.service.RegistrationService;
import hu.sol.j2eeapp.service.UpdateService;

@SpringView(name = MainView.NAME)
public class MainView extends CustomComponent implements View {

	public static final String NAME = "main";

	private Label text = new Label();
	private Label mainLabel = new Label();

	private MenuBar.Command logoutCommand = selectedItem -> logoutConfirm();
	private MenuBar.Command showUsersCommand = selectedItem -> getUI().getNavigator()
			.navigateTo(NAME + "/" + UsersSubView.NAME);
	private MenuBar.Command showTasksCommand = selectedItem -> getUI().getNavigator()
			.navigateTo(NAME + "/" + TaskSubView.NAME);
	private MenuBar.Command homeCommand = selectedItem -> getUI().getNavigator().navigateTo(NAME);

	private Button logout = new Button("Logout", (ClickListener) event -> logoutConfirm());
	private Panel panel = new Panel();
	private User user;

	public MainView() {
		// starts from enter()
	}

	private void init() {
		setCompositionRoot(createUserVerticalLayout());
	}

	private Component createUserVerticalLayout() {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);

		verticalLayout.addComponent(text);
		verticalLayout.addComponent(createMenuBar());

		verticalLayout.addComponent(panel);
		verticalLayout.addComponent(logout);
		return verticalLayout;
	}

	private Component createMenuBar() {
		MenuBar barmenu = new MenuBar();

		MenuItem home = barmenu.addItem("Home", null, homeCommand);
		MenuItem users = barmenu.addItem("Users", null, showUsersCommand);
		users.setEnabled(user.isAdmin());
		MenuItem tasks = barmenu.addItem("Tasks", null, showTasksCommand);
		MenuItem logout = barmenu.addItem("Logout", null, logoutCommand);

		return barmenu;
	}

	private void logoutConfirm() {
		ConfirmDialog.show(this.getUI(), "Are you sure?", dialog -> {
			if (dialog.isConfirmed()) {
				getSession().setAttribute("user", null);
				getUI().getNavigator().navigateTo(NAME);
			}
		});
	}

	@Override
	public void enter(ViewChangeEvent event) {
		user = (User) getSession().getAttribute("user");
		GetService getService = (GetService) getSession().getAttribute("get");
		RegistrationService registrationService = (RegistrationService) getSession().getAttribute("registration");
		UpdateService updateService = (UpdateService) getSession().getAttribute("update");
		DeleteService deleteService = (DeleteService) getSession().getAttribute("delete");

		text.setValue("Hello " + user.getName() + "!");
		text.setStyleName(ValoTheme.LABEL_H2);
		text.setIcon(VaadinIcons.USER);
		Page.getCurrent().setTitle("Spring & Vaadin demo");

		mainLabel.setStyleName(ValoTheme.LABEL_H1);
		mainLabel.setValue("Spring Boot & Vaadin demo application");

		if (event.getParameters() == null || event.getParameters().isEmpty()) {
			panel.setContent(mainLabel);
		} else if (event.getParameters().equals(UsersSubView.NAME)) {
			panel.setContent(new UsersSubView(getService, registrationService, updateService, deleteService));
		} else if (event.getParameters().equals(TaskSubView.NAME)) {
			panel.setContent(new TaskSubView(getService, registrationService, updateService, deleteService, user));
		}

		init();
	}
}
