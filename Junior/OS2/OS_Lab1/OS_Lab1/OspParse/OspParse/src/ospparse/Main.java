package ospparse;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author jhudson
 */
public class Main {

    static class Node {

        double lat, lon;
        boolean inRoad = false;
        double[] p;

        Node(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }

    static class Entity {

        Map<String, String> tags = new TreeMap<>();
    }

    static class Relation extends Entity {

        //type, id
        ArrayList<String[]> members = new ArrayList<>();
    }

    static class Way extends Entity {

        ArrayList<String> nodes = new ArrayList<>();
        String id;
        Set<String> otherNames = new TreeSet<>();
        boolean isRoad;

        Way(String id) {
            this.id = id;
        }
    }

    static class Road {

        ArrayList<String> ways = new ArrayList<>();
        String name;

        private Road(String name) {
            this.name = name;
        }
    }

//    static class ShareInfo {
//
//        Way way;
//        boolean atStart;
//
//        ShareInfo(Way w, boolean atStart) {
//            way = w;
//            atStart = atStart;
//        }
//    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        TreeMap<String, Node> nodes = new TreeMap<>();
        TreeMap<String, Way> ways = new TreeMap<>();
        TreeMap<String, Relation> relations = new TreeMap<>();

        double clipMinLat = 38.7244;
        double clipMaxLat = 38.7670;
        double clipMinLon = -83.0145;
        double clipMaxLon = -82.9534;
        
