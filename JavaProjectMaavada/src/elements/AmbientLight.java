package elements;

import primitives.Color;

/**
 * Ambient Light Color
 */
public class AmbientLight extends Light{

	
    public AmbientLight() {
        super(Color.BLACK);
    }

    /***
     *  call ctor by sending a color times a number
     *  which defines a nuance
     * @param Ia
     * @param Ka
     */
    public AmbientLight(Color Ia, double Ka) {
        super(Ia.scale(Ka));
    }

}