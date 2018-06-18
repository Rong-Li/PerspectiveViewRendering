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
    public Transformation(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
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

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
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


    public Transformation matrixMultiplication(Transformation newMatrix){
        Transformation result = new Transformation(newMatrix.getRows(), this.cols);

        if (newMatrix.getCols() != this.rows) {
            throw new IllegalArgumentException("A:Rows: " + newMatrix.getCols() + " did not match B:Columns " + this.rows + ".");
        }
        for (int i = 0; i < newMatrix.getRows(); i++) { // aRow
            for (int j = 0; j < this.cols; j++) { // bColumn
                for (int k = 0; k < newMatrix.getCols(); k++) { // aColumn
                    result.matrix[i][j] += newMatrix.getMatrix()[i][k] * this.matrix[k][j];
                }
            }
        }
        return result;
    }

    public static Transformation identity(){
        Transformation result = new Transformation();

        result.set(1,1,1.0);
        result.set(2,2,1.0);
        result.set(3,3,1.0);
        result.set(4,4,1.0);
        return result;
    }

    public static Transformation scaleMatrix(double sx, double sy, double sz){
        Transformation result = new Transformation();

        result.set(1,1, sx);
        result.set(2,2, sy);
        result.set(3,3, sz);
        return result;
    }
    public static Transformation translateMatrix(double tx, double ty, double tz){
        Transformation result = new Transformation();

        result.set(1,4, tx);
        result.set(2,4, ty);
        result.set(3,4, tz);
        return result;
    }
    public static  Transformation rotateAroundZ(double degrees){
        Transformation result = new Transformation();
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        result.set(1,1, cos);
        result.set(1,2, -sin);
        result.set(2,1, sin);
        result.set(2,2, cos);
        return result;
    }
    public static  Transformation rotateAroundX(double degrees){
        Transformation result = new Transformation();
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        result.set(2,2, cos);
        result.set(2,3, -sin);
        result.set(3,2, sin);
        result.set(3,3, cos);
        return result;
    }
    public static  Transformation rotateAroundY(double degrees){
        Transformation result = new Transformation();
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        result.set(1,1, cos);
        result.set(1,3, sin);
        result.set(3,1, -sin);
        result.set(3,3, cos);
        return result;
    }
}
