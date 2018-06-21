package polygon;

import geometry.Vertex3D;
import windowing.drawable.Drawable;
import windowing.graphics.Color;

public class blerpingFilledPolygoneRenderer implements PolygonRenderer {
    private blerpingFilledPolygoneRenderer() {
    }

    @Override
    public void drawPolygon(Polygon polygon, Drawable drawable, Shader vertexShader) {
        if (outofRange(polygon, drawable)) {
            return;
        }
        if (polygon.get(0).getY() == polygon.get(1).getY()
                && polygon.get(1).getY() == polygon.get(2).getY()) {
            return;
        }
        if (polygon.get(0).getX() == polygon.get(1).getX()
                && polygon.get(1).getX() == polygon.get(2).getX()) {
            return;
        }
        Chain left_chain = polygon.leftChain();
        Chain right_chain = polygon.rightChain();


        Vertex3D p_top = left_chain.get(0);
        Vertex3D p_bottomLeft = left_chain.get(1);
        Vertex3D p_bottomRight = right_chain.get(1);

        // if having horizontal bottom line
        if (/*left_chain.get(0).getIntY() > left_chain.get(1).getIntY()
                && right_chain.get(0).getIntY() > right_chain.get(1).getIntY()
                && */left_chain.get(1).getIntY() == right_chain.get(1).getIntY()) {
            //System.out.println("wrong1");

            Horizontal_Bottom(p_top, p_bottomLeft, p_bottomRight, drawable);
        }
        // if having Non-horizontal bottom line
        //if left edge if shorter
        else if (left_chain.get(1).getIntY() > right_chain.get(1).getIntY()
                && left_chain.get(1).getIntY() != left_chain.get(0).getIntY()
                && right_chain.get(1).getIntY() != left_chain.get(0).getIntY()) {
            //System.out.println("wrong2");

            Non_Horizontal_LeftShort(p_top, p_bottomLeft, p_bottomRight, drawable);
        }
        //if right edge is shorter
        else if (left_chain.get(1).getIntY() < right_chain.get(1).getIntY()
                && left_chain.get(1).getIntY() != left_chain.get(0).getIntY()
                && right_chain.get(1).getIntY() != left_chain.get(0).getIntY()) {
            //System.out.println("wrong3");
            Non_Horizontal_RightShort(p_top, p_bottomLeft, p_bottomRight, drawable);
        }

        //if horizontal top line
        else if (left_chain.get(1).getIntY() == left_chain.get(0).getIntY()
                || right_chain.get(1).getIntY() == right_chain.get(0).getIntY()) {
            //System.out.println("Right");

            // 1st case when the top is at right node
            if (left_chain.get(1).getIntY() == left_chain.get(0).getIntY()) {
                Vertex3D p_topLeft = left_chain.get(1);
                Vertex3D p_topRight = left_chain.get(0);
                Vertex3D p_bottom = right_chain.get(1);

                Horizontal_top(p_topLeft, p_topRight, p_bottom, drawable);
            }
            // 2nd case when the top is at left node
            else if (right_chain.get(1).getIntY() == right_chain.get(0).getIntY()) {
                Vertex3D p_topLeft = left_chain.get(0);
                Vertex3D p_topRight = right_chain.get(1);
                Vertex3D p_bottom = left_chain.get(1);

                Horizontal_top(p_topLeft, p_topRight, p_bottom, drawable);
            }
        }

    }


    private void Non_Horizontal_RightShort(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        Vertex3D p_middle = p_bottomRight;
        //left long edge
        double deltaX1 = p_top.getIntX() - p_bottomLeft.getIntX();
        double deltaY1 = p_top.getIntY() - p_bottomLeft.getIntY();
        Color m1 = DecrementforColors(p_top, p_bottomLeft);
        double z_slope1 = DecrementforZ(p_top, p_bottomLeft);

        //right short top edge
        double deltaX2 = p_bottomRight.getIntX() - p_top.getIntX();
        double deltaY2 = p_bottomRight.getIntY() - p_top.getIntY();
        Color m2 = DecrementforColors(p_top, p_bottomRight);
        double z_slope2 = DecrementforZ(p_top, p_bottomRight);

        //right short bot edge
        double deltaX2_2 = p_bottomRight.getIntX() - p_bottomLeft.getIntX();
        double deltaY2_2 = p_bottomRight.getIntY() - p_bottomLeft.getIntY();
        Color m2_2 = DecrementforColors(p_bottomRight, p_bottomLeft);
        double z_slope2_2 = DecrementforZ(p_bottomRight, p_bottomLeft);

        double L_slope = deltaX1 / deltaY1;
        double R_slope = deltaX2 / deltaY2;
        double R2_slope = deltaX2_2 / deltaY2_2;

        double start_point = p_top.getIntX();
        double end_point = p_top.getIntX();

        int y = p_top.getIntY();
        Color c1 = p_top.getColor();
        Color c2 = p_top.getColor();
        double z1 = p_top.getIntZ();
        double z2 = p_top.getIntZ();


        //rendering begin
        while (y >= p_bottomLeft.getIntY()) {
            if (y > p_middle.getIntY()) {
                blerping_fillPixels_leftToRight(start_point, end_point, y, c1, c2, drawable, z1, z2);
                start_point = start_point - L_slope;
                end_point = end_point - R_slope;
                c1 = c1.subtract(m1);
                c2 = c2.subtract(m2);
                z1 = z1 - z_slope1;
                z2 = z2 - z_slope2;
                y--;
            } else {
                blerping_fillPixels_leftToRight(start_point, end_point, y, c1, c2, drawable, z1, z2);
                start_point = start_point - L_slope;
                end_point = end_point - R2_slope;
                c1 = c1.subtract(m1);
                c2 = c2.subtract(m2_2);
                z1 = z1 - z_slope1;
                z2 = z2 - z_slope2_2;
                y--;
            }
        }
    }

