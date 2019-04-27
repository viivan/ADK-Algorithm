package elements;

public class Point 
{
	private double x=0;
	private double y=0;
	public Point(){};
	public Point(double x,double y)
	{
		this.x=x;
		this.y=y;
	}
	public double getSimilarity(Point p)
	{
		double distence=(x-p.x)*(x-p.x)+(y-p.y)*(y-p.y);
		return 1/distence;
	}
	public void print()
	{
		System.out.println("("+x+","+y+")");
	}
	public String getName(){return " ";};
}
