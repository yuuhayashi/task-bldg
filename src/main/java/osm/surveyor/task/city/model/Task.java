package osm.surveyor.task.city.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.NumberFormat;

import lombok.Getter;
import lombok.Setter;
import osm.surveyor.task.util.JsonGeometryLine;

@Getter
@Setter
@Entity
@IdClass(TaskPK.class)
public class Task {

	@Id
	@NumberFormat(pattern="#####")
	private String citycode;	// TaskPK.citycode
	
	@Id
	@NumberFormat
	private String meshcode;	// TaskPK.meshcode

	@NotBlank
	private String version;
	
	@NotBlank
	private String path;
	
	private String point;
	
	private String line;
	
	public void setLine(JsonGeometryLine p) {
		this.line = p.toString();
	}
}
