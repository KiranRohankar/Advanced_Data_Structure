package ads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class mst {
	int edges,vertices;
	int [][] adjecancy;
	int[][] cost;
	
	public mst() {
		
		try
		{
			File file = new File("C:\\Users\\kiran\\workspace\\Ads_Project\\input.txt");
			FileReader fr= new FileReader(file);
			//System.out.println(file.exists());
		
		BufferedReader br= new BufferedReader(fr);
		
		
		String line= br.readLine();
		if(line == null)
		{
			System.out.println("File is empty!!!");
			System.exit(0);
		}
		else
		{
			String [] parts= line.split(" ");
			vertices = Integer.parseInt(parts[0]);
			edges = Integer.parseInt(parts[1]);
			
		}
		line=br.readLine();
		cost = new int [vertices][vertices];
		adjecancy = new int [vertices][vertices];
		
		while(line !=null)
		{
			
			
			String [] parts= line.split(" ");
			cost[Integer.parseInt(parts[0])][Integer.parseInt(parts[1])]=Integer.parseInt(parts[2]);
			cost[Integer.parseInt(parts[1])][Integer.parseInt(parts[0])]=Integer.parseInt(parts[2]);
			adjecancy[Integer.parseInt(parts[0])][Integer.parseInt(parts[1])]=1;
			adjecancy[Integer.parseInt(parts[1])][Integer.parseInt(parts[0])]=1;
		
			line=br.readLine();
		}
		for(int i=0;i<vertices;i++)
		{
			System.out.println();
			for(int j=0;j<vertices;j++)
			{
				if(cost[i][j]==0)
				{
					cost[i][j]=-1;
				}
				System.out.print("  "+cost[i][j]);
			}
		}
		System.out.println("\n\n\n");
		for(int i=0;i<vertices;i++)
		{
			System.out.println();
			for(int j=0;j<vertices;j++)
			{
				if(adjecancy[i][j]==0)
				{
					adjecancy[i][j]=-1;
				}
			//	System.out.print("  "+adjecancy[i][j]);
			}
		}
		br.close();
		prims(cost,adjecancy, vertices, edges);
		
		}
		catch(Exception e)  
		{
			//System.out.println("File not Found:  "+e.toString());
		}
		
		

	
	}

	public static void main(String[] args) {
				new mst();

	}
	public void prims(int[][] cost,int adj[][] ,int ver,int edge)
	{
		int [][] ai=new int [ver][ver];
		int [] mincost=new int [ver];
		int min=99999999;
		int i=0,j=0,pos_i=0,pos_j=0;
		int no_of_ticks=0;
		int counter=0;
		int flag=0;
		int [] tick= new int [ver];
		int final_cost=0;
		for(i=0;i<ver;i++)
		{
			tick[i]=-1;
		}
		for( i=0;i<ver;i++)
		{
			for( j=0;j<ver;j++)
			{
				if(cost[i][j]!=-1 && cost[i][j]<min)
				{
					min=cost[i][j];
					pos_i=i;
					pos_j=j;
					
				}
			}
		}
		mincost[counter]=min;
		counter++;
		ai[pos_i][pos_j]=1;
		cost[pos_i][pos_j]=-1;
		cost[pos_j][pos_i]=-1;
		tick[pos_i]=pos_i;
		tick[pos_j]=pos_j;
		while(ver!=no_of_ticks)
		{
		min=999999;
		no_of_ticks=0;flag=0;
		for(i=0;i<tick.length;i++)
		{
			if(tick[i]!=-1)
			{
			for(j=0;j<ver;j++)
			{
				if((cost[j][i]<min) && (cost[j][i]!=-1))
				{
					min=cost[j][i];
					pos_i=i;
					pos_j=j;
				}
				
			}
			}
			
		}
		for(int z=0;z<tick.length;z++)
		{
			if(tick[z]==pos_i)
			{
				flag+=1;
			}
			if(tick[z]==pos_j)
			{
				flag+=1;
			}
		}
		if(flag!=2)
		{
		mincost[counter]=min;
		counter++;
		
		cost[pos_i][pos_j]=-1;
		cost[pos_j][pos_i]=-1;
		tick[pos_i]=pos_i;
		tick[pos_j]=pos_j;
		}
		else
		{
			cost[pos_i][pos_j]=-1;
			cost[pos_j][pos_i]=-1;
			continue;
		}
		for(int k = 0;k<tick.length;k++)
		{
			if(tick[k]!=-1)
			{
				no_of_ticks+=1;
			}
		}

		}
		
		
		
/*	while(ver!=no_of_ticks)
	{
		flag=0;
		no_of_ticks=0;
		min=99999999;
		for( i=0;i<ver;i++)
		{
			for( j=0;j<ver;j++)
			{
				if(cost[i][j]!=-1 && cost[i][j]<min)
				{
					min=cost[i][j];
					pos_i=i;
					pos_j=j;
					
				}
			}
		}
		
		
	
	mincost[counter]=min;
	counter++;
	ai[pos_i][pos_j]=1;
	cost[pos_i][pos_j]=-1;
	cost[pos_j][pos_i]=-1;
	adj[pos_i][pos_j]=0;
	for(int k=0;k<tick.length;k++)
	{
		if(tick[k]==pos_i)
		{
			flag+=1;
		}
		if(tick[k]==pos_j)
		{
			flag+=1;
		}
	}
	if(flag!=2)
	{
	tick[pos_i]=pos_i;
	tick[pos_j]=pos_j;
	}
	for(i=0;i<ver;i++)
	{
		if(tick[i]==0)
		{
			no_of_ticks+=1;
		}
	}
	}*/
	for(i=0;i<counter;i++)
	{
	final_cost+=mincost[i];
	}
	System.out.println("Final cost of minimum spanning tree is:"+final_cost);
		
}
	

}
