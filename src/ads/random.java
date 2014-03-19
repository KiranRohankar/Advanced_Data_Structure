package ads;

import java.util.Random;
import java.util.Stack;

public class random {
	int edges,vertices;
	int [][] adjecancy;
	int[][] cost;
	
	public random(int ver,int edge_number) {
	vertices=ver;
	edges=edge_number;
	adjecancy= new int [ver][ver];
	cost= new int [ver][ver];
	for(int i=0;i<vertices;i++)
	{
		for(int j=0;j<vertices;j++)
		{
			cost[i][j]=-1;	
		}
		
	}
		
	}
	public void create_Graph()
	{
		int edge_count=0;
		
	//	if(vertices==0)
		{
		
			System.out.println(edges);
			while((!depthFirstSearch(cost, vertices)) || edges!=edge_count)
			{
				System.out.println("....");
				edge_count=0;
			Random genrateRandom= new Random();
			Random costgenrator= new Random();
			int vertex1= genrateRandom.nextInt(vertices);
			int vertex2=genrateRandom.nextInt(vertices);
			
		
			int current_cost= (costgenrator.nextInt(1000))+1;
			cost[vertex1][vertex2]=current_cost; 
			cost[vertex2][vertex1]=current_cost;
			
			for(int i=0;i<vertices;i++)
			{
				cost[i][i]=-1;
			}
			for(int i=0;i<vertices;i++)
			{
				for(int j=0;j<vertices;j++)
				{
					if(cost[i][j]!=-1)
					{
						edge_count=edge_count+1;
						
						
					}
				}
			}
			edge_count=edge_count/2;
		//	System.out.println("Edge Count is :"+edge_count);
	
			}
			
			
			
			
		}
		/*else
		{
			System.out.println("Vertices should be greater than 0");
			System.exit(0);
		}*/
	}
	public void print_Graph()
	{
		int i,j;
		for(i=0;i<vertices;i++)
		{
			for(j=0;j<vertices;j++)
			{
				if(cost[i][j]!=-1)
				{
					System.out.println(" "+i+" --->  "+j+" cost is:"+cost[i][j]);
				}
			}
		}
	}
	
	public boolean depthFirstSearch(int [][] adj,int ver)
	{
		Stack<Integer> stack= new Stack<Integer>();
		int [] visited= new int[ver];
		int i,j,k;
		for(i=0;i<ver;i++)
		{
			visited[i]=-1;
		}
		stack.push(0);
		while(!stack.isEmpty())
		{
			int u=stack.pop();
			if(visited[u]==-1)
			{
				visited[u]=1;
			}
			for(i=0;i<ver;i++)
			{
				if(adj[u][i]!=-1 && visited[i]==-1)
				{
					stack.push(i);
				}
			}
		}
		int count=0;
		for(i=0;i<ver;i++)
		{
			if(visited[i]!=-1)
			{
				count++;
			}
		}
			
		if(count==ver)
		{
			//System.out.println("Graph is connected!!!");
			return true;
		}
		else
		{
		//	System.out.println("Graph is not connected to "+(ver-count)+"Nodes");
			return false;
		}
	}
	public void prims(int[][] cost,int ver)
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
		
		
		
	for(i=0;i<counter;i++)
	{
	final_cost+=mincost[i];
	}
	System.out.println("Final cost of minimum spanning tree is:"+final_cost);
		
}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int vertex=Integer.parseInt(args[0]);
		int density=Integer.parseInt(args[1]);
		int max_edges=(vertex*(vertex-1))/2;
		float divide_density=density/100f;
		double possible= Math.ceil((max_edges)*divide_density);
		System.out.println(max_edges+"  "+possible);
		if(possible<vertex-1)
		{
			System.out.println("Provide diff density!!!!");
			System.exit(0);
		}
		int edge_number=(int) possible;
		random rr= new random(vertex,edge_number);
		rr.create_Graph();
		rr.print_Graph();
		rr.prims(rr.cost, rr.vertices);
		
	}

}
