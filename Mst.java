


import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

/*This class is resposible for all Fibonacci heap operations
 * mainly Inserting elelment in the fibonacci heap,remove minimum element 
 */
class FibonacciHeap {

	private FibonacciHeapNode minNode;
	private int nNodes;
	
	public FibonacciHeap()  //Fibonacci heap default constructor
	{
		
		
	}
	
	public boolean isEmpty() //Returns null if heap is empty
	{
		return minNode==null;
	}
	public void clear()      //Resets the heap  
	{
		minNode=null;
		nNodes=0;
	}
	
	//Decalration to keep the fibonacci heap property
	 private static final double oneOverLogPhi =
		        1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);

	 //insert functions inserts the node in the heap in O(1) time.
	public FibonacciHeapNode insert(FibonacciHeapNode node,double key)
	
	{
		node.key=key;
		if(minNode!=null)
		{
			  node.left = minNode;
	            node.right = minNode.right;
	            minNode.right = node;
	            node.right.left = node;

	            if (key < minNode.key) {
	                minNode = node;
	            }
			
		}
		 else {
	            minNode = node;
	        }

	        nNodes++;
	        return node;
	}
	public FibonacciHeapNode min()  //This function returns Minimum node in the heap.
    {
        return minNode;
    }
	
	  public int size()         //This function returns the current size of the haep
	    {
	        return nNodes;
	    }
	  
	  /*This function is a helper function for consolidate function
	   *  for linking of node y and node x in the heap
	   * */
	  protected void link(FibonacciHeapNode y, FibonacciHeapNode x) 
	    {
	        // remove y from root list of heap
	        y.left.right = y.right;
	        y.right.left = y.left;

	        // make y a child of x
	        y.parent = x;

	        if (x.child == null) {
	            x.child = y;
	            y.right = y;
	            y.left = y;
	        } else {
	            y.left = x.child;
	            y.right = x.child.right;
	            x.child.right = y;
	            y.right.left = y;
	        }

	        // increase degree[x]
	        x.degree++;

	        // set mark[y] false
	        y.mark = false;
	    }
	  
	  /**
	     * Removes the smallest element from the heap. This will cause the trees in
	     * the heap to be consolidated, if necessary.
	     * Running time is O(log n) 
	     *
	     *This function returns  node with the smallest key
	     */
	   public FibonacciHeapNode removeMin()
	    {
	        FibonacciHeapNode z = minNode;

	        if (z != null) {
	            int numKids = z.degree;
	            FibonacciHeapNode x = z.child;
	            FibonacciHeapNode tempRight;

	            // for each child of z do...
	            while (numKids > 0) {
	                tempRight = x.right;

	                // remove x from child list
	                x.left.right = x.right;
	                x.right.left = x.left;

	                // add x to root list of heap
	                x.left = minNode;
	                x.right = minNode.right;
	                minNode.right = x;
	                x.right.left = x;

	                // set parent[x] to null
	                x.parent = null;
	                x = tempRight;
	                numKids--;
	            }

	            // remove z from root list of heap
	            z.left.right = z.right;
	            z.right.left = z.left;

	            if (z == z.right) {
	                minNode = null;
	            } else {
	                minNode = z.right;
	                consolidate();
	            }

	            // decrement size of heap
	            nNodes--;
	        }

	        return z;
	    }


	  
	  protected void consolidate()
	    {
	        int arraySize =
	            ((int) Math.floor(Math.log(nNodes) * oneOverLogPhi)) + 1;

	        ArrayList<FibonacciHeapNode> array =
	            new ArrayList<FibonacciHeapNode>(arraySize);

	        // Initialize degree array
	        for (int i = 0; i < arraySize; i++) {
	            array.add(null);
	        }

	        // Find the number of root nodes.
	        int numRoots = 0;
	        FibonacciHeapNode x = minNode;

	        if (x != null) {
	            numRoots++;
	            x = x.right;

	            while (x != minNode) {
	                numRoots++;
	                x = x.right;
	            }
	        }

	        // For each node in root list do...
	        while (numRoots > 0) {
	            // Access this node's degree..
	            int d = x.degree;
	            FibonacciHeapNode next = x.right;

	            // ..and see if there's another of the same degree.
	            for (;;) {
	                FibonacciHeapNode y = array.get(d);
	                if (y == null) {
	                    // Nope.
	                    break;
	                }

	                // There is, make one of the nodes a child of the other.
	                // Do this based on the key value.
	                if (x.key > y.key) {
	                    FibonacciHeapNode temp = y;
	                    y = x;
	                    x = temp;
	                }

	                // FibonacciHeapNode<T> y disappears from root list.
	                link(y, x);

	                // We've handled this degree, go to next one.
	                array.set(d, null);
	                d++;
	            }

	            // Save this node for later when we might encounter another
	            // of the same degree.
	            array.set(d, x);

	            // Move forward through list.
	            x = next;
	            numRoots--;
	        }

	        // Set min to null (effectively losing the root list) and
	        // reconstruct the root list from the array entries in array[].
	        minNode = null;

	        for (int i = 0; i < arraySize; i++) {
	            FibonacciHeapNode y = array.get(i);
	            if (y == null) {
	                continue;
	            }

	            // We've got a live one, add it to root list.
	            if (minNode != null) {
	                // First remove node from root list.
	                y.left.right = y.right;
	                y.right.left = y.left;

	                // Now add to root list, again.
	                y.left = minNode;
	                y.right = minNode.right;
	                minNode.right = y;
	                y.right.left = y;

	                // Check if this is a new min.
	                if (y.key < minNode.key) {
	                    minNode = y;
	                }
	            } else {
	                minNode = y;
	            }
	        }
	    }

	  /**
	     * Performs a cascading cut operation. This cuts y from its parent and then
	     * does the same for its parent, and so on up the tree.
	     *
	     *Running time is: O(log n);
	     */
	 	  
	 	 protected void cascadingCut(FibonacciHeapNode y)
		    {
		        FibonacciHeapNode z = y.parent;

		        // if there's a parent...
		        if (z != null) {
		            // if y is unmarked, set it marked
		            if (!y.mark) {
		                y.mark = true;
		            } else {
		                // it's marked, cut it from parent
		                cut(y, z);

		                // cut its parent as well
		                cascadingCut(z);
		            }
		        }
		    }
	 	 
	 	 
	 	 /**
	      * Decreases the key value for a heap node, given the new value to take on.
	      * The structure of the heap may be changed and will not be consolidated.
	      *
	      * Running time: O(1) amortized
	      */
	 	   public void decreaseKey(FibonacciHeapNode x, double k)
	 	    {
	 	        if (k > x.key) {
	 	        	return;
	 	         //   throw new IllegalArgumentException(
	 	           //     "decreaseKey() got larger key value");
	 	        }

	 	        x.key = k;

	 	        FibonacciHeapNode y = x.parent;

	 	        if ((y != null) && (x.key < y.key)) {
	 	            cut(x, y);
	 	            cascadingCut(y);
	 	        }

	 	        if (x.key < minNode.key) {
	 	            minNode = x;
	 	        }
	 	    }
	 	  
	 	   /*
	 	     * The reverse of the link operation: removes x from the child list of y.
	 	     * This method assumes that min is non-null.
	 	     *
	 	     * Running time is: O(1)
	 	     */
		 
		   protected void cut(FibonacciHeapNode x, FibonacciHeapNode y)
		    {
		        // remove x from childlist of y and decrement degree[y]
		        x.left.right = x.right;
		        x.right.left = x.left;
		        y.degree--;

		        // reset y.child if necessary
		        if (y.child == x) {
		            y.child = x.right;
		        }

		        if (y.degree == 0) {
		            y.child = null;
		        }

		        // add x to root list of heap
		        x.left = minNode;
		        x.right = minNode.right;
		        minNode.right = x;
		        x.right.left = x;

		        // set parent[x] to nil
		        x.parent = null;

		        // set mark[x] to false
		        x.mark = false;
		    }
		   
		   /*This overloaded function generates a minimum spanning tree from a given graph
		    * This function uses RemoveMin and Insert of the Fibonacci heap class for
		    * calculating MST.
		    * It returns weight of the minimum calculated tree for inputed Random Graph.
		    * 
		    * 
		    * */
			 public void prims_Fhip(Random_Graph graph)
			 {
				 Long start=System.currentTimeMillis();
				 int [] vertex_Array= new int[graph.no_of_vertices];
				 int [] cost= new int[graph.no_of_vertices];
				 for(int i=1;i<cost.length;i++)
				 {
					 cost[i]=0;
				 }
				 int[] final_cost= new int[graph.no_of_vertices];
				 FibonacciHeapNode [] refofHeap= new FibonacciHeapNode[graph.no_of_vertices];
				 ArrayList<Integer> visited= new ArrayList<>();
				 vertex_Array[0]=0;
				 FibonacciHeapNode node= new FibonacciHeapNode(0, 0);
				refofHeap[0]= insert(node, 0);
				 for(int i=1;i<graph.no_of_vertices;i++)
				 {
					 node=new FibonacciHeapNode(i, 99999);
					refofHeap[i]= insert(node, 99999);
				 } 
				 
				 FibonacciHeapNode x= removeMin();
				 visited.add(x.data);
				 final_cost[0]=(int)x.key;
				 for(Edge j=graph.adjecency_list[0].adjList;j!=null;j=j.next)
				 {
					 if(!visited.contains(j.vertex_number))
					 {
						//if(cost[j.vertex_number]>j.cost) 
						{	 
					// cost[j.vertex_number]=j.cost;
					 vertex_Array[j.vertex_number]=x.data;
					
						}
						 decreaseKey(refofHeap[j.vertex_number], j.cost);
					
					 }
				 }
				 
				 
				 for(int i=1;i<graph.no_of_vertices;i++)
				 {
					  x= removeMin();
					 visited.add(x.data);
					 final_cost[i]=(int)x.key;
					 cost[i]=(int)x.key;
					 for(Edge j=graph.adjecency_list[x.data].adjList;j!=null;j=j.next)
					 {
						 if(!visited.contains(j.vertex_number))
						 {
							//if(cost[j.vertex_number]>j.cost) 
							{	 
						// cost[j.vertex_number]=j.cost;
								
						 vertex_Array[j.vertex_number]=x.data;
							}
						 decreaseKey(refofHeap[j.vertex_number], j.cost);
						 }
					 }
				 }
			
				 int sum=0;
				 for(int i=0;i<cost.length;i++)
				 {
					 sum+=cost[i];
				 }
				 long stop= System.currentTimeMillis();
				 System.out.println("Final cost of minimum spanning tree is(Using Fheap):"+sum);
				 System.out.println("Time required for an Execution:   "+(stop-start)+" Miliseconds");
			 }
			 /*This overloaded function generates a minimum spanning tree from a given graph
			    * This function uses RemoveMin and Insert of the Fibonacci heap class for
			    * calculating MST.
			    * It returns weight of the minimum calculated tree for inputed File graph.
			    * 
			    * 
			    * */
		 public void prims_Fhip(Mst graph)
		 {
			 //Decalaration of the local variables
			 Long start=System.currentTimeMillis();
			 int [] vertex_Array= new int[graph.no_of_vertices];
			 int [] cost= new int[graph.no_of_vertices];
			 for(int i=1;i<cost.length;i++)
			 {
				 cost[i]=0;
			 }
			 int[] final_cost= new int[graph.no_of_vertices];
			 FibonacciHeapNode [] refofHeap= new FibonacciHeapNode[graph.no_of_vertices];
			 ArrayList<Integer> visited= new ArrayList<>();
			 vertex_Array[0]=0;
			 FibonacciHeapNode node= new FibonacciHeapNode(0, 0);
			refofHeap[0]= insert(node, 0);       //Inserting 1st node in the heap
			 for(int i=1;i<graph.no_of_vertices;i++)
			 {
				 node=new FibonacciHeapNode(i, 99999);//Inserting remaining nodes in on the heap
				refofHeap[i]= insert(node, 99999);
			 } 
			 
			 FibonacciHeapNode x= removeMin();//Removing minimum element from a tree
			 
			 visited.add(x.data);
			 final_cost[0]=(int)x.key;
			 for(Edge j=graph.adjecency_list[0].adjList;j!=null;j=j.next)
			 {
				 if(!visited.contains(j.vertex_number))
				 {
					 
					{	 
	
				 vertex_Array[j.vertex_number]=x.data;    
				
					}
					 decreaseKey(refofHeap[j.vertex_number], j.cost);
				
				 }
			 }
			 
			 
			 for(int i=1;i<graph.no_of_vertices;i++)
			 {
				  x= removeMin();
				  System.out.println(""+ x.data+"  "+vertex_Array[x.data]);
				 visited.add(x.data);
				 final_cost[i]=(int)x.key;   //Adding weight of the minimum edge in the final cost
				 cost[i]=(int)x.key;
				 for(Edge j=graph.adjecency_list[x.data].adjList;j!=null;j=j.next)
				 {
					 if(!visited.contains(j.vertex_number))
					 {
					 
						{	 
					
							
					 vertex_Array[j.vertex_number]=x.data;
						}
					 decreaseKey(refofHeap[j.vertex_number], j.cost);
					 }
				 }
			 }
			 
			 int sum=0;
			 for(int i=0;i<cost.length;i++)
			 {
				 sum+=cost[i];
			 }
			 long stop= System.currentTimeMillis();
			 System.out.println("Final cost of minimum spanning tree is(Using Fheap):"+sum);  //Printing final cost
			 System.out.println("Time required for an Execution:   "+(stop-start)+" Miliseconds"); //Printing time required
		 }
	
	
	

}

