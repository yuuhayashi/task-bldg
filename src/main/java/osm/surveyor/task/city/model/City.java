package osm.surveyor.task.city.model;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import osm.surveyor.task.util.Point;

@Getter
@Setter
@Entity
public class City {
	private static String site;		// 全体に適用する
	
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
	
	public void setSite(String site) {
		City.site = site;
	}
	
	public String getSite() {
		return City.site;
	}
	
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
	
	public void setPoint(String lng, String lat) {
		setLng(lng);
		setLat(lat);
	}
	
	public Point getPoint() {
		Point point = new Point();
		point.setLng(getLng());
		point.setLat(getLat());
		return point;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"code\":\""+ getCitycode() +"\"");
		sb.append(",\"name\":\""+ getCityname() +"\"");
		sb.append(",\"path\":\""+ getFolder() +"\"");
		sb.append(",\"coordinates\":"+ getPoint().toString());
		sb.append("}");
		return sb.toString();
	}
}