        File f= new File("map.osm");
        if(!f.exists()){
            System.out.println("Downloading map...");
            
            //info from https://gist.github.com/michalbcz/4170520
            SSLContext ctx = SSLContext.getInstance("TLS");
            KeyManager[] kmgr = new KeyManager[0];
            TrustManager[] tmgr = new TrustManager[1];
            tmgr[0] = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] xcs,
                        String string) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs,
                        String string) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            
            SecureRandom srand = new SecureRandom();
            ctx.init(kmgr,tmgr,srand);
            SSLContext.setDefault(ctx);
            URL u = new URL("https://overpass-api.de/api/map?bbox="+
                    clipMinLon+","+clipMinLat+","+clipMaxLon+","+clipMaxLat);
            HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            });
            int len = conn.getContentLength();
            InputStream in = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream("map.osm");
            byte[] b = new byte[4096];
            int totalRead=0;
            while(true){
                int nr = in.read(b);
                if(nr<=0)
                    break;
                fos.write(b,0,nr);
                totalRead += nr;
                System.out.println(totalRead+" bytes");
            }
            in.close();
            conn.disconnect();
            fos.close();
        }
        
        System.out.println("Reading from "+f.getName());
        
        SAXParser p = SAXParserFactory.newInstance().newSAXParser();
        p.parse(f, new DefaultHandler() {
            Entity inElem;

            @Override
            public void startElement(String uri, String name, String qname, Attributes attrs) {
                switch (qname) {
                    case "node": {
                        double lat = Double.parseDouble(attrs.getValue("lat"));
                        double lon = Double.parseDouble(attrs.getValue("lon"));
                        if( lat < clipMinLat || lat > clipMaxLat || lon < clipMinLon || lon > clipMaxLon ){
                        } else{
                            String id_ = attrs.getValue("id");
                            nodes.put(id_, new Node(lat, lon));
                        }
                        break;
                    }
                    case "way": {
                        String id_ = attrs.getValue("id");
                        Way w = new Way(id_);
                        inElem = w;
                        ways.put(id_, w);
                        break;
                    }
                    case "relation": {
                        String id_ = attrs.getValue("id");
                        Relation R = new Relation();
                        inElem = R;
                        relations.put(id_, R);
                        break;
                    }
                    case "nd":
                        if (inElem != null) {
                            ((Way) inElem).nodes.add(attrs.getValue("ref"));
                        }
                        break;
                    case "member":
                        if (inElem != null) {
                            ((Relation) inElem).members.add(new String[]{
                                attrs.getValue("type"), attrs.getValue("ref")});
                        }
                        break;
                    case "tag":
                        if (inElem != null) {
                            inElem.tags.put(attrs.getValue("k"), attrs.getValue("v"));
                        }
                        break;
                }
            }

            @Override
            public void endElement(String uri, String name, String qname) {
                switch (qname) {
                    case "way":
                    case "relation":
                        inElem = null;
                }
            }
        });

        //eliminate nonexistent nodes from Ways
        for(String wayID : ways.keySet() ){
            Way way = ways.get(wayID);
            Iterator<String> it = way.nodes.iterator();
            while(it.hasNext()){
                String nodeID = it.next();
                if( nodes.get(nodeID) == null )
                    it.remove();
            }
        }
        
        //key = road name
        Map<String, Road> roadMap = new TreeMap<>();
        ArrayList<Road> roads = new ArrayList<>();

        for (String wayId : ways.keySet()) {
            Way way = ways.get(wayId);
            if (way.tags.containsKey("highway")) {
                way.isRoad = true;
            }
        }

        for (String relID : relations.keySet()) {
            getRoadsFromRelation(ways, relations, relID);
        }

        for (String wayID : ways.keySet()) {
            Way way = ways.get(wayID);
            if (!way.isRoad) {
                continue;
            }

            String name = way.tags.get("name");
            if (name == null && !way.otherNames.isEmpty()) {
                name = way.otherNames.iterator().next();
            }
            if (name == null) {
                continue;
                //name = "<"+wayID+">";
            }

            if (!roadMap.containsKey(name)) {
                Road r = new Road(name);
                roads.add(r);
                roadMap.put(name, r);
            }
            Road road = roadMap.get(name);
            road.ways.add(wayID);
        }

        double minLat = 1E99,
                maxLat = -1E99,
                minLon = 1E99,
                maxLon = -1E99;

        for (Road road : roads) {

            for (String wayID : road.ways) {
                Way way = ways.get(wayID);

                for (String nodeID : way.nodes) {
                    Node n = nodes.get(nodeID);
                    if( n == null )
                        continue;
                    
                    if (n.lat < minLat) {
                        minLat = n.lat;
                    }
                    if (n.lon < minLon) {
                        minLon = n.lon;
                    }
                    if (n.lat > maxLat) {
                        maxLat = n.lat;
                    }
                    if (n.lon > maxLon) {
                        maxLon = n.lon;
                    }
                }
            }

            /*
            while (true) {
                boolean changed = false;
                Map<String, ArrayList<ShareInfo>> endpoints = new TreeMap<>();

                //try to glue connected ways into a single larger way
                for (String wayID : road.ways) {
                    Way way = ways.get(wayID);

                    if( way.nodes.isEmpty() )
                        continue;
                    String stid = way.nodes.get(0);
                    String eid = way.nodes.get(way.nodes.size() - 1);
                    if (!endpoints.containsKey(stid)) {
                        endpoints.put(stid, new ArrayList<>());
                    }
                    if (!endpoints.containsKey(eid)) {
                        endpoints.put(eid, new ArrayList<>());
                    }
                    endpoints.get(stid).add(new ShareInfo(way, true));
                    endpoints.get(eid).add(new ShareInfo(way, false));

                    for (String nid : endpoints.keySet()) {
                        ArrayList<ShareInfo> sharing = endpoints.get(nid);
                        if (sharing.size() == 2) {
                            changed=true;
                            ShareInfo s1 = sharing.get(0);
                            ShareInfo s2 = sharing.get(1);
                            Way w1 = s1.way;
                            Way w2 = s2.way;
                            if (s1.atStart) {
                                if (s2.atStart) {
                                    //  w2.last------w2.first/w1.first------w1.last
                                    ArrayList<String> tmp = new ArrayList<>();
                                    for (int i = w2.nodes.size() - 1; i >= 0; i--) {
                                        tmp.add(w2.nodes.get(i));
                                    }
                                    for (int i = 1; i < w1.nodes.size(); ++i) {
                                        tmp.add(w1.nodes.get(i));
                                    }
                                    w1.nodes = tmp;
                                    w2.nodes.clear();
                                } else {
                                    //  w2.first------w2.last/w1.first-----w1.last
                                    w2.nodes.addAll(w1.nodes);
                                    w1.nodes.clear();
                                }
                            } else {
                                if (s2.atStart) {
                                    //  w1.first-----w1.last/w2.first-----w1.last
                                    w1.nodes.addAll(w2.nodes);
                                    w2.nodes.clear();
                                } else {
                                    // w1.first-------w1.last/w2.last-------w2.first
                                    ArrayList<String> tmp = new ArrayList<>();
                                    for (int i = w2.nodes.size() - 1; i >= 0; i--) {
                                        tmp.add(w2.nodes.get(i));
                                    }
                                    w1.nodes.addAll(tmp);
                                    w2.nodes.clear();
                                }
                            }
                        }
                    }

                }
                if (changed == false) {
                    break;
                }
            }
             */
        }

        double deltaLat = maxLat - minLat;

        double minX = 1E99,
                maxX = -1E99,
                minY = 1E99,
                maxY = -1E99;

        for (Road road : roads) {
            for (String wayID : road.ways) {
                Way way = ways.get(wayID);
                for (String nodeID : way.nodes) {
                    Node n = nodes.get(nodeID);
                    if( n == null )
                        continue;
                    n.inRoad = true;
                    double x = n.lon - minLon;
                    double y = n.lat - minLat;
                    //invert y
                    y = deltaLat - y;
                    x *= 65536;
                    y *= 65536;
                    n.p = new double[]{x, y};
                    minX = Math.min(n.p[0], minX);
                    minY = Math.min(n.p[1], minY);
                    maxX = Math.max(n.p[0], maxX);
                    maxY = Math.max(n.p[1], maxY);
                }
            }
        }

        System.out.println("min:" + minX + " " + minY);
        System.out.println("max:" + maxX + " " + maxY);
        System.out.println("num roads:" + roads.size());

        //outputSvg("/tmp/z.svg", minX,minY,maxX,maxY,roads,ways,nodes);
        
        
        outputPng("map.png",minX,minY,maxX,maxY,roads,ways,nodes,
                (int)(maxX-minX),(int)(maxY-minY),0,0);
        