/**
 * Implements a node of the Fibonacci heap. It holds the information necessary
 * for maintaining the structure of the heap. It also holds the reference to the
 * key value (which is used to determine the heap structure).
 *
 */
class FibonacciHeapNode
{

	int data;
	
	FibonacciHeapNode child;
	FibonacciHeapNode left;
	FibonacciHeapNode right;
	FibonacciHeapNode parent;
	boolean mark;
	double key;
	int degree;
	 public  FibonacciHeapNode(int data,double key) {
		 
		 right=this;
		 left=this;
		 this.data= data;
		 this.key= key;
}
	 public final double getKey()
	 {
		 return key;
	 }
	 public final int getData()
	 {
		 return data;
	 }
	 
	
	 

}
/*Implements the vertex of the graph
 * this class holds necessary information for the vertex of the graph*/
class Vertex
{
	
	int number;
	Edge adjList;
	boolean visited;
	public Vertex(int number, Edge adjList, boolean visited) {
		
		this.number = number;
		this.adjList = adjList;
		this.visited = visited;
	}
	public Vertex() {
		
		this.number = 0;
		this.adjList = null;
		this.visited = false;
		
	}
	
}
/*Implements Edges of the graph.It holds the information about the 
 * edges of the graph such as associate vertex,cost of the edge and it serves as a 
 * linked list by keeping link of the next edge
 * */
