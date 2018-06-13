package polygon;

import geometry.Vertex3D;
import windowing.drawable.Drawable;
import windowing.graphics.Color;

public class blerpingFilledPolygoneRenderer implements PolygonRenderer{
    private blerpingFilledPolygoneRenderer(){}

    @Override
    public void drawPolygon(Polygon polygon, Drawable drawable, Shader vertexShader){
        Chain left_chain = polygon.leftChain();
        Chain right_chain = polygon.rightChain();


        Vertex3D p_top = left_chain.get(0);
        Vertex3D p_bottomLeft = left_chain.get(1);
        Vertex3D p_bottomRight = right_chain.get(1);

        // if having horizontal bottom line
        if(/*left_chain.get(0).getIntY() > left_chain.get(1).getIntY()
                && right_chain.get(0).getIntY() > right_chain.get(1).getIntY()
                && */left_chain.get(1).getIntY() == right_chain.get(1).getIntY()){
            //System.out.println("wrong1");

            Horizontal_Bottom(p_top,p_bottomLeft,p_bottomRight,drawable);
        }
        // if having Non-horizontal bottom line
        //if left edge if shorter
        else if(left_chain.get(1).getIntY() > right_chain.get(1).getIntY()
                && left_chain.get(1).getIntY() != left_chain.get(0).getIntY()
                && right_chain.get(1).getIntY() != left_chain.get(0).getIntY()){
            //System.out.println("wrong2");

            Non_Horizontal_LeftShort(p_top,p_bottomLeft,p_bottomRight,drawable);
        }
        //if right edge is shorter
        else if (left_chain.get(1).getIntY() < right_chain.get(1).getIntY()
                && left_chain.get(1).getIntY() != left_chain.get(0).getIntY()
                && right_chain.get(1).getIntY() != left_chain.get(0).getIntY()){
            //System.out.println("wrong3");
            Non_Horizontal_RightShort(p_top, p_bottomLeft, p_bottomRight, drawable);
        }

        //if horizontal top line
        else if (left_chain.get(1).getIntY() == left_chain.get(0).getIntY()
                || right_chain.get(1).getIntY() == right_chain.get(0).getIntY()){
            //System.out.println("Right");

            // 1st case when the top is at right node
            if (left_chain.get(1).getIntY() == left_chain.get(0).getIntY()){
                Vertex3D p_topLeft = left_chain.get(1);
                Vertex3D p_topRight = left_chain.get(0);
                Vertex3D p_bottom = right_chain.get(1);

                Horizontal_top(p_topLeft,p_topRight,p_bottom,drawable);
            }
            // 2nd case when the top is at left node
            else if(right_chain.get(1).getIntY() == right_chain.get(0).getIntY()){
                Vertex3D p_topLeft = left_chain.get(0);
                Vertex3D p_topRight = right_chain.get(1);
                Vertex3D p_bottom = left_chain.get(1);

                Horizontal_top(p_topLeft,p_topRight,p_bottom,drawable);
            }
        }

    }





    private void Non_Horizontal_RightShort(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        double r = p_top.getColor().getR();
        double g = p_top.getColor().getG();
        double b = p_top.getColor().getB();
        Vertex3D p_middle = p_bottomRight;
        //left long edge
        double deltaX1 = p_top.getIntX() - p_bottomLeft.getIntX();
        double deltaY1 = p_top.getIntY() - p_bottomLeft.getIntY();
        double mr1 = getMforColors(p_top, p_bottomLeft, "red");
        double mg1 = getMforColors(p_top, p_bottomLeft, "green");
        double mb1 = getMforColors(p_top, p_bottomLeft, "blue");

        //right short top edge
        double deltaX2 = p_bottomRight.getIntX() - p_top.getIntX();
        double deltaY2 = p_bottomRight.getIntY() - p_top.getIntY();
        double mr2 = getMforColors(p_top, p_bottomRight, "red");
        double mg2 = getMforColors(p_top, p_bottomRight, "green");
        double mb2 = getMforColors(p_top, p_bottomRight, "blue");

        //right short bot edge
        double deltaX2_2 = p_bottomRight.getIntX() - p_bottomLeft.getIntX();
        double deltaY2_2 = p_bottomRight.getIntY() - p_bottomLeft.getIntY();
        double mr2_2 = getMforColors(p_bottomRight, p_bottomLeft, "red");
        double mg2_2 = getMforColors(p_bottomRight, p_bottomLeft, "green");
        double mb2_2 = getMforColors(p_bottomRight, p_bottomLeft, "blue");

        double L_slope = deltaX1 / deltaY1;
        double R_slope = deltaX2 / deltaY2;
        double R2_slope = deltaX2_2 / deltaY2_2;

        double start_point = p_top.getIntX();
        double end_point = p_top.getIntX();

        int y = p_top.getIntY();
        int argbColor = Color.random().asARGB();

        //rendering begin
        while (y >= p_bottomLeft.getIntY()) {
            if (y > p_middle.getIntY()) {
                fillPixels_leftToRight(start_point, end_point, y, argbColor, drawable);
                start_point = start_point - L_slope;
                end_point = end_point - R_slope;
                y--;
            } else {
                fillPixels_leftToRight(start_point, end_point, y, argbColor, drawable);
                start_point = start_point - L_slope;
                end_point = end_point - R2_slope;
                y--;
            }
        }
    }

