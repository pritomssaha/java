
public class BSTMap<K extends Comparable<K>,V> {
	
	//Entry class
	public static class Entry<K extends Comparable<K>,V>{
		
		private K key;
		private V value;
		private Entry<K,V> left;
		private Entry<K,V> right;
		
		public Entry(K key,V value) {
			this.key=key;
			this.value=value;
			this.left=null;
			this.right=null;
		}		
	}
	
	private Entry<K,V> root;
	
	public BSTMap() {
		this.root=null;
	}
	
	public void put(K key, V value) {
		root=put(key, value, root);
	}

	private Entry<K, V> put(K key, V value, Entry<K, V> root) {
		if(root==null)
			return new Entry<>(key,value);
		int compare=key.compareTo(root.key);		
		//goto left subtree and recursively add to the left
	    if(compare<0)
	    	root.left=put(key,value, root.left);	    
	    //otherwise recursively add to the right subtree
	    else if(compare>0)	
	    	root.right=put(key,value,root.right);
	    //key exist. update the key value
	    else
	    	root.value=value;
		return root;	   		
	}
	
	public boolean contains(K key) {
		return containskey(key,root);
	}
	
	private boolean containskey(K key, Entry<K,V> root) {
		if(root==null)
			return false;
		int compare=key.compareTo(root.key);
		if(compare<0) {
			return containskey(key,root.left);
		}
		else if(compare>0){
			return containskey(key,root.right);
		}
		return true;		
	}
	
	public V get(K k) {
		return get(k,root);
	}

	private V get(K k, Entry<K, V> root) {
		if(root==null)
			return null;
		int compare=k.compareTo(root.key);
		if(compare<0)
			return get(k,root.left);
		else if(compare>0)
			return get(k,root.right);
		else
			return root.value;
	
	}
	
	public void remove(K k) {
		root=remove(k,root);
	}
	private Entry<K, V> remove(K k, Entry<K, V> root) {
		if(root==null)
			return null;
		int compare=k.compareTo(root.key);
		if(compare<0)
			root.left=remove(k,root.left);
		else if(compare>0)
			root.right=remove(k,root.right);
		else {
			if(root.left==null && root.right==null)
				return null;
			else if(root.left==null)
				return root.right;
			else if(root.right==null)
				return root.left;
			else {
				Entry<K,V> t=root.right;
				while(t.left!=null) {
					t=t.left;
				}
				root.value=t.value;
				root.key=t.key;
				root.right=remove(t.key,root.right);				
			}				
		}
		return root;		
	}

	public static void main(String[] args) {
		
		BSTMap<String, Integer> root=new BSTMap<>();
		root.put("a",5);
		root.put("b",4);
		root.put("c",8);
		root.put("d",6);
		//root.remove("a");
		root.remove("d");
		
		
		System.out.println(root.contains("d"));
		System.out.println(root.get("a"));
		
	}	
}
