package hu.sol.j2eeapp.portal.vaadin.ui;

import java.util.Objects;
import java.util.Set;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Binder;
import com.vaadin.data.Validator;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

import hu.sol.j2eeapp.bean.User;
import hu.sol.j2eeapp.service.DeleteService;
import hu.sol.j2eeapp.service.GetService;
import hu.sol.j2eeapp.service.RegistrationService;
import hu.sol.j2eeapp.service.UpdateService;

public class UsersSubView extends VerticalLayout {
	public static final String NAME = "users";

	private GetService getService;
	private RegistrationService registrationService;
	private UpdateService updateService;
	private DeleteService deleteService;

	protected Window editUserWindow;
	private Set<User> selected;

	private Grid<User> grid;

	public UsersSubView(GetService getService, RegistrationService registrationService, UpdateService updateService,
			DeleteService deleteService) {
		super();
		this.getService = getService;
		this.registrationService = registrationService;
		this.updateService = updateService;
		this.deleteService = deleteService;

		this.addComponent(createUserTable());
		this.addComponent(createFunctionLayout());
	}

	@SuppressWarnings("unchecked")
	private Component createUserTable() {
		grid = new Grid("Users");
		grid.setSizeFull();

		refreshGrid();

		MultiSelectionModel<User> selectionModel = (MultiSelectionModel<User>) grid
				.setSelectionMode(SelectionMode.MULTI);

		grid.addColumn(User::getId).setCaption("ID").setId("id");
		grid.addColumn(User::getName).setCaption("NAME").setId("name");
		grid.addColumn(User::getUserName).setCaption("USERNAME").setId("username");
		grid.addColumn(User::getEmail).setCaption("EMAIL").setId("email");
		grid.addColumn(User::isAdmin).setCaption("ADMIN").setId("admin");

		grid.addSelectionListener(event -> {
			selected = event.getAllSelectedItems();
		});

		grid.addColumn(User -> "Edit", new ButtonRenderer(clickEvent -> {
			openEditWindow((User) clickEvent.getItem(), "Edit user");
		})).setCaption("EDIT").setWidth(100);

		return grid;
	}

	private Component createFunctionLayout() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.setMargin(true);

		Button addUserButton = new Button("Add user");
		addUserButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		addUserButton.addClickListener(event -> {
			User user = new User();
			openEditWindow(user, "Add user");
		});

		horizontalLayout.addComponent(addUserButton);

		Button deleteSelectedUsersButton = new Button("Delete selected");
		deleteSelectedUsersButton.setStyleName(ValoTheme.BUTTON_DANGER);

		deleteSelectedUsersButton.addClickListener(event -> {
			if (selected.isEmpty()) {
				Notification.show("Select at least 1 user", Notification.TYPE_WARNING_MESSAGE)
						.setPosition(Position.TOP_CENTER);
			} else {

				ConfirmDialog.show(getUI(), "Are you sure?", dialog -> {
					if (dialog.isConfirmed()) {
						for (User u : selected) {
							try {
								deleteService.deleteUser(u.getId());
							} catch (Exception e) {
								Notification.show(e.getClass().getSimpleName() + e.getMessage(),
										Notification.TYPE_ERROR_MESSAGE).setPosition(Position.TOP_CENTER);
							}
						}
						Notification.show("Success").setPosition(Position.TOP_CENTER);

						refreshGrid();
					}
				});
			}
		});

		horizontalLayout.addComponent(deleteSelectedUsersButton);
		return horizontalLayout;

	}

	private void openEditWindow(User user, String title) {
		editUserWindow = new Window(title);
		editUserWindow.setHeight("70%");
		editUserWindow.setWidth("70%");
		editUserWindow.center();
		editUserWindow.setContent(createUserEditLayout(user));
		getUI().addWindow(editUserWindow);
	}

	protected Component createUserEditLayout(User user) {
		VerticalLayout verticalLayout = new VerticalLayout();
		final Binder<User> userBinder = new Binder<User>(User.class);
		userBinder.setBean(user);
		verticalLayout.addComponent(createUserEditForm(userBinder));

		return verticalLayout;
	}

	private void refreshGrid() {
		grid.setItems(getService.getUserList());
		grid.getDataProvider().refreshAll();
	}

	private Component createUserEditForm(Binder<User> userBinder) {
		FormLayout formLayout = new FormLayout();

		Label validationStatus = new Label();
		validationStatus.setStyleName(ValoTheme.LABEL_BOLD);
		userBinder.setStatusLabel(validationStatus);
		formLayout.addComponent(validationStatus);

		TextField idField = new TextField("ID");
		userBinder.forField(idField).withNullRepresentation("")
				.withConverter(new StringToLongConverter(idField.getValue())).bind(User::getId, User::setId);
		idField.setEnabled(false);
		formLayout.addComponent(idField);

		TextField userNameField = new TextField("User name");
		userBinder.forField(userNameField).asRequired("User name may not be empty").bind(User::getUserName,
				User::setUserName);
		formLayout.addComponent(userNameField);

		TextField nameField = new TextField("Name");
		userBinder.forField(nameField).asRequired("Name may not be empty").bind(User::getName, User::setName);
		formLayout.addComponent(nameField);

		TextField emailField = new TextField("E-mail");
		userBinder.forField(emailField).asRequired("E-mail may not be empty")
				.withValidator(new EmailValidator("Invalid e-mail address format"))
				.bind(User::getEmail, User::setEmail);
		formLayout.addComponent(emailField);

		PasswordField passwordField = new PasswordField("Password");
		userBinder.forField(passwordField).asRequired("Password may not be empty").bind(User::getPassword,
				User::setPassword);
		formLayout.addComponent(passwordField);

		PasswordField confPasswordField = new PasswordField("Confirm password");
		userBinder.forField(confPasswordField).asRequired("Confirm password may not be empty").bind(User::getPassword,
				User::setPassword);
		formLayout.addComponent(confPasswordField);

		userBinder.withValidator(Validator.from(account -> {
			if (passwordField.isEmpty() || confPasswordField.isEmpty()) {
				return true;
			} else {
				return Objects.equals(passwordField.getValue(), confPasswordField.getValue());
			}
		}, "Entered password and confirmation password must match"));

		CheckBox admin = new CheckBox("Admin");
		userBinder.forField(admin).bind(User::isAdmin, User::setAdmin);
		formLayout.addComponent(admin);

		Button saveButton = new Button("Save");
		saveButton.setEnabled(false);
		saveButton.addClickListener(event -> {
			try {
				User bean = userBinder.getBean();
				if (bean.getId() == null || bean.getId().toString().isEmpty()) {
					registrationService.RegisterUser(bean);
				} else {
					updateService.updateUser(bean);
				}
				editUserWindow.close();
				Notification.show("Success").setPosition(Position.TOP_CENTER);
				refreshGrid();
			} catch (Exception e) {
				Notification.show(e.getClass().getSimpleName() + e.getMessage(), Notification.TYPE_ERROR_MESSAGE)
						.setPosition(Position.TOP_CENTER);
			}
		});

		formLayout.addComponent(saveButton);

		userBinder.addStatusChangeListener(event -> saveButton.setEnabled(userBinder.isValid()));

		return formLayout;
	}

}
