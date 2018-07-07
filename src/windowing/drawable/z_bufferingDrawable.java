package windowing.drawable;



public class z_bufferingDrawable extends DrawableDecorator{

    private int width;
    private int height;
    private double zBuffer[][];
    private double min = -200.0;


    public z_bufferingDrawable(Drawable delegate) {
        super(delegate);
        this.width = delegate.getWidth();
        this.height = delegate.getHeight();
        this.zBuffer = new double[this.width][this.height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.zBuffer[i][j] = min;
            }
        }
    }


    @Override
    public double getZValue(int x, int y){
        return this.zBuffer[x][y];
    }

    @Override
    public void setPixel(int x, int y, double z, int argbColor) {
        //delegate.setPixel(x, y, z, argbColor);
        if (z >= -200 && z <= 0 && x >= 0 && x < 650 && y >= 0 && y < 650) {
            if (zBuffer[x][y] < z){
                //System.out.println(zBuffer.get(x).get(y));
                delegate.setPixel(x, y, z, argbColor);
                this.zBuffer[x][y] = z;
            }
        }
        else{
            return;
        }

    }

    @Override
    public void clear() {
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                this.zBuffer[i][j] = min;
            }
        }
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                delegate.setPixel(i,j,-200,ARGB_BLACK);
            }
        }

        //fill(ARGB_BLACK,min);

    }
}





