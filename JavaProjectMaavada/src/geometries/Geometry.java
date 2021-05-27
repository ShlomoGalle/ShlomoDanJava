package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;



public abstract class Geometry  implements Intersectable{

	protected Color _emmission = Color.BLACK;
    private Material material = new Material();
 
	
	public Geometry setEmmission(Color emmission) {
		_emmission = emmission;
		return this;
	}
	
	public Color getEmmission() {
		return _emmission;
	}
	
    public abstract Vector getNormal(Point3D point3D);

    public Material getMaterial() {
        return material;
    }

    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}