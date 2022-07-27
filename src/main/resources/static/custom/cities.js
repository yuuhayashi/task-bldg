var __map;
var __markerLayer = null;
var __overlay = null;		// ポップアップを表示するためのレイヤー
var site = null;

function loadMap() {

	// マップの作成
	__map = new ol.Map({
		target: 'map',
		layers: [
			new ol.layer.Tile({
				source: new ol.source.OSM()
			})
		],
		view: new ol.View({
			center: ol.proj.fromLonLat([139.745433,35.658581]),
			zoom: 8
		})
	});

    // マーカーの見た目の作成
    var style = new ol.style.Style({
        image: new ol.style.Icon({
            src: '/img/osm_200x200.png',
            anchor: [0.5, 0.5],
            scale: 0.2
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

	$.when(
		$.getJSON("/city/index.json")
	).done(function(data) {
		site = data.site;
		style = new ol.style.Style({
		    image: new ol.style.Icon({
		        src: '/img/osm_200x200.png',
		        anchor: [0.5, 0.5],
		        scale: 0.2
		    }),
		    stroke: new ol.style.Stroke({color: '#ff33ff', width: 10})
		});

	    $(data.list).each(function() {
		    var feature = new ol.Feature({
		        geometry: new ol.geom.Point(ol.proj.fromLonLat(this.coordinates)),
		        code: this.code,
		        name: this.name
		    });
		    feature.information = this;
		    feature.setStyle(style);
		    __markerLayer.getSource().addFeature(feature);
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
	                "<div>" + info.code + "</div>" +
	                "<div>" + info.name + "</div>" +
	                "<div><a href='/mesh/"+ info.code +"'>" + info.path + "</a></div>";
	            element.innerHTML = descriptionHTML;
	            __overlay.setPosition(coordinates);
	            __map.addOverlay(__overlay);
	        } else {
	            __map.removeOverlay(__overlay);
	        }
	    });
	});
}
