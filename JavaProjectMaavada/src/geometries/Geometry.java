package geometries;

import primitives.Point3D;
import primitives.Vector;


//public abstract class Geometry  implements Intersectable{
public interface Geometry  extends Intersectable{


    Vector getNormal(Point3D point);
	//abstract Vector getNormal(Point3D point);
}