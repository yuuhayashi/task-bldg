package osm.surveyor.task.city.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Embeddable
public class MapperTaskPK implements Serializable {
	private String username;
	private String meshcode;
	
	public MapperTaskPK() {
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof MapperTaskPK) {
			if (((MapperTaskPK)obj).getUsername().equals(this.username)) {
				if (((MapperTaskPK)obj).getMeshcode().equals(this.meshcode)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int hashCode() {
		String s = this.username + this.meshcode;
		return s.hashCode();
	}
}
