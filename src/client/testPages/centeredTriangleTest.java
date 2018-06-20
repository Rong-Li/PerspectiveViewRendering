package client.testPages;

import geometry.Transformation;
import geometry.Vertex;
import geometry.Vertex3D;
import polygon.Polygon;
import polygon.PolygonRenderer;
import windowing.drawable.Drawable;
import windowing.graphics.Color;

import java.util.Random;

public class centeredTriangleTest {

    private final PolygonRenderer renderer;
    private final Drawable panel;
    private Vertex3D center;
    private Polygon Original;
    private Color colors[] = new Color[6];
    private Polygon polygons[] = new Polygon[6];

    public centeredTriangleTest(Drawable fullPanel, PolygonRenderer polygonRenderer) {
        //preparations
        this.renderer = polygonRenderer;
        this.panel = fullPanel;
        this.center = new Vertex3D(324.0, 324.0, 0.0, Color.BLACK);
        this.Original = makeOriginalPolygon();
        FillinColors();

        for (int i = 0; i < 6; i++){
            Random random = new Random();
            double degrees = random.nextInt(120);
            double z = random.nextInt(198) - 199;

            Polygon temp = RotatedThePolygon_withNewZ(degrees, z);
            polygons[i] = SetNewColor(temp, colors[i]);
            renderer.drawPolygon(polygons[i], panel, null);
        }


//        double degrees = 30.0;
//        double z = 0.0;
//        Color newColor = Color.BLUE;
//        Polygon RotatedPolygonBydegrees = RotatedThePolygon_withNewZ(degrees, z);
//        Polygon FinalVersion = SetNewColor(RotatedPolygonBydegrees, newColor);
//
//        renderer.drawPolygon(FinalVersion, panel, null);
    }


    //preparations
    public Polygon makeOriginalPolygon(){
        Vertex3D vertices[];
        vertices = new Vertex3D[3];

        vertices[0] = new Vertex3D(324, 324 + 275, 0.0, Color.WHITE);//top
        vertices[1] = new Vertex3D(86, 186, 0.0, Color.WHITE);//bottom left
        vertices[2] = new Vertex3D(562, 186, 0.0, Color.WHITE);//bottom right

        Polygon polygon = Polygon.makeEnsuringClockwise(vertices);
        //renderer.drawPolygon(polygon, panel, null);
        return polygon;
    }
    public void FillinColors(){
        this.colors[0] = new Color(1.0,1.0,1.0);
        this.colors[1] = new Color(0.85,0.85,0.85);
        this.colors[2] = new Color(0.7,0.7,0.7);
        this.colors[3] = new Color(0.55,0.55,0.55);
        this.colors[4] = new Color(0.4,0.4,0.4);
        this.colors[5] = new Color(0.25,0.25,0.25);
    }


    //counterclockwise!!!!!counterclockwise!!!!!!!counterclockwise
    public Vertex3D RotatedNewVertices(Vertex3D vertex, double degrees){
        double x = vertex.getX();
        double y = vertex.getY();
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double newX = x * cos - y * sin;
        double newY = x * sin + y * cos;

        Vertex3D newPoint = new Vertex3D(newX, newY, vertex.getZ(), Color.WHITE);
        return newPoint;
    }

