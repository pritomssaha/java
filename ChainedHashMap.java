
public class ChainedHashMap {
	
	private static class Entry{
		private int key;
		private int value;
		private Entry next;
		
		public Entry(int key,int value) {
			this.key=key;
			this.value=value;
			this.next=null;
		}
		
		public int getKey(){
			return this.key;
		}
		public int getvalue(){
			return this.value;
		}
		public Entry getNext(){
			return this.next;
		}
		public void setValue(int value) {
			this.value=value;
		}
		public void setKey(int key) {
			this.key=key;
		}
		public void setNext(Entry next) {
			this.next=next;
		}
	}
	
	private Entry[] table;
	private int table_size=20;
	
	public ChainedHashMap() {
		table=new Entry[table_size];
		for(Entry i:table) {
			i=(Entry)null;
		}
	}
	
	public int get(int key) {
		
		int hash=key%table_size;
		if(table[hash]==null)
			return -1;
		else {
			Entry entry=table[hash];
			while(entry.getNext()!=null && entry.getKey()!=key)
				entry=entry.getNext();
			if(entry==null)
				return -1;
			else
				return entry.getvalue();
		}
			
	}
	
	public void put(int key, int value) {
		
		int hash=key%table_size;
		
		if(table[hash]==null) {
			table[hash]=new Entry(key,value);			
		}
		
		else {
			Entry entry=table[hash];
			while(entry.getNext()!=null && entry.getKey()!=key)
				entry=entry.getNext();
			if(entry.getKey()==key)
				entry.setValue(value);
			else
				entry.setNext(new Entry(key,value));
		}		
	}
	 
	public void remove(int key) {
		int hash=key%table_size;
		if(table[hash]!=null) {
			Entry entry=table[hash];
			Entry prev=null;			
			while(entry.getNext()!=null && entry.getKey()!=key) {
				prev=entry;			
				entry=entry.getNext();
			}
			
			if(entry.getKey()==key) {
				if(prev==null) {
					table[hash]=entry.getNext();
				}
				else
					prev.setNext(entry.getNext());
			}						
		}
	}
	
	public void print() {
		for(int i=0;i<table.length;i++) {
			System.out.println(i+": ");
			for(Entry k=table[i];k!=null;k=k.getNext()) {
				System.out.print(k.getKey()+"="+k.getvalue()+"=>");				
			}
		}
	}
	 public static void main(String[] args) {
		ChainedHashMap h=new ChainedHashMap();
		h.put(2,12);
   	  	h.put(9,5);
   	  	h.put(2, 25);
   	  	h.put(5,30);
   	  	h.remove(9);
   	  	h.print();
   
    }		
}
