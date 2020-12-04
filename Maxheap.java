import java.util.Arrays;

public class Maxheap {
	
	private int[] heap;
	private int size;
	private int capacity;
	
	public Maxheap(int cap) {
		this.heap=new int[cap];
		this.capacity=cap;		
	}
	//get the index of left child
	private int getleftIndex(int parentIndex) {
		return 2*parentIndex+1;
	}
	//get the index of right child
	private int getrightIndex(int parentIndex) {
		return 2*parentIndex+2;
	}
	//get the index of parent
	private int getparentIndex(int childIndex) {
		return (childIndex-1)/2;
	}
	//check if the parent has left child 
	private boolean hasleftchild(int parentindex) {
		return getleftIndex(parentindex)<size;		
	}
	//check if the parent has right child 
	private boolean hasrightchild(int parentindex) {
		return getrightIndex(parentindex)<size;		
	}
	//check at a given index, it has parent 
	private boolean hasparent(int index) {
		return getparentIndex(index)>=0;		
	}
	//return left child value
	private int getLeftchild(int parentIndex) {	
		return heap[getleftIndex(parentIndex)];				
	}
	//return left right value
	private int getrightchild(int parentIndex) {	
		return heap[getrightIndex(parentIndex)];				
	}
	//return parent value
	private int getparent(int childIndex) {	
		return heap[getparentIndex(childIndex)];				
	}

	private void swap(int index1, int index2) {
		int temp=heap[index1];
		heap[index1]=heap[index2];
		heap[index2]=temp;		
	}
	
	public void add(int item) {
		capacity();
		heap[size]=item;
		size++;
		heapifyUp();
	}
	
	private void heapifyUp() {
		int index=size-1;
		while(hasparent(index) && getparent(index)<heap[index]) {
			swap(getparentIndex(index),index);
			index=getparentIndex(index);
		}		
	}
	
	private void capacity() {
		if(capacity==size)
			heap=Arrays.copyOf(heap,capacity*2);
		capacity*=2;		
	}
	
	public int poll() {
		if(size==0)
			throw new IllegalStateException("Heap is Empty");
		int element=heap[0];
		heap[0]=heap[size-1];
		size--;
		heapifyDown();
		return element;
	}
	
	private void heapifyDown() {
		int index=0;
		while(hasleftchild(index)) {
			int smalleschildtIndex=getleftIndex(index);
			if(hasrightchild(index) && getLeftchild(index)<getrightchild(index)) {
				smalleschildtIndex=getrightIndex(index);
			}
			if(heap[index]<heap[smalleschildtIndex]) {
				swap(index,smalleschildtIndex);
			}
			index=smalleschildtIndex;
		}		
	}
	
	public void print() {
		for(int i=0;i<size;i++) {
			System.out.print(heap[i]+" ");
		}
	}
	
	public static void main(String[] args) {
		Maxheap heap=new Maxheap(5);
		heap.add(1);
		heap.add(25);
		heap.add(12);
		heap.add(20);
		heap.add(2);		
		heap.print();
		System.out.println();
		System.out.println(heap.poll());
		heap.print();
		System.out.println();
		System.out.println(heap.poll());
		heap.print();
		
		
	}
	
	

}
