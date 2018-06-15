package windowing.drawable;

import java.util.ArrayList;
import java.util.List;

public class z_bufferingDrawable extends DrawableDecorator{

    private List<List<Double>>  zBuffer= new ArrayList<List<Double>>();


    public z_bufferingDrawable(Drawable delegate) {
        super(delegate);
        int width = delegate.getWidth();
        int height = delegate.getHeight();
        for (int i = 0; i < height; i++) {
            List<Double> c = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                c.add(-Double.MAX_VALUE);
            }
            zBuffer.add(c);
        }
    }

    public List<List<Double>> getMatrix() {
        return zBuffer;
    }

    @Override
    public double getZValue(int x, int y){
        int trueY = delegate.getHeight() - 1 - y;
        return this.zBuffer.get(x).get(trueY);
    }

    @Override
    public void setPixel(int x, int y, double z, int argbColor) {
        delegate.setPixel(x, y, z, argbColor);

//        if (zBuffer.get(x).get(y) < z){
//            delegate.setPixel(x, y, z, argbColor);
//            int trueY = delegate.getHeight() - 1 - y;
//            this.zBuffer.get(x).set(trueY, z);
//        }
    }


}





