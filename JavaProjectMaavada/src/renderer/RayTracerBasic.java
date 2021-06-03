package renderer;

import elements.LightSource;
import geometries.Intersectable;

import static geometries.Intersectable.GeoPoint;
import static primitives.Util.alignZero;

import java.util.List;

import primitives.*;
import scene.Scene;

public class RayTracerBasic extends RayTracerBase {

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
        return _scene._ambientlight.getIntensity()
                .add(point.geometry.getEmmission()
                        .add(calcLocalEffects(point, ray)));
    }

    private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
        Vector v = ray.getDirection();
        Vector n = intersection.geometry.getNormal((intersection.point));
        double nv = alignZero(n.dotProduct(v));

        Material material = intersection.geometry.getMaterial();
        int nShininess = material.nShininess;
        double kd = material.kD, ks = material.kS;
        Color color = Color.BLACK;
        for (LightSource lightSource : _scene._lights) {
            Vector l = lightSource.getL(intersection.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) {
                Color lightIntensity = lightSource.getIntensity(intersection.point);
                color = color.add(calcDiffusive(kd, l, n, lightIntensity), calcSpecular(ks, l, n, v, nShininess, lightIntensity));
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
}