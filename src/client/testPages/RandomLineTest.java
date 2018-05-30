package client.testPages;

import geometry.Vertex3D;
import line.LineRenderer;
import windowing.drawable.Drawable;
import windowing.graphics.Color;

import java.util.Random;

public class RandomLineTest {
    private final LineRenderer renderer;
    private final Drawable panel;

    public RandomLineTest(Drawable panel, LineRenderer renderer) {
        this.panel = panel;
        this.renderer = renderer;

        render();
    }

    private void render() {
        Random random = new Random(10);
        for (int i = 0; i < 30; i++){
            Vertex3D p1 = new Vertex3D(random.nextInt(300), random.nextInt(300), 0.0, Color.random(random));
            Vertex3D p2 = new Vertex3D(random.nextInt(300), random.nextInt(300), 0.0, Color.WHITE);

            renderer.drawLine(p1,p2,panel);
        }
    }
}
