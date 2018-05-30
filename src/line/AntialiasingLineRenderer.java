package line;

import geometry.Vertex;
import geometry.Vertex3D;
import windowing.drawable.Drawable;
import windowing.graphics.Color;

public class AntialiasingLineRenderer implements LineRenderer {
    // use the static factory make() instead of constructor.
    private AntialiasingLineRenderer() {}


    @Override
    public void drawLine(Vertex3D p1, Vertex3D p2, Drawable drawable) {

        //ax + by + c = 0
        double a = p1.getIntY() - p2.getIntY();
        double b = p2.getIntX() - p1.getIntX();
        double c = (p1.getIntX() * p2.getIntY()) - (p2.getIntX() * p1.getIntY());

        //DDA
        double deltaX = p2.getIntX() - p1.getIntX();
        double deltaY = p2.getIntY() - p1.getIntY();

        double slope = deltaY / deltaX;
        double y = p1.getIntY();
        int argbColor = p1.getColor().asARGB();

        for(int x = p1.getIntX(); x <= p2.getIntX(); x++) {

            for (int m = -1; m < 2; m++){
                int point_y = (int) Math.round(y) + m;
                double nominator = (a * (double) x) + (b * (double) point_y) + c;
                double denominator = Math.sqrt(a * a + b * b);
                double point_to_line = Math.abs(nominator) / denominator;

                if (point_to_line > 0.5) {
                    double d = point_to_line - 0.5;
                    //System.out.println("the result is: " + d);

                    if (d >= 0.5) {}
                    else {
                        //get triangle area
                        double triangle_area = d * (Math.sqrt(0.5 * 0.5 - d * d));
                        //get pie widget area
                        double theta = Math.acos(d / 0.5);
                        double pie_area = (1 - theta / Math.PI) * Math.PI * 0.5 * 0.5;
                        //Total area covered
                        double Total_area = triangle_area + pie_area;
                        double Coverage = 1 - (Total_area / (Math.PI * 0.5 * 0.5));
                        //System.out.println("the result is: " + Coverage);


                        //set pixels
                        drawable.setPixelWithCoverage(x, point_y, 0.0, argbColor, Coverage);
                    }

                } else {
                    double d = 0.5 - point_to_line;
                    //System.out.println("the result is: " + d);

                    //get triangle area
                    double triangle_area = d * (Math.sqrt(0.5 * 0.5 - d * d));
                    //System.out.println("the result is: " + triangle_area);

                    //get pie widget area
                    double theta = Math.acos(d / 0.5);
                    double pie_area = (1 - (theta / Math.PI)) * Math.PI * 0.5 * 0.5;
                    //Total area covered
                    double Total_area = triangle_area + pie_area;
                    double Coverage = Total_area / (Math.PI * 0.5 * 0.5);
                    //System.out.println("the result is: " + Coverage);

                    //set pixels
                    drawable.setPixelWithCoverage(x, point_y, 0.0, argbColor, Coverage);
                }
            }
            y = y + slope;
            //System.out.println("the result is: " + point_to_line);


        }
    }

    public static LineRenderer make() {
        return new AnyOctantLineRenderer(new AntialiasingLineRenderer());
    }
}
