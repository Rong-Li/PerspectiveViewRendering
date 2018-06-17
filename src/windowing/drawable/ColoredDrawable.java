package windowing.drawable;

import geometry.Point2D;
import windowing.graphics.Dimensions;

public class ColoredDrawable extends DrawableDecorator {

    private final int color;

    public ColoredDrawable(Drawable delegate, int argbWhite) {
        super(delegate);
        this.color = argbWhite;
    }

    @Override
    public void clear() {
        for (int y = 0; y < 750; y++){
            for (int x = 0; x < 750; x++){
                if (x >= 49 && x < 699 && y >= 49 && y < 699){
                    //delegate.setPixel( x, y,-Double.MAX_VALUE, color);
                }
                else{
                    delegate.setPixel( x, y,0.0, color);
                }
            }
        }
    }



}
