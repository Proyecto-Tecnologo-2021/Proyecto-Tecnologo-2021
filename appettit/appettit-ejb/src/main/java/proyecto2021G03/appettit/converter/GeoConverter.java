package proyecto2021G03.appettit.converter;

import javax.ejb.Singleton;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

@Singleton
public class GeoConverter {
	
	WKTReader fromText = new WKTReader(new GeometryFactory(new PrecisionModel(), 32721)); 
	
	public Point strToPoint(String point) throws ParseException {
		Geometry geom = fromText.read(point);
		
		Point gpoint = (Point) geom;
		
		return gpoint;
	}

	public MultiPolygon strToMultiPolygon(String multipolygon) throws ParseException {
		Geometry geom = fromText.read(multipolygon);
		
		MultiPolygon mpolygon = (MultiPolygon) geom;
		
		return mpolygon;
	}
}
