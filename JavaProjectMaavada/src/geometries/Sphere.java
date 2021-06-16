package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.*;

public class Sphere extends RadialGeometry {
    final Point3D _center;

    public Sphere(double radius, Point3D center) {
        super(radius);
        _center = center;
    }
    
    public double getRadius() {
        return _radius;
    }
    
    public Point3D getCenter() {
        return _center;
    }

    @Override
    public Vector getNormal(Point3D p) {
        Vector p0_p = p.subtract(_center);
        return p0_p.normalize();
    }


	@Override
	public List<GeoPoint> findGeoIntersections(Ray ray) {

	        Point3D p0 = ray.getP0();
	        Point3D o = _center;
	        Vector v = ray.getDirection();
	        if (o.equals(p0)) {
	            return List.of(new GeoPoint(this, p0.add(v.scale(_radius))));
	        }
	        Vector u = o.subtract(p0);
	        double tm = v.dotProduct(u);
	        double d = alignZero(Math.sqrt(u.lengthSquared() - tm * tm));
	        if (d >= _radius) {
	            return null;
	        }
	        double th = alignZero(Math.sqrt(_radius * _radius - d * d));
	        double t1 = alignZero(tm - th);
	        double t2 = alignZero(tm + th);

	        if (t1 > 0 && t2 > 0) {
	            return (List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, ray.getPoint(t2))));
	        }
	        if (t1 > 0) {

	            return (List.of(new GeoPoint(this, ray.getPoint(t1))));
	        }
	        if (t2 > 0) {

	            return (List.of(new GeoPoint(this, ray.getPoint(t2))));
	        }
	        return null;
	}
	
	
    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray,double maxDistance) {
	    //exit the center point of the sphere.
	    if (getCenter().equals(ray.getP0())) {
	        return List.of(new GeoPoint(this,ray.getPoint(getRadius())));
	    }
	    //from top of the ray to center of the sphere.
	    Vector u = _center.subtract(ray.getP0());
	    //ray vector.
	    Vector v = ray.getDirection();
	
	    double tm = alignZero(u.dotProduct(v));
	    double d = alignZero(Math.sqrt(u.lengthSquared()-tm*tm));
	
	    //ray goes out of the sphere.
	    if(alignZero(d - getRadius())>=0){
	        return  null;
	    }
	
	    double th = alignZero(Math.sqrt(_radius*_radius-d*d));
	
	    //p is on the surface of the sphere.
	    if(isZero(th)){
	        return null;
	    }
	
	    double t1=alignZero(tm+th);
	    double t2=alignZero(tm-th);
	
	    //two intersections points.
	    if(t1>0&&t2>0){
	        if (alignZero(maxDistance - t1) > 0 && alignZero(maxDistance - t2) > 0) {
	            Point3D P1 = ray.getP0().add(v.scale(t2));
	            Point3D P2 = ray.getP0().add(v.scale(t1));
	            return List.of(new GeoPoint(this, P1), new GeoPoint(this, P2));
	        }        }
	    //one intersection point.
	    if(t1>0 && alignZero(maxDistance-t1)> 0){
	        return List.of(new GeoPoint(this,ray.getPoint(t1)));
	    }
	    //one intersection point.
	    if(t2>0 && alignZero(maxDistance-t2)> 0){
	        return List.of(new GeoPoint(this,ray.getPoint(t2)));
	    }
	    return null;
	}
}