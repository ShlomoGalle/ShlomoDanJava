package geometries;

import primitives.Point3D;
import primitives.Ray;

import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable {
    private List<Intersectable> _intersectables = new LinkedList<>();
    
    public Geometries(Intersectable... list) {
        add(list);
    }

    public void add(Intersectable... list) {
        Collections.addAll(_intersectables, list);
    }


	@Override
	public List<GeoPoint> findGeoIntersections(Ray ray) {
        List<GeoPoint> result = null;
        for (Intersectable item : _intersectables) {
            //get intersections points of a particular item from _intersectables
            List<GeoPoint> itempoints = item.findGeoIntersections(ray);
            if(itempoints!= null){
                //first time initialize result to new LinkedList
                if(result== null){
                    result= new LinkedList<>();
                }
                //add all item points to the resulting list
                result.addAll(itempoints);
            }
        }
        return result;
	}
	
}