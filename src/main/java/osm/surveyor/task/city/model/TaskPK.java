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
	private String citycode;
	private String meshcode;
	
	public TaskPK() {
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof TaskPK) {
			if (((TaskPK)obj).getCitycode().equals(this.citycode)) {
				if (((TaskPK)obj).getMeshcode().equals(this.meshcode)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int hashCode() {
		String s = this.citycode + this.meshcode;
		return s.hashCode();
	}
}
