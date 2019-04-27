package agnes_keans;

import java.util.ArrayList;
import java.util.Random;

import cluster.Cluster;
import cluster.ClusterService;
import elements.Point;

public class Agnes_Kmeans 
{
	int a[]=new int [2];//相似度最小的两个集群序号
	public int k=200;//kmeans聚类数
	double kline=1.0;//kmeans阈值
	double aline=50;//Agnes阈值
	int center[];//聚类中心
	double R=0.6;//密度范围
	
	private int[] density;//密度矩阵 
	private double [][]m;//相似度矩阵
	private double [][]b;//聚类间相似度矩阵
	private ArrayList<Point> service=new ArrayList<Point>();
	private ArrayList<ClusterService> services=new ArrayList<ClusterService>();//聚类服务对象
	private ArrayList<Cluster> clusters=new ArrayList<Cluster>();//存放聚类
	
	public void setServices(ArrayList<Point> service)
	{
		this.service=service;	
	}
	public ArrayList<Cluster> getClusters()
	{
		return clusters;
	}
	public void setR(double R)
	{
		this.R=R;
	}
	
	//外部设置相似度矩阵
	public void setMatrix(double[][] m)
	{
		this.m=m;
	}
	
	//初始化聚类服务对象
	public void initService()
	{
		for(int i=0;i<service.size();i++)
		{
			ClusterService cs=new ClusterService();
			cs.setService(service.get(i));
			cs.num=i;
			cs.type=0;
			services.add(cs);
		}
	}
		
	//计算相似度矩阵
	void initMatrix()
	{
		m=new double[services.size()][services.size()];
		for(int i=0;i<services.size();i++)
			for(int j=0;j<services.size();j++)
			{
				if(i!=j)
					m[i][j]=services.get(i).getSimilarity(services.get(j));
				else
					m[i][j]=1;
			}
	}
	boolean contain(int[] r,int i)
	{
		for(int j=0;j<i;j++)
		{
			if(r[j]==r[i])
				return true;
		}
		return false;
	}
	
	//随机设置中心点
	public int[] randomcenter()
	{
		int[] r=new int[k];
		Random rm=new Random();
		for(int i=0;i<k;i++)
		{
			do
				r[i]=rm.nextInt(services.size());
			while(contain(r,i));
		}
		return r;
	}
	
	//集群平均相似度大小
	double value()
	{
		double sum=0;
		for(int i=0;i<services.size();i++)
		{
			if(!iscenter(i))
			{
				sum+=m[i][center[services.get(i).type]];
			}
		}
		sum/=(services.size()-k);
		return sum;
	}
	
	//计算总相似度
	double account(int r[])
	{
		double a=0;
		for(int i=0;i<services.size();i++)
		{
			if(!iscenter(i))
			{
				double max=0;
				for(int j=0;j<k;j++)
				{
					if(m[i][r[j]]>max)
						max=m[i][r[j]];
				}
				a+=max;
			}
		}
		return a;
	}
	boolean iscenter(int num)
	{
		for(int i=0;i<k;i++)
		{
			if(num==center[i])
				return true;
		}
		return false;
	}
	
	
	public void Simulate_Anneal()
	{
		center=randomcenter();
		double oldaccount=account(center);
		int r[];
		double newaccount=0;
		
		int t=1000;
		while(t>=1)
		{
			t--;
			r=randomcenter();
			newaccount=account(r);
			//System.out.println("sa:"+newaccount);
			if(newaccount>=oldaccount)
			{
				oldaccount=newaccount;
				center=r;
			}
			else
			{
				
				double rate=Math.exp((newaccount-oldaccount)/t);
				double rrate=Math.random();
				if(rrate<=rate)
				{
					oldaccount=newaccount;
					center=r;
				}
			}
		}	
		
		/*for(int p=0;p<center.length;p++)
			System.out.print(center[p]+" ");
		System.out.println();*/
	}
	
	//计算密度构筑中心点
	public void initDensity()
	{
		center=new int[k];
		density=new int[services.size()];
		for(int i=0;i<services.size();i++)
		{
			density[i]=0;
			for(int j=0;j<services.size();j++)
			{
				//密度范围内
				if(i!=j&&m[i][j]>=R)
					density[i]++;
			}
		}
				
		//选出密度最大的k个对象作为中心点
		for(int i=0;i<k;i++)
		{
			int max=0;
			for(int j=0;j<density.length;j++)
			{
				boolean in=false;
				for(int p=0;p<i;p++)
				{
					if(j==center[p])
						{
							in=true;
							break;
						}
				}
				if(density[j]>=max&&!in)
				{
					max=density[j];
					center[i]=j;
				}
			}
		}
	}
	
