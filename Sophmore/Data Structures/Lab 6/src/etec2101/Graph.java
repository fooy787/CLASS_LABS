package etec2101;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Graph class that allows us to stores node values as well as edges and then connect the nodes and edges.
 * @author Chase Williams
 * @param <N> Type of Node.
 * @param <E> Type of edge.
 */
public class Graph<N,E> {

    protected ArrayList<N> mNodes = new ArrayList();
    protected Object[][] mEdges;
    protected int curAmount = 0;

    /**
     * Constructor for the Graph class, sets the Edges to a table size of 4 by 4
     */
    public Graph(){
        mEdges = new Object[4][4];
    }//End constructor

    /**
     * Resizes the table. Made private.
     * @param toResize The table to Resize.
     * @return Returns a table of 1 size larger.
     */
    private Object[][] returnResize(Object[][] toResize){
        Object[][] newArray = new Object[toResize.length + 1][toResize.length + 1];
        for (int i = 0; i < toResize.length; i ++){
            System.arraycopy(toResize[i], 0, newArray[i], 0, toResize.length);
        }//End for loop
        return newArray;
    }//End resize by 1

    /**
     * Returns either the neighbor or null.
     * @param node The node you want to check.
     * @param SecondIndex The index of the node you want to check against.
     * @return Returns either the neighbor or null
     */
    private N getNeighbors(N node, int SecondIndex){
        int index = mNodes.indexOf(node);
        N neighbor;
        if(mEdges[index][SecondIndex] != null){
            return (N) mEdges[SecondIndex][index];
        }
        return null;
    }
    /**
     * Adds a node
     * @param val the value the Node should hold.
     */
    public void addNode(N val){
        if(curAmount >= mEdges.length) mEdges = returnResize(mEdges);
        mNodes.add(val);
        curAmount++;
    }//End AddNode

    /**
     * Adds an edge between two nodes.
     * @param data The value of the edge.
     * @param nodeA The first node it should go from
     * @param nodeB The other node it is touching.
     * @param bidirectional Whether or not it is bidirectional.
     */
    public void addEdge(E data, N nodeA, N nodeB, boolean bidirectional){
        int indexA = mNodes.indexOf(nodeA);
        int indexB = mNodes.indexOf(nodeB);

        mEdges[indexA][indexB] = data;
        if(bidirectional){
            mEdges[indexB][indexA] = data;

        }//End if bidirectional
    }//End addEdge

    /**
     * Removes an edge from two nodes.
     * @param nodeA The first Node to have an edge removed from.
     * @param nodeB The second Node to have an edge removed from.
     */
    public void removeEdge(N nodeA, N nodeB){
        int indexA = mNodes.indexOf(nodeA);
        int indexB = mNodes.indexOf(nodeB);

        mEdges[indexA][indexB] = null;
        mEdges[indexB][indexA] = null;
    }//End Remove Edge

    /**
     * Takes a parameter i and gets the node at that value
     * @param i the index you want to check
     * @return the returned node.
     */
    public N returnNode(int i){
        return mNodes.get(i);
    }

    public void BFS(){

    }

}//End Graph class