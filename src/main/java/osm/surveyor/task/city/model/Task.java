package osm.surveyor.task.city.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import osm.surveyor.task.util.JsonGeometryLine;
import osm.surveyor.task.util.Point;

@Getter
@Setter
@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String citycode;
	
	@NotBlank
	private String meshcode;
	
	@NotBlank
	private String version;
	
	@NotBlank
	private String path;
	
	private String point;
	
	private String line;
	
	public void setPoint(Point p) {
		this.point = p.toString();
	}
	
	public void setLine(JsonGeometryLine p) {
		this.line = p.toString();
	}
}
