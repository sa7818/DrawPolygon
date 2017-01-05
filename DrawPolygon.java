// Skeletal program for the "Draw Polygons" assignment
// Written by:  Minglun Gong



//Sara Ayubian 


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



// Main class
public class DrawPolygon extends Frame implements ActionListener
{

    GridCanvas grid;
    Timer timer = new Timer();
    // Constructor

    public DrawPolygon(int span)
    {
        super("Arbitrary Polygon");
        // create & add a grid canvas
        grid = new GridCanvas(span);
        add("Center", grid);
        // add a control panel
        Panel controls = new Panel();
        add("South", controls);
        // create & add buttons
        Button button = new Button("Clear Vertices");
        button.addActionListener(this);
        controls.add(button);
        button = new Button("Line Only");
        button.addActionListener(this);
        controls.add(button);
        button = new Button("Fill Only");
        button.addActionListener(this);
        controls.add(button);
        button = new Button("Line & Fill");
        button.addActionListener(this);
        controls.add(button);
        addWindowListener(new ExitListener());
    }
    
    
    
    
    
    
    // Exit listener

    class ExitListener extends WindowAdapter
    {

        @Override
        public void windowClosing(WindowEvent e)
        {
            timer.cancel();
            System.exit(0);
        }
    }

    
    
    
    
    
    
    
    // Action listener for buttons
    @Override
    public void actionPerformed(ActionEvent e)
    {
        // clear control lines and restore the output panel
        switch (((Button) e.getSource()).getLabel())
        {
            case "Clear Vertices":
                grid.vertices.clear();
                grid.LinePoly = false;
                grid.FillPoly = false;
                grid.repaint();
                break;
            case "Line Only":
                grid.LinePoly = true;
                grid.FillPoly = false;
                grid.repaint();
                break;
            case "Fill Only":
                grid.LinePoly = false;
                grid.FillPoly = true;
                grid.repaint();
                break;
            case "Line & Fill":
                grid.LinePoly = true;
                grid.FillPoly = true;
                grid.repaint();
                break;
            default:
                break;
        }
    }

    
    
    
    
    
// Set the size of the screen and Draw Polygon    
    public static void main(String[] args)
    {
        int span = 10;
        if (args.length == 1)
        {
            span = Integer.parseInt(args[0]);
        }
        DrawPolygon window = new DrawPolygon(span);
        window.setSize(400, 450);
        window.setResizable(true);
        window.setVisible(true);
    }
}






// Canvas with grid shown
class GridCanvas extends Canvas
{
    // parameter that controls the span of the grid

    int span, width, height, xoff, yoff;
    List<Dot> vertices = new ArrayList<>();
    boolean LinePoly, FillPoly;
    
    
    
    
    
    // Initialize the grid size;

    public GridCanvas(int s)
    {
        span = s;
        LinePoly = false;
        FillPoly = false;
        addMouseListener(new ClickListener());
    }
    
    
    
    
    
    
    // Represent a dot at given coordinate

    class Dot extends Ellipse2D.Float
    {

        int gx, gy;

        public Dot(int x, int y)
        {
            super(x * span - span / 2 + xoff, -y * span - span / 2 + yoff, span, span);
            gx = x;
            gy = y;
        }

        public Dot(Point pt)
        {
            gx = Math.round(((float) pt.getX() - xoff) / (float) span);
            gy = -Math.round(((float) pt.getY() - yoff) / (float) span);
            setFrame(gx * span - span / 2 + xoff,-gy * span - span / 2 + yoff, span, span);
        }

        public Point toCoord()
        {
            return new Point((int) x + span / 2, (int) y + span / 2);
        }
    }
    
    
    
    
    
    // Draw the grids

    public void drawGrid(Graphics2D g2D)
    {
        g2D.setColor(Color.lightGray);
        for (int x = 0; x <= width; x++)
        {
            g2D.draw(new Line2D.Float(new Dot(x, 0).toCoord(), new Dot(x, height).toCoord()));
        }
        for (int y = 0; y <= height; y++)
        {
            g2D.draw(new Line2D.Float(new Dot(0, y).toCoord(), new Dot(width, y).toCoord()));
        }
    }
    
    
    
    
// Draw line 
    public void Node_Connect(Graphics2D g2D)
    {
        for (int i = 0; i < vertices.size(); i++)
        {
            Dot existing = vertices.get(i);
            Dot next = vertices.get((i + 1) % vertices.size());
            if (existing.gx <= next.gx)
            {
                Nodes_Connect(g2D, new Dot(existing.toCoord()), new Dot(next.toCoord()));
            } else
            {
                Nodes_Connect(g2D, new Dot(next.toCoord()), new Dot(existing.toCoord()));
            }
        }
    }
    
    
    

