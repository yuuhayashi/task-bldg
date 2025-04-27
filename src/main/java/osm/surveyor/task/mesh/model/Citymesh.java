package osm.surveyor.task.mesh.model;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import osm.surveyor.task.city.model.City;
import osm.surveyor.task.city.model.Status;
import osm.surveyor.task.util.JsonGeometryLine;
import osm.surveyor.task.util.Point;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Entity
@IdClass(CitymeshPK.class)
public class Citymesh {

	@Id
	@NumberFormat(pattern="#####")
	private String citycode;	// CitymeshPK.citycode
	
	@Id
	@NumberFormat
	private String meshcode;	// CitymeshPK.meshcode
	
	@ManyToOne
	@JsonIgnore
	City city;					// リレーション: to City 多対１
	
	@NotBlank
	@NumberFormat
	private String lng;
	
	@NotBlank
	@NumberFormat
	private String lat;
	
	private String version;
	
	private String surveyYear;
	
	private String path;
	
	private String line;
	
	@JsonIgnore
	public void setPoint(Point p) {
		setLng(p.getLng());
		setLat(p.getLat());
	}
	
	public void setLine(JsonGeometryLine p) {
		this.line = p.toString();
	}

	/**
	 * ステータス
	 */
	@Enumerated(EnumType.ORDINAL)
	private Status status = Status.PREPARATION;
	
	/**
	 * 編集者
	 */
	private String username;
	
}
