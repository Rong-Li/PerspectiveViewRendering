package client.testPages;

import geometry.Vertex3D;
import line.LineRenderer;
import windowing.drawable.Drawable;
import windowing.graphics.Color;


public class ParallelogramTest {
    private final LineRenderer renderer;
    private final Drawable panel;

    public ParallelogramTest(Drawable panel, LineRenderer renderer) {
        this.panel = panel;
        this.renderer = renderer;

        render();
    }

    private void render() {
        for (int p = 0; p < 50; p++){
            Vertex3D p1 = new Vertex3D(20.0, (300- (80 + p)), 0.0, Color.WHITE);
            Vertex3D p2 = new Vertex3D(150.0, (300 - (150 + p)), 0.0, Color.WHITE);
            renderer.drawLine(p1, p2, panel);
        }

        for (int p = 0; p < 50; p++){
            Vertex3D p1 = new Vertex3D((double)(160 + p), 30.0, 0.0, Color.WHITE);
            Vertex3D p2 = new Vertex3D((double)(240 + p), 260.0, 0.0, Color.WHITE);
            renderer.drawLine(p1, p2, panel);
        }
    }
}
