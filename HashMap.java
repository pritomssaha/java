
//chaining
public class HashMap {

	public static class LinkedHashEntry {
		  private int key;
	      private int value;
	      private LinkedHashEntry next;

	      LinkedHashEntry(int key, int value) {
	            this.key = key;
	            this.value = value;
	            this.next = null;
	      }

	      public int getValue() {
	            return value;
	      }

	      public void setValue(int value) {
	            this.value = value;
	      }

	      public int getKey() {
	            return key;
	      }

	      public LinkedHashEntry getNext() {
	            return next;
	      }

	      public void setNext(LinkedHashEntry next) {
	            this.next = next;
	      }
	}

      private final static int TABLE_SIZE = 7;
      LinkedHashEntry[] table;

      @SuppressWarnings("unused")
	HashMap() {
            table = new LinkedHashEntry[TABLE_SIZE];
            for (LinkedHashEntry i:table)
            	i=(LinkedHashEntry) null;
      }

      public int get(int key) {
            int hash = (key % TABLE_SIZE);
            if (table[hash] == null)
                  return -1;
            else {
                  LinkedHashEntry entry = table[hash];
                  while (entry != null && entry.getKey() != key)
                        entry = entry.getNext();
                  if (entry == null)
                        return -1;
                  else
                        return entry.getValue();
            }
      }

      public void put(int key, int value) {
            int hash = (key % TABLE_SIZE);
            if (table[hash] == null)
                  table[hash] = new LinkedHashEntry(key, value);
            else {
                  LinkedHashEntry entry = table[hash];
                  while (entry.getNext() != null && entry.getKey()!=key )
                        entry = entry.getNext();

                  if (entry.getKey() == key)
                        entry.setValue(value);
                  else
                        entry.setNext(new LinkedHashEntry(key, value));
            }
      }

      public void remove(int key) {
            int hash = (key % TABLE_SIZE);

            if (table[hash] != null) {
                 LinkedHashEntry entry = table[hash];
                 LinkedHashEntry prevEntry = null;

                  while (entry.getNext() != null && entry.getKey() != key) {
                        prevEntry = entry;
                        entry = entry.getNext();
                  }

                  if (entry.getKey() == key) {
                        if (prevEntry == null)
                             table[hash] = entry.getNext();
                        else
                             prevEntry.setNext(entry.getNext());
                  }
            }
      }

      public void print() {
          for (int bucket = 0; bucket < table.length; ++bucket) {
             System.out.print("Bucket " + bucket + ":");
             for (LinkedHashEntry i = table[bucket]; i != null; i = i.next)
                System.out.print(i.key + "=" + i.value+"=>");
             System.out.println();
          }
       }

      public static void main(String[] args) {
    	  HashMap h=new HashMap();
    	  h.put(2,12);
    	  h.put(9,5);
    	  h.put(2, 25);
    	  //h.remove(2);
    	  h.print();


      }

}