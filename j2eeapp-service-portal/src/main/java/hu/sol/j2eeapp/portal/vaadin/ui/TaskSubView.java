package hu.sol.j2eeapp.portal.vaadin.ui;

import java.util.LinkedHashMap;
import java.util.Set;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToLongConverter;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

import hu.sol.j2eeapp.bean.Task;
import hu.sol.j2eeapp.bean.User;
import hu.sol.j2eeapp.service.DeleteService;
import hu.sol.j2eeapp.service.GetService;
import hu.sol.j2eeapp.service.RegistrationService;
import hu.sol.j2eeapp.service.UpdateService;

public class TaskSubView extends VerticalLayout {
	public static final String NAME = "tasks";

	private GetService getService;
	private RegistrationService registrationService;
	private UpdateService updateService;
	private DeleteService deleteService;

	private User user;

	protected Window editTaskWindow;
	private Set<Task> selected;
	private LinkedHashMap<Long, String> users = new LinkedHashMap<>();

	private Grid<Task> grid;

	private CheckBox showOnlyActive;

	public TaskSubView(GetService getService, RegistrationService registrationService, UpdateService updateService,
			DeleteService deleteService, User user) {
		super();
		this.getService = getService;
		this.registrationService = registrationService;
		this.updateService = updateService;
		this.deleteService = deleteService;

		this.user = user;

		this.addComponent(createSortLayout());
		this.addComponent(createTaskTable());
		this.addComponent(createFunctionLayout());

	}

	private Component createSortLayout() {
		HorizontalLayout sort = new HorizontalLayout();
		sort.setCaption("Sorting");
		showOnlyActive = new CheckBox("Hide done");
//		showOnlyActive.addValueChangeListener(event -> {
//			if (showOnlyActive.getValue()){
//				grid.setItems(getService.getUserTaskListDone(user.getId(), false));
//				grid.getDataProvider().refreshAll();
//			} else{
//				refreshGrid();
//			}
//		});
		sort.addComponent(showOnlyActive);
		return sort;
	}

	@SuppressWarnings("unchecked")
	private Component createTaskTable() {
		grid = new Grid("Tasks");
		grid.setSizeFull();

		refreshGrid();

		MultiSelectionModel<Task> selectionModel = (MultiSelectionModel<Task>) grid
				.setSelectionMode(SelectionMode.MULTI);

		grid.addColumn(Task::getId).setCaption("ID").setId("id").setWidth(100);
		grid.addColumn(Task::getTaskName).setCaption("TASKNAME").setId("taskname").setWidth(300);
		grid.addColumn(Task::getDescription).setCaption("DESCRIPTION").setId("description");
		grid.addColumn(Task::isDone).setCaption("DONE").setId("done").setWidth(100);

		grid.addSelectionListener(event -> {
			selected = event.getAllSelectedItems();
		});

		grid.addColumn(Task -> "Edit", new ButtonRenderer(clickEvent -> {
			openEditWindow((Task) clickEvent.getItem(), "Edit task");
		})).setCaption("EDIT").setWidth(100);

		return grid;
	}

	private Component createFunctionLayout() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.setMargin(true);

		Button addTaskButton = new Button("Add task");
		addTaskButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		addTaskButton.addClickListener(event -> {
			Task task = new Task();
			openEditWindow(task, "Add task");
		});

		horizontalLayout.addComponent(addTaskButton);

		Button deleteSelectedTasksButton = new Button("Delete selected");
		deleteSelectedTasksButton.setStyleName(ValoTheme.BUTTON_DANGER);

