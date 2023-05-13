package osm.surveyor.task.city.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.NumberFormat;

import lombok.Getter;
import lombok.Setter;
import osm.surveyor.task.util.JsonGeometryLine;

@Getter
@Setter
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
	City city;					// リレーション: to City 多対１
	
	private String version;
	
	private String path;
	
	private String point;
	
	private String line;
	
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
