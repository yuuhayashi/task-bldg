package osm.surveyor.task.city.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadForm {
	private MultipartFile multipartFile;
}
