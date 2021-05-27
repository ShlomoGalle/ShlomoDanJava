package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

public class Sphere extends RadialGeometry {
    final Point3D _center;

    public Sphere(double radius, Point3D center) {
        super(radius);
        _center = center;
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
}