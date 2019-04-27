package elements;

public class testService extends Point
{
	public String name;
	public testService(){}
	public testService(String name)
	{
		this.name=name;
	}
	public void print()
	{
		System.out.println(name);
	}
	public String getName()
	{
		return name;
	}
}
