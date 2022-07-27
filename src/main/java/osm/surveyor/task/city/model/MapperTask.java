package osm.surveyor.task.city.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.springframework.format.annotation.NumberFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(MapperTaskPK.class)
public class MapperTask {

	@Id
	private String username;	// MapperTaskPK.username
	
	@Id
	@NumberFormat
	private String meshcode;	// MapperTaskPK.meshcode

	@Enumerated(EnumType.ORDINAL)
	private Operation operation = Operation.NOP;

	@Enumerated(EnumType.ORDINAL)
	private Status status = Status.PREPARATION;
	
}
