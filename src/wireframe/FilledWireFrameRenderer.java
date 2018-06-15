package wireframe;

import line.DDALineRenderer;
import line.LineRenderer;
import polygon.Polygon;
import polygon.Shader;
import windowing.drawable.Drawable;

public class FilledWireFrameRenderer implements WireframeRenderer {
    private FilledWireFrameRenderer(){}
    private LineRenderer lineRenderer = DDALineRenderer.make();

    @Override
    public void drawPolygon(Polygon polygon, Drawable drawable, Shader vertexShader) {
        lineRenderer.drawLine(polygon.get(0), polygon.get(1), drawable);
        lineRenderer.drawLine(polygon.get(1), polygon.get(2), drawable);
        lineRenderer.drawLine(polygon.get(0), polygon.get(2), drawable);
    }


    public static WireframeRenderer make() {
        return new FilledWireFrameRenderer();
    }
}
