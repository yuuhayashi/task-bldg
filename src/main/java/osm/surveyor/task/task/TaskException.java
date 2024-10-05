package osm.surveyor.task.task;

import lombok.Getter;
import lombok.Setter;
import osm.surveyor.task.task.model.TaskEntity;

@Setter
@Getter
public class TaskException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private TaskEntity task;
	
	public TaskException(String errorMessage) {
		super(errorMessage);
	}
}
