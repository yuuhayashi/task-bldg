package osm.surveyor.task.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Mapper {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 必須入力、(null,空白不可)
	@NotBlank
    @Size(max = 40)
	private String osmid;
	
	// 'Task' とのリレーション
	@ManyToOne
	private Task task;
}
