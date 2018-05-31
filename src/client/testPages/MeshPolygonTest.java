package client.testPages;

import geometry.Vertex3D;
import polygon.Polygon;
import polygon.PolygonRenderer;
import windowing.drawable.Drawable;
import windowing.graphics.Color;

public class MeshPolygonTest {

    private final PolygonRenderer renderer;
    private final Drawable panel;
    public static String NO_PERTURBATION = "NO_PERTURBATION";
    public static String USE_PERTURBATION = "USE_PERTURBATION";
    private Vertex3D vertices[];

    public MeshPolygonTest(Drawable panel, PolygonRenderer polygonRenderer, String Which) {
        this.panel = panel;
        this.renderer = polygonRenderer;

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
        for (int y = 15; y < 285; y = y + 30) {
            for (int x = 15; x < 285; x = x + 30) {

                Vertex3D leftCorner = new Vertex3D(x, y ,0.0, Color.WHITE);
                Vertex3D rightCorner = new Vertex3D((double)(x+30), (double)(y+30),0.0 , Color.WHITE);
                Vertex3D top_leftCorner = new Vertex3D(x, (double)(y+30),0.0 , Color.WHITE);
                Vertex3D bot_rightCorner = new Vertex3D((double)(x+30), y,0.0 , Color.WHITE);

                //make bottom polygon
                vertices = new Vertex3D[3];

                vertices[0] = rightCorner;
                vertices[1] = bot_rightCorner;
                vertices[2] = leftCorner;

                Polygon polygon_bot = Polygon.makeEnsuringClockwise(vertices);

                //make top polygon
                vertices[0] = rightCorner;
                vertices[1] = top_leftCorner;
                vertices[2] = leftCorner;

                Polygon polygon_top = Polygon.makeEnsuringClockwise(vertices);

                //rendering begin
                renderer.drawPolygon(polygon_bot, panel, null);
                renderer.drawPolygon(polygon_top, panel, null);
            }
        }


    }

    private void render_USE_PERTURBATION() {
    }

}
