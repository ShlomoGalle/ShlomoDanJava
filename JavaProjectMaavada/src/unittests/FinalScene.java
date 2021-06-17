package unittests;

import org.junit.Test;

import elements.*;
import geometries.*;
import primitives.*;
import renderer.*;
import scene.Scene;

public class FinalScene {

	/**
	 * Produce a picture of a sphere and triangle with point light and shade
	 */
	@Test
	public void test() {
        Scene scene = new Scene("Test scene");
    //    Camera camera = new Camera(new Point3D(3000, 200, 50), new Vector(-1, 0, 0), new Vector(0, 1, 0)) //
    //            .setViewPlaneSize(700, 700).setDistance(3000)
                //.setDepthOfFiled(30, 0.1, 200)
    //    		;
        Camera camera = new Camera(new Point3D(-40, 95, 1000), new Vector(0.07, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(400, 400).setDistance(900)
        		.setDepthOfFiled(30, 0.20, 200)
                .turnCamera(new Vector(4, 0, 0.3), 1)
        		;
        
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.50));

        scene._geometries.add(

            new Plane(new Point3D(0, -0, 0), new Point3D(60, -0, 0), new Point3D(30, -0, 51.9615242271)) //
		    .setEmmission(new Color(java.awt.Color.PINK)) //
			.setMaterial(new Material().setkR(0.05).setkD(0.5).setkS(0.7).setnShininess(20)),
		    new Plane(new Point3D(300000, 0, 0), new Point3D(300000, 300000, 0), new Point3D(300000, 0, 300000)) //
		    .setEmmission(new Color(java.awt.Color.darkGray)) //
			.setMaterial(new Material().setkD(0.5).setkS(0.7).setnShininess(20)),
		    new Plane(new Point3D(-300000, 0, 0), new Point3D(-300000, 300, 0), new Point3D(-300000, 0, 300)) //
		    .setEmmission(new Color(java.awt.Color.darkGray)) //
			.setMaterial(new Material().setkD(0.5).setkS(0.7).setnShininess(20)),
			
			
            new Triangle(new Point3D(0, -0, 0), new Point3D(60, -0, 0), new Point3D(30, 120, 25.9807621)) //
            .setEmmission(new Color(java.awt.Color.GRAY)) //
			.setMaterial(new Material().setkD(0.5).setkS(0.7).setnShininess(60)),
            new Triangle(new Point3D(0, -0, 0), new Point3D(30, -0, 51.9615242271), new Point3D(30, 120, 25.9807621)) //
            .setEmmission(new Color(java.awt.Color.GRAY)) //
			.setMaterial(new Material().setkD(0.5).setkS(0.7).setnShininess(60)),
            new Triangle(new Point3D(60, -0, 0), new Point3D(30, -0, 51.9615242271), new Point3D(30, 120, 25.9807621)) //
            .setEmmission(new Color(java.awt.Color.GRAY)) //
			.setMaterial(new Material().setkD(0.5).setkS(0.7).setnShininess(60)),
            new Triangle(new Point3D(0, -0, 0), new Point3D(60, -0, 0), new Point3D(30, -0, 51.9615242271)) //
            .setEmmission(new Color(java.awt.Color.GRAY)) //
			.setMaterial(new Material().setkD(0.5).setkS(0.7).setnShininess(60)),

            new Triangle(new Point3D(0, 300, 0), new Point3D(60, 300, 0), new Point3D(30, 180, 25.9807621)) //
            .setEmmission(new Color(java.awt.Color.GRAY)) //
			.setMaterial(new Material().setkD(0.5).setkS(0.7).setnShininess(60)),
            new Triangle(new Point3D(0, 300, 0), new Point3D(30, 300, 51.9615242271), new Point3D(30, 180, 25.9807621)) //
            .setEmmission(new Color(java.awt.Color.GRAY)) //
			.setMaterial(new Material().setkD(0.5).setkS(0.7).setnShininess(60)),
            new Triangle(new Point3D(60, 300, 0), new Point3D(30, 300, 51.9615242271), new Point3D(30, 180, 25.9807621)) //
            .setEmmission(new Color(java.awt.Color.GRAY)) //
			.setMaterial(new Material().setkD(0.5).setkS(0.7).setnShininess(60)),
            new Triangle(new Point3D(0, 300, 0), new Point3D(30, 300, 51.9615242271), new Point3D(60, 300, 0)) //
            .setEmmission(new Color(java.awt.Color.GRAY)) //
			.setMaterial(new Material().setkD(0.5).setkS(0.7).setnShininess(30)),
			
            new Sphere(30, new Point3D(30, 150, 25.9807621)) //
            .setEmmission(new Color(java.awt.Color.ORANGE)) //
            .setMaterial(new Material().setkD(0.5).setkS(0.7).setnShininess(60))
        );
        
        
   //      scene._lights.add(
    //             new DirectionalLight(new Color(150, 150, 150), new Vector(90, -40, -10)));
         
       scene._lights.add( //
                new SpotLight(new Color(700, 700, 400), new Point3D(-600, 180, -360), new Vector(100, -100, -40)) //
                       .setkL(4E-4).setkQ(2E-5));
        scene._lights.add( //
                new SpotLight(new Color(700, 700, 400), new Point3D(-200, 250, 150), new Vector(10, -10, -40)) //
                       .setkL(4E-4).setkQ(2E-5));
        
        
        ImageWriter imageWriter = new ImageWriter("FinalSceneOF_THE_DOOM2", 600, 600);
        Render render = new Render()
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new RayTracerBasic(scene))
        		.setFlagDOP(true)
                ;

        render.renderImage();
        render.writeToImage();
	}
}
