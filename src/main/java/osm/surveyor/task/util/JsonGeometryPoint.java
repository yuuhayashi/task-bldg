package osm.surveyor.task.util;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

/**
 * 	{
 * 		"coordinates":[141.35625,42.90416666666667],
 * 		"type":"Point"
 * 	}
 */
@Getter
@Setter
public class JsonGeometryPoint extends JsonTemple {
	private Point point;
	
	private String type = "Point";
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		boolean c = false;
		sb.append("{");
		c = out(c, sb, "coordinates", this.point.toString());
		c = outStr(c, sb, "type", this.type);
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
			this.point = new Point();
			this.point.parse(node1);
		}
	}
}
