package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Plane implements FlatGeometry {

    final Point3D _q0;
    final Vector _normal;


    public Point3D getQ0() {
        return _q0;
    }


    @Deprecated
    public Vector getNormal() {
        return _normal;
    }

    
    public Plane(Point3D p0, Vector normal) {
        _q0 = p0;
        _normal = normal.normalized();
    }


    public Plane(Point3D p1, Point3D p2, Point3D p3) {
        _q0 = p1;

        Vector U = p2.subtract(p1);
        Vector V = p3.subtract(p1);

        Vector N = U.crossProduct(V);

        N.normalize();

        _normal = N;
    }


    @Override
    public Vector getNormal(Point3D point) {
        return _normal;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "_q0=" + _q0 +
                ", _normal=" + _normal +
                '}';
    }

    @Override
    public List<Point3D> findIntersections(Ray ray) {
        Point3D P0 = ray.getP0();
        Vector v = ray.getDirection();

        Vector n = _normal;

        if(_q0.equals(P0)){
            return  null;
        }

        Vector P0_Q0 = _q0.subtract(P0);

       //numerator
        double nP0Q0  = alignZero(n.dotProduct(P0_Q0));

        //
        if (isZero(nP0Q0 )){
            return null;
        }

        //denominator
        double nv = alignZero(n.dotProduct(v));

        // ray is lying in the plane axis
        if(isZero(nv)){
            return null;
        }

        double  t = alignZero(nP0Q0  / nv);

        if (t <=0){
            return  null;
        }

        Point3D point = ray.getPoint(t);

        return List.of(point);
    }
}