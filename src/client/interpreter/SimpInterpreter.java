package client.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import client.Clipper;
import geometry.*;
import geometry.Point3DH;
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
    public static Color ambientLight = Color.BLACK;

    private Drawable drawable;
    private Drawable depthCueingDrawable;
    private Drawable ZbufferDrawable;

    private LineRenderer lineRenderer;
    private PolygonRenderer filledRenderer;
    private PolygonRenderer wireframeRenderer;
    private Transformation cameraToScreen;
    private Clipper clipper;
    private Shader ambientShader;

    public enum RenderStyle {
        FILLED,
        WIREFRAME;
    }
    public SimpInterpreter(String filename, Drawable drawable, RendererTrio renderers) {
        this.drawable = drawable;
        this.depthCueingDrawable = drawable;
        this.ZbufferDrawable = drawable;
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
    		case "obj" :		interpretObj(tokens);		break;

            default :
                System.err.println("bad input line: " + tokens);
                break;
        }
    }

    private void interpretObj(String[] tokens) {
        String quotedFilename = tokens[1];
        int length = quotedFilename.length();
        assert quotedFilename.charAt(0) == '"' && quotedFilename.charAt(length-1) == '"';
        String filename = quotedFilename.substring(1, length-1);
        objFile(filename + ".obj");
    }

    private void objFile(String filename) {
        ObjReader objReader = new ObjReader(filename, defaultColor);
        objReader.read();
        objReader.render(this);
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
    private static double cleanNumber(String string) {
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
            //System.out.println(vertices[i]);
        }
        Polygon polygon = Polygon.makeEnsuringClockwise(vertices);

        RenderPolygon(polygon);
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

    public static Point3DH interpretPoint(String[] tokens, int startingIndex) {
        double x = cleanNumber(tokens[startingIndex]);
        double y = cleanNumber(tokens[startingIndex + 1]);
        double z = cleanNumber(tokens[startingIndex + 2]);

        Point3DH result = new Point3DH(x, y, z, 1.0);
        return result;
    }
    public static Point3DH interpretPointWithW(String[] tokens, int startingIndex) {
        double x = cleanNumber(tokens[startingIndex]);
        double y = cleanNumber(tokens[startingIndex + 1]);
        double z = cleanNumber(tokens[startingIndex + 2]);
        double w = cleanNumber(tokens[startingIndex + 3]);
        Transformation vector = new Transformation(4,1);
        vector.set(1,1,x);
        vector.set(2,1,y);
        vector.set(3,1,z);
        vector.set(4,1,w);
        vector = vector.homogeneousTransfer_4X1();

        Point3DH result = new Point3DH(vector.get(1,1), vector.get(2,1), vector.get(3,1), vector.get(4,1));
        return result;
    }
    public static Color interpretColor(String[] tokens, int startingIndex) {
        double r = cleanNumber(tokens[startingIndex]);
        double g = cleanNumber(tokens[startingIndex + 1]);
        double b = cleanNumber(tokens[startingIndex + 2]);

        Color result = new Color(r,g,b);
        //result = result.multiply(ambientLight);
        return result;
    }




    private void interpretCamera(String[] tokens) {

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
        this.clipper = new Clipper(hither, yon, xLow, xHigh, yLow, yHigh);
        projectedToScreen = Transformation.identity();
        double scaleSize_X = 650/(xHigh - xLow);
        double scaleSize_Y = 650/(yHigh - yLow);
        double translate_X = 650 - xHigh * 325;
        double translate_Y = 650 - yHigh * 325;
        //scalling
        projectedToScreen.set(1,1,scaleSize_X);
        projectedToScreen.set(2,2,scaleSize_Y);
        //translating
        projectedToScreen.set(1,4,translate_X);
        projectedToScreen.set(2,4,translate_Y);

        if (xHigh != yHigh){
            projectedToScreen.set(2,2,0.5*projectedToScreen.get(2,2));
            projectedToScreen.set(2,4,projectedToScreen.get(2,4)-160);
        }

        //simple matrix

        simplePerspectiveMatrix = Transformation.identity();
        simplePerspectiveMatrix.set(4,4,0);
        simplePerspectiveMatrix.set(4,3,-1);



        this.cameraToScreen = simplePerspectiveMatrix.matrixMultiplication(projectedToScreen);
    }



    private void interpretAmbient(String[] tokens) {
        double r = cleanNumber(tokens[1]);
        double g = cleanNumber(tokens[2]);
        double b = cleanNumber(tokens[3]);
        ambientLight = new Color(r,g,b);
        ambientShader = c -> ambientLight.multiply(c);
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


    public Vertex3D transformToPerspective(Vertex3D vertex){
        Transformation vector = Transformation.vertexToVector(vertex);

        double z_toKeep = vector.get(3,1);
        vector = vector.matrixMultiplication(simplePerspectiveMatrix);
        vector = vector.homogeneousTransfer_4X1();

        Vertex3D result = new Vertex3D(vector.get(1,1), vector.get(2,1), 1/z_toKeep, vertex.getColor());
        return result;

    }
    public Vertex3D transformToCamera(Vertex3D vertex) {
        Transformation vector = Transformation.vertexToVector(vertex);


        vector = vector.matrixMultiplication(projectedToScreen);

        Vertex3D result = new Vertex3D(vector.get(1,1), vector.get(2,1), vector.get(3,1), vertex.getColor());
        return result;
    }

    public void RenderPolygon(Polygon polygon){
        //clip
        List<Vertex3D> array_clippedZ= this.clipper.clipZ_toVertexArray(polygon);



        //**some try
        for (int i = 0; i < array_clippedZ.size(); i++){
            Vertex3D temp = transformToPerspective(array_clippedZ.get(i));
            array_clippedZ.set(i,temp);
        }
        if(array_clippedZ.size() == 0){
            return;
        }
        List<Vertex3D> array_clippedX = this.clipper.clipX_toVertexArray(array_clippedZ);

        if(array_clippedX.size() == 0){
            return;
        }
        List<Vertex3D> array_clippedY = this.clipper.clipY_toVertexArray(array_clippedX);
        if(array_clippedY.size() == 0){
            return;
        }

        for (int i = 0; i < array_clippedY.size(); i++){
            Vertex3D temp = transformToCamera(array_clippedY.get(i));
            array_clippedY.set(i,temp);
            //System.out.println("the points: "+ array_clippedY.get(i).getIntX() + " " + array_clippedY.get(i).getIntY() + " " +array_clippedY.get(i).getZ());
        }
        //System.out.println("");



        Vertex3D[] result = new Vertex3D[array_clippedY.size()];


        Polygon finalPolygon = Polygon.makeEnsuringClockwise(array_clippedY.toArray(result));
        if(this.renderStyle == RenderStyle.FILLED){
            List<Polygon> listOfPolygons = Clipper.Triangulation(finalPolygon);
            for (int i = 0; i < listOfPolygons.size(); i++){
                filledRenderer.drawPolygon(listOfPolygons.get(i), this.drawable, ambientShader);
            }
        }
        else if(this.renderStyle == RenderStyle.WIREFRAME){
            wireframeRenderer.drawPolygon(finalPolygon, this.ZbufferDrawable, ambientShader);
        }
    }


    public Transformation getCTM() {
        return CTM;
    }

    public Clipper getClipper() {
        return clipper;
    }

    public RenderStyle getRenderStyle() {
        return renderStyle;
    }

    public PolygonRenderer getFilledRenderer() {
        return filledRenderer;
    }

    public PolygonRenderer getWireframeRenderer() {
        return wireframeRenderer;
    }

    public Drawable getDrawable() {
        return drawable;
    }
}
