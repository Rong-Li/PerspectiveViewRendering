package windowing.drawable;

import windowing.graphics.Color;

public class DepthCueingDrawable extends DrawableDecorator {

    private int front;
    private int back;
    private Color color;

    public DepthCueingDrawable(Drawable delegate, int i, int i1, Color color) {
        super(delegate);
        this.front = i;
        this.back = i1;
        this.color = color;
    }


    @Override
    public void setPixel(int x, int y, double z, int argbColor) {
        double fraction = 1 - (z/back);
        double r = this.color.getR() * fraction;
        double g = this.color.getG() * fraction;
        double b = this.color.getB() * fraction;
        Color newColor = new Color(r,g,b);
        delegate.setPixel(x, y, z, newColor.asARGB());
    }
}
