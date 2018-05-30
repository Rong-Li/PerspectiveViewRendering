package client.testPages;

import geometry.Vertex3D;
import polygon.Polygon;
import polygon.PolygonRenderer;
import polygon.Shader;
import windowing.drawable.Drawable;
import windowing.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class StarburstPolygonTest {

    private final PolygonRenderer renderer;
    private final Drawable panel;
    private Vertex3D vertices[];

    public StarburstPolygonTest(Drawable panel, PolygonRenderer polygonRenderer) {
        this.panel = panel;
        this.renderer = polygonRenderer;

        render();
    }

    private void render() {
//        Vertex3D p1 = new Vertex3D(250.0, 200, 0.0, Color.WHITE);// top
//        Vertex3D p2 = new Vertex3D(10.0, 100, 0.0, Color.WHITE); // left
//        Vertex3D p3 = new Vertex3D(100.0, 20, 0.0, Color.WHITE); // right


//        Vertex3D p1 = new Vertex3D(100.0, 200, 0.0, Color.WHITE);// top
//        Vertex3D p2 = new Vertex3D(20.0, 20, 0.0, Color.WHITE); // left
//        Vertex3D p3 = new Vertex3D(200.0, 20, 0.0, Color.WHITE); // right

        Vertex3D p1 = new Vertex3D(200.0, 250.0, 0.0, Color.WHITE);// top
        Vertex3D p2 = new Vertex3D(10.0, 10.0, 0.0, Color.WHITE); // left
        Vertex3D p3 = new Vertex3D(180.0, 100, 0.0, Color.WHITE); // right

        vertices = new Vertex3D[3];

        vertices[0] = p1;
        vertices[1] = p2;
        vertices[2] = p3;


        Polygon polygon = Polygon.make(vertices);

        renderer.drawPolygon(polygon, panel, null);
    }
}
