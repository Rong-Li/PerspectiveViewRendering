package client.testPages;

import polygon.PolygonRenderer;
import windowing.drawable.Drawable;

public class centeredTriangleTest {

    private final PolygonRenderer renderer;
    private final Drawable panel;

    public centeredTriangleTest(Drawable fullPanel, PolygonRenderer polygonRenderer) {
        this.renderer = polygonRenderer;
        this.panel = fullPanel;
    }
}
