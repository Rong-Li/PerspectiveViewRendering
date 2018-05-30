package line;

import geometry.Vertex;
import geometry.Vertex3D;
import windowing.drawable.Drawable;
import windowing.graphics.Color;

public class BresenhamLineRenderer implements LineRenderer {
    private BresenhamLineRenderer(){}

    @Override
    public void drawLine(Vertex3D p1, Vertex3D p2, Drawable drawable) {
        double deltaX = p2.getIntX() - p1.getIntX();
        double deltaY = p2.getIntY() - p1.getIntY();

        double m = 2 * deltaY;
        double q = m - 2 * deltaX;
        int argbColor = p1.getColor().asARGB();
        drawable.setPixel(p1.getIntX(), p1.getIntY(), 0.0, argbColor);

        double y = p1.getIntY();
        double err = m - deltaX;

        for(int x = p1.getIntX() + 1; x <= p2.getIntX(); x++) {
            if (err >= 0){
                err += q;
                y++;
            }
            else{
                err += m;
            }
            drawable.setPixel(x, (int)y, 0.0, argbColor);
        }


    }

    public static LineRenderer make() {
        return new AnyOctantLineRenderer(new BresenhamLineRenderer());
    }

}
