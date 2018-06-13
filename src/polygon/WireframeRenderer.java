package polygon;

import geometry.Vertex3D;
import polygon.Polygon;
import polygon.Shader;
import windowing.drawable.Drawable;

public interface WireframeRenderer {
    public void drawPolygon(Polygon polygon, Drawable drawable, Shader vertexShader);

    default public void drawPolygon(Polygon polygon, Drawable panel) {
        drawPolygon(polygon, panel,  c -> c);
    };
}