    public Polygon RotatedThePolygon_withNewZ(double degrees, double newZ){
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        Transformation rotate = new Transformation(3,3);
        rotate.set(1,1, cos);
        rotate.set(1,2, -sin);
        rotate.set(2,1, sin);
        rotate.set(2,2, cos);
        rotate.set(3,3, 1);
        System.out.println("rotate!!!");
        rotate.printMatrix();

        Transformation translate = new Transformation(3,3);
        translate.set(1,1, 1);
        translate.set(2,2, 1);
        translate.set(3,3, 1);
        translate.set(1,3, -325);
        translate.set(2,3, -325);
        System.out.println("translate!!!");
        translate.printMatrix();

        Transformation back = new Transformation(3,3);
        back.set(1,1, 1);
        back.set(2,2, 1);
        back.set(3,3, 1);
        back.set(1,3, 325);
        back.set(2,3, 325);
        System.out.println("back!!!");
        back.printMatrix();

        Transformation p1 = new Transformation(3,1);
        p1.set(1,1, Original.get(0).getX());
        p1.set(2,1, Original.get(0).getY());
        p1.set(3,1, 1);
        System.out.println("p1 before!!!");
        p1.printMatrix();

        Transformation p2 = new Transformation(3,1);
        p2.set(1,1, Original.get(1).getX());
        p2.set(2,1, Original.get(1).getY());
        p2.set(3,1, 1);
        System.out.println("p2 before!!!");
        p2.printMatrix();

        Transformation p3 = new Transformation(3,1);
        p3.set(1,1, Original.get(2).getX());
        p3.set(2,1, Original.get(2).getY());
        p3.set(3,1, 1);
        System.out.println("p3 before!!!");
        p3.printMatrix();

        p1 = p1.matrixMultiplication(translate);
        p1 = p1.matrixMultiplication(rotate);
        p1 = p1.matrixMultiplication(back);
        System.out.println("p1 after!!!");
        p1.printMatrix();

        p2 = p2.matrixMultiplication(translate);
        p2 = p2.matrixMultiplication(rotate);
        p2 = p2.matrixMultiplication(back);
        System.out.println("p2 after!!!");
        p2.printMatrix();

        p3 = p3.matrixMultiplication(translate);
        p3 = p3.matrixMultiplication(rotate);
        p3 = p3.matrixMultiplication(back);
        System.out.println("p3 after!!!");
        p3.printMatrix();

        Vertex3D vertices[];
        vertices = new Vertex3D[3];

        //Rotate the origin centered Polygon then by adding origin to move back the polygon
        vertices[0] = new Vertex3D(p1.get(1,1), p1.get(2,1), newZ, Color.BLACK);
        vertices[1] = new Vertex3D(p2.get(1,1), p2.get(2,1), newZ, Color.BLACK);
        vertices[2] = new Vertex3D(p3.get(1,1), p3.get(2,1), newZ, Color.BLACK);

        Polygon result = Polygon.makeEnsuringClockwise(vertices);


        return result;
    }



//    public Polygon RotatedThePolygon_withNewZ(double degrees, double newZ){
//        //Moving the Polygon to be center at the origin
//        Vertex3D topVertex = new Vertex3D(0.0, 275.0, newZ, Color.BLACK);
//        Vertex3D BotLeftVertex = new Vertex3D(-238, -138, newZ, Color.BLACK);
//        Vertex3D BotRightVertex = new Vertex3D(238, -138, newZ, Color.BLACK);
//
//        Vertex3D vertices[];
//        vertices = new Vertex3D[3];
//
//        //Rotate the origin centered Polygon then by adding origin to move back the polygon
//        vertices[0] = RotatedNewVertices(topVertex, degrees).add(this.center);
//        vertices[1] = RotatedNewVertices(BotLeftVertex, degrees).add(this.center);
//        vertices[2] = RotatedNewVertices(BotRightVertex, degrees).add(this.center);
//
//        Polygon result = Polygon.makeEnsuringClockwise(vertices);
//        return result;
//    }

    public Polygon SetNewColor(Polygon polygon, Color newColor){
        Vertex3D vertices[];
        vertices = new Vertex3D[3];

        //Rotate the origin centered Polygon then by adding origin to move back the polygon
        vertices[0] = polygon.get(0).replaceColor(newColor);
        vertices[1] = polygon.get(1).replaceColor(newColor);
        vertices[2] = polygon.get(2).replaceColor(newColor);
        Polygon result = Polygon.makeEnsuringClockwise(vertices);
        //ensuring replacing color doesn't change Z value
        if (result.get(0).getZ() != polygon.get(0).getZ())
            System.out.println("WRONG!!!!!!!!!!");
        return result;
    }
}
