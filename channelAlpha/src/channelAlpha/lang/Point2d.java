package channelAlpha.lang;

/*****************************************************************************************************
 * Simple class for representing a point in a 2 dimensional space with 2 doubles.
 *  "amplitude" means "Math.sqrt(x*x+y*y)",
 *  "minus" means "-x, -y", "inverse" means "1.f/x, 1.f/y",
 *  "mirrorto" gets symmetry, "mirrored" generates a new point which is symmetric,
 *  "increase" increases current point, "sumwith" make a new point which is the sum,
 * 	"amplify" multiplies the current point, "multiply" returns a new point which is multiplication.
 *****************************************************************************************************/
public class Point2d {
    public int x, y;
    public Point2d() { x = 0; y = 0; }
    public Point2d(double x, double y) { this.x = (int)x;this.y = (int)y; }
    public Point2d(Point2d o) { x = o.x; y = o.y; }
    public Point2d(double o) { x = (int)o; y = (int)o; }
    public Point2d(Point2i o) { x = (int)o.x; y = (int)o.y; }
    public Point2d(Point2f o) { x = (int)o.x; y = (int)o.y; }
	public double getAmplitude() { return Math.sqrt(x*x+y*y); }
    public Point2d setAmplitude(double amplitude) { amplify(amplitude/getAmplitude()); return this; }
    public Point2d disAmplitude() { double a = getAmplitude(); x/=a; y/=a; return this; }
    public Point2d minus() { return new Point2d(-x,-y); }
    public Point2d inverse() { return new Point2d(1/x,1/y); }
    public Point2i toFloat() { return new Point2i(this); }
    public Point2d absolute() { x = x<0.f ? -x:x; y = y<0.f ? -y:y; return this; }
    @Override public String toString() { return x+"|"+y; }
    @Override public boolean equals(Object other) { return x ==((Point2d)other).x && y ==((Point2d)other).y; }
    @Override protected Object clone() { return new Point2d(this); }
    
    public Point2d mirrorto(Point2d o) { x = o.x*2-x; y = o.y*2-y; return this; }
    public Point2d mirrorto(double ox, double oy) { x = (int)ox*2-x; y = (int)oy*2-y; return this; }
    public Point2d mirroredto(Point2d o) { return new Point2d(o.x*2-x,o.y*2-y); }
    public Point2d mirroredto(double ox, double oy) { return new Point2d(ox*2-x,oy*2-y); }
    public double distanceto(Point2d o) { int dx = x-o.x, dy = y-o.y; return Math.sqrt(dx*dx+dy*dy); }
    public double distanceto(double ox, double oy) { int dx = (int)(x-ox), dy = (int)(y-oy); return Math.sqrt(dx*dx+dy*dy); }
    
	public Point2d copy(Point2i o) { x=(int)o.x;y=(int)o.y; return this; }
	public Point2d copy(Point2d o) { x=o.x;y=o.y; return this; }
	public Point2d copy(Point2f o) { x=(int)o.x;y=(int)o.y; return this; }
    public Point2d copy(double ox, double oy) { x = (int)ox; y = (int)oy; return this; }
    public Point2d copy(double o) { x = (int)o; y = (int)o; return this; }
    
    public Point2d increase(Point2d o) { x+=o.x; y+=o.y; return this; }
    public Point2d sumwith(Point2d o) { return new Point2d(x+o.x,y+o.y); }
    public Point2d amplify(Point2d o) { x*=o.x; y*=o.y; return this; }
    public Point2d multiply(Point2d o) { return new Point2d(x*o.x,y*o.y); }
    public Point2d increase(double ox, double oy) { x+=ox; y+=oy; return this; }
    public Point2d sumwith(double ox, double oy) { return new Point2d(x+ox,y+oy); }
    public Point2d amplify(double ox, double oy) { x*=ox; y*=oy; return this; }
    public Point2d multiply(double ox, double oy) { return new Point2d(x*ox,y*oy); }
    public Point2d increase(double o) { x+=o; y+=o; return this; }
    public Point2d sumwith(double o) { return new Point2d(x+o,y+o); }
    public Point2d amplify(double o) { x*=o; y*=o; return this; }
    public Point2d multiply(double o) { return new Point2d(x*o,y*o); }

}
