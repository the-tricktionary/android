package trictionary.jumproper.com.jumpropetrictionary.networkgraph.layout;

import net.xqhs.graphs.graph.Edge;
import net.xqhs.graphs.graph.Node;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.map.LazyMap;

import android.util.Log;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import trictionary.jumproper.com.jumpropetrictionary.networkgraph.beans.Dimension;
import trictionary.jumproper.com.jumpropetrictionary.networkgraph.beans.NetworkGraph;
import trictionary.jumproper.com.jumpropetrictionary.networkgraph.beans.Point2D;
import trictionary.jumproper.com.jumpropetrictionary.networkgraph.beans.RandomLocationTransformer;

/**
 * The type FR layout.
 */
public class FRLayout extends AbstractLayout {

    private double temperature;

    private int currentIteration;

    private int mMaxIterations = 700;

    private Map<Node, FRVertexData> frVertexData = LazyMap.lazyMap(new HashMap<Node, FRVertexData>(), new Factory<FRVertexData>() {
        public FRVertexData create() {
            return new FRVertexData();
        }
    });

    private double attraction_multiplier = 0.75;

    private double attraction_constant;

    private double repulsion_multiplier = 0.75;

    private double repulsion_constant;

    private double max_dimension;

    /**
     * Creates an instance for the specified graph.
     *
     * @param g the g
     */
    public FRLayout(NetworkGraph g) {
        super(g);
    }

