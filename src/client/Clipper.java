package client;

import geometry.Vertex3D;
import polygon.Polygon;
import windowing.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class Clipper {
    private double near;
    private double far;

    public Clipper(){
        this.near = 0;
        this.far = -200;
    }

    public Clipper(double near, double far){
        this.near = near;
        this.far = far;
    }
    public List<Vertex3D> clipZ_toVertexArray(Polygon polygon){
        if (outofRangeCompletely(polygon)){
            List<Vertex3D> sameArray = new ArrayList<Vertex3D>();
            int numberOfEdges = polygon.length();
            for(int i = 0; i < numberOfEdges; i++){
                sameArray.add(polygon.get(i));
            }
            return sameArray;
        }
        List<Vertex3D> vertexArray = new ArrayList<Vertex3D>();
        int numberOfEdges = polygon.length();

        //clip by *far* clipping plane
        for(int i = 0; i < numberOfEdges; i++){
            //lowerBond test
            int testCase = lowerBondTest(polygon.get(i).getZ(), polygon.get(i+1).getZ(), this.far);
            if (testCase == 1){
                vertexArray.add(polygon.get(i+1)); //output 2nd point
            }else if (testCase == 2){
                vertexArray.add(getintersectWithZ(polygon.get(i),polygon.get(i+1),this.far));
            }else if (testCase == 4){
                vertexArray.add(getintersectWithZ(polygon.get(i),polygon.get(i+1),this.far));
                vertexArray.add(polygon.get(i+1)); //output 2nd point
            }
        }
        //building a far clipping plane clipped polygon
        //System.out.println("List size!!!!" + vertexArray.size());
        Vertex3D tempArray[] = new Vertex3D[vertexArray.size()];
        Polygon newPolygon = Polygon.make(vertexArray.toArray(tempArray));
        numberOfEdges = vertexArray.size();
        vertexArray = new ArrayList<Vertex3D>();
        //clip by *near* clipping plane
        for(int i = 0; i < numberOfEdges; i++){
            //upperbond test
            int testCase = upperBondTest(newPolygon.get(i).getZ(), newPolygon.get(i+1).getZ(), this.near);
            if (testCase == 1){
                if(!vertexArray.contains(newPolygon.get(i+1))){
                    vertexArray.add(newPolygon.get(i+1)); //output 2nd point
                }
            }else if (testCase == 2){
                Vertex3D temp = getintersectWithZ(newPolygon.get(i),newPolygon.get(i+1),this.near);
                if(!vertexArray.contains(temp)){
                    vertexArray.add(getintersectWithZ(newPolygon.get(i),newPolygon.get(i+1),this.near));
                }
            }else if (testCase == 4){
                Vertex3D temp = getintersectWithZ(newPolygon.get(i),newPolygon.get(i+1),this.near);
                if(!vertexArray.contains(temp)){
                    vertexArray.add(getintersectWithZ(newPolygon.get(i),newPolygon.get(i+1),this.near));
                }
                if(!vertexArray.contains(newPolygon.get(i+1))){
                    vertexArray.add(newPolygon.get(i+1)); //output 2nd point
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

    public Vertex3D getintersectWithZ(Vertex3D p1, Vertex3D p2, double z){
        //get (a,b,c)
        Vertex3D v = new Vertex3D(p1.getX()-p2.getX(), p1.getY()-p2.getY(), p1.getZ()-p2.getZ(), p1.getColor());
        //(x−x0)/a = (y−y0)/b = (z−z0)/c
        double temp = (z - p1.getZ()) / v.getZ(); // (z−z0)/c
        double resultX = temp * v.getX() + p1.getX();
        double resultY = temp * v.getY() + p1.getY();
        //get the color as well
        //(x−x0)/a = (y−y0)/b = (z−z0)/c = (r-r0)/d = (g-g0)/e = (b-b0)/f
        Color resultColor;
        if (p1.getColor() == p2.getColor()){
            resultColor = p1.getColor();
        }
        else{
            double d = p1.getColor().getR() - p2.getColor().getR();
            double e = p1.getColor().getG() - p2.getColor().getG();
            double f = p1.getColor().getB() - p2.getColor().getB();
            double resultR = temp * d + p1.getColor().getR();
            double resultG = temp * e + p1.getColor().getG();
            double resultB = temp * f + p1.getColor().getB();
            resultColor = new Color(resultR,resultG,resultB);
        }


        //use resultX, resultY, resultZ, resultColor to get vertex
        Vertex3D result = new Vertex3D(resultX,resultY,z,resultColor);
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


    public boolean outofRangeCompletely(Polygon polygon){
        boolean result = false;
        double z1 = polygon.get(0).getZ();
        double z2 = polygon.get(1).getZ();
        double z3 = polygon.get(2).getZ();
        if (z1 < far && z2 < far && z3 < far){
            result = true;
        }
        if (z1 > near && z2 > near && z3 > near){
            result = true;
        }
        return result;
    }
}
