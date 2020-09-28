
public class SLinkedList {
	
	private static class Node{
		
		int data;
		Node next;
		
		public Node(int data, Node next) {
			this.data=data;
			this.next=next;			
		}		
	}	
	private Node head;
	private Node tail;
	private int size;
	
	public SLinkedList() {
		this.head=null;
		this.tail=null;
		this.size=0;
	}
	
	public boolean isEmpty() {
		return (size==0);
	}

	public int size() {
		return this.size;
	}
	
	public void addFirst(int e) {
		Node newNode=new Node(e,null);
		newNode.next=head;
		this.head=newNode;
		size++;
		if(size==1) {
			this.tail=head;
		}	
		
	}
	
	public void addLast(int e) {
		Node newNode=new Node(e,null);
		if(isEmpty()) {
			head=newNode;
		}		
		else 
			this.tail.next=newNode;
		this.tail=newNode;
		size++;
	}
	
	public int removeFirst() {
		if(isEmpty()) throw new IndexOutOfBoundsException();
		int data=head.data;
		this.head=head.next;
		this.size--;
		if(size==0)
			this.tail=null;
		return data;				
	}
	
	public int removeLast(){
		int data=0;
		if(isEmpty()) throw new IndexOutOfBoundsException();
		if(size==1) {
			data=head.data;
			head=head.next;
			this.tail=null;
			size--;
		}
		else{
		data=tail.data;
		Node current=head;
		while(current.next.next!=null) {
			current=current.next;
		}
		current.next=null;
		this.tail=current;
		this.size--;
		if(size==0) {
			this.tail=null;
			}
		}
		return data;
	}
	
	public void print() {
		Node current=head;
		while(current!=null) {
			System.out.print("->"+current.data+"->");
			current=current.next;
		}
	}
	
	public boolean find(int data) {
		Node current=head;
		while(current!=null) {
			if(current.data==data)
				return true;
			current=current.next;
		}
		return false;
	}
	
	public int getNth(int index) {
		if(index<0 || index>size)
			throw new IndexOutOfBoundsException();
		Node current=head;
		for(int i=0;i<index;i++) {
			current=current.next;
		}
		return current.data;
	}
	
	public int printNthfromLast(int index) {
		if(index<0 || index>size)
			throw new IndexOutOfBoundsException();
		Node current=head;
		for(int i=0;i<this.size-index-1;i++) {
			current=current.next;
		}
		return current.data;
	}
	
	public int numberOfOccurence(int data) {
		Node current=head;
		int count=0;
		while(current!=null) {
			if(current.data==data)
				count++;
			current=current.next;
		}
		return count;
	}
	//https://media.geeksforgeeks.org/wp-content/cdn-uploads/RGIF2.gif
	public void reverse() {
		Node prev=null;
		Node next=null;
		Node current=head;
		while(current!=null) {
			next=current.next;
			current.next=prev;
			prev=current;
			current=next;
		}
		head=prev;
		Node cur=head;
		while(cur!=null) {
			System.out.print("->"+cur.data+"->");
			cur=cur.next;
		}
	}
	
	public void sort() {
		Node current=head;
		Node index=null;
		int temp;
		
		if(head==null)
			throw new IndexOutOfBoundsException();
		else {
			while(current!=null) {
				index=current.next;
				while(index!=null) {
					if(current.data<index.data) {
						temp=index.data;
						index.data=current.data;
						current.data=temp;
					}
					index=index.next;
				}
				current=current.next;
			}
		}
	}
	
	//Main method
	public static void main(String[] args) {
		SLinkedList sl=new SLinkedList();
		sl.addLast(25);
		sl.addLast(39);
		sl.addFirst(69);
		sl.print();
		System.out.println();
		sl.sort();
		sl.print();
	}

}
