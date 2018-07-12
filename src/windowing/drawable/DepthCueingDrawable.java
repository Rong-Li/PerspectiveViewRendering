package windowing.drawable;

import windowing.graphics.Color;

public class DepthCueingDrawable extends DrawableDecorator {

    private int near;
    private int far;
    private Color color;

    public DepthCueingDrawable(Drawable delegate, int near, int far, Color color) {
        super(delegate);
        this.near = near;
        this.far = far;
        this.color = color;
    }


    @Override
    public void setPixel(int x, int y, double z, int argbColor) {
        z = 1/z;

        if (z >= far && z <= near
                && x >= 0 && x < 650
                && y >= 0 && y < 650)
        {
            Color lightColor = Color.fromARGB(argbColor);
            //csz <= far
            if (z <= far){
                //System.out.println("********");
                delegate.setPixel(x, y, z, this.color.asARGB());
            }
            //csz >= near
            else if (z >= near){
                //System.out.println("#########!!!!!!!!!!");
                delegate.setPixel(x, y, z, argbColor);
            }
            //near >= csz >= far
            else{
                double fraction = 1 - (z / far);

                Color result = lightColor.blendInto(fraction, this.color);

                delegate.setPixel(x, y, z, result.asARGB());
            }
        } else {
            return;
        }

    }
}