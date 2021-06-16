package geometries;

import java.util.LinkedList;
import java.util.List;

import primitives.*;

import static primitives.Util.*;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan zilberstein
 */
public class Polygon extends Geometry {
    /**
     * List of polygon's vertices
     */
    protected List<Point3D> _vertices;

    /**
     * Associated plane in which the polygon lays
     */
    protected Plane _plane;


    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point3D... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        _plane = new Plane(vertices[0], vertices[1], vertices[2]);


        _vertices = List.of(vertices);
        if (vertices.length == 3)
            return; // no need for more tests for a Triangle

//        Vector n = _plane.getNormal(null);
        Vector n = _plane.getNormal();

        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with
        // the normal. If all the rest consequent edges will generate the same sign -
        // the
        // polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (int i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    @Override
    public Vector getNormal(Point3D point) {
        return _plane.getNormal(null);
//        return _plane.getNormal();
    }

    @Override
    public String toString() {
        String result = "Vertices = <";
        for (Point3D vertex : _vertices) {
            result += vertex;
        }
        result += '>';
        return result;
    }



	@Override
	public List<GeoPoint> findGeoIntersections(Ray ray) {

		List<Point3D> result = _plane.findIntersections(ray);

		if (result == null) {
			return null;
		}

		Point3D P0 = ray.getP0();
		Vector v = ray.getDirection();

		Point3D P1 = _vertices.get(1);
		Point3D P2 = _vertices.get(0);

		Vector v1 = P1.subtract(P0);
		Vector v2 = P2.subtract(P0);

		double sign = alignZero(v.dotProduct(v1.crossProduct(v2)));

		if (isZero(sign)) {
			return null;
		}

		boolean positive = sign > 0;

		//iterate through all vertices of the polygon
		for (int i = _vertices.size() - 1; i > 0; --i) {
			v1 = v2;
			v2 = _vertices.get(i).subtract(P0);

			sign = alignZero(v.dotProduct(v1.crossProduct(v2)));
			if (isZero(sign)) {
				return null;
			}

			if (positive != (sign > 0)) {
				return null;
			}
		}
		return List.of(new GeoPoint(this,result.get(0))) ;
	}
	
	@Override
	public List<Point3D> findIntersections(Ray ray) {
		List<Point3D> result = _plane.findIntersections(ray);

		if (result == null) {
			return result;
		}

		Point3D P0 = ray.getP0();
		Vector v = ray.getDirection();

		Point3D P1 = _vertices.get(1);
		Point3D P2 = _vertices.get(0);

		Vector v1 = P1.subtract(P0);
		Vector v2 = P2.subtract(P0);

		double sign = alignZero(v.dotProduct(v1.crossProduct(v2)));

		if (isZero(sign)) {
			return null;
		}

		boolean positive = sign > 0;

		//iterate through all vertices of the polygon
		for (int i = _vertices.size() - 1; i > 0; --i) {
			v1 = v2;
			v2 = _vertices.get(i).subtract(P0);

			sign = alignZero(v.dotProduct(v1.crossProduct(v2)));
			if (isZero(sign)) {
				return null;
			}

			if (positive != (sign > 0)) {
				return null;
			}
		}

		return result;

	}
	
    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray,double maxDistance) {
        List<GeoPoint> planeIntersection=_plane.findGeoIntersections(ray,maxDistance);
        Plane plane = new Plane(_vertices.get(0), _vertices.get(1), _vertices.get(2));
        // check for intersection point
        if (planeIntersection != null) {


            //check for intersections with angles using a scalar product between the Ray and the normal

            // Ray head
            Point3D P0 = ray.getP0();
            // ray intersection vector
            Vector v = plane.findGeoIntersections(ray,maxDistance).get(0).point.subtract(P0);

            LinkedList<Vector> vectorList = new LinkedList<>();
            //the vectors between the head of the ray and the vertices of the polygon.
            for (Point3D point3D : _vertices) {
                vectorList.add(point3D.subtract(P0));
            }

            LinkedList<Vector> normalList = new LinkedList<>();
            //the normals from between every two adjacent vectors.
            for (int i = 0; i < vectorList.size() - 1; i++) {
                normalList.add(vectorList.get(i).crossProduct(vectorList.get(i + 1)));
            }

            //last normal from between first and last vector in the list.
            normalList.add(vectorList.get(vectorList.size() - 1).crossProduct(vectorList.get(0)));

            boolean flag = true;
            boolean flag1 = true;
            //check the type of angle between the normal and the ray.
            for (Vector normal : normalList) {
                if (alignZero(v.dotProduct(normal)) <= 0) {
                    flag = false;
                }
                if (alignZero(v.dotProduct(normal)) >= 0) {
                    flag1 = false;
                }
            }
            if (flag || flag1) {
                return List.of(new GeoPoint(this,planeIntersection.get(0).point));
            }
        }
        return null;    }
	
}