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

        double p1_r = p1.getColor().getR();
        double p2_r = p2.getColor().getR();
        double delta_r = p2_r - p1_r;
        double mr = delta_r/deltaX;

        double p1_g = p1.getColor().getG();
        double p2_g = p2.getColor().getG();
        double delta_g = p2_g - p1_g;
        double mg = delta_g/deltaX;

        double p1_b = p1.getColor().getB();
        double p2_b = p2.getColor().getB();
        double delta_b = p2_b - p1_b;
        double mb = delta_b/deltaX;

        double y = p1.getIntY();
        double r = p1.getColor().getR();
        double g = p1.getColor().getG();
        double b = p1.getColor().getB();


        for(int x = p1.getIntX(); x <= p2.getIntX(); x++) {
            Color color = new Color(r,g,b);
            drawable.setPixel(x, (int)Math.round(y), 0.0, color.asARGB());
            y = y + slope;
            r = r + mr;
            g = g + mg;
            b = b + mb;
        }
    }

    public static LineRenderer make() {
        return new AnyOctantLineRenderer(new DDALineRenderer());
    }

}
