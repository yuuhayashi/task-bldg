const geojson = "index.geojson";

var __map = null;
var __markerLayer = null;
var __lineLayer = null;

var dir = "./";
var cityname = "";
var line = [];

var queryString = window.location.search;
var queryObject = new Object();
if(queryString){
	queryString = queryString.substring(1);
	var parameters = queryString.split('&');
	  
	for (var i = 0; i < parameters.length; i++) {
		var element = parameters[i].split('=');
	    
		var paramName = decodeURIComponent(element[0]);
		var paramValue = decodeURIComponent(element[1]);
	
		queryObject[paramName] = paramValue;
	}
}
  
const mesh = /*[[${mesh}]]*/"mesh";
const citycode = queryObject['citycode'];
const meshcode = queryObject['meshcode'];

function loadMap() {
	
	// 'citycode'に対応するデータを読む
	var lonlat = [139.7637,35.6808];
	$.when(
		$.getJSON("./city/index.json")
	).done(function(data1) {
		site = data1.site;
		$(data1.list).each(function() {
			if (this.code == citycode) {
				dir = this.path + "/bldg/";
				cityname = this.name;
				loadMesh(dir, citycode, meshcode);
			}
		})
	});


	// マップの作成
	__map = new ol.Map({
		target: 'map',
		layers: [
			new ol.layer.Tile({
				source: new ol.source.OSM()
			})
		],
		view: new ol.View({
			center: ol.proj.fromLonLat(lonlat),
			zoom: 15
		})
	});
	
    // 表示するためのレイヤーを作成する
    __markerLayer = new ol.layer.Vector({
        source: new ol.source.Vector()
    });
    __map.addLayer(__markerLayer);
    
    // ポップアップを表示するためのオーバーレイを作成する
    __overlay = new ol.Overlay({
        element: document.getElementById('popup'),
        positioning: 'bottom-center'
    });

	
    // 地図のクリックイベントを設定
    __map.on('click', function (evt) {
        var feature = __map.forEachFeatureAtPixel(
			evt.pixel,
            function (feature) {
                return feature;
            }
        );
        if (feature) {
            var coordinates = feature.getGeometry().getCoordinates();
            var info = feature.information;
            var element = __overlay.getElement();
            var descriptionHTML =
                "<div>code: " + info.properties.id + "</div>" +
                "<div>version: " + info.properties.version + "</div>" +
                "<div><a href='"+ site + dir + info.properties.path +"'>" + info.properties.path + "</a></div>";
            element.innerHTML = descriptionHTML;
            __overlay.setPosition(coordinates);
            __map.addOverlay(__overlay);
        } else {
            __map.removeOverlay(__overlay);
        }
    });


}


// マップ表示のための中心位置を読み取る
function loadMesh(dir) {
	$.when(
		$.getJSON("/city/"+ dir + geojson)
	).done(function(data2) {
		features = data2.features;
	    $(features).each(function() {
			if (this.properties != null) {
				if (this.properties.id == meshcode) {
					if (this.geometry != null) {
						if (this.geometry.type == "Point") {
						    lonlat = this.geometry.coordinates;	// 中心位置
							
							// マーカーの表示
						    featurePoint = new ol.Feature({
						        geometry: new ol.geom.Point(ol.proj.fromLonLat(lonlat))
						    });
						    featurePoint.information = this;
						    featurePoint.setStyle(new ol.style.Style({
							    image: new ol.style.Icon({
							        src: './img/osm_200x200.png',
							        anchor: [0.5, 0.5],
							        scale: 0.2
							    }),
							    stroke: new ol.style.Stroke({color: '#ff33ff', width: 10})
							}));
						    __markerLayer.getSource().addFeature(featurePoint);
						    __map.getView().setCenter(ol.proj.fromLonLat(lonlat));
						}
						if (this.geometry.type == "LineString") {
							drawPolygon([this.geometry.coordinates]);
						}
					}
				}
			}
	    });
	});
}


/**
 * ポリゴンを描画する
 ret: routeLayer
 */
function drawPolygon(coordinates) {
    // ジオメトリの作成
    var polygon = new ol.geom.Polygon([]);
    polygon.setCoordinates(coordinates);

    // 地物オブジェクトの作成　〜　レイヤーの作成
    var feature = new ol.Feature(
        polygon.transform('EPSG:4326', 'EPSG:3857')
    );
    feature.setId(meshcode);

    var vector = new ol.source.Vector({
        features: [feature]
    });
    routeLayer = new ol.layer.Vector({
        source: vector,
        style: new ol.style.Style({
            stroke: new ol.style.Stroke({ color: '#000000', width: 2 })
            //fill: new ol.style.Fill({ color: [0, 0, 0, 0.2] })
        })
    });

    // 作成したポリゴンをレイヤーにのせる
    __map.addLayer(routeLayer);
}

