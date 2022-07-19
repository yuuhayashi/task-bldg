package osm.surveyor.task.city.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Embeddable
public class CityPK implements Serializable {
	private String citycode;
	
	public CityPK() {
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof CityPK) {
			if (((CityPK)obj).getCitycode().equals(this.citycode)) {
				return true;
			}
		}
		return false;
	}
	
	public int hashCode() {
		String s = this.citycode;
		return s.hashCode();
	}
}
