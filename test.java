public class test {

	public static void main(String[] args) {
		String test="HELLO";
		String test1="";
		String test3="Replaces the first occurrence of a";
		
		String test2=String.format("%s=>%d",test,25);
		
		System.out.println("string format: "+test2);	
				
		char[] c = new char[test.length()+1];
		test.getChars(0, test.length(), c, 1);
		System.out.print("getchar: ");
		for(char p:c) {
			System.out.print(p+" ");
		}
		System.out.println();
		System.out.print("getBytes: ");
		byte[] b=test.getBytes();
		for(byte q:b) {
			System.out.print(q+" ");
		}
		System.out.println();
		
		char[] ch= {'H','e','l','l','o'};
		test1=String.copyValueOf(ch);
		System.out.println("copyValueOf: "+test1);
		
		System.out.println("charAt: "+test.charAt(4));
		System.out.println("codePointAt: "+test.codePointAt(4));
		System.out.println("codePointBefore: "+test.codePointBefore(5));
		System.out.println("codePointCount: "+test.codePointCount(0,4));
		System.out.println("compareToIgnoreCase: "+test.compareToIgnoreCase(test1));
		System.out.println("concat: "+test.concat(test1));
		System.out.println("contentEquals: "+test.contentEquals(test1));
		System.out.println("endsWith: "+test.endsWith("o"));
		System.out.println("equals: "+test.equals(test1));
		System.out.println("equalsIgnoreCase: "+test.equalsIgnoreCase(test1));
		System.out.println("indexOf: "+test.indexOf('L'));
		System.out.println("intern: "+test.intern());
		System.out.println("isEmpty: "+test.isEmpty());
		System.out.println("lastIndexOf: "+test.lastIndexOf('L'));
		System.out.println("length: "+test.length());
		System.out.println("replace: "+test.replace('L','P'));
		System.out.println("replaceFirst: "+test.replaceFirst("L","O"));
		System.out.println("substring :"+test.substring(1, 3));
		
		String[] w=test3.split(", ");
		System.out.print("split(): ");
		for(String i:w) {
			System.out.print(i+" ");
		}
		
		System.out.print("\ntoCharArray: ");		
		char[] n=test3.toCharArray();
		for(int i=0;i<n.length;i++) {
			System.out.print(n[i]);
		}
		
		System.out.println();
		
		System.out.println(test3.toUpperCase());
		System.out.println(test3.toLowerCase());
		String test4="        "+test3.toUpperCase()+"       ";
		System.out.println(test4);
		test4=test4.trim();
		System.out.println("trim(): "+test4);
		

	}

}
