package osm.surveyor.task.city.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.NumberFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(MapperTaskPK.class)
public class MapperTask {
	
    @Id
    //@GenericGenerator(name = "UuidGenerator", strategy = "osm.surveyor.task.util.UuidGenerator") 
    //@GeneratedValue(generator = "UuidGenerator") 
    @Column(name = "current_id")
    private String currentId;
    
	@NotBlank
    @Column(name = "pre_id")
    private String preId;

	@Id
	@NumberFormat
	private String meshcode;	// MapperTaskPK.meshcode

	/**
	 * ステータス
	 */
	@Enumerated(EnumType.ORDINAL)
	private Status status = Status.PREPARATION;
	
	/**
	 * インポート実行者
	 */
	private String username;
	
	/**
	 * 検証者
	 */
	private String validator;
	
	/**
	 * 操作内容
	 */
	@Enumerated(EnumType.ORDINAL)
	private Operation operation = Operation.NOP;

	/**
	 * 更新日時
	 */
	@Temporal(TemporalType.TIMESTAMP)
    Date updateTime;
}