//        int tileSize = 512;
//        for(int y=0,ty=0;y<maxY;y+=tileSize,ty++){
//            for(int x=0,tx=0;x<maxX;x+=tileSize,tx++){
//                outputPng("tile"+tx+","+ty+".png", minX,minY,maxX,maxY,roads,ways,nodes,tileSize,tileSize,x,y);
//            }
//        }
        
        PrintWriter pw = new PrintWriter("graph.txt");
        for (String nid : nodes.keySet()) {
            Node n = nodes.get(nid);
            if (n.inRoad) {
                pw.println("node " + nid + " " + n.p[0] + " " + n.p[1]);
            }
        }
        for (Road road : roads) {
            for (String wayID : road.ways) {
                Way way = ways.get(wayID);

                for (int i = 1; i < way.nodes.size(); ++i) {
                    String n0id = way.nodes.get(i - 1);
                    String n1id = way.nodes.get(i);
                    Node n0 = nodes.get(n0id);
                    Node n1 = nodes.get(n1id);
                    double deltaX = n0.p[0] - n1.p[0];
                    double deltaY = n0.p[1] - n1.p[1];
                    double le = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                    pw.println("edge " + " " + n0id + " " + n1id + " " + le);
                    pw.println("edge " + " " + n1id + " " + n0id + " " + le);
                }
            }
        }
        pw.close();
    }

    static void outputSvg(String fname, double minX, double minY, double maxX, double maxY,
            ArrayList<Road> roads, Map<String, Way> ways, Map<String, Node> nodes) throws IOException {
        PrintWriter pw = new PrintWriter(fname);

        pw.print("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        pw.print("<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"11in\" width=\"8.5in\">\n");

        pw.printf("<rect x=\"%f\" y=\"%f\" width=\"%f\" height=\"%f\" style=\"fill:#AA9E62\"/>",
                minX, minY, maxX - minX, maxY - minY);

        for (Road road : roads) {

            pw.println("<!-- " + road.name + " -->");
            pw.printf("<path d=\"");

            for (String wayID : road.ways) {
                boolean first = true;
                Way way = ways.get(wayID);
                for (String nid : way.nodes) {
                    Node n = nodes.get(nid);
                    if( n == null )
                        continue;
                    if (first) {
                        pw.printf("M%f,%f ", n.p[0], n.p[1]);
                    } else {
                        pw.printf("L%f,%f ", n.p[0], n.p[1]);
                    }
                    first = false;
                }
            }
            pw.println("\" style=\"stroke:rgb(192,192,192);stroke-width:4px;fill:none\" />");
        }

        System.out.println("SVG Drew roads");

        for (Road road : roads) {
            for (String wayID : road.ways) {
                if( road.name.length() > 0 ){
                    
                    Way way = ways.get(wayID);
                    int i1 = way.nodes.size() / 2 - 1;

                    if (way.nodes.size() < 2) {
                        return;
                    }

                    String nid1 = way.nodes.get(i1);
                    String nid2 = way.nodes.get(i1 + 1);
                    Node n1 = nodes.get(nid1);
                    Node n2 = nodes.get(nid2);
                    double[] p1 = n1.p;
                    double[] p2 = n2.p;
                    if (p1[0] > p2[0]) {
                        double[] tmp = p1;
                        p1 = p2;
                        p2 = tmp;
                    }

                    double rot = Math.atan2(p2[1] - p1[1], p2[0] - p1[0]);
                    rot = rot / Math.PI * 180.0;
                    if (rot < 0) {
                        rot += 360;
                    }

                    String name = road.name.replace(" Street", "");
                    name = name.replace(" Avenue", "");
                    name = name.replace(" Road", "");
                    name = name.replace(" Drive", "");

                    pw.printf(
                            "<text x=\"0\" y=\"0.5\" font-size=\"4px\" style=\"fill:black\" transform=\"translate(%f,%f) rotate(%f 0,0)\">",
                            p1[0], p1[1], rot);
                    pw.println(name + "</text>");
                }
            }
        }

        System.out.println("SVG Drew labels");

        pw.println("</svg>");
        pw.close();

    }

    static void outputPng(String fname, double minX, double minY, double maxX, double maxY,
            ArrayList<Road> roads, Map<String, Way> ways, Map<String, Node> nodes,
            int width, int height, int offsetX, int offsetY) throws IOException {

        System.out.println(fname);
        BufferedImage img = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D G = (Graphics2D) img.getGraphics();
        G.translate(-offsetX,-offsetY);
        G.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        G.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        G.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        G.setPaint(new Color(0xaa, 0x9e, 0x62));
        G.fillRect(0, 0, (int) maxX - (int) minX, (int) maxY - (int) minY);

        G.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        G.setPaint(new Color(192, 192, 192));
        G.setFont(new Font("SansSerif", Font.PLAIN, 8));

        for (Road road : roads) {
            Path2D.Double path = new Path2D.Double();

            for (String wayID : road.ways) {
                boolean first = true;
                Way way = ways.get(wayID);
                for (String nid : way.nodes) {
                    Node n = nodes.get(nid);
                    if (first) {
                        path.moveTo(n.p[0], n.p[1]);
                    } else {
                        path.lineTo(n.p[0], n.p[1]);
                    }
                    first = false;
                }
            }
            G.draw(path);
        }

        //System.out.println("PNG Drew roads");

        G.setPaint(new Color(192,224,255));
        for( String nodeID : nodes.keySet()){
            Node node = nodes.get(nodeID);
            if( node.p != null ){
                G.fillRect( (int)node.p[0]-1,(int)node.p[1]-1,2,2);
            }
        }
        
        G.setPaint(new Color(0, 0, 0));
        for (Road road : roads) {
            for (String wayID : road.ways) {
                Way way = ways.get(wayID);
                int i1 = way.nodes.size() / 2 - 1;

                if (way.nodes.size() < 2) {
                    continue;
                }

                String nid1 = way.nodes.get(i1);
                String nid2 = way.nodes.get(i1 + 1);
                Node n1 = nodes.get(nid1);
                Node n2 = nodes.get(nid2);
                double[] p1 = n1.p;
                double[] p2 = n2.p;
                if (p1[0] > p2[0]) {
                    double[] tmp = p1;
                    p1 = p2;
                    p2 = tmp;
                }

                double rot = Math.atan2(p2[1] - p1[1], p2[0] - p1[0]);
                rot = rot / Math.PI * 180.0;
                if (rot < 0) {
                    rot += 360;
                }

                String name = road.name.replace(" Street", "");
                name = name.replace(" Avenue", "");
                name = name.replace(" Road", "");
                name = name.replace(" Drive", "");

                AffineTransform trans = G.getTransform();

                G.translate(p1[0], p1[1]);
                G.rotate(rot / 180 * Math.PI, 0, 0);
                G.drawString(name, 0, 0);
                G.setTransform(trans);
            }
        }

        //System.out.println("PNG Drew labels");

        G.dispose();
        ImageIO.write(img, "png", new File(fname));

    }

    static void getRoadsFromRelation(Map<String, Way> ways, Map<String, Relation> relations, String relID) {
        Relation relation = relations.get(relID);
        if (relation == null) {
            return;
        }

        String rtype = relation.tags.getOrDefault("type", "?UNKNOWN?");
        switch (rtype) {
            case "boundary":
            case "multipolygon":
            case "restriction":
            case "waterway":
                return;
            case "route":
                break;
            default:
                System.out.println("Don't know about "+rtype);
                return;
        }

        if (relation.tags.get("railway") != null) {
            return; //don't look at railroads
        }
        if ("railway".equals(relation.tags.get("route"))) {
            return; //don't look at railroads
        }
        if (!"road".equals(relation.tags.get("route"))) {
            return;
        }

        for (String[] tmp : relation.members) {
            String type = tmp[0];
            String id = tmp[1];
            if ("way".equals(type)) {
                Way way = ways.get(id);
                if (way == null) {
                    //some relations include things that aren't on
                    //the map
                    continue;
                }
                String name = relation.tags.get("name");
                if (name != null) {
                    way.otherNames.add(name);
                }
                way.isRoad = true;
            } else if ("relation".equals(type)) {
                getRoadsFromRelation(ways, relations, id);
            }
        }
    }

    /*                
    if 0:
        for rr in relations:
            R = relations[rr]
            if relations[rr].tags["type"] == "route":
                if "name" not in R.tags:
                    R.tags["name"] = R.tags["network"]+R.tags["ref"]
                road = Road()
                road.name = R.tags["name"]
                for wayId in R.members:
                    if wayId not in ways:
                        continue
                    way = ways[wayId]
                    for node in way.members:
                        road.nodes.append(node)
                roads.append(road)
     */
}
