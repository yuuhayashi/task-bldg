package osm.surveyor.task.city;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.city.model.Task;
import osm.surveyor.task.city.model.UploadForm;

@RequiredArgsConstructor
@Controller
public class IoController {
	private final TaskRepository taskRepository;
	
	@GetMapping("/io")
	public String io(Model model)
	{
		return "admin";
	}

	@PostMapping("/io/download")
    public String download(HttpServletResponse response) {
        try (OutputStream os = response.getOutputStream();) {
        	List<Task> list = taskRepository.findAll();
        	StringBuffer sb = new StringBuffer();
    		boolean c1 = false;
        	sb.append("[");
        	sb.append(System.lineSeparator());
        	for (Task task : list) {
    			if (c1) {
    				sb.append(",");
    			}
    			else {
    				c1 = true;
    			}
        		sb.append(task.toString());
            	sb.append(System.lineSeparator());
        	}
        	sb.append("]");
            byte[] fb1 = String.valueOf(sb).getBytes();

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename="+ "task-bldg.json");
            response.setContentLength(fb1.length);
            os.write(fb1);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	/**
	 * データファイルアップロード
	 * @param multipartFile
	 * @param model
	 * @return
	 */
	@PostMapping("/io/upload")
	public String upload(UploadForm uploadForm, Model model) {
	    model.addAttribute("originalFilename", uploadForm.getMultipartFile().getOriginalFilename());
	    return "upload";
	}
}
