package com.huf.g1test.redis;

import org.junit.jupiter.api.Test;

import java.util.*;

class ListNode {
    int val;
    ListNode next;
    ListNode(int x) {
        val = x;
        next = null;
    }
}
public class LeetcodeTest {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        Stack<ListNode> stackA = new Stack<>();
        Stack<ListNode> stackB = new Stack<>();
        while(headA != null){
            stackA.push(headA);
            headA = headA.next;
        }
        while (headB != null) {
            stackB.push(headB);
            headB = headB.next;
        }
        ListNode prev = null;
        while(!stackA.isEmpty() && !stackB.isEmpty()){
            if (stackA.peek() == stackB.peek()) {
                prev = stackA.pop();
                stackB.pop();
            }else{
                break;
            }
        }
        return prev;
    }



    public int subarraySum(int[] nums, int k) {
        Map<Integer,Integer> map = new HashMap<>();
        int count = 0;
        for (int num : nums) {
            count += num;
            if(!map.containsKey(count)){
                map.put(count,1);
            }else{
                map.put(count,map.get(count)+1);
            }
        }
        count = 0;
        int notInclude = 0;
        for(int num : nums){
            int target = k+notInclude;
            if (map.containsKey(target)) {
                count+=map.get(target);
            }
            notInclude += num;
            if(map.get(notInclude)==1){
                map.remove(notInclude);
            }else{
                map.put(notInclude,map.get(notInclude)-1);
            }
        }
        return count;
    }

    public void setZeroes(int[][] matrix) {
        int[] xArr = new int[matrix.length];
        int[] yArr = new int[matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    xArr[i] = 1;
                    yArr[j] = 1;
                }
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (xArr[i] == 1 || yArr[j] == 1) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    public static void main(String[] args) {
        LeetcodeTest test = new LeetcodeTest();
        int[][] matrix = new int[][]{{1,1,1},{1,0,1},{1,1,1}};
        test.setZeroes(matrix);
        System.out.println("haha");
        /*int result = test.subarraySum(new int[]{-1,-1,1},0);
        System.out.println(result);*/
    }
}