class Edge
{

	int vertex_number;
	int cost;
	Edge next;
	public Edge(int vertex_number, int cost, Edge next) {
		
		this.vertex_number = vertex_number;
		this.cost = cost;
		this.next = next;
	}
	public Edge() {
		this.vertex_number = 0;
		this.cost = 0;
		this.next = null;
	}
	
	
}
/*
 * This class creates the random graph.It used vertex and edge class for the initializing of graph
 * vertices and edges.
 */
class Random_Graph
{
	int no_of_vertices;
	int no_of_edges;
	Vertex [] adjecency_list;
	int density;
	public Random_Graph(int vertices,int density)
	{
		int max_edges=(vertices*(vertices-1))/2;
		float divide_density=density/100f;
		double possible= Math.ceil((max_edges)*divide_density);
		
		no_of_vertices=vertices;
		no_of_edges= (int)possible;
	//	adjecency_list= new Vertex[no_of_vertices];
	}
	/*
	 * This function creates a random graph.and returns an instance of it*/
	public void createGraph()
	{
		Random generator= new Random();
		Random cost_generator= new Random();
		int vertex1,vertex2,edge_cost;
		int edge_count=0;
		boolean b[][] = new boolean[no_of_vertices][no_of_vertices]; 
		
		adjecency_list= new Vertex[no_of_vertices];
		for(int i=0;i<no_of_vertices;i++)
		{
			adjecency_list[i]=new Vertex(i,null,false);
		}
		vertex1=generator.nextInt(no_of_vertices);
		vertex2= generator.nextInt(no_of_vertices);
		while(vertex1==vertex2)
		{
			vertex2= generator.nextInt(no_of_vertices);
		}
		edge_cost=cost_generator.nextInt(1000)+1;
		if(!b[vertex1][vertex2] && !b[vertex2][vertex1])
		{
		adjecency_list[vertex1].adjList= new Edge(vertex2, edge_cost,adjecency_list[vertex1].adjList);
		adjecency_list[vertex2].adjList= new Edge(vertex1,edge_cost,adjecency_list[vertex2].adjList);
		b[vertex1][vertex2]=true;
		b[vertex2][vertex1]=true;
		edge_count++;
		}
		
		while( edge_count!=no_of_edges || !depthFisrtSearch(this))
		{
			vertex1=generator.nextInt(no_of_vertices);
			vertex2= generator.nextInt(no_of_vertices);
			while(vertex1==vertex2)
			{
				
				vertex2= generator.nextInt(no_of_vertices);
			}
			edge_cost=cost_generator.nextInt(1000)+1;
			if(!b[vertex1][vertex2] && !b[vertex2][vertex1])
			{
			adjecency_list[vertex1].adjList= new Edge(vertex2, edge_cost,adjecency_list[vertex1].adjList);
			adjecency_list[vertex2].adjList= new Edge(vertex1,edge_cost,adjecency_list[vertex2].adjList);
			b[vertex1][vertex2]=true;
			b[vertex2][vertex1]=true;
			edge_count++;
			}
			
		}
		System.out.println("Total Edges are:"+edge_count);
	//	print();
		
	}
	