    public void Nodes_Connect(Graphics2D g2D, Dot s, Dot e)
    {
        g2D.setColor(Color.darkGray);

        int DeltaX = e.gx - s.gx;
        int DeltaY = e.gy - s.gy;

        int Dis1 = DeltaY * 2 - DeltaX;
        int Dis2 = DeltaY - 2 * DeltaX;
        int Dis3 = DeltaY * 2 + DeltaX;
        int Dis4 = DeltaY + 2 * DeltaX;
        int Distance = Math.abs(DeltaX) - Math.abs(DeltaY);

        int x = s.gx;
        int y = s.gy;
        g2D.fill(s);
        while (Math.abs(x - e.gx) > 0 || Math.abs(y - e.gy) > 0)
        {
            if (Distance >= 0)
            {
                if (DeltaY >= 0)
                {
                    if (Dis1 <= 0)
                    {
                        Dis1 += (DeltaY * 2);
                        x++;
                    } else
                    {
                        Dis1 += ((DeltaY - DeltaX) * 2);
                        x++;
                        y++;
                    }
                } else if (Dis3 >= 0)
                {
                    Dis3 += (DeltaY * 2);
                    x++;
                } else
                {
                    Dis3 += ((DeltaY + DeltaX) * 2);
                    x++;
                    y--;
                }
            } else if (DeltaY >= 0)
            {
                if (Dis2 >= 0)
                {
                    Dis2 -= (DeltaX * 2);
                    y++;
                } else
                {
                    Dis2 += ((DeltaY - DeltaX) * 2);
                    x++;
                    y++;
                }
            } else if (Dis4 <= 0)
            {
                Dis4 += (DeltaX * 2);
                y--;
            } else
            {
                Dis4 += ((DeltaY + DeltaX) * 2);
                x++;
                y--;
            }
            g2D.fill(new Dot(x, y));
        }
    }

    
    
    
    
    
   
    
    public class Area
    {

        int now;
        int following;

        public Area(int _now, int _following)
        {
            now = _now;
            following = _following;
        }

        public boolean surface(int _value)
        {
            return (_value - now) * (_value - following) <= 0;
        }
    }

    
    
  //Draw edge between two node 
    public class EdgeDraw
    {

        int end_y;
        float present_x, slope;

        public EdgeDraw(Dot _s, Dot _e)
        {
            end_y = _e.gy;
            present_x = _s.gx;
            slope = (float) (_s.gx - _e.gx) / (float) (_s.gy - _e.gy);
        }
    }

    
    
    public class Scanlines
    {

        Map<Integer, List<Area>> sections;
        
        
        
        

        
        
        public Scanlines()
        {
            sections = new HashMap<>();
        }

        public void AddSection(int y, int now, int following)
        {
            Area _r = new Area(now, following);
            if (sections.containsKey(y))
            {
                sections.get(y).add(_r);
            } else
            {
                List<Area> _rs = new ArrayList<>();
                _rs.add(_r);
                sections.put(y, _rs);
            }
        }
        
        
        

        
        
