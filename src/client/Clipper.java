package client;

import geometry.Vertex3D;
import polygon.Polygon;
import windowing.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class Clipper {
    private double near;
    private double far;
    private double xlow;
    private double xhigh;
    private double ylow;
    private double yhigh;

    public Clipper() {
        this.near = 0;
        this.far = -200;
        this.xlow = 0;
        this.xhigh = 650;
        this.ylow = 0;
        this.yhigh = 650;
    }

    public Clipper(double near, double far, double xlow, double xhigh, double ylow, double yhigh) {
        this.near = near;
        this.far = far;
        this.xlow = xlow;
        this.xhigh = xhigh;
        this.ylow = ylow;
        this.yhigh = yhigh;
    }

    public List<Vertex3D> clipZ_toVertexArray(Polygon polygon) {
        if (Z_outofRangeCompletely(this.far, this.near, polygon)) {
            List<Vertex3D> sameArray = new ArrayList<Vertex3D>();
            int numberOfEdges = polygon.length();
            for (int i = 0; i < numberOfEdges; i++) {
                sameArray.add(polygon.get(i));
            }
            return sameArray;
        }
        List<Vertex3D> vertexArray = new ArrayList<Vertex3D>();
        int numberOfEdges = polygon.length();

        //clip by *far* clipping plane
        for (int i = 0; i < numberOfEdges; i++) {
            //lowerBond test
            int testCase = lowerBondTest(polygon.get(i).getZ(), polygon.get(i + 1).getZ(), this.far);
            if (testCase == 1) {
                vertexArray.add(polygon.get(i + 1)); //output 2nd point
            } else if (testCase == 2) {
                vertexArray.add(getintersectWithZ(polygon.get(i), polygon.get(i + 1), this.far));
            } else if (testCase == 4) {
                vertexArray.add(getintersectWithZ(polygon.get(i), polygon.get(i + 1), this.far));
                vertexArray.add(polygon.get(i + 1)); //output 2nd point
            }
        }
        //building a far clipping plane clipped polygon
        //System.out.println("List size!!!!" + vertexArray.size());
        Vertex3D tempArray[] = new Vertex3D[vertexArray.size()];
        Polygon newPolygon = Polygon.make(vertexArray.toArray(tempArray));
        numberOfEdges = vertexArray.size();
        vertexArray = new ArrayList<Vertex3D>();
        //clip by *near* clipping plane
        for (int i = 0; i < numberOfEdges; i++) {
            //upperbond test
            int testCase = upperBondTest(newPolygon.get(i).getZ(), newPolygon.get(i + 1).getZ(), this.near);
            if (testCase == 1) {
                if (!vertexArray.contains(newPolygon.get(i + 1))) {
                    vertexArray.add(newPolygon.get(i + 1)); //output 2nd point
                }
            } else if (testCase == 2) {
                Vertex3D temp = getintersectWithZ(newPolygon.get(i), newPolygon.get(i + 1), this.near);
                if (!vertexArray.contains(temp)) {
                    vertexArray.add(getintersectWithZ(newPolygon.get(i), newPolygon.get(i + 1), this.near));
                }
            } else if (testCase == 4) {
                Vertex3D temp = getintersectWithZ(newPolygon.get(i), newPolygon.get(i + 1), this.near);
                if (!vertexArray.contains(temp)) {
                    vertexArray.add(getintersectWithZ(newPolygon.get(i), newPolygon.get(i + 1), this.near));
                }
                if (!vertexArray.contains(newPolygon.get(i + 1))) {
                    vertexArray.add(newPolygon.get(i + 1)); //output 2nd point
                }
            }
        }
        return vertexArray;
    }

    public List<Vertex3D> clipX_toVertexArray(Polygon polygon) {
        if (X_outofRangeCompletely(this.xlow, this.xhigh, polygon)) {
            List<Vertex3D> sameArray = new ArrayList<Vertex3D>();
            int numberOfEdges = polygon.length();
            for (int i = 0; i < numberOfEdges; i++) {
                sameArray.add(polygon.get(i));
            }
            return sameArray;
        }
        List<Vertex3D> vertexArray = new ArrayList<Vertex3D>();
        int numberOfEdges = polygon.length();

        //clip by *xlow* clipping plane
        for (int i = 0; i < numberOfEdges; i++) {
            //lowerBond test
            int testCase = lowerBondTest(polygon.get(i).getX(), polygon.get(i + 1).getX(), this.xlow);
            if (testCase == 1) {
                vertexArray.add(polygon.get(i + 1)); //output 2nd point
            } else if (testCase == 2) {
                vertexArray.add(getintersectWithX(polygon.get(i), polygon.get(i + 1), this.xlow));
            } else if (testCase == 4) {
                vertexArray.add(getintersectWithX(polygon.get(i), polygon.get(i + 1), this.xlow));
                vertexArray.add(polygon.get(i + 1)); //output 2nd point
            }
        }
        //building a xlow clipping plane clipped polygon
        //System.out.println("List size!!!!" + vertexArray.size());
        Vertex3D tempArray[] = new Vertex3D[vertexArray.size()];
        Polygon newPolygon = Polygon.makeEnsuringClockwise(vertexArray.toArray(tempArray));
        numberOfEdges = vertexArray.size();
        vertexArray = new ArrayList<Vertex3D>();
        //clip by *xhigh* clipping plane
        for (int i = 0; i < numberOfEdges; i++) {
            //upperbond test
            int testCase = upperBondTest(newPolygon.get(i).getX(), newPolygon.get(i + 1).getX(), this.xhigh);
            if (testCase == 1) {
                if (!vertexArray.contains(newPolygon.get(i + 1))) {
                    vertexArray.add(newPolygon.get(i + 1)); //output 2nd point
                }
            } else if (testCase == 2) {
                Vertex3D temp = getintersectWithX(newPolygon.get(i), newPolygon.get(i + 1), this.xhigh);
                if (!vertexArray.contains(temp)) {
                    vertexArray.add(getintersectWithX(newPolygon.get(i), newPolygon.get(i + 1), this.xhigh));
                }
            } else if (testCase == 4) {
                Vertex3D temp = getintersectWithX(newPolygon.get(i), newPolygon.get(i + 1), this.xhigh);
                if (!vertexArray.contains(temp)) {
                    vertexArray.add(getintersectWithX(newPolygon.get(i), newPolygon.get(i + 1), this.xhigh));
                }
                if (!vertexArray.contains(newPolygon.get(i + 1))) {
                    vertexArray.add(newPolygon.get(i + 1)); //output 2nd point
                }
            }
        }

        return vertexArray;
    }



    public List<Vertex3D> clipY_toVertexArray(Polygon polygon) {
        if (Y_outofRangeCompletely(this.ylow, this.yhigh, polygon)) {
            List<Vertex3D> sameArray = new ArrayList<Vertex3D>();
            int numberOfEdges = polygon.length();
            for (int i = 0; i < numberOfEdges; i++) {
                sameArray.add(polygon.get(i));
            }
            return sameArray;
        }
        List<Vertex3D> vertexArray = new ArrayList<Vertex3D>();
        int numberOfEdges = polygon.length();

        //clip by *far* clipping plane
        for (int i = 0; i < numberOfEdges; i++) {
            //lowerBond test
            int testCase = lowerBondTest(polygon.get(i).getY(), polygon.get(i + 1).getY(), this.ylow);
            if (testCase == 1) {
                vertexArray.add(polygon.get(i + 1)); //output 2nd point
            } else if (testCase == 2) {
                vertexArray.add(getintersectWithY(polygon.get(i), polygon.get(i + 1), this.ylow));
            } else if (testCase == 4) {
                vertexArray.add(getintersectWithY(polygon.get(i), polygon.get(i + 1), this.ylow));
                vertexArray.add(polygon.get(i + 1)); //output 2nd point
            }
        }
        //building a far clipping plane clipped polygon
        //System.out.println("List size!!!!" + vertexArray.size());
        Vertex3D tempArray[] = new Vertex3D[vertexArray.size()];
        Polygon newPolygon = Polygon.make(vertexArray.toArray(tempArray));
        numberOfEdges = vertexArray.size();
        vertexArray = new ArrayList<Vertex3D>();
        //clip by *near* clipping plane
        for (int i = 0; i < numberOfEdges; i++) {
            //upperbond test
            int testCase = upperBondTest(newPolygon.get(i).getY(), newPolygon.get(i + 1).getY(), this.yhigh);
            if (testCase == 1) {
                if (!vertexArray.contains(newPolygon.get(i + 1))) {
                    vertexArray.add(newPolygon.get(i + 1)); //output 2nd point
                }
            } else if (testCase == 2) {
                Vertex3D temp = getintersectWithY(newPolygon.get(i), newPolygon.get(i + 1), this.yhigh);
                if (!vertexArray.contains(temp)) {
                    vertexArray.add(getintersectWithY(newPolygon.get(i), newPolygon.get(i + 1), this.yhigh));
                }
            } else if (testCase == 4) {
                Vertex3D temp = getintersectWithY(newPolygon.get(i), newPolygon.get(i + 1), this.yhigh);
                if (!vertexArray.contains(temp)) {
                    vertexArray.add(getintersectWithY(newPolygon.get(i), newPolygon.get(i + 1), this.yhigh));
                }
                if (!vertexArray.contains(newPolygon.get(i + 1))) {
                    vertexArray.add(newPolygon.get(i + 1)); //output 2nd point
                }
            }
        }
        return vertexArray;

    }



    //for *far, *xlow, *ylow clipping plane
    public int lowerBondTest(double a, double b, double lowerBond){
        int result = 0;
        if ((a >= lowerBond) && (b >= lowerBond)){
            result = 1;
        }
        else if (a >= lowerBond && b < lowerBond){
            result = 2;
        }
        else if (a < lowerBond && b < lowerBond){
            result = 3;
        }
        else if (a < lowerBond && b >= lowerBond){
            result = 4;
        }
        return result;
    }
    //for *near, *xhigh, *yhigh clipping plane
    public int upperBondTest(double a, double b, double higherBond){
        int testCase = -1;
        if (a <= higherBond && b <= higherBond){
            testCase = 1;
        }
        else if (a <= higherBond && b > higherBond){
            testCase = 2;
        }
        else if (a > higherBond && b > higherBond){
            testCase = 3;
        }
        else if (a > higherBond && b <= higherBond){
            testCase = 4;
        }
        return testCase;
    }

    private Vertex3D getintersectWithZ(Vertex3D p1, Vertex3D p2, double z){
        //get (a,b,c)
        Vertex3D v = new Vertex3D(p1.getX()-p2.getX(), p1.getY()-p2.getY(), p1.getZ()-p2.getZ(), p1.getColor());
        //(x−x0)/a = (y−y0)/b = (z−z0)/c
        double temp = (z - p1.getZ()) / v.getZ(); // (z−z0)/c
        double resultX = temp * v.getX() + p1.getX();
        double resultY = temp * v.getY() + p1.getY();
        //get the color as well

        //use resultX, resultY, resultZ, resultColor to get vertex
        Vertex3D result = new Vertex3D(resultX,resultY,z,p1.getColor());
        return result;
    }

    private Vertex3D getintersectWithX(Vertex3D p1, Vertex3D p2, double x) {
        //get (a,b,c)
        Vertex3D v = new Vertex3D(p1.getX()-p2.getX(), p1.getY()-p2.getY(), p1.getZ()-p2.getZ(), p1.getColor());
        //(x−x0)/a = (y−y0)/b = (z−z0)/c
        double temp = (x - p1.getX()) / v.getX(); // (x−x0)/a
        double resultZ = temp * v.getZ() + p1.getZ();
        double resultY = temp * v.getY() + p1.getY();


        //use resultX, resultY, resultZ, resultColor to get vertex
        System.out.println("xClipped Z!!:" + resultZ);
        Vertex3D result = new Vertex3D(x,resultY,resultZ,p1.getColor());
        return result;
    }

    private Vertex3D getintersectWithY(Vertex3D p1, Vertex3D p2, double y) {
        //get (a,b,c)
        Vertex3D v = new Vertex3D(p1.getX()-p2.getX(), p1.getY()-p2.getY(), p1.getZ()-p2.getZ(), p1.getColor());
        //(x−x0)/a = (y−y0)/b = (z−z0)/c
        double temp = (y - p1.getY()) / v.getY(); // (y−y0)/b
        double resultZ = temp * v.getZ() + p1.getZ();
        double resultX = temp * v.getX() + p1.getX();
        //get the color as well
        //use resultX, resultY, resultZ, resultColor to get vertex
        System.out.println("yClipped Z!!:" + resultZ);
        Vertex3D result = new Vertex3D(resultX,y,resultZ,p1.getColor());



        return result;
    }

    public static List<Polygon> Triangulation(Polygon general){
        List<Polygon> result = new ArrayList<Polygon>();
        for (int i = 0; i < general.length()-2; i++){
            Vertex3D vertices[] = new Vertex3D[3];
            vertices[0] = general.get(0);
            vertices[1] = general.get(i+1);
            vertices[2] = general.get(i+2);
            Polygon triangle = Polygon.makeEnsuringClockwise(vertices);
            result.add(triangle);
        }
        return result;
    }


    public boolean Z_outofRangeCompletely(double lowerBond, double upperBond, Polygon polygon){
        boolean result = false;
        double z1 = polygon.get(0).getZ();
        double z2 = polygon.get(1).getZ();
        double z3 = polygon.get(2).getZ();
        if (z1 < lowerBond && z2 < lowerBond && z3 < lowerBond){
            result = true;
        }
        if (z1 > upperBond && z2 > upperBond && z3 > upperBond){
            result = true;
        }
        return result;
    }
    public boolean X_outofRangeCompletely(double lowerBond, double upperBond, Polygon polygon){
        boolean result = false;
        double z1 = polygon.get(0).getX();
        double z2 = polygon.get(1).getX();
        double z3 = polygon.get(2).getX();
        if (z1 < lowerBond && z2 < lowerBond && z3 < lowerBond){
            result = true;
        }
        if (z1 > upperBond && z2 > upperBond && z3 > upperBond){
            result = true;
        }
        return result;
    }
    public boolean Y_outofRangeCompletely(double lowerBond, double upperBond, Polygon polygon){
        boolean result = false;
        double z1 = polygon.get(0).getY();
        double z2 = polygon.get(1).getY();
        double z3 = polygon.get(2).getY();
        if (z1 < lowerBond && z2 < lowerBond && z3 < lowerBond){
            result = true;
        }
        if (z1 > upperBond && z2 > upperBond && z3 > upperBond){
            result = true;
        }
        return result;
    }
}
