package unittests.elementsTest;

import org.junit.Test;

import elements.*;
import geometries.*;
import primitives.*;
import renderer.*;
import scene.Scene;

public class SuperSamplingTest {

	/**
	 * Produce a picture of a sphere and triangle with point light and shade
	 */
	@Test
	public void test() {
        Scene scene = new Scene("Test scene");
        Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(200, 200).setDistance(1000)
                .setDepthOfFiled(30, 0.1, 200);

        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene._geometries.add(
                new Sphere(50, new Point3D(0, -0, -148)) //
                        .setEmmission(new Color(java.awt.Color.MAGENTA)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)),
				new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500),
						new Point3D(670, 670, 3000)) //
								.setEmmission(new Color(20, 20, 20)) //
								.setMaterial(new Material().setkR(1).setkD(0.5).setkS(0.5).setnShininess(30)),
				new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500),
						new Point3D(-1500, -1500, -2000)) //
								.setEmmission(new Color(20, 20, 20)) //
								.setMaterial(new Material().setkR(0.5).setkD(0.5).setkS(0.5).setnShininess(30))

        );
        // scene.lights.add(
        //         new DirectionalLight(new Color(300, 150, 150), new Vector(0, 0, -1)));
        scene._lights.add( //
                new SpotLight(new Color(700, 700, 400), new Point3D(-40, -80, 115), new Vector(-1, 1, -4)) //
                        .setkL(4E-4).setkQ(2E-5));

        ImageWriter imageWriter = new ImageWriter("SuperSampling1", 600, 600);
        Render render = new Render()
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new RayTracerBasic(scene))
        		.setFlagDOP(true);

        render.renderImage();
        render.writeToImage();
	}
	
	
	@Test
	public void test2() {
        Scene scene = new Scene("Test scene");
        Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(200, 200).setDistance(1000)
                .setDepthOfFiled(30, 0.1, 200);

        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.30));

        scene._geometries.add(
                new Sphere(60, new Point3D(0, -0, -148)) //
                        .setEmmission(new Color(java.awt.Color.YELLOW)) //
                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60))
        );

        scene._lights.add( //
                new SpotLight(new Color(700, 700, 400), new Point3D(-40, -80, 115), new Vector(-1, 1, -4)) //
                        .setkL(4E-4).setkQ(2E-5));

        ImageWriter imageWriter = new ImageWriter("SuperSampling2", 600, 600);
        Render render = new Render()
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new RayTracerBasic(scene))
        		.setFlagDOP(true);

        render.renderImage();
        render.writeToImage();
	}
	
	
	@Test
	public void test3() {
        Scene scene = new Scene("Test scene");
        Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(200, 200).setDistance(1000)
                .setDepthOfFiled(30, 0.1, 200);

        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.35));

		scene._geometries.add( //
				new Triangle(new Point3D(-150, -150, -115), new Point3D(150, -150, -135), new Point3D(75, 75, -150)) //
						.setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)), //
				new Triangle(new Point3D(-150, -150, -115), new Point3D(-70, 70, -140), new Point3D(75, 75, -150)) //
						.setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)), //
				new Sphere(30, new Point3D(60, 50, -50)) //
						.setEmmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.6)));

		scene._lights.add(new SpotLight(new Color(700, 400, 400), new Point3D(140, 100, 50), new Vector(0, 0, -1)) //
				.setkL(4E-5).setkQ(2E-7));
        scene._lights.add( //
                new SpotLight(new Color(700, 700, 400), new Point3D(-40, -80, 115), new Vector(-1, 1, -4)) //
                        .setkL(4E-4).setkQ(2E-5));

        ImageWriter imageWriter = new ImageWriter("SuperSampling3", 600, 600);
        Render render = new Render()
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new RayTracerBasic(scene))
        		.setFlagDOP(true);

        render.renderImage();
        render.writeToImage();
	}
	

}
