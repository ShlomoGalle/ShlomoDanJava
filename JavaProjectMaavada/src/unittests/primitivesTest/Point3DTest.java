package unittests.primitivesTest;

import org.junit.jupiter.api.Test;

import primitives.Point3D;

class Point3DTest {

    @Test
    void testDistance() {


       Point3D point3 = new Point3D(0.5, 0, -100);
        double resultsquared;
        double result;

        resultsquared = point3.distanceSquared(new Point3D(0,0,-100));
        System.out.println(resultsquared);
        result = point3.distance(new Point3D(0,0,-100));
        System.out.println(result);
    }
}