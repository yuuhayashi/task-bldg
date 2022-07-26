package osm.surveyor.task.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonGeometryLine extends JsonTemple {
	
	/**
	 *	{
	 *		"coordinates":[
	 *			[141.35,42.9],
	 *			[141.36249999999998,42.9],
	 *			[141.36249999999998,42.90833333333333],
	 *			[141.35,42.90833333333333],
	 *			[141.35,42.9]
	 *		],
	 *		"type":"LineString"
	 *	}
	 */

	private List<Point> coordinates;
	
	private String type = "LineString";
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		boolean first = false;
		sb.append("{");
		if (coordinates != null) {
			first = true;
			boolean c1 = false;
			sb.append("[");
			for (JsonNumberArray arry : coordinates) {
				c1 = out(c1, sb, null, arry);
			}
			sb.append("]");
		}
		if (type != null) {
			first = outStr(first, sb, "type", getType());
		}
		sb.append("}");
		return sb.toString();
	}
	
	public void parse(JsonNode node) {
		JsonNode node1 = node.get("type");
		if (node1 != null) {
			this.type = node1.textValue();
		}
		
		node1 = node.get("coordinates");
		if (node1 != null) {
			this.coordinates = new ArrayList<Point>();
			for (JsonNode node2 : node1) {
				Point point = new Point();
				point.parse(node2);
				this.coordinates.add(point);
			}
		}
	}
}
