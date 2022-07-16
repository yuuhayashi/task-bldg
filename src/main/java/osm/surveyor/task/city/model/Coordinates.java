package osm.surveyor.task.city.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coordinates {
	
	private String lng = "0.0";
	
	private String lat = "0.0";
	
	public void setLng(String str) {
		this.lng = str; 
	}

	public void setLng(BigDecimal dec) {
		this.lng = dec.toString(); 
	}

	public void setLat(String str) {
		this.lat = str; 
	}

	public void setLat(BigDecimal dec) {
		this.lat = dec.toString(); 
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(getLng());
		sb.append(getLat());
		sb.append("]");
		return sb.toString();
	}
}
