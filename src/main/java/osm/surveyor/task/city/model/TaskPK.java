package osm.surveyor.task.city.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Embeddable
public class TaskPK implements Serializable {
	private String currentId;
	
	public TaskPK() {
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof TaskPK) {
			if (((TaskPK)obj).getCurrentId().equals(this.currentId)) {
				return true;
			}
		}
		return false;
	}
	
	public int hashCode() {
		String s = this.currentId;
		return s.hashCode();
	}
}
