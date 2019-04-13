package Lab06_cw;

import etec2101.Graph;

import org.newdawn.slick.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class DrawableGraph extends Graph<DrawableGraph.DrawableNode, DrawableGraph.DrawableEdge> {
    private Graph<DrawableNode, DrawableEdge> DG;
    protected Color c = new Color(255,0,0);
    private ArrayList<DrawableEdge> DE = new ArrayList<>();
    private ArrayList<DrawableNode> DN = new ArrayList<>();

    DrawableGraph(String file){

        DG = new Graph<>();

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            while(br.ready()){
                String line = br.readLine(); //Reads a line
                line = line.trim(); //Trims the line
                String map[] = line.split(" ");
                String type = map[0];
                if (type.equals("n")){
                    String name = map[1];
                    int xValue = Integer.parseInt(map[2]);
                    int yValue = Integer.parseInt(map[3]);
                    int radius = Integer.parseInt(map[4]);
                    DrawableNode tmpNode = new DrawableNode(xValue, yValue, radius, name);
                    DG.addNode(tmpNode);
                    DN.add(tmpNode);
                }//End if type n

                else if(type.equals("e")){
                    int nodeOne = Integer.parseInt(map[1]); //Gets the first node
                    int nodeTwo = Integer.parseInt(map[2]);//Gets the second node
                    float edgeValue = Float.parseFloat(map[3]);//Gets the float value
                    DrawableEdge tmpEdge = new DrawableEdge(nodeOne, nodeTwo, edgeValue);//Creates a temp edge
                    DrawableNode n1 = DG.returnNode(nodeOne);//Returns the first node in node form
                    DrawableNode n2 = DG.returnNode(nodeTwo);//Returns the second node in node form
                    DG.addEdge(tmpEdge, n1, n2, false);//Adds the edge to the graph
                    DE.add(tmpEdge);
                }//End if it's a type e
            }//End while loop
        }//End try

        catch(java.io.IOException e){
            System.out.print("Error file does not exist.");
        }//End catch
    }//End Constructor

    public class DrawableNode{
        float x;
        float y;
        int radius;
        String name;

        public DrawableNode(int xVal, int yVal, int radVal, String nodeName){
            x = xVal;
            y = yVal;
            radius = radVal;
            name = nodeName;
        }//End Constructor

        protected void nodeDraw(Graphics g){
            g.setColor(c);
            g.fillOval((int)x - radius, (int)y - radius, radius * 2, radius * 2);

        }//End node draw
    }//End Drawable Node

    public class DrawableEdge{
        int connectFrom;
        int connectTo;
        float value;
        public DrawableEdge(int nodeOne, int nodeTwo, float valuePass){
            connectFrom = nodeOne;
            connectTo = nodeTwo;
            value = valuePass;
        }//End constructor

        protected void edgeDraw(Graphics g){
            double theta = 60;
            g.setColor(Color.black);
            g.drawLine(DG.returnNode(connectFrom).x,DG.returnNode(connectFrom).y, DG.returnNode(connectTo).x, DG.returnNode(connectTo).y);
            g.drawLine(DG.returnNode(connectTo).x, DG.returnNode(connectTo).y, (float) (DG.returnNode(connectTo).x + (5/Math.cos(theta))), (float) (DG.returnNode(connectTo).y + (5/Math.cos(theta))));
        }//End edgeDraw
    }//End DrawableEdge

    public void drawAll(Graphics g){
        for(int index = 0; index < DN.size(); index++){
            DrawableNode tmpNode = DN.get(index);
            tmpNode.nodeDraw(g);
        }//End drawing nodes
        for(int index = 0; index < DE.size(); index++){
            DrawableEdge tmpNode = DE.get(index);
            tmpNode.edgeDraw(g);
        }//End drawing edges
    }//End draw all function

    private float returnHighX(){
        float x = 0;
        for(int index = 0; index < DN.size(); index++){
            if(DN.get(index).x > x){
                x = DN.get(index).x;
            }
        }
        return x;
    }

    protected float toScaleX(int Resolution){
        float highestX = returnHighX();
        return Resolution / highestX;
    }

    private float returnHighY(){
        float y = 1000;
        for(int index = 0; index < DN.size(); index++){
            if(DN.get(index).y < y){
                y = DN.get(index).y;
            }
        }
        return y;
    }

    protected float toScaleY(int Resolution){
        float highestY = returnHighY();
        return Resolution / (Resolution - highestY);
    }

    public void setVals(float ScaleX, float ScaleY){
        for(int index = 0; index < DN.size(); index++){
            DN.get(index).x *= ScaleX;
            DN.get(index).y *= ScaleY;
        }
    }
}//End Drawable Graph