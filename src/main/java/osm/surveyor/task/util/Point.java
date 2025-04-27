package osm.surveyor.task.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Point extends JsonNumberArray {
	
	@NotBlank
	@NumberFormat
	private String lng = "0.0";
	
	@NotBlank
	@NumberFormat
	private String lat = "0.0";

	public void setLng(String str) {
		this.lng = str;
	}

	public void setLng(BigDecimal dec) {
		this.lng = dec.toString();
	}

	public void setLat(String str) {
		this.lat = str;
	}

	public void setLat(BigDecimal dec) {
		this.lat = dec.toString();
	}
	
	public void setPoint(Point p) {
		setPoint(p.getLng(), p.getLat());
	}
	
	public void setPoint(BigDecimal lng, BigDecimal lat) {
		setLng(lng);
		setLat(lat);
	}
	
	public void setPoint(String lng, String lat) {
		setLng(lng);
		setLat(lat);
	}
	
	private void store() {
		List<String> list = new ArrayList<>();
		list.add(getLng());
		list.add(getLat());
		setList(list);
	}
	
	/*
	 * [141.35625,42.90416666666667]
	 */
	@Override
	public String toString() {
		store();
		return super.toString();
	}
	
	public void parse(JsonNode node) {
		if (node != null) {
			boolean one = false;
			for (JsonNode node2 : node) {
				if (!one) {
					setLng(node2.asText());
					one = true;
				}
				else {
					setLat(node2.asText());
				}
			}
		}
	}
	
	/*
	 *	{
	 *		"geometry":{
	 *			"coordinates":[141.35625,42.90416666666667],
	 *			"type":"Point"
	 *		},
	 *		"type":"Feature",
	 *		"properties":{
	 *			"path":"64412288_bldg_6697_op.zip",
	 *			"id":"64412288",
	 *			"version":"1.4.6"
	 *		}
	 *	}
	 */
	public String getGeometry() {
		store();
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"geometry\":{");
			sb.append("\"type\":\"Point\"");
			sb.append(",\"coordinates\":"+ toString());
			sb.append("}");
		sb.append("}");
		return sb.toString();
	}
}
