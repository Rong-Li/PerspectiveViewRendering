package client;

import geometry.Vertex3D;
import polygon.Polygon;
import windowing.graphics.Color;

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

    public Vertex3D[] clipZ_toVertexArray(Polygon polygon){
        Vertex3D vertexArray[] = new Vertex3D[10];
        int index = 0;
        int numberOfEdges = 3;

        //clip by *far* clipping plane
        for(int i = 0; i < numberOfEdges; i++){
            //lowerBond test
            int testCase = lowerBondTest(polygon.get(i).getZ(), polygon.get(i+1).getZ(), this.far);
            if (testCase == 1){
                vertexArray[index] = polygon.get(i+1); //output 2nd point
                index++;
            }else if (testCase == 2){
                vertexArray[index] = getintersectWithZ(polygon.get(i),polygon.get(i+1),this.far); //output 2nd point
                index++;
            }else if (testCase == 4){
                vertexArray[index] = getintersectWithZ(polygon.get(i),polygon.get(i+1),this.far); //output 2nd point
                index++;
                vertexArray[index] = polygon.get(i+1);
                index++;
            }
        }

        //clip by *near* clipping plane
        for(int i = 0; i < 3; i++){
            //upperbond test
            int testCase = upperBondTest(polygon.get(i).getZ(), polygon.get(i+1).getZ(), this.near);
            if (testCase == 1){
                if(notInArray(vertexArray, polygon.get(i+1))){
                    vertexArray[index] = polygon.get(i+1); //output 2nd point
                    index++;
                }
            }else if (testCase == 2){
                Vertex3D temp = getintersectWithZ(polygon.get(i),polygon.get(i+1),this.far);
                if(notInArray(vertexArray, temp)){
                    vertexArray[index] = temp; //output 2nd point
                    index++;
                }
            }else if (testCase == 4){
                Vertex3D temp = getintersectWithZ(polygon.get(i),polygon.get(i+1),this.far);
                if(notInArray(vertexArray, temp)){
                    vertexArray[index] = getintersectWithZ(polygon.get(i),polygon.get(i+1),this.far); //output 2nd point
                    index++;
                }
                if(notInArray(vertexArray, polygon.get(i+1))){
                    vertexArray[index] = polygon.get(i+1);
                    index++;
                }
            }
        }
        return vertexArray;
    }

    public boolean notInArray(Vertex3D[] array, Vertex3D vertex){
        boolean result = true;
        int i = 0;
        while(array[i] != null){
            if (array[i] == vertex){
                result = false;
            }
            i++;
        }
        return result;
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
        Vertex3D v = new Vertex3D(p1.getX()-p2.getIntX(), p1.getY()-p2.getY(), p1.getZ()-p2.getZ(), p1.getColor());
        //(x−x0)/a = (y−y0)/b = (z−z0)/c
        double temp = (z - p1.getZ()) / v.getZ(); // (z−z0)/c
        double resultX = temp * v.getX() + p1.getX();
        double resultY = temp * v.getY() + p1.getY();
        //get the color as well
        //(x−x0)/a = (y−y0)/b = (z−z0)/c = (r-r0)/d = (g-g0)/e = (b-b0)/f
        double d = p1.getColor().getR() - p2.getColor().getR();
        double e = p1.getColor().getG() - p2.getColor().getG();
        double f = p1.getColor().getB() - p2.getColor().getB();
        double resultR = temp * d + p1.getColor().getR();
        double resultG = temp * e + p1.getColor().getG();
        double resultB = temp * f + p1.getColor().getB();
        Color resultColor = new Color(resultR,resultG,resultB);

        //use resultX, resultY, resultZ, resultColor to get vertex
        Vertex3D result = new Vertex3D(resultX,resultY,z,resultColor);
        return result;
    }

}
