package client.interpreter;

import java.util.Stack;

import client.Clipper;
import geometry.*;
import line.LineRenderer;
import client.RendererTrio;
import geometry.Transformation;
import polygon.Polygon;
import polygon.PolygonRenderer;
import polygon.Shader;
import windowing.drawable.DepthCueingDrawable;
import windowing.drawable.Drawable;
import windowing.graphics.Color;
import windowing.graphics.Dimensions;

public class SimpInterpreter {
    private static final int NUM_TOKENS_FOR_POINT = 3;
    private static final int NUM_TOKENS_FOR_COMMAND = 1;
    private static final int NUM_TOKENS_FOR_COLORED_VERTEX = 6;
    private static final int NUM_TOKENS_FOR_UNCOLORED_VERTEX = 3;
    private static final char COMMENT_CHAR = '#';
    private RenderStyle renderStyle;

    private Transformation CTM;
    private Transformation worldToScreen;
    private Transformation projectedToScreen;
    private Transformation simplePerspectiveMatrix;
    private Stack<Transformation> matrixStack;

    private static int WORLD_LOW_X = -100;
    private static int WORLD_HIGH_X = 100;
    private static int WORLD_LOW_Y = -100;
    private static int WORLD_HIGH_Y = 100;

    private LineBasedReader reader;
    private Stack<LineBasedReader> readerStack;

    private Color defaultColor = Color.WHITE;
    private Color ambientLight = Color.BLACK;

    private Drawable drawable;
    private Drawable depthCueingDrawable;

    private LineRenderer lineRenderer;
    private PolygonRenderer filledRenderer;
    private PolygonRenderer wireframeRenderer;
    private Transformation cameraToScreen;
    private Clipper clipper;

    public enum RenderStyle {
        FILLED,
        WIREFRAME;
    }
    public SimpInterpreter(String filename, Drawable drawable, RendererTrio renderers) {
        this.drawable = drawable;
        this.depthCueingDrawable = drawable;
        this.lineRenderer = renderers.getLineRenderer();
        this.filledRenderer = renderers.getFilledRenderer();
        this.wireframeRenderer = renderers.getWireframeRenderer();
        this.defaultColor = Color.WHITE;
        CTM = Transformation.identity();
        makeWorldToScreenTransform(drawable.getDimensions());

        reader = new LineBasedReader(filename);
        readerStack = new Stack<>();
        renderStyle = RenderStyle.FILLED;
        this.matrixStack = new Stack<>();
    }


    private void makeWorldToScreenTransform(Dimensions dimensions) {
        this.worldToScreen = Transformation.identity();
        //scalling
        this.worldToScreen.set(1,1,3.25);
        this.worldToScreen.set(2,2,3.25);
//        this.worldToScreen.set(3,3,3.25);
        //translating
        this.worldToScreen.set(1,4,324);
        this.worldToScreen.set(2,4,324);
        //this.worldToScreen.printMatrix();
        //worldToScreen.printMatrix();
    }

    public void interpret() {

        while(reader.hasNext() ) {
            String line = reader.next().trim();
            interpretLine(line);
            while(!reader.hasNext()) {
                if(readerStack.isEmpty()) {
                    return;
                }
                else {
                    reader = readerStack.pop();
                }
            }
        }
    }
    public void interpretLine(String line) {
        if(!line.isEmpty() && line.charAt(0) != COMMENT_CHAR) {
            String[] tokens = line.split("[ \t,()]+");
            if(tokens.length != 0) {
                interpretCommand(tokens);
            }
        }
    }
    private void interpretCommand(String[] tokens) {
        switch(tokens[0]) {
            case "{" :      push();   break;
            case "}" :      pop();    break;
            case "wire" :   wire();   break;
            case "filled" : filled(); break;

            case "file" :		interpretFile(tokens);		break;
            case "scale" :		interpretScale(tokens);		break;
            case "translate" :	interpretTranslate(tokens);	break;
            case "rotate" :		interpretRotate(tokens);	break;
            case "line" :		interpretLine(tokens);		break;
            case "polygon" :	interpretPolygon(tokens);	break;
		    case "camera" :		interpretCamera(tokens);	break;
    		case "surface" :	interpretSurface(tokens);	break;
            case "ambient" :	interpretAmbient(tokens);	break;
    		case "depth" :		interpretDepth(tokens);		break;
//		case "obj" :		interpretObj(tokens);		break;

            default :
                System.err.println("bad input line: " + tokens);
                break;
        }
    }




