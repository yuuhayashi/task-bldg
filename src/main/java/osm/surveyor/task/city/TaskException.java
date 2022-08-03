package osm.surveyor.task.city;

import lombok.Getter;
import lombok.Setter;
import osm.surveyor.task.city.model.Task;

@Setter
@Getter
public class TaskException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private Task task;
	
	public TaskException(String errorMessage) {
		super(errorMessage);
	}
}
