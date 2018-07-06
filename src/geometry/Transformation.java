package geometry;

import java.text.DecimalFormat;

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

    //this is for getting inverse matrix without changing the old one
    public double[][] getMatrix_withoutAliasing() {
        double temp[][] = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                temp[i][j] = this.getMatrix()[i][j];
            }
        }
        return temp;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double get(int x, int y){
        int trueX = x - 1;
        int trueY = y - 1;
        double result = this.matrix[trueX][trueY];
        return result;
    }

    public void set(int x, int y, double value){
        int trueX = x - 1;
        int trueY = y - 1;
        this.matrix[trueX][trueY] = value;
    }

    //only copy the data but do not aliasing
    public void setMatrix(double matrix[][]){
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
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

    public Transformation InversedMatrix(){
        double temp[][] = this.getMatrix_withoutAliasing();
        double mat[][] = this.getMatrix_withoutAliasing();
        double det = 0;
        for(int i = 0; i < 3; i++)
            det = det + (mat[0][i] * (mat[1][(i+1)%3] * mat[2][(i+2)%3] - mat[1][(i+2)%3] * mat[2][(i+1)%3]));
        if (det == 0){
            System.out.println("wrong !!!!!determine is 0");
            return null;
        }
        else{
            System.out.println("The determine is:  " + det);
            for(int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j){
                    temp[i][j] = ((mat[(j + 1) % 3][(i + 1) % 3] * mat[(j + 2) % 3][(i + 2) % 3]) - (mat[(j + 1) % 3][(i + 2) % 3] * mat[(j + 2) % 3][(i + 1) % 3])) / det;
                }
            }
        }
        Transformation result = new Transformation();
        result.setMatrix(temp);
        return result;
    }


    public static Transformation identity(){
        Transformation result = new Transformation();

        result.set(1,1,1.0);
        result.set(2,2,1.0);
        result.set(3,3,1.0);
        result.set(4,4,1.0);
        //result.printMatrix();
        return result;
    }


    public void printMatrix(){
        double temp;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                temp = this.matrix[i][j];
                System.out.print(roundTwoDecimals(temp) + "," + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static Transformation scaleMatrix(double sx, double sy, double sz){
        Transformation result = Transformation.identity();

        result.set(1,1, sx);
        result.set(2,2, sy);
        result.set(3,3, sz);
        return result;
    }
    public static Transformation translateMatrix(double tx, double ty, double tz){
        Transformation result = Transformation.identity();

        result.set(1,4, tx);
        result.set(2,4, ty);
        result.set(3,4, tz);
        return result;
    }
    public static  Transformation rotateAroundZ(double degrees){
        Transformation result = Transformation.identity();
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
        Transformation result = Transformation.identity();
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
        Transformation result = Transformation.identity();
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        result.set(1,1, cos);
        result.set(1,3, sin);
        result.set(3,1, -sin);
        result.set(3,3, cos);
        return result;
    }

    public static Transformation vertexToVector(Vertex3D p){
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();
        Transformation vector = new Transformation(4,1);
        vector.set(1,1, x);
        vector.set(2,1, y);
        vector.set(3,1, z);
        vector.set(4,1,1);

        return vector;
    }


    public double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
}
