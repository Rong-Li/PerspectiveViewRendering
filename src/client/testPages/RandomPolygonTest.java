package client.testPages;

import geometry.Vertex3D;
import polygon.Polygon;
import polygon.PolygonRenderer;
import windowing.drawable.Drawable;
import windowing.graphics.Color;

import java.util.Random;

public class RandomPolygonTest {
    private final PolygonRenderer renderer;
    private final Drawable panel;
    private Vertex3D vertices[];

    public RandomPolygonTest(Drawable panel, PolygonRenderer polygonRenderer) {
        this.panel = panel;
        this.renderer = polygonRenderer;

        render();
    }

    private void render() {
        Random random = new Random();
        for (int i = 0; i < 20; i++){
            Vertex3D p1 = new Vertex3D(random.nextInt(300), random.nextInt(300), 0.0, Color.random(random));
            Vertex3D p2 = new Vertex3D(random.nextInt(300), random.nextInt(300), 0.0, Color.random(random));
            Vertex3D p3 = new Vertex3D(random.nextInt(300), random.nextInt(300), 0.0, Color.random(random));

            vertices = new Vertex3D[3];

            vertices[2] = p1;
            vertices[0] = p2;
            vertices[1] = p3;

            Polygon polygon = Polygon.makeEnsuringClockwise(vertices);

            renderer.drawPolygon(polygon, panel, null);
        }
    }
}
