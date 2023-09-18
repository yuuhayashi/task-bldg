package osm.surveyor.task.util;

import java.math.BigDecimal;
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
			sb.append("\"coordinates\":[");
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
	
	/**
	 * このラインの中心点を取得する
	 * 各ラインの頂点の最大最小値の中間
	 */
	public Point getCenter() {
		if (coordinates == null) {
			return null;
		}
		double maxlng = -180.0;
		double maxlat = -90.0;
		double minlng = 180.0;
		double minlat = 90.0;
		for (JsonNumberArray arry : coordinates) {
			boolean first = true;
			List<String> lnglat = arry.getList();
			for (String str : lnglat) {
				double dd = Double.parseDouble(str);
				if (first) {
					if (maxlng < dd) {
						maxlng = dd;
					}
					if (minlng > dd) {
						minlng = dd;
					}
					first = false;
				}
				else {
					if (maxlat < dd) {
						maxlat = dd;
					}
					if (minlat > dd) {
						minlat = dd;
					}
				}
			}
		}
		double lng = (minlng + maxlng) / 2;
		double lat = (minlat + maxlat) / 2;

		Point point = new Point();
		point.setLat(BigDecimal.valueOf(lat));
		point.setLng(BigDecimal.valueOf(lng));
		return point;
	}
}