        public boolean surface(int y, int val)
        {
            if (sections.size() > 0)
            {
                if (sections.containsKey(y))
                {
                    for (int i = 0; i < sections.get(y).size(); i++)
                    {
                        if (sections.get(y).get(i).surface(val))
                        {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    
    
    
    
    
    
    
    // Draw Polygon 
    
    public void FigureDraw(Graphics2D g2D)
    {
        g2D.setColor(Color.pink);
        Map<Integer, List<EdgeDraw>> edgeTable = new HashMap<>();
        List<EdgeDraw> activeEdgeTable = new ArrayList<>();
        List<Dot> ignoreVs = new ArrayList<>();
        Scanlines Scanlines = new Scanlines();
        int y_maximum = Integer.MIN_VALUE;
        int y_minimum = Integer.MAX_VALUE;

        for (int i = 0; i < vertices.size(); i++)
        {
            Dot previous = vertices.get((i + vertices.size() - 1) % vertices.size());
            Dot existing = vertices.get(i);
            Dot next = vertices.get((i + 1) % vertices.size());

            Dot p = new Dot(previous.toCoord());
            Dot c = new Dot(existing.toCoord());
            Dot n = new Dot(next.toCoord());

            if ((p.gy - c.gy) * (n.gy - c.gy) > 0)
            {
                ignoreVs.add(c);
            }

            Dot start, end;

            if (c.gy != n.gy)
            {
                if (c.gy > n.gy)
                {
                    start = c;
                    end = n;
                } else
                {
                    end = c;
                    start = n;
                }

                EdgeDraw _edge = new EdgeDraw(start, end);
                if (edgeTable.containsKey(start.gy))
                {
                    edgeTable.get(start.gy).add(_edge);
                } else
                {
                    List<EdgeDraw> _edges = new ArrayList<>();
                    _edges.add(_edge);
                    edgeTable.put(start.gy, _edges);
                }
                if (start.gy > y_maximum)
                {
                    y_maximum = start.gy;
                }
                if (end.gy < y_minimum)
                {
                    y_minimum = end.gy;
                }
            } else
            {
                Scanlines.AddSection(c.gy, c.gx, n.gx);
            }
        }

        if (y_maximum >= y_minimum)
        {
            int y = y_maximum;
            while ((!edgeTable.isEmpty()) || (!activeEdgeTable.isEmpty()))
            {
                if (edgeTable.containsKey(y))
                {
                    activeEdgeTable.addAll(edgeTable.get(y));
                    edgeTable.remove(y);
                }

                Iterator<EdgeDraw> iter = activeEdgeTable.iterator();
                while (iter.hasNext())
                {
                    EdgeDraw e = iter.next();
                    if (e.end_y >= y)
                    {
                        iter.remove();
                    }
                }
                Set<Float> sequence = new TreeSet<>();
                for (int i = 0; i < activeEdgeTable.size(); i++)
                {
                    boolean ignore = false;
                    for (int j = 0; j < ignoreVs.size(); j++)
                    {
                        if (y == ignoreVs.get(j).gy && activeEdgeTable.get(i).present_x == ignoreVs.get(j).gx)
                        {
                            ignore = true;
                        }
                    }
                    if (!ignore)
                    {
                        sequence.add(activeEdgeTable.get(i).present_x);
                    }
                    activeEdgeTable.get(i).present_x -= activeEdgeTable.get(i).slope;
                }

                Iterator<Float> sit = sequence.iterator();
                while (sit.hasNext())
                {
                    int n1 = (int) Math.ceil(sit.next());
                    if (sit.hasNext())
                    {
                        int n2 = (int) Math.floor(sit.next());
                        for (int j = n1; j < n2 + 1; j++)
                        {
                            if (!Scanlines.surface(y, j))
                            {
                                g2D.fill(new Dot(j, y));
                            }
                        }
                    }
                }
                y--;
            }
        }
    }
    
    
    

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2D = (Graphics2D) g;
        // calculate the number of cells to be shown
        width = (getWidth() / span - 1) / 2 * 2;
        height = (getHeight() / span - 1) / 2 * 2;
        xoff = getWidth() / 2 - span * width / 2;
        yoff = getHeight() / 2 + span * height / 2;
        drawGrid(g2D);
        g2D.setColor(Color.black);
        for (int i = 0; i < vertices.size(); i++)
        {
            Dot existing = vertices.get(i);
            Dot next = vertices.get((i + 1) % vertices.size());
            Dot c = new Dot(existing.toCoord());
            Dot n = new Dot(next.toCoord());
            g2D.draw(new Line2D.Float(c.toCoord(), n.toCoord()));
        }

        if (FillPoly)
        {
            FigureDraw(g2D);              
        }
        if (LinePoly)
        {
            Node_Connect(g2D);
        }

        g2D.setColor(Color.orange);
        vertices.stream().forEach((v)
                -> 
                {
                    g2D.fill(new Dot(v.toCoord()));
        });
    }

    
    
    
    // respond to mouse click
    class ClickListener extends MouseAdapter
    {

        @Override
        public void mouseClicked(MouseEvent e)
        {
            vertices.add(new Dot(e.getPoint()));
            repaint();
        }
    }
}