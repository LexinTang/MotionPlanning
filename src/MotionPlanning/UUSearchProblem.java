
// CLEARLY INDICATE THE AUTHOR OF THE FILE HERE (YOU),
//  AND ATTRIBUTE ANY SOURCES USED (INCLUDING THIS STUB, BY
//  DEVIN BALKCOM).

package MotionPlanning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class UUSearchProblem {
	
	// used to store performance information about search runs.
	//  these should be updated during the process of searches

	// see methods later in this class to update these values
	protected int nodesExplored;
	protected int maxMemory;

	protected UUSearchNode startNode;
	
	protected interface UUSearchNode {
		public ArrayList<UUSearchNode> getSuccessors();
		public boolean goalTest();
		public int getDepth();
		public int getCost();
		public UUSearchNode getParent();
		public int[] getState();
	}
	
	// breadthFirstSearch:  return a list of connecting Nodes, or null
	// no parameters, since start and goal descriptions are problem-dependent.
	//  therefore, constructor of specific problems should set up start
	//  and goal conditions, etc.
	
	public List<UUSearchNode> breadthFirstSearch(){
		resetStats();
		// You will write this method
		HashMap<UUSearchNode, UUSearchNode> visited = new HashMap<UUSearchNode, UUSearchNode>();
		visited.put(startNode, null);
		
		Queue<UUSearchNode> queue = new LinkedList<UUSearchNode>();
		queue.offer(startNode);
		
		while (!queue.isEmpty()){
			int size = queue.size();
			for (int i = 0; i < size; i++){
				UUSearchNode node = queue.poll();
				incrementNodeCount();
				//System.out.println("polling " + node.toString());
				if (node.goalTest()){
					return backchain(node);
				}
				
				ArrayList<UUSearchNode> nextNodeList = node.getSuccessors();
				for (int j = 0; j < nextNodeList.size(); j++){
					
					UUSearchNode nextNode = nextNodeList.get(j);
					//System.out.println("nextNode " + nextNode.toString());
					if (!visited.containsKey(nextNode)){
						queue.offer(nextNode);
						visited.put(nextNode, node);
						updateMemory(visited.size() + queue.size());
					}
				}
			}
		}
		
		return null;
	}
	
	// backchain should only be used by bfs, not the recursive dfs
	private List<UUSearchNode> backchain(UUSearchNode node) {
		// you will write this method
		List<UUSearchNode> result = new ArrayList<UUSearchNode>(); 
		UUSearchNode prevNode = node.getParent();
		result.add(node);
		while (prevNode != null){
			node = prevNode;
			result.add(node);
			prevNode = node.getParent();
		}
		
		Collections.reverse(result);
		return result;
	}
	
	
	public List<UUSearchNode> depthFirstMemoizingSearch(int maxDepth) {
		resetStats(); 

		// You will write this method
		HashMap<UUSearchNode, Integer> visited = new HashMap<UUSearchNode, Integer>();
		
		List<UUSearchNode> result = dfsrm(startNode, visited, 0, maxDepth);
		
		Collections.reverse(result);
		return result;
	}

	// recursive memoizing dfs. Private, because it has the extra
	// parameters needed for recursion.  
	private List<UUSearchNode> dfsrm(UUSearchNode currentNode, HashMap<UUSearchNode, Integer> visited, 
			int depth, int maxDepth) {

		visited.put(currentNode, depth);
		
		// keep track of stats; these calls charge for the current node
		updateMemory(visited.size());
		incrementNodeCount();
		
		if (depth > maxDepth){
			return null;
		}
		
		// you write this method.  Comments *must* clearly show the 
		//  "base case" and "recursive case" that any recursive function has.	

		if (currentNode.goalTest()){
			List<UUSearchNode> result = new ArrayList<UUSearchNode>();
			result.add(currentNode);
			return result;
		}
		
		ArrayList<UUSearchNode> nextNodeList = currentNode.getSuccessors();
		for (int i = 0; i < nextNodeList.size(); i++){
			UUSearchNode nextNode = nextNodeList.get(i);
			if (!visited.containsKey(nextNode)){
				List<UUSearchNode> result = dfsrm(nextNode, visited, depth + 1, maxDepth);
				if (result != null){
					result.add(currentNode);
					return result;
				}
			}
		}
		
		return null;
	}
	
	
	// set up the iterative deepening search, and make use of dfsrpc
	public List<UUSearchNode> IDSearch(int maxDepth) {
		resetStats();
		// you write this method
		List<UUSearchNode> result = new ArrayList<UUSearchNode>();
		HashSet<UUSearchNode> currentPath = new HashSet<UUSearchNode>();
		for (int i = 0; i <= maxDepth; i++){
			result = dfsrpc(startNode, currentPath, 0, i);
			if (result != null){
				Collections.reverse(result);
				return result;
			}
		}
		return null;
	}

	// set up the depth-first-search (path-checking version), 
	//  but call dfspc to do the real work
	public List<UUSearchNode> depthFirstPathCheckingSearch(int maxDepth) {
		resetStats();
		
		// I wrote this method for you.  Nothing to do.

		HashSet<UUSearchNode> currentPath = new HashSet<UUSearchNode>();

		//return dfsrpc(startNode, currentPath, 0, maxDepth);
		List<UUSearchNode> result = dfsrpc(startNode, currentPath, 0, maxDepth);
		Collections.reverse(result);
		return result;
	}

	// recursive path-checking dfs. Private, because it has the extra
	// parameters needed for recursion.
	private List<UUSearchNode> dfsrpc(UUSearchNode currentNode, HashSet<UUSearchNode> currentPath,
			int depth, int maxDepth) {
		// you write this method
		if (depth > maxDepth){
			return null;
		}
		
		currentPath.add(currentNode);
		// keep track of stats; these calls charge for the current node
		updateMemory(currentPath.size());
		incrementNodeCount();

		if (currentNode.goalTest()){
			List<UUSearchNode> result = new ArrayList<UUSearchNode>();
			result.add(currentNode);
			return result;
		}
		
		ArrayList<UUSearchNode> nextNodeList = currentNode.getSuccessors();
		for (int i = 0; i < nextNodeList.size(); i++){
			UUSearchNode nextNode = nextNodeList.get(i);
			if (!currentPath.contains(nextNode)){
				List<UUSearchNode> result = dfsrpc(nextNode, currentPath, depth + 1, maxDepth);
				if (result != null){
					result.add(currentNode);
					return result;
				}
			}
		}
		
		currentPath.remove(currentNode);
		return null;
	}
	
	public List<UUSearchNode> AStarSearch() {
		resetStats(); 
		
		HashMap<UUSearchNode, Integer> visited = new HashMap<UUSearchNode, Integer>();
		PriorityQueue<UUSearchNode> queue = new PriorityQueue<UUSearchNode>();
		queue.offer(startNode);
		
		while (!queue.isEmpty()){
			int size = queue.size();
			for (int i = 0; i < size; i++){
				UUSearchNode node = queue.poll();
				
				if (node.goalTest()){
					return backchain(node);
				}
				
				ArrayList<UUSearchNode> nextNodeList = node.getSuccessors();
				for (int j = 0; j < nextNodeList.size(); j++){
					UUSearchNode nextNode = nextNodeList.get(j);
					
					if (!visited.containsKey(nextNode) || visited.get(nextNode) > nextNode.getCost()){
						queue.offer(nextNode);
						visited.put(nextNode, nextNode.getCost());
						incrementNodeCount();
						updateMemory(visited.size() + queue.size());
					}
				}
			}
		}
		
		return null;
	}

	protected void resetStats() {
		nodesExplored = 0;
		maxMemory = 0;
	}
	
	protected void printStats() {
		System.out.println("Nodes explored during last search:  " + nodesExplored);
		System.out.println("Maximum memory usage during last search " + maxMemory);
	}
	
	protected void updateMemory(int currentMemory) {
		maxMemory = Math.max(currentMemory, maxMemory);
	}
	
	protected void incrementNodeCount() {
		nodesExplored++;
	}

}