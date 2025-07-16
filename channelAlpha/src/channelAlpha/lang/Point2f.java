package channelAlpha.lang;

/*****************************************************************************************************
 * Simple class for representing a point in a 2 dimensional space with 2 floats.
 *  "amplitude" means "Math.sqrt(x*x+y*y)",
 *  "minus" means "-x, -y", "inverse" means "1.f/x, 1.f/y",
 *  "mirrorto" gets symmetry, "mirrored" generates a new point which is symmetric,
 *  "increase" increases current point, "sumwith" make a new point which is the sum,
 * 	"amplify" multiplies the current point, "multiply" returns a new point which is multiplication.
 *****************************************************************************************************/
public class Point2f {
    public int x, y;
    public Point2f() { x = 0; y = 0; }
    public Point2f(double x, double y) { this.x = (int)x;this.y = (int)y; }
    public Point2f(Point2f o) { x = o.x; y = o.y; }
    public Point2f(double o) { x = (int)o; y = (int)o; }
    public Point2f(Point2i o) { x = (int)o.x; y = (int)o.y; }
    public Point2f(Point2d o) { x = (int)o.x; y = (int)o.y; }
	public double getAmplitude() { return Math.sqrt(x*x+y*y); }
    public Point2f setAmplitude(double amplitude) { amplify(amplitude/getAmplitude()); return this; }
    public Point2f disAmplitude() { double a = getAmplitude(); x/=a; y/=a; return this; }
    public Point2f minus() { return new Point2f(-x,-y); }
    public Point2f inverse() { return new Point2f(1/x,1/y); }
    public Point2i toFloat() { return new Point2i(this); }
    public Point2f absolute() { x = x<0.f ? -x:x; y = y<0.f ? -y:y; return this; }
    @Override public String toString() { return x+"|"+y; }
    @Override public boolean equals(Object other) { return x ==((Point2f)other).x && y ==((Point2f)other).y; }
    @Override protected Object clone() { return new Point2f(this); }
    
    public Point2f mirrorto(Point2f o) { x = o.x*2-x; y = o.y*2-y; return this; }
    public Point2f mirrorto(double ox, double oy) { x = (int)ox*2-x; y = (int)oy*2-y; return this; }
    public Point2f mirroredto(Point2f o) { return new Point2f(o.x*2-x,o.y*2-y); }
    public Point2f mirroredto(double ox, double oy) { return new Point2f(ox*2-x,oy*2-y); }
    public double distanceto(Point2f o) { int dx = x-o.x, dy = y-o.y; return Math.sqrt(dx*dx+dy*dy); }
    public double distanceto(double ox, double oy) { int dx = (int)(x-ox), dy = (int)(y-oy); return Math.sqrt(dx*dx+dy*dy); }
    
	public Point2f copy(Point2i o) { x=(int)o.x;y=(int)o.y; return this; }
	public Point2f copy(Point2f o) { x=o.x;y=o.y; return this; }
	public Point2f copy(Point2d o) { x=(int)o.x;y=(int)o.y; return this; }
    public Point2f copy(double ox, double oy) { x = (int)ox; y = (int)oy; return this; }
    public Point2f copy(double o) { x = (int)o; y = (int)o; return this; }
    
    public Point2f increase(Point2f o) { x+=o.x; y+=o.y; return this; }
    public Point2f sumwith(Point2f o) { return new Point2f(x+o.x,y+o.y); }
    public Point2f amplify(Point2f o) { x*=o.x; y*=o.y; return this; }
    public Point2f multiply(Point2f o) { return new Point2f(x*o.x,y*o.y); }
    public Point2f increase(double ox, double oy) { x+=ox; y+=oy; return this; }
    public Point2f sumwith(double ox, double oy) { return new Point2f(x+ox,y+oy); }
    public Point2f amplify(double ox, double oy) { x*=ox; y*=oy; return this; }
    public Point2f multiply(double ox, double oy) { return new Point2f(x*ox,y*oy); }
    public Point2f increase(double o) { x+=o; y+=o; return this; }
    public Point2f sumwith(double o) { return new Point2f(x+o,y+o); }
    public Point2f amplify(double o) { x*=o; y*=o; return this; }
    public Point2f multiply(double o) { return new Point2f(x*o,y*o); }

}
