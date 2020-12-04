import java.util.Arrays;

public class sort {

    public static  void main(String[] args){
        int[] a={25,5,3,9,25,45,21,1};
        mergesort(a,a.length);
        for(int i:a) {
            System.out.print(i+" ");
        }
    }

    public static int[] quicksort(int[] arr, int left, int right){
        int index=partiton(arr,left,right);
        if(left<index-1)
            quicksort(arr,left,index-1);
        if(index<right)
            quicksort(arr,index,right);
        return arr;

    }

    private static int partiton(int[] arr, int left, int right) {
        int i=left, j=right;
        int temp;
        int pivot=arr[(left+right)/2];
        while(i<=j){
            while(arr[i]<pivot) i++;
            while(arr[j]>pivot) j--;
            if(i<=j){
                temp=arr[i];
                arr[i]=arr[j];
                arr[j]=temp;
                i++;
                j--;

            }
        }
        return i;
    }

    private static void mergesort(int[] d, int n) {
        if(n<2)
            return;
        int middle=n/2;
        int[] s1= Arrays.copyOfRange(d, 0, middle);
        int[] s2=Arrays.copyOfRange(d, middle, n);
        mergesort(s1,middle);
        mergesort(s2,n-middle);
        merge(d,s1,s2);
    }

    private static void merge(int[] d,int[] s1, int[] s2) {
        int left=s1.length;
        int right=s2.length;
        int i=0,j=0,k=0;

        while(i<left && j<right) {
            if (s1[i]<s2[j]) {
                d[k++]=s1[i++];
            }
            else {
                d[k++]=s2[j++];
            }
        }
        while(i<left) {
            d[k++]=s1[i++];
        }
        while(j<right) {
            d[k++]=s2[j++];
        }

    }

    private static void insertionsort(int[] c, int length) {
        for(int i=0;i<length;i++) {
            int j=i;
            while(j>0 && c[j]<c[j-1] ) {
                int temp=c[j];
                c[j]=c[j-1];
                c[j-1]=temp;
                j--;
            }
        }

    }

    private static void selectionsort(int[] b, int length) {
        for(int i=0;i<length-1;i++) {
            int min=i;
            for(int j=i+1;j<length;j++) {
                if(b[j]<b[min]) {
                    min=j;
                }
            }
            int temp=b[i];
            b[i]=b[min];
            b[min]=temp;
        }

    }

    private static void bubblesort(int[] a, int length) {
        for(int i=0;i<length-1;i++) {
            for(int j=0;j<length-i-1;j++) {
                if(a[j+1]<a[j]) {
                    int temp=a[j];
                    a[j]=a[j+1];
                    a[j+1]=temp;
                }
            }
        }

    }

}
