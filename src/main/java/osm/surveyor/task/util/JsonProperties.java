package osm.surveyor.task.util;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonProperties {
	/*
	 *	"properties":{
	 *		"id":"64412288"
	 *	}
	 */
	private String name;
	private String path;
	private String id;
	private String version;
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		boolean c1 = false;
		sb.append("{");
		c1 = Geojson.outStr(c1, sb, "name", this.name);
		c1 = Geojson.outStr(c1, sb, "path", this.path);
		c1 = Geojson.outStr(c1, sb, "id", this.id);
		c1 = Geojson.outStr(c1, sb, "version", this.version);
		sb.append("}");
		return sb.toString();
	}
	
	public void parse(JsonNode node) {
		JsonNode node1 = node.get("name");
		if (node1 != null) {
			this.name = node1.textValue();
		}
		
		node1 = node.get("path");
		if (node1 != null) {
			this.path = node1.textValue();
		}
		
		node1 = node.get("id");
		if (node1 != null) {
			this.id = node1.textValue();
		}

		node1 = node.get("version");
		if (node1 != null) {
			this.version = node1.textValue();
		}
	}
}
