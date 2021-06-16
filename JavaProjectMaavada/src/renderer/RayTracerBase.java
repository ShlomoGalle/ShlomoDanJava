package renderer;

import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import scene.Scene;
import java.util.List;

public abstract class RayTracerBase {

    protected Scene _scene;

    public RayTracerBase(Scene scene) {
        this._scene = scene;
    }
    
    public abstract Color traceRay(Ray ray);
    public abstract Color traceRay(List<Ray> rays);

}	