package polygon;

import geometry.Vertex;
import geometry.Vertex3D;
import windowing.drawable.Drawable;
import windowing.graphics.Color;

public class FilledPolygonRenderer implements PolygonRenderer{
    private FilledPolygonRenderer(){}

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
            //System.out.println("x_start: !!!!!!!!!!!!!!!!!");

            if (p_top.getIntX() > p_bottomLeft.getIntX() && p_top.getIntX() < p_bottomRight.getIntX()){
                Horizontal_Bottom_topInMiddle(p_top, p_bottomLeft, p_bottomRight, drawable);
            }
            else if (p_top.getIntX() <= p_bottomLeft.getIntX()){
                Horizontal_Bottom_topInLeft(p_top, p_bottomLeft, p_bottomRight, drawable);
            }
            else if (p_top.getIntX() >= p_bottomRight.getIntX()){
                Horizontal_Bottom_topInRight(p_top, p_bottomLeft, p_bottomRight, drawable);
            }
        }
        // if having Non-horizontal bottom line
            //if left edge if shorter
        else if(left_chain.get(1).getIntY() > right_chain.get(1).getIntY()){
            if (p_top.getIntX() > p_bottomLeft.getIntX() && p_top.getIntX() < p_bottomRight.getIntX()){
                Non_Horizontal_LeftShort_topInMiddle(p_top, p_bottomLeft, p_bottomRight, drawable);
            }
            else if (p_top.getIntX() <= p_bottomLeft.getIntX()){
                Non_Horizontal_LeftShort_topInLeft(p_top, p_bottomLeft, p_bottomRight, drawable);
            }
            else if (p_top.getIntX() >= p_bottomRight.getIntX()){
                Non_Horizontal_LeftShort_topInRight(p_top, p_bottomLeft, p_bottomRight, drawable);
            }
        }
            //if right edge is shorter
        else if (left_chain.get(1).getIntY() < right_chain.get(1).getIntY()){
            if (p_top.getIntX() > p_bottomLeft.getIntX() && p_top.getIntX() < p_bottomRight.getIntX()){
                Non_Horizontal_RightShort_topInMiddle(p_top, p_bottomLeft, p_bottomRight, drawable);
            }
            else if (p_top.getIntX() <= p_bottomLeft.getIntX()){
                Non_Horizontal_RightShort_topInLeft(p_top, p_bottomLeft, p_bottomRight, drawable);
            }
            else if (p_top.getIntX() >= p_bottomRight.getIntX()){
                Non_Horizontal_RightShort_topInRight(p_top, p_bottomLeft, p_bottomRight, drawable);
            }
        }

        //if horizontal top line
        else if (left_chain.get(0).getIntY() == right_chain.get(1).getIntY()
                || left_chain.get(1).getIntY() == right_chain.get(0).getIntY()){

            if (p_top.getIntX() > p_bottomLeft.getIntX() && p_top.getIntX() < p_bottomRight.getIntX()){
                Horizontal_Top_topInMiddle(p_top, p_bottomLeft, p_bottomRight, drawable);
            }
            else if (p_top.getIntX() <= p_bottomLeft.getIntX()){
                Horizontal_Top_topInLeft(p_top, p_bottomLeft, p_bottomRight, drawable);
            }
            else if (p_top.getIntX() >= p_bottomRight.getIntX()){
                Horizontal_Top_topInRight(p_top, p_bottomLeft, p_bottomRight, drawable);
            }
        }


    }

    private void Horizontal_Top_topInMiddle(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        //DDA left line
        double deltaX1 = p_top.getIntX() - p_bottomLeft.getIntX();
        double deltaY1 = p_top.getIntY() - p_bottomLeft.getIntY();

        double deltaX2 = p_bottomRight.getIntX() - p_top.getIntX();
        double deltaY2 = p_top.getIntY() - p_bottomRight.getIntY();

        double L_slope = deltaX1 / deltaY1;
        double R_slope = deltaX2 / deltaY2;
        double L_x = p_top.getIntX();
        double R_x = p_top.getIntX();

        int argbColor = Color.random().asARGB();

        //rendering begin
        for (int y = p_top.getIntY(); y >= p_bottomLeft.getIntY() ; y--){
            fillPixels_leftToRight(L_x, R_x, y, argbColor, drawable);
            L_x = L_x - L_slope;
            R_x = R_x + R_slope;
        }
    }

    private void Horizontal_Top_topInLeft(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {

    }

    private void Horizontal_Top_topInRight(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {

    }


    //Horizontal bottom line implementation
    private void Horizontal_Bottom_topInRight(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        //DDA left line
        double deltaX1 = p_top.getIntX() - p_bottomLeft.getIntX();
        double deltaY1 = p_top.getIntY() - p_bottomLeft.getIntY();

        double deltaX2 = p_top.getIntX() - p_bottomRight.getIntX();
        double deltaY2 = p_top.getIntY() - p_bottomRight.getIntY();

        double L_slope = deltaX1 / deltaY1;
        double R_slope = deltaX2 / deltaY2;
        double L_x = p_top.getIntX();
        double R_x = p_top.getIntX();

        int argbColor = Color.random().asARGB();
        //rendering begin
        for (int y = p_top.getIntY(); y >= p_bottomLeft.getIntY() ; y--){
            fillPixels_leftToRight(L_x, R_x, y, argbColor, drawable);
            L_x = L_x - L_slope;
            R_x = R_x - R_slope;
        }
    }
    private void Horizontal_Bottom_topInLeft(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        //DDA left line
        double deltaX1 = p_bottomLeft.getIntX() - p_top.getIntX();
        double deltaY1 = p_top.getIntY() - p_bottomLeft.getIntY();

        double deltaX2 = p_bottomRight.getIntX() - p_top.getIntX();
        double deltaY2 = p_top.getIntY() - p_bottomRight.getIntY();



        double L_slope = deltaX1 / deltaY1;
        double R_slope = deltaX2 / deltaY2;
        double L_x = p_top.getIntX();
        double R_x = p_top.getIntX();

        int argbColor = Color.random().asARGB();

        //rendering begin
        for (int y = p_top.getIntY(); y >= p_bottomLeft.getIntY() ; y--){
            fillPixels_leftToRight(L_x, R_x, y, argbColor, drawable);
            L_x = L_x + L_slope;
            R_x = R_x + R_slope;
        }
    }
    private void Horizontal_Bottom_topInMiddle(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        //DDA left line
        double deltaX1 = p_top.getIntX() - p_bottomLeft.getIntX();
        double deltaY1 = p_top.getIntY() - p_bottomLeft.getIntY();

        double deltaX2 = p_bottomRight.getIntX() - p_top.getIntX();
        double deltaY2 = p_top.getIntY() - p_bottomRight.getIntY();

        double L_slope = deltaX1 / deltaY1;
        double R_slope = deltaX2 / deltaY2;
        double L_x = p_top.getIntX();
        double R_x = p_top.getIntX();

        int argbColor = Color.random().asARGB();

        //rendering begin
        for (int y = p_top.getIntY(); y >= p_bottomLeft.getIntY() ; y--){
            fillPixels_leftToRight(L_x, R_x, y, argbColor, drawable);
            L_x = L_x - L_slope;
            R_x = R_x + R_slope;
        }
    }

    //Non_horizontal left shorter implementation
    private void Non_Horizontal_LeftShort_topInMiddle(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        Vertex3D p_middle = p_bottomLeft;
        //first DDA left line
        double deltaX1_1 = p_top.getIntX() - p_bottomLeft.getIntX();
        double deltaY1_1 = p_top.getIntY() - p_bottomLeft.getIntY();

        double deltaX1_2 = p_bottomRight.getIntX() - p_bottomLeft.getIntX();
        double deltaY1_2 = p_bottomLeft.getIntY() - p_bottomRight.getIntY();

        double deltaX2 = p_bottomRight.getIntX() - p_top.getIntX();
        double deltaY2 = p_top.getIntY() - p_bottomRight.getIntY();

        double L1_slope = deltaX1_1 / deltaY1_1;
        double L2_slope = deltaX1_2 / deltaY1_2;
        double R_slope = deltaX2 / deltaY2;

        double x_start = p_top.getIntX();
        double x_end = p_top.getIntX();

        int argbColor = Color.random().asARGB();
        for (int y = p_top.getIntY(); y >= p_bottomRight.getIntY(); y--){
            if (y > p_middle.getIntY()){
                fillPixels_leftToRight(x_start, x_end, y, argbColor, drawable);
                x_start = x_start - L1_slope;
                x_end = x_end + R_slope;
            }
            else{
                fillPixels_leftToRight(x_start, x_end, y, argbColor, drawable);
                x_start = x_start + L2_slope;
                x_end = x_end + R_slope;
            }
        }

    }
    private void Non_Horizontal_LeftShort_topInLeft(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        Vertex3D p_middle = p_bottomLeft;
        //first DDA left line
        double deltaX1_1 = p_bottomLeft.getIntX() - p_top.getIntX();
        double deltaY1_1 = p_top.getIntY() - p_bottomLeft.getIntY();

        double deltaX1_2 = p_bottomRight.getIntX() - p_bottomLeft.getIntX();
        double deltaY1_2 = p_bottomLeft.getIntY() - p_bottomRight.getIntY();

        double deltaX2 = p_bottomRight.getIntX() - p_top.getIntX();
        double deltaY2 = p_top.getIntY() - p_bottomRight.getIntY();

        double L1_slope = deltaX1_1 / deltaY1_1;
        double L2_slope = deltaX1_2 / deltaY1_2;
        double R_slope = deltaX2 / deltaY2;

        double x_start = p_top.getIntX();
        double x_end = p_top.getIntX();

        int argbColor = Color.random().asARGB();

        for (int y = p_top.getIntY(); y >= p_bottomRight.getIntY(); y--){
            if (y > p_middle.getIntY()){
                fillPixels_leftToRight(x_start, x_end, y, argbColor, drawable);
                x_start = x_start + L1_slope;
                x_end = x_end + R_slope;
            }
            else{
                fillPixels_leftToRight(x_start, x_end, y, argbColor, drawable);
                x_start = x_start + L2_slope;
                x_end = x_end + R_slope;
            }
        }
    }
    private void Non_Horizontal_LeftShort_topInRight(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        Vertex3D p_middle = p_bottomLeft;
        //first DDA left line
        double deltaX1_1 = p_top.getIntX() - p_bottomLeft.getIntX();
        double deltaY1_1 = p_top.getIntY() - p_bottomLeft.getIntY();

        double deltaX1_2 = p_bottomRight.getIntX() - p_bottomLeft.getIntX();
        double deltaY1_2 = p_bottomLeft.getIntY() - p_bottomRight.getIntY();

        double deltaX2 = p_top.getIntX() - p_bottomRight.getIntX();
        double deltaY2 = p_top.getIntY() - p_bottomRight.getIntY();

        double L1_slope = deltaX1_1 / deltaY1_1;
        double L2_slope = deltaX1_2 / deltaY1_2;
        double R_slope = deltaX2 / deltaY2;

        double x_start = p_top.getIntX();
        double x_end = p_top.getIntX();

        int argbColor = Color.random().asARGB();

        for (int y = p_top.getIntY(); y >= p_bottomRight.getIntY(); y--){
            if (y > p_middle.getIntY()){
                fillPixels_leftToRight(x_start, x_end, y, argbColor, drawable);
                x_start = x_start - L1_slope;
                x_end = x_end - R_slope;
            }
            else{
                fillPixels_leftToRight(x_start, x_end, y, argbColor, drawable);
                x_start = x_start + L2_slope;
                x_end = x_end - R_slope;
            }
        }

    }

    //Non_horizontal right shorter implementation
    private void Non_Horizontal_RightShort_topInMiddle(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        Vertex3D p_middle = p_bottomRight;
        //first DDA left line
        double deltaX1_1 = p_top.getIntX() - p_bottomLeft.getIntX();
        double deltaY1_1 = p_top.getIntY() - p_bottomLeft.getIntY();

        double deltaX2 = p_bottomRight.getIntX() - p_top.getIntX();
        double deltaY2 = p_top.getIntY() - p_bottomRight.getIntY();

        double deltaX2_2 = p_bottomRight.getIntX() - p_bottomLeft.getIntX();
        double deltaY2_2 = p_bottomRight.getIntY() - p_bottomLeft.getIntY();

        double L_slope = deltaX1_1 / deltaY1_1;
        double R_slope = deltaX2 / deltaY2;
        double R2_slope = deltaX2_2 / deltaY2_2;

        double x_start = p_top.getIntX();
        double x_end = p_top.getIntX();

        int argbColor = Color.random().asARGB();

        for (int y = p_top.getIntY(); y >= p_bottomLeft.getIntY(); y--){
            if (y > p_middle.getIntY()){
                fillPixels_leftToRight(x_start, x_end, y, argbColor, drawable);
                x_start = x_start - L_slope;
                x_end = x_end + R_slope;
            }
            else{
                fillPixels_leftToRight(x_start, x_end, y, argbColor, drawable);
                x_start = x_start - L_slope;
                x_end = x_end - R2_slope;
            }
        }

    }
    private void Non_Horizontal_RightShort_topInLeft(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        Vertex3D p_middle = p_bottomRight;
        //first DDA left line
        double deltaX1_1 = p_bottomRight.getIntX() - p_top.getIntX();
        double deltaY1_1 = p_top.getIntY() - p_bottomLeft.getIntY();

        double deltaX2 = p_bottomRight.getIntX() - p_top.getIntX();
        double deltaY2 = p_top.getIntY() - p_bottomRight.getIntY();

        double deltaX2_2 = p_bottomRight.getIntX() - p_bottomLeft.getIntX();
        double deltaY2_2 = p_bottomRight.getIntY() - p_bottomLeft.getIntY();

        double L_slope = deltaX1_1 / deltaY1_1;
        double R_slope = deltaX2 / deltaY2;
        double R2_slope = deltaX2_2 / deltaY2_2;

        double x_start = p_top.getIntX();
        double x_end = p_top.getIntX();

        int argbColor = Color.random().asARGB();

        for (int y = p_top.getIntY(); y >= p_bottomLeft.getIntY(); y--){
            if (y > p_middle.getIntY()){
                fillPixels_leftToRight(x_start, x_end, y, argbColor, drawable);
                x_start = x_start + L_slope;
                x_end = x_end + R_slope;
            }
            else{
                fillPixels_leftToRight(x_start, x_end, y, argbColor, drawable);
                x_start = x_start + L_slope;
                x_end = x_end - R2_slope;
            }
        }

    }
    private void Non_Horizontal_RightShort_topInRight(Vertex3D p_top, Vertex3D p_bottomLeft, Vertex3D p_bottomRight, Drawable drawable) {
        Vertex3D p_middle = p_bottomRight;
        //first DDA left line
        double deltaX1_1 = p_top.getIntX() - p_bottomLeft.getIntX();
        double deltaY1_1 = p_top.getIntY() - p_bottomLeft.getIntY();

        double deltaX2 = p_top.getIntX() - p_bottomRight.getIntX();
        double deltaY2 = p_top.getIntY() - p_bottomRight.getIntY();

        double deltaX2_2 = p_bottomRight.getIntX() - p_bottomLeft.getIntX();
        double deltaY2_2 = p_bottomRight.getIntY() - p_bottomLeft.getIntY();

        double L_slope = deltaX1_1 / deltaY1_1;
        double R_slope = deltaX2 / deltaY2;
        double R2_slope = deltaX2_2 / deltaY2_2;

        double x_start = p_top.getIntX();
        double x_end = p_top.getIntX();

        int argbColor = Color.random().asARGB();

        for (int y = p_top.getIntY(); y >= p_bottomLeft.getIntY(); y--){
            if (y > p_middle.getIntY()){
                fillPixels_leftToRight(x_start, x_end, y, argbColor, drawable);
                x_start = x_start - L_slope;
                x_end = x_end - R_slope;
            }
            else{
                fillPixels_leftToRight(x_start, x_end, y, argbColor, drawable);
                x_start = x_start - L_slope;
                x_end = x_end - R2_slope;
            }
        }
    }

    //



    private void fillPixels_leftToRight(double x_start, double x_end, int y, int color, Drawable drawable) {
        if((int)Math.round(x_start) == (int)Math.round(x_end)){
            drawable.setPixel((int)Math.round(x_start), y, 0.0, color);
        }else{
            for (int i = (int)Math.round(x_start); i < (int)Math.round(x_end); i++)
                drawable.setPixel(i, y, 0.0, color);
        }
    }
    public static PolygonRenderer make() {
        return new FilledPolygonRenderer();
    }

}
