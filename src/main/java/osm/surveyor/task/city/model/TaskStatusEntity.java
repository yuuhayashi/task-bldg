package osm.surveyor.task.city.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_status")
public class TaskStatusEntity {
	
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer status_code;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @Column(name = "is_reserve", nullable = false)
    private Boolean isReserve;
    
    @Column(name = "is_reserve_cancel", nullable = false)
    private Boolean isReserveCancel;
    
    @Column(name = "is_edit_ok", nullable = false)
    private Boolean isEditOk;
    
    @Column(name = "is_edit_ng", nullable = false)
    private Boolean isEditNg;
    
}