    private double getMforColors(Vertex3D p1, Vertex3D p2, String color) {
        double deltaY = p1.getIntY() - p2.getIntY();
        double p1_r = 1.0;
        double p2_r = 0.0;
        if (color == "red"){
            p1_r = p1.getColor().getR();
            p2_r = p2.getColor().getR();

        } else if (color == "green"){
            p1_r = p1.getColor().getG();
            p2_r = p2.getColor().getG();

        } else if (color == "blue"){
            p1_r = p1.getColor().getB();
            p2_r = p2.getColor().getB();
        }
        double deltaR = p1_r - p2_r;
        double m = deltaR/deltaY;
        return m;
    }

    private void Non_Horizontal_LeftShort(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        Vertex3D p_middle = p_bottomLeft;

        double deltaX1_1 = p_top.getIntX() - p_bottomLeft.getIntX();
        double deltaY1_1 = p_top.getIntY() - p_bottomLeft.getIntY();

        double deltaX1_2 = p_bottomRight.getIntX() - p_bottomLeft.getIntX();
        double deltaY1_2 = p_bottomRight.getIntY() - p_bottomLeft.getIntY();

        double deltaX2 = p_bottomRight.getIntX() - p_top.getIntX();
        double deltaY2 = p_bottomRight.getIntY() - p_top.getIntY();

        double L1_slope = deltaX1_1 / deltaY1_1;
        double L2_slope = deltaX1_2 / deltaY1_2;
        double R_slope = deltaX2 / deltaY2;

        double start_point = p_top.getIntX();
        double end_point = p_top.getIntX();

        int y = p_top.getIntY();
        int argbColor = Color.random().asARGB();

        //rendering begin
        while(y >= p_bottomRight.getIntY()){
            if (y > p_middle.getIntY()){
                fillPixels_leftToRight(start_point, end_point, y, argbColor, drawable);
                start_point = start_point - L1_slope;
                end_point = end_point - R_slope;
                y--;
            }
            else{
                fillPixels_leftToRight(start_point, end_point, y, argbColor, drawable);
                start_point = start_point - L2_slope;
                end_point = end_point - R_slope;
                y--;
            }
        }
    }

    private void Horizontal_Bottom(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        double deltaX1 = p_top.getIntX() - p_bottomLeft.getIntX();
        double deltaY1 = p_top.getIntY() - p_bottomLeft.getIntY();

        double deltaX2 = p_top.getIntX() - p_bottomRight.getIntX();
        double deltaY2 = p_top.getIntY() - p_bottomRight.getIntY();

        double L_slope = deltaX1 / deltaY1;
        double R_slope = deltaX2 / deltaY2;
        double start_point = p_top.getIntX();
        double end_point = p_top.getIntX();
        int y = p_top.getIntY();
        int argbColor = Color.random().asARGB();

        //rendering begin
        while(y > p_bottomLeft.getIntY()){
            fillPixels_leftToRight(start_point, end_point, y, argbColor, drawable);
            start_point = start_point - L_slope;
            end_point = end_point - R_slope;
            y--;
        }
    }

    private void Horizontal_top(Vertex3D p_topLeft, Vertex3D p_topRight, Vertex3D p_bottom, Drawable drawable) {
        double deltaX1 = p_topLeft.getIntX() - p_bottom.getIntX();
        double deltaY1 = p_topLeft.getIntY() - p_bottom.getIntY();

        double deltaX2 = p_topRight.getIntX() - p_bottom.getIntX();
        double deltaY2 = p_topRight.getIntY() - p_bottom.getIntY();

        double L_slope = deltaX1 / deltaY1;
        double R_slope = deltaX2 / deltaY2;
        double start_point = p_topLeft.getIntX();
        double end_point = p_topRight.getIntX();
        int y = p_topLeft.getIntY();
        int argbColor = Color.random().asARGB();

        //rendering begin
        while(y >= p_bottom.getIntY()){
            fillPixels_leftToRight(start_point, end_point, y, argbColor, drawable);
            start_point = start_point - L_slope;
            end_point = end_point - R_slope;
            y--;
        }
    }



    private void fillPixels_leftToRight(double x_start, double x_end, int y, int color, Drawable drawable) {
        int start = (int)Math.round(x_start);
        int end = (int)Math.round(x_end);
        if( start == end) {
//            if (drawable.getPixel(start,y) != drawable.ARGB_BLACK){}
//            else{
//                drawable.setPixel(start, y, 0.0, color);
//             }

            drawable.setPixel(start, y, 0.0, color);

        }else{
            for (int i = start; i < end; i++)
                drawable.setPixel(i, y, 0.0, color);
        }
    }







    public static PolygonRenderer make() {
        return new blerpingFilledPolygoneRenderer();
    }

}
