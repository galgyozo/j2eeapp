package hu.sol.j2eeapp.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "task", schema = "public")
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "task_name", length = 50)
	private String taskName;

	@Column(name = "description", length = 500)
	private String description;

	@Column(name = "done")
	private Boolean done;

	@Override
	public String toString() {
		return "Task [id=" + id + ", userId=" + userId + ", taskName=" + taskName + ", description=" + description
				+ ", done=" + done + "]";
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean isDone() {
		return done;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}

}