		deleteSelectedTasksButton.addClickListener(event -> {
			if (selected.isEmpty()) {
				Notification.show("Select at least 1 task", Notification.TYPE_WARNING_MESSAGE)
						.setPosition(Position.TOP_CENTER);
			} else {

				ConfirmDialog.show(getUI(), "Are you sure?", dialog -> {
					if (dialog.isConfirmed()) {
						for (Task u : selected) {
							try {
								deleteService.deleteTask(u.getId());
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

		horizontalLayout.addComponent(deleteSelectedTasksButton);
		return horizontalLayout;

	}

	private void openEditWindow(Task task, String title) {
		editTaskWindow = new Window(title);
		editTaskWindow.setHeight("70%");
		editTaskWindow.setWidth("70%");
		editTaskWindow.center();
		editTaskWindow.setContent(createTaskEditLayout(task));
		getUI().addWindow(editTaskWindow);
	}

	protected Component createTaskEditLayout(Task task) {
		VerticalLayout verticalLayout = new VerticalLayout();
		final Binder<Task> taskBinder = new Binder<Task>(Task.class);
		taskBinder.setBean(task);
		verticalLayout.addComponent(createTaskEditForm(taskBinder));

		return verticalLayout;
	}

	private void refreshGrid() {
		grid.setItems(getService.getUserTaskList(user.getId()));
		grid.getDataProvider().refreshAll();
	}

	private Component createTaskEditForm(Binder<Task> taskBinder) {
		FormLayout formLayout = new FormLayout();

		Label validationStatus = new Label();
		validationStatus.setStyleName(ValoTheme.LABEL_BOLD);
		taskBinder.setStatusLabel(validationStatus);
		formLayout.addComponent(validationStatus);

		TextField idField = new TextField("ID");
		taskBinder.forField(idField).withNullRepresentation("")
				.withConverter(new StringToLongConverter(idField.getValue())).bind(Task::getId, Task::setId);
		idField.setEnabled(false);
		formLayout.addComponent(idField);

		TextField userField = new TextField("User ID");
		taskBinder.forField(userField).withNullRepresentation("")
				.withConverter(new StringToLongConverter(userField.getValue())).bind(Task::getUserId, Task::setUserId);
		userField.setEnabled(false);
		userField.setValue(user.getId().toString());
		formLayout.addComponent(userField);

		// ComboBox<String> userCombo = new ComboBox<>("User");
		// for (User user : getService.getUserList()) {
		// users.put(user.getId(), user.getName() + " (ID:" + user.getId() +
		// ")");
		// }
		// userCombo.setItems(users.values());
		// userCombo.setEmptySelectionAllowed(false);
		// taskBinder.forField(userCombo).asRequired("User may not be
		// empty").withConverter(new StringToLongConverter(userCombo.getValue().
		// substring((userCombo.getSelectedItem().toString().indexOf("(")+4),
		// userCombo.getValue().indexOf(")")))).bind(Task::getUserId,
		// Task::setUserId);

		// System.err.println(userCombo.getSelectedItem().toString());
		// formLayout.addComponent(userCombo);

		TextField taskNameField = new TextField("Task name");
		taskBinder.forField(taskNameField).asRequired("Task name may not be empty").bind(Task::getTaskName,
				Task::setTaskName);
		formLayout.addComponent(taskNameField);

		TextField descriptionField = new TextField("Description");
		taskBinder.forField(descriptionField).bind(Task::getDescription, Task::setDescription);
		formLayout.addComponent(descriptionField);

		CheckBox done = new CheckBox("Done");
		taskBinder.forField(done).bind(Task::isDone, Task::setDone);
		formLayout.addComponent(done);

		Button saveButton = new Button("Save");
		saveButton.setEnabled(false);
		saveButton.addClickListener(event -> {
			try {
				Task bean = taskBinder.getBean();
				if (bean.getId() == null || bean.getId().toString().isEmpty()) {
					registrationService.RegisterTask(bean);
				} else {
					updateService.updateTask(bean);
				}
				editTaskWindow.close();
				Notification.show("Success").setPosition(Position.TOP_CENTER);
				refreshGrid();
			} catch (Exception e) {
				Notification.show(e.getClass().getSimpleName() + e.getMessage(), Notification.TYPE_ERROR_MESSAGE)
						.setPosition(Position.TOP_CENTER);
			}
		});

		formLayout.addComponent(saveButton);

		taskBinder.addStatusChangeListener(event -> saveButton.setEnabled(taskBinder.isValid()));

		return formLayout;
	}

}