    private void push() {
        this.matrixStack.push(CTM);
    }
    private void pop() {
        this.CTM = this.matrixStack.pop();
    }
    private void wire() {
        this.renderStyle = RenderStyle.WIREFRAME;
    }
    private void filled() {
        this.renderStyle = RenderStyle.FILLED;
    }

    // this one is complete.
    private void interpretFile(String[] tokens) {
        String quotedFilename = tokens[1];
        int length = quotedFilename.length();
        assert quotedFilename.charAt(0) == '"' && quotedFilename.charAt(length-1) == '"';
        String filename = quotedFilename.substring(1, length-1);
        file(filename + ".simp");
    }
    private void file(String filename) {
        readerStack.push(reader);
        reader = new LineBasedReader(filename);
    }

    //
    private void interpretScale(String[] tokens) {
        double sx = cleanNumber(tokens[1]);
        double sy = cleanNumber(tokens[2]);
        double sz = cleanNumber(tokens[3]);

        Transformation scale = Transformation.scaleMatrix(sx,sy,sz);
        CTM = scale.matrixMultiplication(CTM);
        //System.out.println("it is a Scaling Matrix: ");
        //CTM.printMatrix();
    }
    private void interpretTranslate(String[] tokens) {
        double tx = cleanNumber(tokens[1]);
        double ty = cleanNumber(tokens[2]);
        double tz = cleanNumber(tokens[3]);

        Transformation translate = Transformation.translateMatrix(tx,ty,tz);
        CTM = translate.matrixMultiplication(CTM);
        //System.out.println("it is a Translating Matrix: ");
        //CTM.printMatrix();
    }
    private void interpretRotate(String[] tokens) {
        String axisString = tokens[1];
        double angleInDegrees = cleanNumber(tokens[2]);
        Transformation rotate = Transformation.identity();
        if(axisString.equals("Z")){
            //System.out.println("it is a Z");
            rotate = Transformation.rotateAroundZ(angleInDegrees);
        } else if(axisString.equals("Y")){
            //System.out.println("it is a Y");
            rotate = Transformation.rotateAroundY(angleInDegrees);
        } else if(axisString.equals("X")){
            //System.out.println("it is a X");
            rotate = Transformation.rotateAroundX(angleInDegrees);
        }
        //System.out.println("Rotate by " + axisString);
        CTM = rotate.matrixMultiplication(CTM);
        //CTM.printMatrix();
    }
    private double cleanNumber(String string) {
        return Double.parseDouble(string);
    }

    private enum VertexColors {
        COLORED(NUM_TOKENS_FOR_COLORED_VERTEX),
        UNCOLORED(NUM_TOKENS_FOR_UNCOLORED_VERTEX);

        private int numTokensPerVertex;