	/*
	 * This function returns true if the graph is connected*/

	public boolean depthFisrtSearch(Mst Graph_for_dfs)
	{
		Stack<Integer> stack= new Stack<Integer>();
		int [] visited= new int [Graph_for_dfs.no_of_vertices];
		for(int i=0;i<visited.length;i++)
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
			for(Edge iterator= Graph_for_dfs.adjecency_list[u].adjList;iterator!=null;iterator=iterator.next)
			{
				if(visited[iterator.vertex_number]==-1)
				{
				stack.push(iterator.vertex_number);
				}
			}
			
		}
		int count=0;
		for(int i=0;i<visited.length;i++)
		{
			if(visited[i]==1)
			{
				count++;
			}
		}
		if(count==Graph_for_dfs.no_of_vertices)
		{
			//System.out.println("Graph is connected!!");
			return true;
		}
		else
		{
			System.out.println("Graph is not conneceted by "+(Graph_for_dfs.no_of_vertices-count)+" Nodes");
			return false;
		}
	}
	/*/*
	 * This function returns true if the graph is connected
	 * Difference is this dfs works for random generated graph*/
public boolean depthFisrtSearch(Random_Graph Graph_for_dfs)
{
	Stack<Integer> stack= new Stack<Integer>();
	int [] visited= new int [Graph_for_dfs.no_of_vertices];
	for(int i=0;i<visited.length;i++)
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
		for(Edge iterator= Graph_for_dfs.adjecency_list[u].adjList;iterator!=null;iterator=iterator.next)
		{
			if(visited[iterator.vertex_number]==-1)
			{
			stack.push(iterator.vertex_number);
			}
		}
		
	}
	int count=0;
	for(int i=0;i<visited.length;i++)
	{
		if(visited[i]==1)
		{
			count++;
		}
	}
	if(count==Graph_for_dfs.no_of_vertices)
	{
		//System.out.println("Graph is connected!!");
		return true;
	}
	else
	{
	//	System.out.println("Graph is not conneceted by "+(Graph_for_dfs.no_of_vertices-count)+" Nodes");
		return false;
	}
}

