package renderer;

import elements.LightSource;
import geometries.Intersectable;
import geometries.Intersectable.GeoPoint;

import static geometries.Intersectable.GeoPoint;
import static primitives.Util.*;

import java.util.List;

import primitives.*;
import scene.Scene;

public class RayTracerBasic extends RayTracerBase {

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final double INITIAL_K = 1.0;
	
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> geoList = _scene._geometries.findGeoIntersections(ray);
        if (geoList != null) {
            GeoPoint closestPoint = ray.findClosestGeoPoint(geoList);
            return calcColor(closestPoint, ray);
        }
        return _scene._background;
    }

    /**
     * @param point
     * @return
     */
    private Color calcColor(GeoPoint point, Ray ray) {
       // return _scene._ambientlight.getIntensity()
       //         .add(point.geometry.getEmmission()
       //                 .add(calcLocalEffects(point, ray)));
        
        return calcColor(point, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(_scene._ambientlight.getIntensity());

    }
    
    private  Color calcColor(GeoPoint intersection,Ray ray, int level, double k) {
        Color color = intersection.geometry.getEmmission();
        color = color.add(calcLocalEffects(intersection, ray, k));
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, ray, level, k));
    }

    
    
    private Color calcLocalEffects(GeoPoint intersection, Ray ray, double k) {

        
        Vector v = ray.getDirection();
        Vector n = intersection.geometry.getNormal((intersection.point));
        double nV = alignZero(n.dotProduct(v));
        if(isZero(nV)) {
            return Color.BLACK;
        }
        Material material = intersection.geometry.getMaterial();
        int nShininess = material.nShininess;
        double kd = material.kD;
        double ks = material.kS;
        Color color = Color.BLACK;
        for(LightSource lightSource : _scene._lights){
            Vector l = lightSource.getL(intersection.point);
            double nl = alignZero(n.dotProduct(l));
            // check they got the same sign (the light and the camera are in the same side of the given point)
            if (checkSign(nl, nV)) {
                double ktr = transparency(lightSource, l, n, intersection);
                if (ktr * k > MIN_CALC_COLOR_K) {
                    Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
                    color = color.add(calcDiffusive(kd, l, n, lightIntensity), calcSpecular(ks, l, n, v, nShininess, lightIntensity));
                }
            }
        }
        return color;
    }

    
    private Color calcGlobalEffects(GeoPoint intersection, Ray ray, int level , double k){
        Color color = Color.BLACK;
        Material material = intersection.geometry.getMaterial();
        double kr = material.getkR();
        double kkr= k*kr;
        Vector n = intersection.geometry.getNormal(intersection.point);
        if(kkr > MIN_CALC_COLOR_K) {
            Ray reflectedRay = constructReflectedRay(n, intersection.point, ray);
            GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
            if (reflectedPoint != null) {
                color = color.add(calcColor(reflectedPoint, reflectedRay, level - 1, kkr).scale(kr));
            }
        }
        double kt = material.kT;
        double kkt = k*kt;
        if(kkt > MIN_CALC_COLOR_K) {
            Ray refractedRay = constructRefractedRay(n, intersection.point, ray);
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);
            if (refractedPoint != null) {
                color = color.add(calcColor(refractedPoint, refractedRay, level - 1, kkt).scale(kt));
            }
        }
        return color;
    }
    
    private Color calcDiffusive(double kd, Vector l, Vector n, Color lightIntensity) {
        double factor = kd * Math.abs(l.dotProduct(n));
        return lightIntensity.scale(factor);
    }

    private Color calcSpecular(double ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        Vector r = l.subtract(n.scale(2 * l.dotProduct(n)));
        double minusVr = v.dotProduct(r) * -1;
        return lightIntensity.scale(ks * Math.pow(Math.max(0, minusVr), nShininess));
    }
    
    private Ray constructRefractedRay(Vector n, Point3D point, Ray ray) {
        return new Ray(point, ray.getDirection(), n);
    }
    
    private Ray constructReflectedRay(Vector n, Point3D point, Ray ray) {
        // r = v - 2 * (v * n) * n
        Vector v = ray.getDirection();
        Vector r = null;
        try {
            r = v.subtract(n.scale(v.dotProduct(n)).scale(2)).normalized();
        }
        catch (Exception e) {
            return null;
        }
        return new Ray(point, r, n);
    }
    
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> geoPoints = _scene._geometries.findGeoIntersections(ray);
        return ray.findClosestGeoPoint(geoPoints);
    }
    
    private double transparency(LightSource ls, Vector l, Vector n, GeoPoint intersection) {
        Point3D point = intersection.point;
        Vector lightDirection = l.scale(-1); // from point to light source
        // ray from point toward light direction offset by delta
        Ray lightRay = new Ray(point, lightDirection, n);
        double lightDistance = ls.getDistance(point);
        var intersections = _scene._geometries.findGeoIntersections(lightRay, ls.getDistance(lightRay.getP0()));
        if (intersections == null) {
            return 1.0;
        }
        double ktr = 1.0;
        for (GeoPoint gp : intersections) {
            if (alignZero(gp.point.distance(point) - lightDistance) <= 0) {
                ktr *= gp.geometry.getMaterial().getkT();
                if (ktr < MIN_CALC_COLOR_K) {
                    return 0.0;
                }
            }
        }
        return ktr;
    }
}