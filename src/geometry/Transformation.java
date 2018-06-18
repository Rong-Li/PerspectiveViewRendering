package geometry;

public class Transformation {
    private int rows;
    private int cols;
    private double matrix[][];

    public Transformation() {
        this.rows = 4;
        this.cols = 4;
        this.matrix = new double[rows][cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.matrix[i][j] = 0.0;
            }
        }
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public double get(int x, int y){
        int trueX = this.rows - x;
        int trueY = this.cols - y;
        double result = this.matrix[trueX][trueY];
        return result;
    }

    public void set(int x, int y, double value){
        int trueX = this.rows - x;
        int trueY = this.cols - y;
        this.matrix[trueX][trueY] = value;
    }


    public static Transformation identity(){
        Transformation result = new Transformation();
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                result.matrix[i][j] = 0.0;
            }
        }
        result.matrix[3][0] = 1;
        result.matrix[2][1] = 1;
        result.matrix[1][2] = 1;
        result.matrix[0][3] = 1;
        return result;
    }
}
