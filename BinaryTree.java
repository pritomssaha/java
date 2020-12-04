
import java.util.LinkedList;
import java.util.Queue;

public class BinaryTree{

  public static class Node{
    int value;
    Node left;
    Node right;

    public Node(int value){
      this.value=value;
      this.right=null;
      this.left=null;
    }
  }
  
  private Node root;
  
  public BinaryTree() {
	  this.root=null;
  }
  
  public void add(int value){
      root=addBST(root, value);  
   }
//adding values
private Node addBST(Node root, int value) {	
	if(root==null) {
		Node newNode=new Node(value);
		return newNode;		
	}else if(root.value>value) {
		root.left=addBST(root.left,value);
	}else if(root.value<value) {
		root.right=addBST(root.right,value);
	}
	return root;
}
//finding elements
public boolean contains(int value) {
	return containsNode(root, value);
}

private boolean containsNode(Node root, int value) {	
	if(root==null)
		return false;
	else if (root.value==value)
		return true;
	else if(root.value>value)
		return containsNode(root.left,value);
	else return containsNode(root.right,value);		
}

//deleting node
public void delete(int value){
	root=deleterecursively(root,value);
}

private Node deleterecursively(Node root, int value) {
	if(root==null)
		return null;
	if(root.value==value) {
		//have no children
		if(root.left==null && root.right==null)
			return null;
		//have one child
		else if(root.left==null)
			return root.right;
		else if(root.right==null)
			return root.left;
		//have 2 child		 
		int min=findsmallest(root.right);
		root.value=min;
		root.right=deleterecursively(root.right, min);
		return root;				
	}
	else if(root.value>value){
		root.left=deleterecursively(root.left,value);
	}
	else
		root.right=deleterecursively(root.right,value);
	return root;
}

public int min() {
	return findsmallest(root);
}

private int findsmallest(Node root) {
	if(root.left==null)	
		return root.value;
	else 
		return findsmallest(root.left);
}

public int max() {
	return findlargest(root);
}

private int findlargest(Node root) {
	if(root.right==null)	
		return root.value;
	else 
		return findlargest(root.right);
}

 private void inorder(Node root) {
	 if(root!=null) {
		 inorder(root.left);
		 System.out.print(root.value+" ");
		 inorder(root.right);
	 }
 }
 
 private void preorder(Node root) {
	 if(root!=null) {
		 System.out.print(root.value+" ");
		 inorder(root.left);		 
		 inorder(root.right);
	 }
 } 
 
 private void postorder(Node root) {
	 if(root!=null) {
		 inorder(root.left);		 
		 inorder(root.right);
		 System.out.print(root.value+" ");
	 }
 }
 
 public void printinorder() {
	 inorder(root);
 }
 public void printpreorder() {
	 preorder(root);
 }
 public void printpostorder() {
	 postorder(root);
 }
 public int getheight() {
	 return height(root);
 }
 
 private int height(Node root) {
	 if(root==null)
		 return 0;
	 else 
		 return 1+Math.max(height(root.left),height(root.right));
 }
 
 public void levelorder() {	
	Queue<Node> q=new LinkedList<Node>();
	q.add(root);
		while(!q.isEmpty()) {
			Node node=q.poll();
			System.out.print(node.value+" ");
			if(node.left!=null) {
				q.add(node.left);
			}
			
			if(node.right!=null) {
				q.add(node.right);
			}
		}
 }

public static void main(String[] args) {
	
	BinaryTree root=new BinaryTree();
	root.add(2);
	root.add(4);
	root.add(6);
	root.add(1);
	root.add(3);
	System.out.println(root.contains(2));	
	root.printinorder();
	System.out.println();
	root.levelorder();
	System.out.println("\nHeight: "+root.getheight());	
	System.out.println("smallest: "+ root.min());
	System.out.println("smallest: "+ root.max());	
	}
}