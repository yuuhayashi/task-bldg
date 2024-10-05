package osm.surveyor.task.mesh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // メソッド開始時にトランザクションを開始、終了時にコミットする
public class CitymeshService {

	@Autowired
	CitymeshRepository meshRepository;

}
