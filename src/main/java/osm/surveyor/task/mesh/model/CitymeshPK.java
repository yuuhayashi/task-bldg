package osm.surveyor.task.mesh.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Embeddable
public class CitymeshPK implements Serializable {
	private String citycode;
	private String meshcode;
	
	public CitymeshPK() {
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof CitymeshPK) {
			if (((CitymeshPK)obj).getCitycode().equals(this.citycode)) {
				if (((CitymeshPK)obj).getMeshcode().equals(this.meshcode)) {
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
