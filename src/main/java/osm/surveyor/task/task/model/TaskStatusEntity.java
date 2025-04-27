package osm.surveyor.task.task.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
