package osm.surveyor.task.city.model;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class City {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String citycode;
	
	@NotBlank
	private String cityname;
	
	@NotBlank
	private String folder;
	
	@NotBlank
	private String lng = "0.0";
	
	@NotBlank
	private String lat = "0.0";
	
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	
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
	
	public void setCoordinates(BigDecimal lng, BigDecimal lat) {
		setLng(lng);
		setLat(lat);
	}
	
	public void setCoordinates(String lng, String lat) {
		setLng(lng);
		setLat(lat);
	}
	
	public Coordinates getCoordinates() {
		Coordinates coordinates = new Coordinates();
		coordinates.setLng(getLng());
		coordinates.setLat(getLat());
		return coordinates;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"code\":\""+ getCitycode() +"\"");
		sb.append(",\"name\":\""+ getCityname() +"\"");
		sb.append(",\"path\":\""+ getFolder() +"\"");
		sb.append(",\"coordinates\":"+ getCoordinates().toString());
		sb.append("}");
		return sb.toString();
	}
}
