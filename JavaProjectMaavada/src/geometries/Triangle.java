package geometries;

import primitives.*;

import java.util.List;

public class Triangle extends Polygon {  //implements Geometry

    public Triangle(Point3D p1, Point3D p2, Point3D p3) {
        super(p1, p2, p3);
    }

    @Override
    public String toString() {
        return "Triangle{} " + super.toString();
    }
    
    @Override
    public List<Point3D> findIntersections(Ray ray) {
        return super.findIntersections(ray);
    }
    
    public List<GeoPoint> findGeoIntersections(Ray ray){
        return super.findGeoIntersections(ray);
    }
}