    private void Non_Horizontal_LeftShort(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        Vertex3D p_middle = p_bottomLeft;

        double deltaX1_1 = p_top.getIntX() - p_bottomLeft.getIntX();
        double deltaY1_1 = p_top.getIntY() - p_bottomLeft.getIntY();
        Color m1_1 = DecrementforColors(p_top, p_bottomLeft);
        double z_slope1_1 = DecrementforZ(p_top, p_bottomLeft);


        double deltaX1_2 = p_bottomRight.getIntX() - p_bottomLeft.getIntX();
        double deltaY1_2 = p_bottomRight.getIntY() - p_bottomLeft.getIntY();
        Color m1_2 = DecrementforColors(p_bottomRight, p_bottomLeft);
        double z_slope1_2 = DecrementforZ(p_bottomRight, p_bottomLeft);

        double deltaX2 = p_bottomRight.getIntX() - p_top.getIntX();
        double deltaY2 = p_bottomRight.getIntY() - p_top.getIntY();
        Color m2 = DecrementforColors(p_bottomRight, p_top);
        double z_slope2 = DecrementforZ(p_bottomRight, p_top);


        double L1_slope = deltaX1_1 / deltaY1_1;
        double L2_slope = deltaX1_2 / deltaY1_2;
        double R_slope = deltaX2 / deltaY2;

        double start_point = p_top.getIntX();
        double end_point = p_top.getIntX();


        int y = p_top.getIntY();
        Color c1 = p_top.getColor();
        Color c2 = p_top.getColor();
        double z1 = p_top.getIntZ();
        double z2 = p_top.getIntZ();

        //rendering begin
        while (y >= p_bottomRight.getIntY()) {
            if (y > p_middle.getIntY()) {
                blerping_fillPixels_leftToRight(start_point, end_point, y, c1, c2, drawable, z1, z2);
                start_point = start_point - L1_slope;
                end_point = end_point - R_slope;
                c1 = c1.subtract(m1_1);
                c2 = c2.subtract(m2);
                z1 = z1 - z_slope1_1;
                z2 = z2 - z_slope2;
                y--;
            } else {
                blerping_fillPixels_leftToRight(start_point, end_point, y, c1, c2, drawable, z1, z2);
                start_point = start_point - L2_slope;
                end_point = end_point - R_slope;
                c1 = c1.subtract(m1_2);
                c2 = c2.subtract(m2);
                z1 = z1 - z_slope1_2;
                z2 = z2 - z_slope2;
                y--;
            }
        }
    }

    private void Horizontal_Bottom(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        double deltaX1 = p_top.getIntX() - p_bottomLeft.getIntX();
        double deltaY1 = p_top.getIntY() - p_bottomLeft.getIntY();
        Color m1 = DecrementforColors(p_top, p_bottomLeft);
        double z_slope1 = DecrementforZ(p_top, p_bottomLeft);

        double deltaX2 = p_top.getIntX() - p_bottomRight.getIntX();
        double deltaY2 = p_top.getIntY() - p_bottomRight.getIntY();
        Color m2 = DecrementforColors(p_top, p_bottomRight);
        double z_slope2 = DecrementforZ(p_top, p_bottomRight);

        double L_slope = deltaX1 / deltaY1;
        double R_slope = deltaX2 / deltaY2;
        double start_point = p_top.getIntX();
        double end_point = p_top.getIntX();
        int y = p_top.getIntY();
        Color c1 = p_top.getColor();
        Color c2 = p_top.getColor();
        double z1 = p_top.getIntZ();
        double z2 = p_top.getIntZ();

        //rendering begin
        while (y > p_bottomLeft.getIntY()) {
            blerping_fillPixels_leftToRight(start_point, end_point, y, c1, c2, drawable, z1, z2);
            start_point = start_point - L_slope;
            end_point = end_point - R_slope;
            c1 = c1.subtract(m1);
            c2 = c2.subtract(m2);
            z1 = z1 - z_slope1;
            z2 = z2 - z_slope2;
            y--;
        }
    }

