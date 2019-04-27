package test;

import java.util.ArrayList;
import java.util.List;

import agnes_keans.Agnes_Kmeans;
import cluster.Cluster;
import elements.Point;
import elements.testService;
import fileread.FileAna;

public class filetest {
	public static void main(String[] args)
	{
		int size=2048;
		FileAna fa=new FileAna();
		String path="simmatrix.txt";
		String filepath="mashup.xlsx";
		fa.initM(path, size);//读取矩阵信息
		fa.initNames(filepath);//读取服务信息
		List<String> names=fa.getNames();
		ArrayList<Point> services=new ArrayList<Point>();
		
		for(int i=0;i<size;i++)
			{
				services.add(new testService(names.get(i)));
			}
		
		double sumrate=0;
		for(double round=0.1;round<1;round+=0.05)
		{
			Agnes_Kmeans ak=new Agnes_Kmeans();
			ak.k=5;
			ak.setR(round);//设置密度选取范围
			ak.setServices(services);
			ak.initService();//初始化服务
			ak.setMatrix(fa.getM());
			
			
			if(services.size()>ak.k)//kmeans预处理
			{
				ak.initDensity();
								
				ak.kmeans();
			}
			
			//ak.agnes();
			//ak.print();
			
			ArrayList<Cluster> clusters=ak.getClusters();		
			
			//计算准确率
			double rate=correctrate.calcorrect(clusters, size);
			sumrate+=rate;
			System.out.println("密度选取范围为 "+round);
			System.out.println("rate:"+rate);
		}
		
		//System.out.println("avg:"+sumrate/((1-0.1)/0.05-1));
	}
}
