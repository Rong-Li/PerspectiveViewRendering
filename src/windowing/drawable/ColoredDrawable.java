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
                if ((x > 50 && x < 350 && y > 50 && y < 350)
                        || (x > 50 && x < 350 && y > 400 && y < 700)
                        || (x > 400 && x < 700 && y > 400 && y < 700)
                        || (x > 400 && x < 700 && y > 50 && y < 350)){

                }else{
                    delegate.setPixel( x, y,0.0, color);
                }

            }
        }
    }



}