    private void Horizontal_top(Vertex3D p_topLeft, Vertex3D p_topRight, Vertex3D p_bottom, Drawable drawable) {
        double deltaX1 = p_topLeft.getIntX() - p_bottom.getIntX();
        double deltaY1 = p_topLeft.getIntY() - p_bottom.getIntY();
        Color m1 = DecrementforColors(p_topLeft, p_bottom);
        double z_slope1 = DecrementforZ(p_topLeft, p_bottom);


        double deltaX2 = p_topRight.getIntX() - p_bottom.getIntX();
        double deltaY2 = p_topRight.getIntY() - p_bottom.getIntY();
        Color m2 = DecrementforColors(p_topRight, p_bottom);
        double z_slope2 = DecrementforZ(p_topRight, p_bottom);

        double L_slope = deltaX1 / deltaY1;
        double R_slope = deltaX2 / deltaY2;
        double start_point = p_topLeft.getIntX();
        double end_point = p_topRight.getIntX();
        int y = p_topLeft.getIntY();
        Color c1 = p_topLeft.getColor();
        Color c2 = p_topRight.getColor();
        double z1 = p_topLeft.getIntZ();
        double z2 = p_topRight.getIntZ();

        //rendering begin
        while (y >= p_bottom.getIntY()) {
            blerping_fillPixels_leftToRight(start_point, end_point, y, c1, c2, drawable, z1, z2);
            start_point = start_point - L_slope;
            end_point = end_point - R_slope;
            c1 = c1.subtract(m1);
            c2 = c2.subtract(m2);
            z1 = z1 - z_slope1;
            z2 = z2 - z_slope2;
            y--;
        }
    }


    private Color DecrementforColors(Vertex3D p1, Vertex3D p2) {
        double deltaY = p1.getIntY() - p2.getIntY();

        double r1 = p1.getColor().getR();
        double r2 = p2.getColor().getR();
        double deltaR = r1 - r2;
        double mr = deltaR / deltaY;

        double g1 = p1.getColor().getG();
        double g2 = p2.getColor().getG();
        double deltaG = g1 - g2;
        double mg = deltaG / deltaY;

        double b1 = p1.getColor().getB();
        double b2 = p2.getColor().getB();
        double deltaB = b1 - b2;
        double mb = deltaB / deltaY;

        Color result = new Color(mr, mg, mb);
        return result;
    }

    private double DecrementforZ(Vertex3D p1, Vertex3D p2) {
        double deltaY = p1.getIntY() - p2.getIntY();
        double deltaZ = p1.getIntZ() - p2.getIntZ();
        double result = deltaZ / deltaY;
        return result;
    }

    private void blerping_fillPixels_leftToRight(double x_start, double x_end, int y, Color c1, Color c2, Drawable drawable, double z1, double z2) {
        Color newColor = c1;
        double z = z1;
        double deltaX = x_end - x_start;
        double deltaR = c2.getR() - c1.getR();
        double deltaG = c2.getG() - c1.getG();
        double deltaB = c2.getB() - c1.getB();
        double deltaZ = z2 - z1;
        double m_r = deltaR / deltaX;
        double m_g = deltaG / deltaX;
        double m_b = deltaB / deltaX;
        double z_slope = deltaZ / deltaX;

        Color addOn = new Color(m_r, m_g, m_b);

        int start = (int) Math.round(x_start);
        int end = (int) Math.round(x_end);
        if (start == end) {
            drawable.setPixel(start, y, z, c1.asARGB());

        } else {
            for (int i = start; i < end; i++) {
                drawable.setPixel(i, y, z, newColor.asARGB());
                newColor = newColor.add(addOn);
                z = z + z_slope;
            }
        }
    }


    public static PolygonRenderer make() {
        return new blerpingFilledPolygoneRenderer();
    }

    public boolean outofRange(Polygon polygon, Drawable panel) {
        boolean result = false;
        Vertex3D p1 = polygon.get(0);
        Vertex3D p2 = polygon.get(1);
        Vertex3D p3 = polygon.get(2);
        if (vertexIsOutSideOfPanel(p1,panel) && vertexIsOutSideOfPanel(p2,panel) && vertexIsOutSideOfPanel(p3,panel)) {
            result = true;
        }
        return result;
    }

    public boolean vertexIsOutSideOfPanel(Vertex3D p, Drawable panel){
        boolean result = false;
        if (p.getIntX() > panel.getWidth() || p.getIntY() > panel.getHeight()){
            result = true;
        }
        return result;
    }
}
