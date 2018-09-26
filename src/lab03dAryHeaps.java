import java.util.ArrayList;
import java.util.Scanner;

public class lab03dAryHeaps {
    static int d = 2;
    public static void main(String[] args){

        ArrayList<Integer> a = new ArrayList<>();
        a.add(56); a.add(20);a.add(2);
        a.add(5); a.add(6);a.add(12);
        a.add(64); a.add(2032);a.add(3);
        a.add(222);a.add(31);
        maxHeapInsert(a,5000);
        System.out.println(heapExtractMax(a));
        for(int i =0;i<a.size();i++){
            System.out.println(a.get(i));
        }


    }
    public static int heapExtractMax(ArrayList<Integer> arr){
        if(arr.size()<1){
            System.out.println("heap underflow\n");
            System.exit(0);
        }
        buildMaxHeap(arr);
        int max = arr.get(0);
        arr.set(0,arr.get(arr.size()-1));
        arr.remove(arr.size()-1);
        maxHeapify(arr,1);
        return max;
    }
    public static void buildMaxHeap(ArrayList<Integer> arr){
        for (int k = arr.size()/2; k >=1; k--){
            maxHeapify(arr,k);
        }
    }

    public static void maxHeapify(ArrayList<Integer> arr, int i){
        int[] childrens = new int[d];
        childrens[0] = getjChildren(i,1)-1;
        int largest = i-1;
        if(childrens[0]<=(arr.size()-1)&&arr.get(childrens[0])>arr.get(i-1)) largest = childrens[0];
        for (int k = 1 ; k < d; k++){
            childrens[k] = getjChildren(i,k+1)-1;
            if(childrens[k]>arr.size()-1) break;
            if(childrens[k]<=(arr.size()-1)&&arr.get(childrens[k])>arr.get(largest)) largest = childrens[k];
        }
        if(largest != i-1){
            int change = arr.get(i-1);
            arr.set(i-1,arr.get(largest));
            arr.set(largest,change);
            maxHeapify(arr,largest+1);
        }
    }
    public static int getjChildren(int i, int j){
        return d*(i-1)+j+1;
    }

    public static void maxHeapInsert(ArrayList<Integer> arr, int key){
        arr.add(-999999999);
        heapIncreaseKey(arr,arr.size(),key);
    }

    public static void heapIncreaseKey(ArrayList<Integer> arr, int i, int key){
        if(key<arr.get(i-1)){
            System.out.println("new key is smaller than current key\n");
            System.exit(0);
        }
        arr.set(i-1,key);
        int parent = parent(i)-1;
        while (i>1&&arr.get(parent)<arr.get(i-1)){
            int change = arr.get(i-1);
            arr.set(i-1,arr.get(parent));
            arr.set(parent,change);
            i = parent(i);
        }
    }

    public static int parent(int i){
        return (i-2)/d+1;
    }
}
