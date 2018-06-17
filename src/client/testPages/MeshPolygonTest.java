package client.testPages;

import geometry.Vertex3D;
import polygon.Polygon;
import polygon.PolygonRenderer;
import windowing.drawable.Drawable;
import windowing.graphics.Color;

import java.util.*;

public class MeshPolygonTest {

    private  PolygonRenderer renderer;
    private  Drawable panel;
    public static String NO_PERTURBATION = "NO_PERTURBATION";
    public static String USE_PERTURBATION = "USE_PERTURBATION";
    private Vertex3D vertices[];
    private int a;
    private int b;
    private int addon = -12;

    private List<Integer> assets = new ArrayList<>();

    private Vertex3D points[][] = new Vertex3D[10][10];



    public MeshPolygonTest(Drawable panel, PolygonRenderer renderer, String Which){
        this.panel = panel;
        this.renderer = renderer;

        fillInVertices();

        //if NO_PERTURBATION
        if (Which.equals(NO_PERTURBATION) == true){
            render_NO_PERTURBATION();
        }
        //if USE_PERTURBATION
        else if(Which.equals(USE_PERTURBATION) == true){
            RandomdizePoints();
            DrawPolygons_UsingGivenVertices();
        }
    }


    private void fillInVertices() {
        points[0][0] = new Vertex3D(19, 19 ,0.0, Color.WHITE);
        int temp;
        for (int i = 1; i < 10; i++){
            temp = points[i-1][0].getIntX() + 68;
            points[i][0] = new Vertex3D(temp, 19, 0.0, Color.WHITE);
        }
        for (int j = 1; j < 10; j++){
            for (int i = 0; i < 10; i++){
                int X = points[i][j-1].getIntX();
                int Y = points[i][j-1].getIntY() + 68;
                points[i][j] = new Vertex3D(X, Y, 0.0, Color.WHITE);
            }
        }
    }

    private void render_NO_PERTURBATION() {
        DrawPolygons_UsingGivenVertices();
    }

    private void RandomdizePoints() {
        Random random = new Random();

        for (int i = 0; i < 10 ; i++){
            for (int j = 0; j < 10; j++){
                Random randomColor = new Random();
                int rand = random.nextInt(24) - 12;
                a = points[i][j].getIntX()+rand;
                rand = random.nextInt(24) - 12;
                b = points[i][j].getIntY()+rand;
                points[i][j] = new Vertex3D(a, b, 0.0, Color.random(randomColor));
            }
        }
    }

    private void DrawPolygons_UsingGivenVertices() {
        for (int j = 0; j < 9; j++){
            for (int i = 0; i < 9; i++){
                //make bottom polygon
                vertices = new Vertex3D[3];

                vertices[0] = points[i][j];
                vertices[1] = points[i+1][j];
                vertices[2] = points[i+1][j+1];

                Polygon polygon_bot = Polygon.makeEnsuringClockwise(vertices);

                //make top polygon
                vertices[0] = points[i][j];
                vertices[1] = points[i][j+1];
                vertices[2] = points[i+1][j+1];

                Polygon polygon_top = Polygon.makeEnsuringClockwise(vertices);

                //rendering begin
                renderer.drawPolygon(polygon_bot, panel, null);
                renderer.drawPolygon(polygon_top, panel, null);
            }
        }
    }

}
