/**
 * 
 */
var $mj = jQuery.noConflict();
var lat = -34.86157;
var lon = -56.17938;
var zoom = 15;
var map;
var zmap;
var selectarea = null;
var results;


const myIcon = L.icon({
	iconUrl: '../resources/images/map/iconmap.png',
	iconSize: [32, 43],
	iconAnchor: [16, 43],
	popupAnchor: [0, -43]

});

var marker = L.marker([lat, lon], {
	icon: myIcon,
	draggable: true,
	autoPan: true,
});

var geocodeService = L.esri.Geocoding.geocodeService({
	apikey: 'AAPK46e02c88e9284088aa4476b1780a56d1BzwMDybORolvb0Ei8R9aTA6SwflWgmZXBVm9zjrPh5BQQJsGIxtSf8hKHXwVMtN-' // replace with your api key - https://developers.arcgis.com
});

var searchControl = L.esri.Geocoding.geosearch({
	position: 'topright',
	placeholder: 'Ingrese direcci\u00F3n o sitio, e.j. Palacio Legislativo, Montevideo',
	useMapBounds: false,
	providers: [L.esri.Geocoding.arcgisOnlineProvider({
		apikey: 'AAPK46e02c88e9284088aa4476b1780a56d1BzwMDybORolvb0Ei8R9aTA6SwflWgmZXBVm9zjrPh5BQQJsGIxtSf8hKHXwVMtN-', // replace with your api key - https://developers.arcgis.com
		nearby: {
			lat: lat,
			lng: lon
		}
	})]
});
/*
searchControl.on('results', function(data) {
	if (data.results.length > 0) {
		var popup = L.popup()
			.setLatLng(data.results[0].latlng)
			.setContent(data.results[0].text)
			.openOn(map);
		map.setView(data.results[0].latlng)
	}
})
*/

function initMap() {
	map = L.map('map').setView([lat, lon], zoom);
	map.invalidateSize();


	new L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
		attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
	}).addTo(map);

	results = L.layerGroup().addTo(map);

	map.addControl(searchControl);

	//results.addLayer(marker);

	map.on('click', function(e) {
		geocodeService.reverse().latlng(e.latlng).run(function(error, result) {
			if (error) {
				return;
			}

			results.clearLayers();

			var proj32721 = toProj32721(result.latlng.lat, result.latlng.lng);
			var strPOINT = 'POINT(' + proj32721['x'] + ' ' + proj32721['y'] + ')';

			updateLatLng(result.latlng.lat, result.latlng.lng);
			$mj("input[id*='addPoint']").val(strPOINT);
			$mj("input[id*='addAddress']").val(result.address.ShortLabel);
			$mj("input[id*='addAddressNumber']").val(result.address.AddNum);
			$mj("#textAddAdress").html(result.address.ShortLabel);

			results.addLayer(L.marker(result.latlng, { icon: myIcon }).bindPopup(result.address.ShortLabel).openPopup());

		});
	});



	searchControl.on('results', function(data) {
		results.clearLayers();
		for (var i = data.results.length - 1; i >= 0; i--) {

			var proj32721 = toProj32721(data.results[i].latlng.lat, data.results[i].latlng.lng);
			var strPOINT = 'POINT(' + proj32721['x'] + ' ' + proj32721['y'] + ')';

			updateLatLng(data.results[i].latlng.lat, data.results[i].latlng.lng);
			$mj("input[id*='addPoint']").val(strPOINT);
			$mj("input[id*='addAddress']").val(data.results[i].properties.ShortLabel);
			$mj("input[id*='addAddressNumber']").val(data.results[i].properties.AddNum);
			$mj("#textAddAdress").html(data.results[i].properties.ShortLabel);

			results.addLayer(L.marker(data.results[i].latlng, { icon: myIcon }).bindPopup(data.results[i].properties.ShortLabel).openPopup());
		}
	});

}

function getLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			lat = position.coords.latitude;
			lon = position.coords.longitude;
			/*  Seteo la lat, long y zoom del mapa  */
			map.setView([lat, lon], zoom);

			results.clearLayers();
			//results.addLayer(L.marker([lat, lon], { icon: myIcon }).bindPopup('Ubicaci\u00F3n actual'));

		});
	} else {
		Console.log("ERROR GEOLOCALIZANDO");
	}
}


function updateLatLng(la, lo) {
	lat = la;
	lon = lo;
}

function toProj32721(lat, lon) {

	var proj4326 = new Proj4js.Proj('EPSG:4326');    //source coordinates will be in Longitude/Latitude
	var proj32721 = new Proj4js.Proj('EPSG:32721');     //destination coordinates in LCC, south of France

	var coordinates = L.Projection.LonLat.project(L.latLng(lat, lon));

	//console.log(coordinates);

	var point = new Proj4js.Point(coordinates.x, coordinates.y);   //any object will do as long as it has 'x' and 'y' properties
	//console.log(point)
	var point32721 = Proj4js.transform(proj4326, proj32721, point);      //do the transformation.  x and y are modified in place

	//	console.log(point32721);
	return point32721;
}


function initMapSelectRegion() {

	var customControl = L.Control.extend({
		options: {
			position: 'topleft'
		},
		onAdd: function(map) {
			var container = L.DomUtil.create('div', 'leaflet-bar leaflet-control leaflet-control-custom');
			container.style.backgroundColor = '#ffffff';
			container.style.margin = '-2px 10px 10px 10px';
			container.style.width = '34px';
			container.style.height = '34px';

			var link = L.DomUtil.create('a', '', container);
			link.innerHTML = '<img src="../resources/images/map/mapspolygon.svg" style="width: 20px">';
			link.href = '#';
			link.title = 'Establecer zona reparto';
			link.style.width = '28px';
			link.style.height = '28px';
			link.style.backgroundColor = '#ffffff';

			container.onclick = function() {

				if (selectarea == null || (!selectarea._enabled)) {
					container.style.backgroundColor = '#008037';
					link.style.backgroundColor = '#008037';

					selectarea = map.selectAreaFeature.enable();
					selectarea.options.color = '#030303';
					selectarea.options.weight = 3;
				} else {
					container.style.backgroundColor = '#ffffff';
					link.style.backgroundColor = '#ffffff';
					selectarea.disable();

				}

			};
			return container;
		}
	});

	map.addControl(new customControl());


}

function zonaReparto() {
	if (selectarea == null || (!selectarea._enabled)) {
		
		selectarea = map.selectAreaFeature.enable();
		selectarea.options.color = '#008037';
		selectarea.options.weight = 3;
	} else {
		selectarea.disable();

	}

}

function cleanZonaReparto(){
	selectarea.removeAllArea();
}

function confirmarZonaReparto(){
	if (selectarea != null ) {
		console.log(selectarea);
		console.log(selectarea.getFeaturesSelected('all'));
		$mj("input[id*='addDeliveryArea']").val(selectarea.getFeaturesSelected('all'));		
		selectarea.disable();
	}
	
}

