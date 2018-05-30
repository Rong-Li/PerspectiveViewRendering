package line;

import geometry.Vertex;
import geometry.Vertex3D;
import windowing.drawable.Drawable;
import windowing.graphics.Color;

public class DDALineRenderer implements LineRenderer {
    private DDALineRenderer(){}

    @Override
    public void drawLine(Vertex3D p1, Vertex3D p2, Drawable drawable) {
        double deltaX = p2.getIntX() - p1.getIntX();
        double deltaY = p2.getIntY() - p1.getIntY();

        double slope = deltaY / deltaX;
        double intercept = p2.getIntY() - slope * p2.getIntX();
        double y = p1.getIntY();
        int argbColor = p1.getColor().asARGB();

        for(int x = p1.getIntX(); x <= p2.getIntX(); x++) {
            drawable.setPixel(x, (int)Math.round(y), 0.0, argbColor);
            y = y + slope;
        }
    }

    public static LineRenderer make() {
        return new AnyOctantLineRenderer(new DDALineRenderer());
    }

}
