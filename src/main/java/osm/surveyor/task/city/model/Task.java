package osm.surveyor.task.city.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;
import osm.surveyor.task.util.JsonTemple;

@Getter
@Setter
@Entity
public class Task extends JsonTemple {
	
    @Id
    @Column(name = "current_id")
    private String currentId;
    
	@NotBlank
    @Column(name = "pre_id")
    private String preId;

	@NumberFormat
	private String citycode;	// CitymeshPK.citycode

	@NumberFormat
	private String meshcode;	// CitymeshPK.meshcode
	
	@ManyToOne
	Citymesh mesh;				// リレーション: to Citymesh 多対１

	/**
	 * ステータス
	 */
	@Enumerated(EnumType.STRING)
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
	 * 変更セットNo
	 */
	@NumberFormat
    @Column(name = "changeset")
	private String changeSet;
	
	/**
	 * コメント
	 */
	private String comment;
	
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
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		boolean c1 = false;
		sb.append("{");
		c1 = outStr(c1, sb, "currentId", this.getCurrentId());
		c1 = outStr(c1, sb, "preId", this.getPreId());
		c1 = outStr(c1, sb, "citycode", this.getCitycode());
		c1 = outStr(c1, sb, "meshcode", this.getMeshcode());
		c1 = outStr(c1, sb, "status", this.getStatus().toString());
		c1 = outStr(c1, sb, "username", this.getUsername());
		c1 = outStr(c1, sb, "validator", this.getValidator());
		c1 = outStr(c1, sb, "changeSet", this.getChangeSet());
		c1 = outStr(c1, sb, "comment", this.getComment());
		c1 = outStr(c1, sb, "operation", this.getOperation().toString());
		c1 = outStr(c1, sb, "updateTime", this.getUpdateTime().toString());
		sb.append("}");
		return sb.toString();
	}
	
	@Override
	public void parse(JsonNode node) {
		// TODO
	}
}
