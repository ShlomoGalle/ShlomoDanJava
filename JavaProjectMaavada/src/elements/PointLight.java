package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

public class PointLight extends Light implements LightSource {

    protected Point3D position;
    private double kC;
    private double kL;
    private double kQ;

    /***
     *
     * @param intensity
     * @param position
     */
    public PointLight(Color intensity, Point3D position) {
        super(intensity);
        this.position = position;
        this.kC = 1;
        this.kL = 0;
        this.kQ = 0;
    }

    /***
     *
     * @param intensity
     * @param position
     * @param kC
     * @param kL
     * @param kQ
     */
    protected PointLight(Color intensity, Point3D position, double kC, double kL, double kQ) {
        super(intensity);
        this.position = position;
        this.kC = kC;
        this.kL = kL;
        this.kQ = kQ;
    }


    @Override
    public Color getIntensity(Point3D p) {

        double denominator, distance;
        distance = position.distance(p);
        denominator = kC + kL * distance + kQ * distance * distance;
        return getIntensity().reduce(denominator);
    }

    @Override
    public Vector getL(Point3D p) {
        return p.subtract(position).normalize();
    }
    
    @Override
    public double getDistance(Point3D point3D) {
        return position.distance(point3D);
    }


    public PointLight setkC(double kC) {
        this.kC = kC;
        return this;
    }

    public PointLight setkL(double kL) {
        this.kL = kL;
        return this;
    }

    public PointLight setkQ(double kQ) {
        this.kQ = kQ;
        return this;
    }
}