package client;

import line.LineRenderer;
import polygon.PolygonRenderer;

public class RendererTrio {
    private LineRenderer lineRenderer;
    private PolygonRenderer polygonRenderer;
    private PolygonRenderer wireframeRenderer;

    RendererTrio(LineRenderer lineRenderer, PolygonRenderer polygonRenderer, PolygonRenderer wireframeRenderer){
        this.lineRenderer = lineRenderer;
        this.polygonRenderer = polygonRenderer;
        this.wireframeRenderer = wireframeRenderer;
    }

    public LineRenderer getLineRenderer(){
        return lineRenderer;
    }

    public PolygonRenderer getFilledRenderer(){
        return polygonRenderer;
    }

    public  PolygonRenderer getWireframeRenderer(){
        return wireframeRenderer;
    }


}
