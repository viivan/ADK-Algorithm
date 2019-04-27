package cluster;

import java.util.ArrayList;

public class FCluster 
{
	public ArrayList<FClusterService> services=new ArrayList<FClusterService>();
	public int center;
	private double wholesimilarity(double [][]m,FClusterService service)
	{
		double sum=0;
		for(FClusterService cs:services)
		{
			if(cs.num!=service.num)
				sum+=m[cs.num][service.num];
		}
		//System.out.println("s"+service.num+" "+sum);
		return sum;
	}
	public void setcenter(double m[][])
	{
		double max=0;
		for(FClusterService cs:services)
		{
			double now=wholesimilarity(m,cs);
			if(max<now)
			{
				max=now;
				center=cs.num;			
			}
		}
	}
	public void print()
	{
		for(FClusterService cs:services)
				cs.service.print();
		System.out.println();
	}
}