        private VertexColors(int numTokensPerVertex) {
            this.numTokensPerVertex = numTokensPerVertex;
        }
        public int numTokensPerVertex() {
            return numTokensPerVertex;
        }
    }
    private void interpretLine(String[] tokens) {
        Vertex3D[] vertices = interpretVertices(tokens, 2, 1);
        lineRenderer.drawLine(vertices[0], vertices[1], this.drawable);

    }
    private void interpretPolygon(String[] tokens) {
        Vertex3D[] vertices = interpretVertices(tokens, 3, 1);
        for (int i = 0; i < 3; i++){
            Transformation vector = Transformation.vertexToVector(vertices[i]);
            vector = vector.matrixMultiplication(this.CTM);
            vertices[i] = new Vertex3D(vector.get(1,1), vector.get(2,1), vector.get(3,1), vertices[i].getColor());
        }
        Polygon polygon = Polygon.makeEnsuringClockwise(vertices);
        //clip
        Vertex3D[] array = this.clipper.clipZ_toVertexArray(polygon);
        int index = 0;
        while(array[index] != null){
            array[index] = transformToCamera(array[index]);
        }
        Polygon finalPolygon = Polygon.makeEnsuringClockwise(array);
        if(this.renderStyle == RenderStyle.FILLED){
            filledRenderer.drawPolygon(polygon, this.drawable, null);
        }
        else if(this.renderStyle == RenderStyle.WIREFRAME){
            wireframeRenderer.drawPolygon(finalPolygon, this.drawable, null);
        }
    }


    public Vertex3D[] interpretVertices(String[] tokens, int numVertices, int startingIndex) {
        VertexColors vertexColors = verticesAreColored(tokens, numVertices);
        Vertex3D vertices[] = new Vertex3D[numVertices];

        for(int index = 0; index < numVertices; index++) {
            vertices[index] = interpretVertex(tokens, startingIndex + index * vertexColors.numTokensPerVertex(), vertexColors);
        }

        return vertices;
    }
    public VertexColors verticesAreColored(String[] tokens, int numVertices) {
        return hasColoredVertices(tokens, numVertices) ? VertexColors.COLORED :
                VertexColors.UNCOLORED;
    }
    public boolean hasColoredVertices(String[] tokens, int numVertices) {
        return tokens.length == numTokensForCommandWithNVertices(numVertices);
    }
    public int numTokensForCommandWithNVertices(int numVertices) {
        return NUM_TOKENS_FOR_COMMAND + numVertices*(NUM_TOKENS_FOR_COLORED_VERTEX);
    }
    private Vertex3D interpretVertex(String[] tokens, int startingIndex, VertexColors colored) {
        Point3DH point = interpretPoint(tokens, startingIndex);

        Color color = defaultColor;
        if(colored == VertexColors.COLORED) {
            color = interpretColor(tokens, startingIndex + NUM_TOKENS_FOR_POINT);
        }

        Vertex3D result = new Vertex3D(point,color);
        //System.out.println("then****!!!!!!" + result.toString());
        return result;
    }

    public Point3DH interpretPoint(String[] tokens, int startingIndex) {
    double x = cleanNumber(tokens[startingIndex]);
    double y = cleanNumber(tokens[startingIndex + 1]);
    double z = cleanNumber(tokens[startingIndex + 2]);

    Point3DH result = new Point3DH(x, y, z, 1.0);
    return result;
}
    public Color interpretColor(String[] tokens, int startingIndex) {
        double r = cleanNumber(tokens[startingIndex]);
        double g = cleanNumber(tokens[startingIndex + 1]);
        double b = cleanNumber(tokens[startingIndex + 2]);

        Color result = new Color(r,g,b);
        return result;
    }




//    private void interpretCamera(String[] tokens) {
//        Transformation temp = Transformation.identity();
//        temp.set(1,1,2);
//        temp.set(1,2,0);
//        temp.set(1,3,0);
//        temp.set(1,4,1);
//        temp.set(2,1,0);
//        temp.set(2,2,4);
//        temp.set(2,3,3);
//        temp.set(2,4,2);
//        temp.set(3,1,0);
//        temp.set(3,2,1);
//        temp.set(3,3,1);
//        temp.set(3,4,3);
//
//        temp.printMatrix();
//
//        System.out.println("AFTER inverse!!!");
//
//        Transformation result = temp.InversedMatrix();
//        result.printMatrix();
//
//    }

