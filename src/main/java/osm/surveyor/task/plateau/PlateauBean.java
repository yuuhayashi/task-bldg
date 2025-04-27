package osm.surveyor.task.plateau;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PlateauBean {

	@Value("${app.plateau.dir}")
	private String plateauDirName;
	
    public String getPlateauDirName() {
        return plateauDirName;
    }
    
}
