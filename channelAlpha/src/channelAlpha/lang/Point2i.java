package channelAlpha.lang;

/*****************************************************************************************************
 * Simple class for representing a point in a 2 dimensional space with 2 ints.
 *  "amplitude" means "Math.sqrt(x*x+y*y)",
 *  "minus" means "-x, -y", "inverse" means "1.f/x, 1.f/y",
 *  "mirrorto" gets symmetry, "mirrored" generates a new point which is symmetric,
 *  "increase" increases current point, "sumwith" make a new point which is the sum,
 * 	"amplify" multiplies the current point, "multiply" returns a new point which is multiplication.
 *****************************************************************************************************/
public class Point2i {
    public int x, y;
    public Point2i() { x = 0; y = 0; }
    public Point2i(double x, double y) { this.x = (int)x;this.y = (int)y; }
    public Point2i(Point2i o) { x = o.x; y = o.y; }
    public Point2i(double o) { x = (int)o; y = (int)o; }
    public Point2i(Point2f o) { x = (int)o.x; y = (int)o.y; }
    public Point2i(Point2d o) { x = (int)o.x; y = (int)o.y; }
	public double getAmplitude() { return Math.sqrt(x*x+y*y); }
    public Point2i setAmplitude(double amplitude) { amplify(amplitude/getAmplitude()); return this; }
    public Point2i disAmplitude() { double a = getAmplitude(); x/=a; y/=a; return this; }
    public Point2i minus() { return new Point2i(-x,-y); }
    public Point2i inverse() { return new Point2i(1/x,1/y); }
    public Point2f toFloat() { return new Point2f(this); }
    public Point2i absolute() { x = x<0.f ? -x:x; y = y<0.f ? -y:y; return this; }
    @Override public String toString() { return x+"|"+y; }
    @Override public boolean equals(Object other) { return x ==((Point2i)other).x && y ==((Point2i)other).y; }
    @Override protected Object clone() { return new Point2i(this); }
    
    public Point2i mirrorto(Point2i o) { x = o.x*2-x; y = o.y*2-y; return this; }
    public Point2i mirrorto(double ox, double oy) { x = (int)ox*2-x; y = (int)oy*2-y; return this; }
    public Point2i mirroredto(Point2i o) { return new Point2i(o.x*2-x,o.y*2-y); }
    public Point2i mirroredto(double ox, double oy) { return new Point2i(ox*2-x,oy*2-y); }
    public double distanceto(Point2i o) { int dx = x-o.x, dy = y-o.y; return Math.sqrt(dx*dx+dy*dy); }
    public double distanceto(double ox, double oy) { int dx = (int)(x-ox), dy = (int)(y-oy); return Math.sqrt(dx*dx+dy*dy); }
    
	public Point2i copy(Point2f o) { x=(int)o.x;y=(int)o.y; return this; }
	public Point2i copy(Point2i o) { x=o.x;y=o.y; return this; }
	public Point2i copy(Point2d o) { x=(int)o.x;y=(int)o.y; return this; }
    public Point2i copy(double ox, double oy) { x = (int)ox; y = (int)oy; return this; }
    public Point2i copy(double o) { x = (int)o; y = (int)o; return this; }
    
    public Point2i increase(Point2i o) { x+=o.x; y+=o.y; return this; }
    public Point2i sumwith(Point2i o) { return new Point2i(x+o.x,y+o.y); }
    public Point2i amplify(Point2i o) { x*=o.x; y*=o.y; return this; }
    public Point2i multiply(Point2i o) { return new Point2i(x*o.x,y*o.y); }
    public Point2i increase(double ox, double oy) { x+=ox; y+=oy; return this; }
    public Point2i sumwith(double ox, double oy) { return new Point2i(x+ox,y+oy); }
    public Point2i amplify(double ox, double oy) { x*=ox; y*=oy; return this; }
    public Point2i multiply(double ox, double oy) { return new Point2i(x*ox,y*oy); }
    public Point2i increase(double o) { x+=o; y+=o; return this; }
    public Point2i sumwith(double o) { return new Point2i(x+o,y+o); }
    public Point2i amplify(double o) { x*=o; y*=o; return this; }
    public Point2i multiply(double o) { return new Point2i(x*o,y*o); }

}