	void kmeanstep()
	{
		//重置中心点
		//替换各集群中对象
		for(int i=0;i<k;i++)
		{
			clusters.get(i).setcenter(m);
			center[i]=clusters.get(i).center;
		}
		for(int i=0;i<services.size();i++)
		{
			if(!iscenter(i))
			{
				ClusterService temple=services.get(i);
				int formertype=temple.type;
				double sim=0;
				for(int j=0;j<k;j++)
				{
					if(m[i][center[j]]>sim)
					{
						temple.type=j;
						sim=m[i][center[j]];
					}
				}
				if(temple.type!=formertype)
				{
					clusters.get(formertype).services.remove(temple);
					clusters.get(temple.type).services.add(temple);
				}
			}
		}
	}
	
	//kmeans主方法
	public void kmeans()
	{		
		for(int i=0;i<k;i++)//初始化集群
		{
			Cluster temple=new Cluster();
			temple.services.add(services.get(center[i]));
			services.get(center[i]).type=i;
			temple.center=center[i];
			clusters.add(temple);
		}
		
		//找到中心和自身相似度最高的集群并插入
		for(int i=0;i<services.size();i++)
		{	
			if(!iscenter(i))
			{
				ClusterService temple=services.get(i);
				double sim=0;
				for(int j=0;j<k;j++)
				{
					if(m[i][center[j]]>sim)
					{
						temple.type=j;
						sim=m[i][center[j]];
					}
				}
				clusters.get(temple.type).services.add(temple);
			}
		}
		double oldvalue=0;
		double newvalue =value();

		while((newvalue<kline)&&(oldvalue!=newvalue))
		{
			kmeanstep();
			oldvalue=newvalue;
			newvalue=value();		
		}
	}
	double distence_between_cluster(Cluster c1,Cluster c2)//计算两集群之间的平均相似度
	{
		double sum=0;
		for(int i=0;i<c1.services.size();i++)
			for(int j=0;j<c2.services.size();j++)
			{
				int s1=c1.services.get(i).num;
				int s2=c2.services.get(j).num;
				sum+=m[s1][s2];
			}
		sum=sum/(c1.services.size()*c2.services.size());
		return sum;
	}
	void initb()//集群间相似度矩阵
	{
		for(int i=0;i<clusters.size();i++)
			for(int j=0;j<clusters.size();j++)
			{
				if(i!=j)
					b[i][j]=distence_between_cluster(clusters.get(i),clusters.get(j));
				else
					b[i][j]=0;
			}
	}
	double max_similarity_between(int a[])//找到相似度最大的两个集群
	{
		double sim=0;
		for(int i=0;i<clusters.size();i++)
			for(int j=0;j<clusters.size();j++)
			{
				if(b[i][j]>=sim)
				{
					sim=b[i][j];
					a[0]=i;
					a[1]=j;
				}
			}
		return sim;
	}
	public void agnescluster()
	{
		for(int i=0;i<services.size();i++)
		{
			Cluster c=new Cluster();
			c.services.add(services.get(i));
			c.center=services.get(i).num;
			clusters.add(c);
		}
	}
	//层次聚类
	public void agnes()
	{
		b=new double [clusters.size()][clusters.size()];
		initb();//因为之后只会减少所以可以确定矩阵大小
		System.out.println(max_similarity_between(a)+" avalue");
		while(clusters.size()>5)//聚类数到5停止
		{
			System.out.println(clusters.size());
			//此时a中存放两个相似度最高的集群序号
			//合并,a[1]放a[0]
			//删除,初始化矩阵
			max_similarity_between(a);
			//System.out.println(a[0]+"  "+a[1]);
			Cluster c1=clusters.get(a[0]);
			Cluster c2=clusters.get(a[1]);
			for(ClusterService cs:c2.services)
				c1.services.add(cs);
			clusters.remove(c2);
			initb();//重新计算集群间平均相似度矩阵
		}
	}
	public void start()
	{
		initService();//初始化服务
		initMatrix();//初始化矩阵
		if(services.size()>k)//kmeans预处理
			kmeans();
		agnes();
	}
	public void print()
	{
		for(int i=0;i<clusters.size();i++)
		{
			
			System.out.println("type "+i+"num "+clusters.get(i).services.size()+"-----------------------------------------------------------");
			clusters.get(i).print();
		}
	}
}
