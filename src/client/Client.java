package client;

import client.testPages.*;
import geometry.Point2D;
import line.*;
import polygon.*;
import client.interpreter.SimpInterpreter;
import windowing.PageTurner;
import windowing.drawable.*;
import windowing.graphics.Color;
import windowing.graphics.Dimensions;
import wireframe.FilledWireFrameRenderer;

public class Client implements PageTurner {
	private static final int ARGB_WHITE = 0xff_ff_ff_ff;
	private static final int ARGB_GREEN = 0xff_00_ff_40;
	
	private static final int NUM_PAGES = 8;
	protected static final double GHOST_COVERAGE = 0.14;

	private static final int NUM_PANELS = 4;
	private static final Dimensions PANEL_SIZE = new Dimensions(300, 300);
	private static final Point2D[] lowCornersOfPanels = {
			new Point2D( 50, 400),
			new Point2D(400, 400),
			new Point2D( 50,  50),
			new Point2D(400,  50),
	};
	
	private final Drawable drawable;
	private int pageNumber = 0;
	
	private Drawable image;
	private Drawable[] panels;
	private Drawable[] ghostPanels;					// use transparency and write only white
	private Drawable largePanel;
	private Drawable fullPanel;
	
	private LineRenderer lineRenderer;
	private PolygonRenderer polygonRenderer;
    private PolygonRenderer wireframeRenderer;
    private RendererTrio renderers;
    private SimpInterpreter interpreter;


    public Client(Drawable drawable) {
		this.drawable = drawable;	
		createDrawables();
		createRenderers();
	}

	//createDrawables() creates drawables for the entire window (called image),
	// for the entire window minus a 50-pixel wide border (largePanel),
	// for the four subwindows that I told you to draw to (panels, inside createPanels()),
	// and an alternative set for those four panels for use on page 5 (ghostPanels).
	public void createDrawables() {
		image = new InvertedYDrawable(drawable);
		image = new TranslatingDrawable(image, point(0, 0), dimensions(750, 750));
        //image = new z_bufferingDrawable(image);
        image = new ColoredDrawable(image, ARGB_WHITE);

		
		//largePanel = new TranslatingDrawable(image, point(  50, 50),  dimensions(650, 650));
        fullPanel = new TranslatingDrawable(image, point(  50, 50),  dimensions(650, 650));
        fullPanel = new z_bufferingDrawable(fullPanel);

        //createPanels();
		//createGhostPanels();
	}


	private void createGhostPanels() {
		ghostPanels = new Drawable[NUM_PANELS];
		
		for(int index = 0; index < NUM_PANELS; index++) {
			Drawable drawable = panels[index];
			ghostPanels[index] = new GhostWritingDrawable(drawable, GHOST_COVERAGE);
		}
	}
	private Point2D point(int x, int y) {
		return new Point2D(x, y);
	}	
	private Dimensions dimensions(int x, int y) {
		return new Dimensions(x, y);
	}
	private void createRenderers() {
		lineRenderer = DDALineRenderer.make();
		polygonRenderer = blerpingFilledPolygoneRenderer.make();
		wireframeRenderer = FilledWireFrameRenderer.make();
		renderers = new RendererTrio(lineRenderer, polygonRenderer, wireframeRenderer);
    }


	@Override
	public void nextPage() {
		Drawable depthCueingDrawable;
		System.out.println("PageNumber " + (pageNumber + 1));
		pageNumber = (pageNumber + 1) % NUM_PAGES;

		image.clear();
		fullPanel.clear();

		switch(pageNumber) {
			case 1:  new MeshPolygonTest(fullPanel, wireframeRenderer, MeshPolygonTest.USE_PERTURBATION);
				break;
			case 2:  new MeshPolygonTest(fullPanel, polygonRenderer, MeshPolygonTest.USE_PERTURBATION);
				break;
			case 3:	 new centeredTriangleTest(fullPanel, polygonRenderer);
				break;

			case 4:  depthCueingDrawable = new DepthCueingDrawable(fullPanel, 0, -200, Color.GREEN);
				interpreter = new SimpInterpreter("tomsPage4.simp", depthCueingDrawable, renderers);
				interpreter.interpret();
				break;
//
//			case 5:  depthCueingDrawable = new DepthCueingDrawable(fullPanel, 0, -200, Color.RED);
//				interpreter = new SimpInterpreter("tomsPage5.simp", depthCueingDrawable, renderers);
//				interpreter.interpret();
//				break;

//			case 6:  depthCueingDrawable = new DepthCueingDrawable(fullPanel, 0, -200, Color.WHITE);
//				interpreter = new SimpInterpreter("page6.simp", depthCueingDrawable, renderers);
//				interpreter.interpret();
//				break;
//
//			case 7:  depthCueingDrawable = new DepthCueingDrawable(fullPanel, 0, -200, Color.WHITE);
//				interpreter = new SimpInterpreter("page7.simp", depthCueingDrawable, renderers);
//				interpreter.interpret();
//				break;
//
//			case 0:  depthCueingDrawable = new DepthCueingDrawable(fullPanel, 0, -200, Color.WHITE);
//				interpreter = new SimpInterpreter("page8.simp", depthCueingDrawable, renderers);
//				interpreter.interpret();
//				break;

			default: defaultPage();
				break;
		}
	}




	private void defaultPage() {
		image.clear();
		fullPanel.fill(ARGB_GREEN, Double.MAX_VALUE);
	}
}