    /**
     * Creates an instance of size {@code d} for the specified graph.
     *
     * @param g the g
     * @param d the d
     */
    public FRLayout(NetworkGraph g, Dimension d) {
        super(g, new RandomLocationTransformer<Node>(d), d);
        initialize();
        max_dimension = Math.max(d.getHeight(), d.getWidth());
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    @Override
    public void setSize(Dimension size) {
        if (!initialized) {
            setInitializer(new RandomLocationTransformer<Node>(size));
        }
        super.setSize(size);
        max_dimension = Math.max(size.getHeight(), size.getWidth());
    }

    /**
     * Sets the attraction multiplier.
     *
     * @param attraction the attraction
     */
    public void setAttractionMultiplier(double attraction) {
        this.attraction_multiplier = attraction;
    }

    /**
     * Sets the repulsion multiplier.
     *
     * @param repulsion the repulsion
     */
    public void setRepulsionMultiplier(double repulsion) {
        this.repulsion_multiplier = repulsion;
    }

    /**
     * Reset void.
     */
    public void reset() {
        doInit();
    }

    /**
     * Initialize void.
     */
    public void initialize() {
        doInit();
    }

    /**
     * Sets graph.
     *
     * @param graph the graph
     */
    @Override
    public void setGraph(final NetworkGraph graph) {
        super.setGraph(graph);
    }

    /**
     * Do init.
     */
    private void doInit() {
        NetworkGraph graph = getGraph();
        Dimension d = getSize();
        if (graph != null && d != null) {
            currentIteration = 0;
            temperature = d.getWidth() / 10;
            double forceConstant = Math.sqrt(d.getHeight() * d.getWidth() / graph.getVertex().size());
            attraction_constant = attraction_multiplier * forceConstant;
            repulsion_constant = repulsion_multiplier * forceConstant;
        }
    }

    /**
     * The EPSILON.
     */
    private double EPSILON = 0.000001D;

    /**
     * Moves the iteration forward one notch, calculation attraction and
     * repulsion between vertices and edges and cooling the temperature.
     */
    public synchronized void step() {
        currentIteration++;
        while (true) {
            try {
                for (Node v1 : getGraph().getNodes()) {
                    calcRepulsion(v1);
                }
                break;
            } catch (ConcurrentModificationException cme) {
                Log.e(FRLayout.class.getName(), cme.getMessage());
            }
        }
        while (true) {
            try {
                for (Edge e : getGraph().getEdges()) {

                    calcAttraction(e);
                }
                break;
            } catch (ConcurrentModificationException cme) {
                Log.e(FRLayout.class.getName(), cme.getMessage());
            }
        }
        while (true) {
            try {
                for (Node v : getGraph().getNodes()) {
                    if (isLocked(v)) {
                        continue;
                    }
                    calcPositions(v);
                }
                break;
            } catch (ConcurrentModificationException cme) {
                Log.e(FRLayout.class.getName(), cme.getMessage());
            }
        }
        cool();
    }

    /**
     * Calc positions.
     *
     * @param v the v
     */
    private synchronized void calcPositions(Node v) {
        FRVertexData fvd = getFRData(v);
        if (fvd == null) {
            return;
        }
        Point2D xyd = transform(v);
        double deltaLength = Math.max(EPSILON, fvd.norm());
        double newXDisp = fvd.getX() / deltaLength * Math.min(deltaLength, temperature);
        if (Double.isNaN(newXDisp)) {
            throw new IllegalArgumentException("Unexpected mathematical result in FRLayout:calcPositions [xdisp]");
        }
        double newYDisp = fvd.getY() / deltaLength * Math.min(deltaLength, temperature);
        xyd.setLocation(xyd.getX() + newXDisp, xyd.getY() + newYDisp);
        double borderWidth = getSize().getWidth() / 50.0;
        double newXPos = xyd.getX();
        if (newXPos < borderWidth) {
            newXPos = borderWidth + Math.random() * borderWidth * 2.0;
        } else {
            if (newXPos > (getSize().getWidth() - borderWidth)) {
                newXPos = getSize().getWidth() - borderWidth - Math.random() * borderWidth * 2.0;
            }
        }

        double newYPos = xyd.getY();
        if (newYPos < borderWidth) {
            newYPos = borderWidth + Math.random() * borderWidth * 2.0;
        } else {
            if (newYPos > (getSize().getHeight() - borderWidth)) {
                newYPos = getSize().getHeight() - borderWidth - Math.random() * borderWidth * 2.0;
            }
        }
        xyd.setLocation(newXPos, newYPos);
    }

    /**
     * Calc attraction.
     *
     * @param e the e
     */
    private void calcAttraction(Edge e) {
        Node v1 = e.getFrom();
        Node v2 = e.getTo();
        boolean v1_locked = isLocked(v1);
        boolean v2_locked = isLocked(v2);

        if (v1_locked && v2_locked) {
            // both locked, do nothing
            return;
        }
        Point2D p1 = transform(v1);
        Point2D p2 = transform(v2);
        if (p1 == null || p2 == null) {
            return;
        }
        double xDelta = p1.getX() - p2.getX();
        double yDelta = p1.getY() - p2.getY();

        double deltaLength = Math.max(EPSILON, Math.sqrt((xDelta * xDelta) + (yDelta * yDelta)));

        double force = (deltaLength * deltaLength) / attraction_constant;

        if (Double.isNaN(force)) {
            throw new IllegalArgumentException("Unexpected mathematical result in FRLayout:calcPositions [force]");
        }

        double dx = (xDelta / deltaLength) * force;
        double dy = (yDelta / deltaLength) * force;
        if (!v1_locked) {
            FRVertexData fvd1 = getFRData(v1);
            fvd1.offset(-dx, -dy);
        }
        if (!v2_locked) {
            FRVertexData fvd2 = getFRData(v2);
            fvd2.offset(dx, dy);
        }
    }

    /**
     * Calc repulsion.
     *
     * @param v1 the v 1
     */
    private void calcRepulsion(Node v1) {
        FRVertexData fvd1 = getFRData(v1);
        if (fvd1 == null) {
            return;
        }
        fvd1.setLocation(0, 0);

        try {
            for (Node v2 : getGraph().getNodes()) {
                // if (isLocked(v2)) continue;
                if (v1 != v2) {
                    Point2D p1 = transform(v1);
                    Point2D p2 = transform(v2);
                    if (p1 == null || p2 == null) {
                        continue;
                    }
                    double xDelta = p1.getX() - p2.getX();
                    double yDelta = p1.getY() - p2.getY();

                    double deltaLength = Math.max(EPSILON, Math.sqrt((xDelta * xDelta) + (yDelta * yDelta)));

                    double force = (repulsion_constant * repulsion_constant) / deltaLength;

                    if (Double.isNaN(force)) {
                        throw new RuntimeException("Unexpected mathematical result in FRLayout:calcPositions [repulsion]");
                    }
                    fvd1.offset((xDelta / deltaLength) * force, (yDelta / deltaLength) * force);
                }
            }
        } catch (ConcurrentModificationException cme) {
            calcRepulsion(v1);
        }
    }

    /**
     * Cool void.
     */
    private void cool() {
        temperature *= (1.0 - currentIteration / (double) mMaxIterations);
    }

    /**
     * Sets the maximum number of iterations.
     *
     * @param maxIterations the max iterations
     */
    public void setMaxIterations(int maxIterations) {
        mMaxIterations = maxIterations;
    }

    /**
     * Gets fR data.
     *
     * @param v the v
     * @return the fR data
     */
    private FRVertexData getFRData(Node v) {
        return frVertexData.get(v);
    }

    /**
     * This one is an incremental visualization.
     *
     * @return the boolean
     */
    public boolean isIncremental() {
        return true;
    }

    /**
     * Returns true once the current iteration has passed the maximum count,
     * <tt>MAX_ITERATIONS</tt>.
     *
     * @return the boolean
     */
    public boolean done() {
        return currentIteration > mMaxIterations || temperature < 1.0 / max_dimension;
    }

    /**
     * The type FR vertex data.
     */
    private static class FRVertexData extends Point2D {

        /**
         * Offset void.
         *
         * @param x the x
         * @param y the y
         */
        void offset(double x, double y) {
            this.x += x;
            this.y += y;
        }

        /**
         * Norm double.
         *
         * @return the double
         */
        double norm() {
            return Math.sqrt(x * x + y * y);
        }
    }
}