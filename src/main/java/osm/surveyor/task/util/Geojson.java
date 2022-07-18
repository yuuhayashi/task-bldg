package osm.surveyor.task.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Geojson {
	
	private String type = "FeatureCollection";
	
	private JsonCrs crs;
	
	private List<JsonFeature> features = new ArrayList<>();
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		boolean c = false;
		sb.append("{");
		c = Geojson.outStr(c, sb, "type", this.type);
		c = Geojson.out(c, sb, "crs", this.crs);
		if (c) {
			sb.append(",");
		}
		sb.append("\"features\":[");
		boolean c2 = false;
		for (JsonFeature f : features) {
			c2 = Geojson.out(c2, sb, null, f);
		}
		sb.append("]");
		
		sb.append("}");
		return sb.toString();
	}
	
	public void parse(JsonNode node) {
		setType(node.get("type").textValue());
		
		JsonNode nodeCrs = node.get("crs");
		if (nodeCrs != null) {
			this.crs = new JsonCrs();
			this.crs.parse(nodeCrs);
		}
		
		JsonNode f = node.get("features");
		if (f != null) {
			this.features = new ArrayList<JsonFeature>();
		    for (JsonNode f1 : f) {
		    	JsonFeature feature = new JsonFeature();
		    	feature.parse(f1);
		    	this.features.add(feature);
			}
		}
	}
	
	public static boolean out(boolean c, StringBuffer sb, String name, Object obj) {
		boolean c1 = c;
		if (obj != null) {
			if (c1) {
				sb.append(",");
			}
			else {
				c1 = true;
			}
			if (name != null) {
				sb.append("\""+ name +"\":");
			}
			sb.append(obj.toString());
		}
		return c1;
	}
	
	public static boolean outStr(boolean c, StringBuffer sb, String name, Object obj) {
		boolean c1 = c;
		if (obj != null) {
			if (c1) {
				sb.append(",");
			}
			else {
				c1 = true;
			}
			if (name != null) {
				sb.append("\""+ name +"\":");
			}
			sb.append("\""+ obj.toString() +"\"");
		}
		return c1;
	}
}
