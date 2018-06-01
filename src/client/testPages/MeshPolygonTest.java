package client.testPages;

import geometry.Vertex3D;
import polygon.Polygon;
import polygon.PolygonRenderer;
import windowing.drawable.Drawable;
import windowing.graphics.Color;

import java.lang.reflect.Array;
import java.util.*;

public class MeshPolygonTest {

    private final PolygonRenderer renderer;
    private final Drawable panel;
    public static String NO_PERTURBATION = "NO_PERTURBATION";
    public static String USE_PERTURBATION = "USE_PERTURBATION";
    private Vertex3D vertices[];
    private int a;
    private int b;
    //public int assets[] = new int[25];
    private int addon = -12;
//    private int i;
//    private int j;
    private List<Integer> assets = new ArrayList<>();

    private Vertex3D points[][] = new Vertex3D[10][10];

    public MeshPolygonTest(Drawable panel, PolygonRenderer polygonRenderer, String Which) {
        this.panel = panel;
        this.renderer = polygonRenderer;

        for (int m = 0; m < 25; m++) {
            assets.add(addon);
            addon++;
        }


        fillInVertices();


        //if NO_PERTURBATION
        if (Which.equals(NO_PERTURBATION) == true){
            render_NO_PERTURBATION();
        }
        //if USE_PERTURBATION
        else if(Which.equals(USE_PERTURBATION) == true){
            render_USE_PERTURBATION();
        }

    }


    private void render_NO_PERTURBATION() {
        DrawPolygons_UsingGivenVertices();
    }



    private void render_USE_PERTURBATION() {
        Random random = new Random();

        for (int i = 0; i < 10 ; i++){
            for (int j = 0; j < 10; j++){
                int rand = random.nextInt(24) - 12;
                a = points[i][j].getIntX()+rand;
                rand = random.nextInt(24) - 12;
                b = points[i][j].getIntY()+rand;
                points[i][j] = new Vertex3D(a, b, 0.0, Color.WHITE);
            }
        }

        DrawPolygons_UsingGivenVertices();
    }


////
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


    private void fillInVertices() {
        points[0][0] = new Vertex3D(15, 15 ,0.0, Color.WHITE);
        int temp;
        for (int i = 1; i < 10; i++){
            temp = points[i-1][0].getIntX() + 30;
            points[i][0] = new Vertex3D(temp, 15, 0.0, Color.WHITE);
        }
        for (int j = 1; j < 10; j++){
            for (int i = 0; i < 10; i++){
                int X = points[i][j-1].getIntX();
                int Y = points[i][j-1].getIntY() + 30;
                points[i][j] = new Vertex3D(X, Y, 0.0, Color.WHITE);
            }
        }
    }

}





























//    private void render_NO_PERTURBATION() {
//        for (int y = 15; y < 285; y = y + 30) {
//            for (int x = 15; x < 285; x = x + 30) {
//
//                Vertex3D leftCorner = new Vertex3D(x, y ,0.0, Color.WHITE);
//                Vertex3D rightCorner = new Vertex3D((double)(x+30), (double)(y+30),0.0 , Color.WHITE);
//                Vertex3D top_leftCorner = new Vertex3D(x, (double)(y+30),0.0 , Color.WHITE);
//                Vertex3D bot_rightCorner = new Vertex3D((double)(x+30), y,0.0 , Color.WHITE);
//
//
//                //make bottom polygon
//                vertices = new Vertex3D[3];
//
//                vertices[0] = rightCorner;
//                vertices[1] = bot_rightCorner;
//                vertices[2] = leftCorner;
//
//                Polygon polygon_bot = Polygon.makeEnsuringClockwise(vertices);
//
//                //make top polygon
//                vertices[0] = rightCorner;
//                vertices[1] = top_leftCorner;
//                vertices[2] = leftCorner;
//
//                Polygon polygon_top = Polygon.makeEnsuringClockwise(vertices);
//
//                //rendering begin
//                renderer.drawPolygon(polygon_bot, panel, null);
//                renderer.drawPolygon(polygon_top, panel, null);
//            }
//        }
//    }
//
//








//    private void render_USE_PERTURBATION() {
//        for (int y = 15; y < 285; y = y + 30) {
//            for (int x = 15; x < 285; x = x + 30) {
//                //leftCorner
//                Collections.shuffle(assets);
//                a = x + assets.get(0);
//                b = y + assets.get(0);
//                i = 0; j = 0;
//                while(i < 25 && panel.getPixel(a,b) != Drawable.ARGB_BLACK){
//                    while(j < 25 && panel.getPixel(a,b) != Drawable.ARGB_BLACK){
//                        b = y + assets.get(j);
//                        j++;
//                    }
//                    a = x + assets.get(i);
//                    i++;
//                }
//                Vertex3D leftCorner = new Vertex3D(a, b, 0.0, Color.WHITE);
//
//
//                //rightCorner
//                Collections.shuffle(assets);
//                a = x + 30 + assets.get(0);
//                b = y + 30 + assets.get(0);
//                i = 0; j = 0;
//                while(i < 25 && panel.getPixel(a,b) != Drawable.ARGB_BLACK){
//                    while(j < 25 && panel.getPixel(a,b) != Drawable.ARGB_BLACK){
//                        b = y + 30 + assets.get(j);
//                        j++;
//                    }
//                    a = x + 30 + assets.get(i);
//                    i++;
//                }
//                Vertex3D rightCorner = new Vertex3D(a, b, 0.0, Color.WHITE);
//
//
//
//                //top_left_Corner
//                Collections.shuffle(assets);
//                a = x + assets.get(0);
//                b = y + 30 + assets.get(0);
//                i = 0; j = 0;
//                while(i < 25 && panel.getPixel(a,b) != Drawable.ARGB_BLACK){
//                    while(j < 25 && panel.getPixel(a,b) != Drawable.ARGB_BLACK){
//                        b = y + 30 + assets.get(j);
//                        j++;
//                    }
//                    a = x + assets.get(i);
//                    i++;
//                }
//                Vertex3D top_leftCorner = new Vertex3D(a, b, 0.0, Color.WHITE);
//
//
//
//                //bot_right__Corner
//                Collections.shuffle(assets);
//                a = x + 30 + assets.get(0);
//                b = y + assets.get(0);
//                i = 0; j = 0;
//                while(i < 25 && panel.getPixel(a,b) != Drawable.ARGB_BLACK){
//                    while(j < 25 && panel.getPixel(a,b) != Drawable.ARGB_BLACK){
//                        b = y + assets.get(j);
//                        j++;
//                    }
//                    a = x + 30 + assets.get(i);
//                    i++;
//                }
//                Vertex3D bot_rightCorner = new Vertex3D(a, b, 0.0, Color.WHITE);
//
//
//                //make bottom polygon
//                vertices = new Vertex3D[3];
//
//                vertices[0] = rightCorner;
//                vertices[1] = bot_rightCorner;
//                vertices[2] = leftCorner;
//
//                Polygon polygon_bot = Polygon.makeEnsuringClockwise(vertices);
//
//                //make top polygon
//                vertices[0] = rightCorner;
//                vertices[1] = top_leftCorner;
//                vertices[2] = leftCorner;
//
//                Polygon polygon_top = Polygon.makeEnsuringClockwise(vertices);
//
//                //rendering begin
//                renderer.drawPolygon(polygon_bot, panel, null);
//                renderer.drawPolygon(polygon_top, panel, null);
//            }
//
//
//        }
//    }