/*
 * prints the graph*/
public void print()
{
	for(int i=0;i<adjecency_list.length;i++)
	{
		for(Edge next=adjecency_list[i].adjList;next!=null;next=next.next)
		{
			System.out.println(adjecency_list[i].number+"  "+next.vertex_number+"  "+next.cost);
		}
	}
}



}
 
/*This is the main class which reads the input and handles the flow of the program*/

public class Mst {
	
	int no_of_vertices;
	int no_of_edges;
	Vertex [] adjecency_list;

	public Mst(int no_of_vertices, int no_of_edges, Vertex[] adjecency_list) {
		
		this.no_of_vertices = no_of_vertices;
		this.no_of_edges = no_of_edges;
		this.adjecency_list = adjecency_list;
		
		
	}
	/*Reads the input file provided and creates the graph*/
	
	public void createGraph(String filename)
	{
		
		try {
			
			Scanner Reader=null;
			File file1= new File(filename);
			Reader= new Scanner(file1);
			this.no_of_vertices=Reader.nextInt();
			this.no_of_edges=Reader.nextInt();
			adjecency_list= new Vertex[no_of_vertices];
			boolean b[][] = new boolean[no_of_vertices][no_of_vertices];
			
			//Reading vertices numbers
			for(int i=0;i<no_of_vertices;i++)
			{
				adjecency_list[i]=new Vertex(i,null,false);
			}
			while(Reader.hasNext())
			{
				int vertex1=Reader.nextInt();
				int vertex2= Reader.nextInt();
				int edge_cost=Reader.nextInt();
				if(!b[vertex1][vertex2] && !b[vertex2][vertex1])
				{
				adjecency_list[vertex1].adjList= new Edge(vertex2, edge_cost, adjecency_list[vertex1].adjList);
				adjecency_list[vertex2].adjList= new Edge(vertex1,edge_cost,adjecency_list[vertex2].adjList);
				b[vertex1][vertex2]=true;
				b[vertex2][vertex1]=true;
				}
			}
			Reader.close();
			//print();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception occured!!!");
		}
		
	}
	

