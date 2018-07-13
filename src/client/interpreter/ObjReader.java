package client.interpreter;

import java.util.ArrayList;
import java.util.List;

import client.Clipper;
import geometry.Point3DH;
import geometry.Transformation;
import geometry.Vertex3D;
import polygon.Polygon;
import windowing.graphics.Color;

class ObjReader {
	private static final char COMMENT_CHAR = '#';
	private static final int NOT_SPECIFIED = -1;

	private class ObjVertex {
	    private int vertex_index;
        private int texture_index;
        private int normal_index;


        public ObjVertex(int vertex_index, int texture_index) {
            this.vertex_index = vertex_index;
            this.texture_index = texture_index;

        }
        public ObjVertex(int vertex_index, int texture_index, int normal_index) {
            this.vertex_index = vertex_index;
            this.texture_index = texture_index;
            this.normal_index = normal_index;
        }

        public int getIndex() {
            return vertex_index;
        }
        public int getTexture_index() {
            return texture_index;
        }

        public int getNormal_index() {
            return normal_index;
        }

    }
	private class ObjFace extends ArrayList<ObjVertex> {
		private static final long serialVersionUID = -4130668677651098160L;
	}
	private LineBasedReader reader;

	private List<Vertex3D> objVertices;
	private List<Vertex3D> transformedVertices;
	private List<Point3DH> objNormals;
	private List<ObjFace> objFaces;

	private Color defaultColor;

	ObjReader(String filename, Color defaultColor) {
	    this.defaultColor = defaultColor;
	    this.reader = new LineBasedReader(filename);
	    this.objVertices = new ArrayList<>();
	    this.transformedVertices = new ArrayList<>();
	    this.objNormals = new ArrayList<>();
	    this.objFaces = new ArrayList<>();
	}

	public void render(SimpInterpreter interpreter) {
        transformVertices(interpreter);

        for(ObjFace face: objFaces) {
            Polygon polygon = polygonForFace(face);
            interpreter.RenderPolygon(polygon);
        }
	}

    private void transformVertices(SimpInterpreter interpreter) {
	    for (int i = 0; i < objVertices.size(); i++){
            Transformation vector = Transformation.vertexToVector(objVertices.get(i));
            vector = vector.matrixMultiplication(interpreter.getCTM());
            Vertex3D newVertex = new Vertex3D(vector.get(1,1), vector.get(2,1), vector.get(3,1), objVertices.get(i).getColor());
            transformedVertices.add(newVertex);
        }
    }

    private Polygon polygonForFace(ObjFace face) {


        Polygon result = Polygon.makeEmpty();
        for(ObjVertex objVertex: face) {
            int vertexIndex = objVertex.getIndex();
            result.add(transformedVertices.get(vertexIndex));
        }
        return result;

	}

	public void read() {
		while(reader.hasNext() ) {
			String line = reader.next().trim();
			interpretObjLine(line);
		}
	}
	private void interpretObjLine(String line) {
		if(!line.isEmpty() && line.charAt(0) != COMMENT_CHAR) {
			String[] tokens = line.split("[ \t,()]+");
			if(tokens.length != 0) {
				interpretObjCommand(tokens);
			}
		}
	}

	private void interpretObjCommand(String[] tokens) {
		switch(tokens[0]) {
		case "v" :
		case "V" :
			interpretObjVertex(tokens);
			break;
		case "vn":
		case "VN":
			interpretObjNormal(tokens);
			break;
		case "f":
		case "F":
			interpretObjFace(tokens);
			break;
		default:	// do nothing
			break;
		}
	}
	private void interpretObjFace(String[] tokens) {
		ObjFace face = new ObjFace();

		for(int i = 1; i<tokens.length; i++) {
			String token = tokens[i];
			String[] subtokens = token.split("/");

			if(subtokens.length == 3){
                int vertexIndex  = objIndex(subtokens, 0, objVertices.size());
                int textureIndex = objIndex(subtokens, 1, 0);
                int normalIndex  = objIndex(subtokens, 2, objNormals.size());
                ObjVertex objVertex = new ObjVertex(vertexIndex,textureIndex,normalIndex);
                face.add(objVertex);
            }
            else if(subtokens.length == 2){
                int vertexIndex  = objIndex(subtokens, 0, objVertices.size());
                int textureIndex = objIndex(subtokens, 1, 0);
                ObjVertex objVertex = new ObjVertex(vertexIndex,textureIndex);
                face.add(objVertex);
            }


		}
		objFaces.add(face);
		// TODO: fill in action to take here.
	}

	private int objIndex(String[] subtokens, int tokenIndex, int baseForNegativeIndices) {
		// TODO: write this.  subtokens[tokenIndex], if it exists, holds a string for an index.
		// use Integer.parseInt() to get the integer value of the index.
		// Be sure to handle both positive and negative indices.
        int result;
        String input = subtokens[tokenIndex];
        result = Integer.parseInt(input)-1;


        if (result >= 0){
            return result;
        }
        else {
            return baseForNegativeIndices + result;
        }

	}

	private void interpretObjNormal(String[] tokens) {
		int numArgs = tokens.length - 1;
		if(numArgs != 3) {
			throw new BadObjFileException("vertex normal with wrong number of arguments : " + numArgs + ": " + tokens);
		}
		Point3DH normal = SimpInterpreter.interpretPoint(tokens, 1);
        objNormals.add(normal);
	}
	private void interpretObjVertex(String[] tokens) {
		int numArgs = tokens.length - 1;
		Point3DH point = objVertexPoint(tokens, numArgs);
		Color color = objVertexColor(tokens, numArgs);

        Vertex3D vertex = new Vertex3D(point, color);
		this.objVertices.add(vertex);
	}

	private Color objVertexColor(String[] tokens, int numArgs) {
		if(numArgs == 6) {
			return SimpInterpreter.interpretColor(tokens, 4);
		}
		if(numArgs == 7) {
			return SimpInterpreter.interpretColor(tokens, 5);
		}
		return defaultColor;
	}

	private Point3DH objVertexPoint(String[] tokens, int numArgs) {
		if(numArgs == 3 || numArgs == 6) {
			return SimpInterpreter.interpretPoint(tokens, 1);
		}
		else if(numArgs == 4 || numArgs == 7) {
			return SimpInterpreter.interpretPointWithW(tokens, 1);
		}
		throw new BadObjFileException("vertex with wrong number of arguments : " + numArgs + ": " + tokens);
	}
}