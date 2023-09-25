package osm.surveyor.task.city.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import osm.surveyor.task.util.Point;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Entity
@Table(name = "city")
public class City {

	@Id
	@NumberFormat(pattern="#####")
	private String citycode;
	
	@NotBlank
	private String cityname;
	
	@NotBlank
	private String folder;
	
	@NotBlank
	@NumberFormat
	private String lng = "0.0";
	
	@NotBlank
	@NumberFormat
	private String lat = "0.0";
	
	private String site;
	
	
	/**
	 * ステータス
	 */
	@Enumerated(EnumType.STRING)
	private Status status = Status.PREPARATION;
	
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
	
	@JsonIgnore
	public void setPoint(BigDecimal lng, BigDecimal lat) {
		setLng(lng);
		setLat(lat);
	}
	
	@JsonIgnore
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
