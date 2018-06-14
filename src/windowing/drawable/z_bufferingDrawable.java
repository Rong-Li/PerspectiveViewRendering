package windowing.drawable;

import java.util.ArrayList;
import java.util.List;

public class z_bufferingDrawable extends DrawableDecorator{
    private List<List<Double>>  matrix= new ArrayList<List<Double>>();


    public z_bufferingDrawable(Drawable delegate) {
        super(delegate);
        int width = delegate.getWidth();
        int height = delegate.getHeight();
        for (int i = 0; i < height; i++) {
            List<Double> c = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                c.add(-Double.MAX_VALUE);
            }
            matrix.add(c);
        }
    }

    public List<List<Double>> getMatrix() {
        return matrix;
    }

    public double getZ(int x, int y){
        int trueY = delegate.getHeight() - 1 - y;
        return this.matrix.get(x).get(trueY);
    }

    public void setZ(int x, int y, double newValue){
        int trueY = delegate.getHeight() - 1 - y;
        this.matrix.get(x).set(trueY, newValue);
    }
}





