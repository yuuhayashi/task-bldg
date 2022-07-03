package osm.surveyor.task.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
    @Size(max = 40)
	private String citycode;
	
	@NotBlank
    @Size(max = 40)
	private String meshcode;
	
	@NotBlank
    @Size(max = 40)
	private String cityname;
	
	// 'Mapper' とのリレーション
	@OneToMany(mappedBy = "task")
	private List<Mapper> mappers;
}
