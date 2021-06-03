package scene;

import elements.AmbientLight;
import elements.LightSource;
import geometries.Geometries;
import primitives.Color;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Scene {
    private final String _name;

    public Color _background = Color.BLACK;
    public AmbientLight _ambientlight= new AmbientLight();
    public Geometries _geometries = null;
    public List<LightSource> _lights;

    public Scene(String name) {
        this._name = name;
        _geometries= new Geometries();
        _lights = new LinkedList<LightSource>(); 
    }

    public Scene setBackground(Color background) {
        this._background = background;
        return  this;
    }

    public Scene setAmbientLight(AmbientLight ambientlight) {
        this._ambientlight = ambientlight;
        return this;
    }

    public Scene setGeometries(Geometries geometries) {
        this._geometries = geometries;
        return  this;
    }
    
    public Scene setlights(LinkedList<LightSource> lights) {
        this._lights = lights;
        return this;
    }

}