package osm.surveyor.task.city.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import osm.surveyor.task.util.Point;

@Getter
@Setter
public class CityJson {
	
	private String code;
	
	private String name;
	
	private String path;
	
	private List<Double> coordinates = new ArrayList<>();
	
	public Point toCoordinates() {
		Point obj = new Point();
		if (this.coordinates.size() >= 2) {
			obj.setLng(Double.toString(this.coordinates.get(0)));
			obj.setLat(Double.toString(this.coordinates.get(1)));
			return obj;
		}
		return obj;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"code\":\""+ getCode() +"\"");
		sb.append(",\"name\":\""+ getName() +"\"");
		sb.append(",\"path\":\""+ getPath() +"\"");
		sb.append(",\"coordinates\":"+ getCoordinates().toString());
		sb.append("}");
		return sb.toString();
	}
}
