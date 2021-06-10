package geometries;


import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;

import primitives.*;



public abstract class Geometry  implements Intersectable{
    
	public abstract Vector getNormal(Point3D point3D);
    protected Color _emmission = Color.BLACK;
    private Material _material = new Material();
	
	public Geometry setEmmission(Color emmission) {
		this._emmission = emmission;
		return this;
	}
	
	public Color getEmmission() {
		return _emmission;
	}
	

    public Material getMaterial() {
        return _material;
    }

    public Geometry setMaterial(Material material) {
        this._material = material;
        return this;
    }
}