	public Mst() {
		this.no_of_vertices = 0;
		this.no_of_edges = 0;
		this.adjecency_list =null;
	
	}
/*Prints the graph*/
public void print()
{
	for(int i=0;i<adjecency_list.length;i++)
	{
		for(Edge next=adjecency_list[i].adjList;next!=null;next=next.next)
		{
			System.out.println(adjecency_list[i].number+"  "+next.vertex_number+"  "+next.cost);
		}
	}
}

public static void main(String[] args) {
	
	
	if(args.length<0)
	{
		System.out.println("Provide input!!");
	}
	else
	{
		if(args.length==2)
		{
		String filename=args[1];
			if(args[0].equals("-s"))
			{
				System.out.println("Simple scheme output");
				System.out.println("********************************************");
				Mst graph= new Mst();
				graph.createGraph(filename);
				new Prims(graph);
				
			}
			else
			{
				System.out.println("Fibonacci scheme output");
				System.out.println("********************************************");
				
				//System.out.println(filename);
				Mst graph= new Mst();
				graph.createGraph(filename);
				FibonacciHeap fhip= new FibonacciHeap();
				fhip.prims_Fhip(graph);
			}
		}
		else if(args.length==3)
		{
			int n= Integer.parseInt(args[1]);
			int d=Integer.parseInt(args[2]);
			Random_Graph rg= new Random_Graph(n, d);
			rg.createGraph();
			new Prims(rg);
			FibonacciHeap fhip= new FibonacciHeap();
			fhip.prims_Fhip(rg);
			
			 
		}
	} 
	

}


}
/*This class is responsible for calculating minimum spanning tree from a given graph*/
class Prims
{
	Mst graph;
	Random_Graph graph1;
	int i=0,j=0,k=0;
	
