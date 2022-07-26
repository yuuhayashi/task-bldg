package osm.surveyor.task.util;

import java.util.Hashtable;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonProperties extends JsonTemple {
	/*
	 *	"properties":{
	 *		"id":"64412288"
	 *	}
	 */
	private String name;
	private String path;
	private String id;
	private String version;
	private Long prefcode;
	private String prefname;
	private Long citycode;
	private String cityname;
	private Long meshcode;
	
	public Hashtable<String,Object> items = new Hashtable<>();
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		boolean c1 = false;
		sb.append("{");
		c1 = outStr(c1, sb, "name", this.name);
		c1 = outStr(c1, sb, "path", this.path);
		c1 = outStr(c1, sb, "id", this.id);
		c1 = outStr(c1, sb, "version", this.version);
		c1 = out(c1, sb, "prefcode", getPrefcode().toString());
		c1 = outStr(c1, sb, "prefname", getPrefname());
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

		node1 = node.get("prefcode");
		if (node1 != null) {
			setPrefcode(node1.longValue());
		}

		node1 = node.get("prefname");
		if (node1 != null) {
			setPrefname(node1.textValue());
		}
	}
}
