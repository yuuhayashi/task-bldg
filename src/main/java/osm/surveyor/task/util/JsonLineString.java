package osm.surveyor.task.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonLineString {
	
	private List<Point> line = new ArrayList<>();
	
	public void addPoint(Point point) {
		this.line.add(point);
	}
	
	/*
	 *	[
	 *		[141.35,42.9],
	 *		[141.36249999999998,42.9],
	 *		[141.36249999999998,42.90833333333333],
	 *		[141.35,42.90833333333333],
	 *		[141.35,42.9]
	 *	]
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		boolean first = true;
		for (Point point : this.line) {
			if (first) {
				first = false;
			}
			else {
				sb.append(",");
			}
			sb.append(point.toString());
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 *	{
	 *		"geometry":{
	 *			"coordinates":[
	 *				[141.35,42.9],
	 *				[141.36249999999998,42.9],
	 *				[141.36249999999998,42.90833333333333],
	 *				[141.35,42.90833333333333],
	 *				[141.35,42.9]
	 *			],
	 *			"type":"LineString"
	 *		},
	 *		"type":"Feature",
	 *		"properties":{"id":"64412288"}
	 *	}
	 */
	public String getGeometry() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"geometry\":{");
			sb.append("\"type\":\"LineString\"");
			sb.append(",\"coordinates\":"+ toString());
			sb.append("}");
		sb.append("}");
		return sb.toString();
	}
}