	int min=99999999;
	int []mincost;
	int counter=0;
	int ssss=0;
	int final_edges[][];
	ArrayList<Integer> visited;
	//calculate MST for random graph
	public Prims(Random_Graph graph)
	{
		
		long start= System.currentTimeMillis();
		this.graph1=graph;
	
		
		mincost= new int[graph.no_of_vertices];
		visited= new ArrayList<Integer>();
		i=0;
		
		for(Edge iterator=graph.adjecency_list[0].adjList;iterator!=null;iterator=iterator.next)
		{
			
			if(iterator.cost<min)
			{
				
				min=iterator.cost;
				i=iterator.vertex_number;
		
			}
		}
		mincost[counter]=min;
		graph.adjecency_list[0].visited=true;
		graph.adjecency_list[i].visited=true;
		counter++;
		visited.add(graph.adjecency_list[0].number);
		visited.add(graph.adjecency_list[i].number);
		
		
		while(visited.size()<graph.no_of_vertices)
		{
			min=9999999;
			for(int j=0;j<visited.size();j++)
			{
				for(Edge Iterator= graph.adjecency_list[visited.get(j)].adjList;Iterator!=null;Iterator=Iterator.next)
				{
					if(Iterator.cost<min && !visited.contains(Iterator.vertex_number))
					{
						min=Iterator.cost;
						i=Iterator.vertex_number;
						
					}
				}
				
			}
			
			visited.add(i);
			mincost[counter]=min;
			counter++;
		}
		min=0;
		for(k=0;k<mincost.length;k++)
		{
			min= mincost[k]+min;
		}
		System.out.println("Final cost of spanning tree is(using simple scheme): "+min);
		long stop= System.currentTimeMillis();
		System.out.println("Time for execution: "+(stop-start)+" Miliseconds");
	}
	//calculate MST for input file graph
	public Prims(Mst graph)
	{
		final_edges=new int[graph.no_of_vertices][graph.no_of_vertices]; 
		long start=System.currentTimeMillis();
		this.graph=graph;
		
		mincost= new int[graph.no_of_vertices];
		visited= new ArrayList<Integer>();
		i=0;
		
		for(Edge iterator=graph.adjecency_list[0].adjList;iterator!=null;iterator=iterator.next)
		{
			
			if(iterator.cost<min)
			{
				
				min=iterator.cost;
				i=iterator.vertex_number;
				
			}
		}
		System.out.println(i+" 0");
		mincost[counter]=min;
		graph.adjecency_list[0].visited=true;
		graph.adjecency_list[i].visited=true;
		counter++;
		visited.add(graph.adjecency_list[0].number);
		visited.add(graph.adjecency_list[i].number);
		
		
		while(visited.size()<graph.no_of_vertices)
		{
			min=9999999;
			for(int j=0;j<visited.size();j++)
			{
				for(Edge Iterator= graph.adjecency_list[visited.get(j)].adjList;Iterator!=null;Iterator=Iterator.next)
				{
					if(Iterator.cost<min && !visited.contains(Iterator.vertex_number))
					{
						min=Iterator.cost;
						i=Iterator.vertex_number;
						ssss=visited.get(j);
					}
				}
				
			}
			visited.add(i);
			System.out.println(i+" "+ssss);
			final_edges[ssss][i]=1;
			mincost[counter]=min;
			counter++;
		}
		min=0;
		for(k=0;k<mincost.length;k++)
		{
			min= mincost[k]+min;
		}
		for(i=0;i<graph.no_of_vertices;i++)
		{
			for(j=0;j<graph.no_of_vertices;j++)
			{
				//if(final_edges[i][j]==1)
					//System.out.println(""+i+"  "+j);
			}
		}
		System.out.println("Final cost of spanning tree is(using simple scheme): "+min);
		long stop= System.currentTimeMillis();
		System.out.println("Time for execution: "+(stop-start)+" Miliseconds");
	
	}
	
	
	
}



