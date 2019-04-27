package test;
import java.util.ArrayList;

import cluster.Cluster;
import elements.Point;
import elements.testService;

public class correctrate 
{
	static boolean contain(String[] arr,String temple)
	{
		for(int i=0;i<arr.length;i++)
		{
			if(arr[i]!=null)
				if(arr[i].equals(temple))
					return true;
		}
		return false;
	}
	
	public static double calcorrect(ArrayList<Cluster> clusters,int size)
	{
		String[] types={"Mapping","Social","search","eCommerce","Photos"};
		int [][]sum=new int[5][5];
		String[] typeforc=new String[5];
		double rate=0;
		for(int i=0;i<clusters.size();i++)
		{
			Cluster c=clusters.get(i);
			for(int j=0;j<c.services.size();j++)
			{
				String info=c.services.get(j).service.getName();
				String type=info.substring(info.lastIndexOf(" ")+1);
				//System.out.println(info);
				//System.out.println(type);
				for(int p=0;p<5;p++)
				{
					if(type.equals(types[p]))
						sum[i][p]++;
				}
			}
		}
		/*System.out.println();
		for(int i=0;i<5;i++)
		{
			for(int j=0;j<5;j++)
			{
				System.out.print(sum[i][j]+" ");
			}
			System.out.println();
		}*/
		
		
		for(int j=4;j>=0;j--)
		{
			int max=0;
			int index=0;
			for(int i=0;i<5;i++)
				if(sum[i][j]>max)
				{
					max=sum[i][j];
					index=i;
				}
			typeforc[index]=types[j];
		}
		
		
		for(int i=0;i<5;i++)
		{
			if(typeforc[i]==null)
			{	
				int max=0;
				int index=0;
				for(int j=0;j<5;j++)
				{					
					if(!contain(typeforc,types[j]))
					{
						if(sum[i][j]>max)
						{
							index=j;
							max=sum[i][j];
						}
						
					}
				}
				typeforc[i]=types[index];
			}
		}
		
		
		double correctsum=0;
		for(int i=0;i<clusters.size();i++)
		{
			Cluster c=clusters.get(i);
			for(int j=0;j<c.services.size();j++)
			{
				String info=c.services.get(j).service.getName();
				String type=info.substring(info.lastIndexOf(" ")+1);			
				if(type.equals(typeforc[i]))
					correctsum++;
			}
		}
		rate=correctsum/size;
		return rate;
	}
}
