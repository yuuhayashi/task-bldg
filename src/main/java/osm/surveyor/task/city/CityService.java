package osm.surveyor.task.city;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import osm.surveyor.task.city.model.City;
import osm.surveyor.task.city.model.Citymesh;
import osm.surveyor.task.city.model.Status;

@Service
@Transactional // メソッド開始時にトランザクションを開始、終了時にコミットする
public class CityService {

	@Autowired
	CityRepository repository;
	
	@Autowired
	CitymeshRepository meshRepository;

	public List<City> getAll() {
		return repository.findAll(Sort.by(Sort.Direction.ASC, "citycode"));
	}
	
	/**
	 * ステータスを更新
	 * @param task
	 */
	public void updateStatus(String citycode) {
		List<Citymesh> list = meshRepository.findByCitycode(citycode);
		double cnt = 0;
		double err = 0;
		double edit = 0;
		double resv = 0;
		double total = list.size();
		for (Citymesh mesh : list) {
			Status status = mesh.getStatus();
			switch (status) {
			case OK:
				cnt++;
				break;
			case NG:
				err++;
				break;
			case EDITING:
				edit++;
				break;
			case RESERVED:
				resv++;
				break;
			case ACCEPTING:
				resv++;
				break;
			default:
				break;
			}
		}
		
		City city = getCity(citycode);
		if (err > 0) {
			city.setStatus(Status.NG);
		}
		else if ((cnt / total) > 0.99d) {
			city.setStatus(Status.OK);
		}
		else if ((cnt / total) > 0.9d) {
			city.setStatus(Status.EDITING);
		}
		else if (edit > 0d) {
			city.setStatus(Status.RESERVED);
		}
		else if (resv > 0d) {
			city.setStatus(Status.ACCEPTING);
		}
		else {
			city.setStatus(Status.PREPARATION);
		}
		repository.save(city);
	}

	public void store(City city) {
		repository.save(city);
	}

	public City getCity(String citycode) {
		return repository.findByCitycode(citycode);
	}

	public void deleteCity(String citycode) {
		repository.deleteByCitycode(citycode);
	}

}