    private void interpretCamera(String[] tokens) {
        System.out.println("CTM!!!!!!!!!");
        CTM.printMatrix();

        worldToScreen = CTM.InversedMatrix();
        CTM = CTM.matrixMultiplication(worldToScreen);

        //multiply everything in matrix stack with worldToscreen
        Stack<Transformation> temp = new Stack<>();
        while(!matrixStack.empty()){
            Transformation t = matrixStack.pop();
            t = t.matrixMultiplication(worldToScreen);
            temp.push(t);
        }
        while(!temp.empty()){
            Transformation tt = temp.pop();
            matrixStack.push(tt);
        }


        //Projection part
        double xLow = cleanNumber(tokens[1]);
        double yLow = cleanNumber(tokens[2]);
        double xHigh = cleanNumber(tokens[3]);
        double yHigh = cleanNumber(tokens[4]);
        //set clipper
        double hither = cleanNumber(tokens[5]);
        double yon = cleanNumber(tokens[6]);
        this.clipper = new Clipper(hither,yon);
        projectedToScreen = Transformation.identity();
        double scaleSize_X = 650/(xHigh - xLow);
        double scaleSize_Y = 650/(yHigh - yLow);
        //scalling
        projectedToScreen.set(1,1,scaleSize_X);
        projectedToScreen.set(2,2,scaleSize_Y);
        //translating
        projectedToScreen.set(1,4,325);
        projectedToScreen.set(2,4,325);

        System.out.println("projectionto screen");
        projectedToScreen.printMatrix();

        //simple matrix

        simplePerspectiveMatrix = Transformation.identity();
        simplePerspectiveMatrix.set(4,4,0);
        simplePerspectiveMatrix.set(4,3,-1);

        System.out.println("simple matrix screen");
        simplePerspectiveMatrix.printMatrix();

        this.cameraToScreen = simplePerspectiveMatrix.matrixMultiplication(projectedToScreen);

        System.out.println("Camera to screen");
        cameraToScreen.printMatrix();
    }



    private void interpretAmbient(String[] tokens) {
        double r = cleanNumber(tokens[1]);
        double g = cleanNumber(tokens[2]);
        double b = cleanNumber(tokens[3]);
        ambientLight = new Color(r,g,b);
        Shader ambientShader = c -> ambientLight.multiply(c);
    }


    private void interpretSurface(String[] tokens) {
        double r = cleanNumber(tokens[1]);
        double g = cleanNumber(tokens[2]);
        double b = cleanNumber(tokens[3]);
        Color color = new Color(r,g,b);
        this.defaultColor = color;
    }
    private void interpretDepth(String[] tokens) {
        double near = cleanNumber(tokens[1]);
        double far = cleanNumber(tokens[2]);
        double r = cleanNumber(tokens[3]);
        double g = cleanNumber(tokens[4]);
        double b = cleanNumber(tokens[5]);
        Color color = new Color(r,g,b);
        depthCueingDrawable = new DepthCueingDrawable(this.drawable, (int)Math.round(near), (int)Math.round(far), color);
        this.drawable = depthCueingDrawable;
    }
//    private void line(Vertex3D p1, Vertex3D p2) {
//        Vertex3D screenP1 = transformToCamera(p1);
//        Vertex3D screenP2 = transformToCamera(p2);
//        // TODO: finish this method
//    }
//    private void polygon(Vertex3D p1, Vertex3D p2, Vertex3D p3) {
//        Vertex3D screenP1 = transformToCamera(p1);
//        Vertex3D screenP2 = transformToCamera(p2);
//        Vertex3D screenP3 = transformToCamera(p3);
//        // TODO: finish this method
//    }
//
    private Vertex3D transformToCamera(Vertex3D vertex) {
        Transformation vector = Transformation.vertexToVector(vertex);

        //vector = vector.matrixMultiplication(this.CTM);

        double z_toKeep = vector.get(3,1);

        vector = vector.matrixMultiplication(cameraToScreen);
        vector = vector.homogeneousTransfer_4X1();
        vector.set(3,1,z_toKeep);
//        System.out.println("After!!!!!!");
//        vector.printMatrix();

        Vertex3D result = new Vertex3D(vector.get(1,1), vector.get(2,1), vector.get(3,1), vertex.getColor());
        return result;
    }







}
