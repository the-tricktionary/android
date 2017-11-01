package trictionary.jumproper.com.jumpropetrictionary.activities;

import android.os.Bundle;

import net.xqhs.graphs.graph.SimpleNode;

import java.util.ArrayList;

import trictionary.jumproper.com.jumpropetrictionary.R;
import trictionary.jumproper.com.jumpropetrictionary.networkgraph.beans.NetworkGraph;
import trictionary.jumproper.com.jumpropetrictionary.networkgraph.beans.Vertex;
import trictionary.jumproper.com.jumpropetrictionary.utils.Trick;

public class TrickNetwork extends BaseActivity {

    private ArrayList<ArrayList<Trick>> tricktionary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_network);
        NetworkGraph graph = new NetworkGraph();
        tricktionary = ((GlobalData) this.getApplication()).getTricktionary();

        for(ArrayList<Trick> level : tricktionary){
            for(Trick mTrick : level){
                graph.getVertex().add(new Vertex(new SimpleNode(mTrick.getName()),getResources().getDrawable(R.drawable.icon_alpha_small)));
            }
        }
        /**
        for(Vertex v : graph.getVertex()){
            graph.addEdge(new SimpleEdge(graph.getVertex().get((int)Math.random()*100).getNode(),v.getNode(),v.getNode().getLabel()));
        }

        Node v1 = new SimpleNode("18");
        Node v2 = new SimpleNode("24");
        graph.getVertex().add(new Vertex(v1, getResources().getDrawable(R.drawable.icon_alpha_small)));
        graph.getVertex().add(new Vertex(v2, getResources().getDrawable(R.drawable.icon_alpha_small)));
        graph.addEdge(new SimpleEdge(v1, v2, "12"));

        Node v3 = new SimpleNode("7");
        graph.getVertex().add(new Vertex(v3, getResources().getDrawable(R.drawable.icon_alpha_small)));
        graph.addEdge(new SimpleEdge(v2, v3, "23"));

        v1 = new SimpleNode("14");
        graph.getVertex().add(new Vertex(v1, getResources().getDrawable(R.drawable.icon_alpha_small)));
        graph.addEdge(new SimpleEdge(v3, v1, "34"));

        v1 = new SimpleNode("10");
        graph.getVertex().add(new Vertex(v1, getResources().getDrawable(R.drawable.icon_alpha_small)));
        graph.addEdge(new SimpleEdge(v3, v1, "35"));

        v1 = new SimpleNode("11");
        graph.getVertex().add(new Vertex(v1, getResources().getDrawable(R.drawable.icon_alpha_small)));
        graph.addEdge(new SimpleEdge(v1, v3, "36"));
        graph.addEdge(new SimpleEdge(v3, v1, "6"));
         **/
/**
        GraphSurfaceView surface = (GraphSurfaceView) findViewById(R.id.mysurface);
        surface.init(graph);
        surface.canScrollHorizontally(50);
        surface.canScrollVertically(50);
        surface.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                view.scrollTo((int)dragEvent.getX(),(int)dragEvent.getY());
                return false;
            }
        });
 **/
    }
    /**
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isProcessed = scaleGestureDetector.onTouchEvent(event);

        if (isProcessed) {

            // Handle touch events here...
            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    start.set(event.getX(), event.getY());
                    tap( event.getX(), event.getY() );
                    mode = DRAG_OR_TAP;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG_OR_TAP) {
                        doPan(event.getX() - start.x, event.getY() - start.y);
                        start.set(event.getX(), event.getY());
                    }
                    break;
            }
        }

        myView.scale(currentPan, currentScaleFactor);

        invalidate();
        return true;
    }

    private void doPan(float panX, float panY) {
        currentPan.x = currentPan.x + panX;
        currentPan.y = currentPan.y + panY;

    }

    private void tap(float x, float y) {

    }
    private class ScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float value = detector.getScaleFactor();
            currentScaleFactor = currentScaleFactor * value;

            // don't let the object get too small or too large.
            boolean doesntMatch = false;
            if (currentScaleFactor < 1f || currentScaleFactor > 20f){
                currentScaleFactor = Math.max(1f, Math.min(currentScaleFactor, 20f));
                doesntMatch = true;
            }

            if(!doesntMatch){
                //scale the viewport as well
                currentPan.x = currentPan.x*value;
                currentPan.y = currentPan.y*value;

            }
            return true;
        }
    }
    **/
}
