package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class SearchAlgorithm {
	Strategy strategy;
	SearchProblem sp;
	Boolean visualize;
	public SearchAlgorithm(SearchProblem sp, Strategy strategy, Boolean visualize) {
		this.strategy = strategy;
		this.sp = sp;
		this.visualize = visualize;
	}
	public void run() {
		switch(strategy){
			case DF: depthOrID(sp,2147483646 ); break;
			case ID: depthOrID(sp, 0); break;
			case BF: breadthFirst(sp); break;
			case UC: uniformCost(sp);break;
			default: break;
		}
	}
	private void uniformCost(SearchProblem sp) {
		Node root = sp.genRoot();
		Comparator<Node> pathComparator = new Comparator<Node>(){
			
			@Override
			public int compare(Node n1, Node n2) {
	            return (int) (Node.pathCost(n1)- Node.pathCost(n2));
	        }
		};
		Queue<Node> queue = new PriorityQueue<>(pathComparator);
		queue.add(root);
		while(!queue.isEmpty()) {
			Node n = queue.poll();
			ArrayList<Node>nodes =sp.expandNode(n);
			if(visualize) {
				sp.visualize(n);
			}
			for(int i = nodes.size()-1;i >=0;i--){
				Node n1 = nodes.get(i);
				if(n1.state != null){
					queue.add(n1);
				}
			}
			
			if(sp.goalTest(n)){
				System.out.println("Goal Reached!");
				System.out.println("Path cost: "+Node.pathCost(n));
				ArrayList<Operator> operators = new ArrayList<>();
				while(n.parentNode!=null) {
					operators.add(n.op);
					n = n.parentNode;
				}
				System.out.print("Sequence: ");
				for(int i = operators.size()-1; i>=0;i--) {
					System.out.print(operators.get(i)+" ");
				}
				break;
			}
			if(queue.isEmpty()) {
				System.out.println("No possible solution");
			}
		}
	}
	public void breadthFirst(SearchProblem sp){
		Node root = sp.genRoot();
		Deque<Node> queue = new LinkedList<>();
		queue.addFirst(root);
		while(!queue.isEmpty()){
			Node n = queue.removeFirst();
			ArrayList<Node>nodes =sp.expandNode(n);
			if(visualize) {
				sp.visualize(n);
			}
			for(int i = 0;i < nodes.size();i++){
				Node n1 = nodes.get(i);
				if(n1.state != null){
					queue.addLast(n1);
				}
			}
			if(sp.goalTest(n)){
				System.out.println("Goal Reached!");
				System.out.println("Path cost: "+Node.pathCost(n));
				ArrayList<Operator> operators = new ArrayList<>();
				while(n.parentNode!=null) {
					operators.add(n.op);
					n = n.parentNode;
				}
				System.out.print("Sequence: ");
				for(int i = operators.size()-1; i>=0;i--) {
					System.out.print(operators.get(i)+" ");
				}
				break;
			}
			if(queue.isEmpty()) {
				System.out.println("No possible solution");
			}
		}
		
	}
	private void depthOrID(SearchProblem sp,int limit) {
		while(true){
			int maxDepth =0;
			Node root = sp.genRoot();
		    Deque<Node> queue = new LinkedList<>();
		    queue.addFirst(root);
		    Node n = null;
		    while(!queue.isEmpty()){
		    	n = queue.removeFirst();
		    	if(n.depth>maxDepth) {
					maxDepth = n.depth;
				}
				if(!sp.arrayContains(sp.states, n.state)&&n.depth<limit) {
					sp.states.add(n.state);
				} else {
					continue;
				}
	    		ArrayList<Node>nodes =sp.expandNode(n);
	    		for(int i = nodes.size()-1;i>=0;i--){
					Node n1 = nodes.get(i);
					if(n1.state != null){
						queue.addFirst(n1);
					}
				}
		    	if(visualize) {
		    		System.out.println(n.depth);
					sp.visualize(n);
				}
		    	if(sp.goalTest(n)){
					System.out.println("Goal Reached!");
					System.out.println("Path cost: "+Node.pathCost(n));
					ArrayList<Operator> operators = new ArrayList<>();
					while(n.parentNode!=null) {
						operators.add(n.op);
						n = n.parentNode;
					}
					System.out.print("Sequence: ");
					for(int i = operators.size()-1; i>=0;i--) {
						System.out.print(operators.get(i)+" ");
					}
					return;
				}
		    }
		    if(maxDepth<limit){
		    	System.out.println();
		    	System.out.println("No possible solution. Depth: "+n.depth+" Limit: "+limit);
		    	return;
		    }
		    limit++;
		}
	}
}

