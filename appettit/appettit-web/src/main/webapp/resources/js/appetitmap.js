/**
 * 
 */
	var lat = -34.86157;
	var lon = -56.17938;
	var zoom = 15;
	var map;
	var selectarea = null;
	
	
	
	const myIcon = L.icon({
		    iconUrl: '../resources/images/map/iconmap.png',
		    iconSize: [32, 43],
		    iconAnchor: [16, 43],
		    popupAnchor: [-3, -76]
		    
	});
	
	const marker = L.marker([lat, lon],{
		icon: myIcon,
	    draggable: true,
	    autoPan: true,
	});	
	
	//var markers = L.MarkerClusterGroup();
	//markers.addLayer(marker);
	
	const search = new GeoSearch.GeoSearchControl({
		notFoundMessage: 'Lo sentimos, la direcci&oacute;n no fue localizada.',
		provider: new GeoSearch.OpenStreetMapProvider(),
		style: 'bar',
		searchLabel: 'Ingrese direcci\u00F3n',
		zoomLevel: zoom,
		showMarker: true,
		marker: { icon: myIcon,
				  draggable: false,
				},
	});
		
	
	function initMap(){
		map = L.map('map').setView([lat, lon], zoom);
		new L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
			attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
		}).addTo(map);
		
		
		map.addControl(search);
		
		marker.addTo(map);
		
		marker.on('dragend', function (e) {
			updateLatLng(marker.getLatLng().lat, marker.getLatLng().lng);
		});
		
				
		map.on('geosearch/showlocation', () => {
			if (marker) {
				map.removeControl(marker);
			}
		});

	}
	
	function getLocation() {
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(function(position) {
				lat = position.coords.latitude;
				lon = position.coords.longitude;
				/*  Seteo la lat, long y zoom del mapa  */
    			map.setView([lat,lon], zoom);
			});
		} else { 
			Console.log("ERROR GEOLOCALIZANDO");	
		}
	}
		
	
	function updateLatLng(lat,lng) {
		lat = marker.getLatLng().lat;
		lon = marker.getLatLng().lng;
	}
	


	function initMapSelectRegion() {

		var customControl =  L.Control.extend({
			options: {
				position: 'topleft'
			},
			onAdd: function (map) {
				var container = L.DomUtil.create('div', 'leaflet-bar leaflet-control leaflet-control-custom');
			  	//container.style.backgroundColor 	= 'var(--main-bg-input-color)';
			    container.style.width 				= '30px';
			    container.style.height 				= '30px';
		
			    var link 	= L.DomUtil.create('a', '', container);
		        link.innerHTML 				= '<img src="../images/maps/mapspolygon.svg" style="width: 18px; height:18px">';
		        link.href 					= '#';
		        link.title 					= 'Establecer zona reparto';
		        link.style.padding			= '3px';
		        link.style.width 			= '24px';
		        link.style.height 			= '24px';
		        link.style.backgroundColor 	= '#f2a22c';
			    
			    container.onclick = function(){
			    	
			    	if(selectarea==null || (!selectarea._enabled)){
			    		container.style.backgroundColor = '#008037';
			    		link.style.backgroundColor 		= '#008037';
			    			
			    		selectarea	= map.selectAreaFeature.enable();
			    		selectarea.options.color = '#030303';
				    	selectarea.options.weight = 3 ;
			    	}else{
			    		container.style.backgroundColor = '#f2a22c';
			    		link.style.backgroundColor 		= '#f2a22c';
			    		selectarea.disable();
			    		
			    		
			    	}
				        	
			    };	
		        return container;
			}
		});
		
		map.addControl(new customControl());
	
	}	
		
	