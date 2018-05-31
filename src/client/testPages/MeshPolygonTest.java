package client.testPages;

import polygon.PolygonRenderer;
import windowing.drawable.Drawable;

public class MeshPolygonTest {

    private final PolygonRenderer renderer;
    private final Drawable panel;
    public static String NO_PERTURBATION = "NO_PERTURBATION";
    public static String USE_PERTURBATION = "USE_PERTURBATION";

    public MeshPolygonTest(Drawable panel, PolygonRenderer polygonRenderer, String Which) {
        this.panel = panel;
        this.renderer = polygonRenderer;

        if (Which.equals(NO_PERTURBATION) == true){
            render_NO_PERTURBATION();
        }
        else if(Which.equals(USE_PERTURBATION) == true){
            render_USE_PERTURBATION();
        }

    }


    private void render_NO_PERTURBATION() {



    }

    private void render_USE_PERTURBATION() {
    }

}
