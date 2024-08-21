package com.huf.algorithmn;

import java.util.*;

public class AlTest {
    public int[] twoSum(int[] nums, int target) {
        //直接用map key=nums[i],value=i
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer another = map.get(target-nums[i]);
            if (another != null && another != i) {
                return new int[]{another, i};
            }
            map.put(nums[i], i);
        }
        return new int[]{0, 0};
    }

    public int[][] merge(int[][] intervals) {
        if (intervals.length < 2) {
            return intervals;
        }
        Arrays.sort(intervals, Comparator.comparingInt(interval -> interval[0]));
        List<int[]> list = new ArrayList<>();
        int[] temp = intervals[0];
        for (int i = 1; i < intervals.length; i++) {
            //如果temp不重合，那就插入list
            if(temp[1]<intervals[i][0]){
                list.add(new int[]{temp[0],temp[1]});
                temp=intervals[i];
            }else{
                temp[1] = Math.max(intervals[i][1],temp[1]);
            }
        }
        list.add(temp);
        int[][] result = new int[list.size()][2];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    /**
     * 283.移动零
     * @param nums
     */
    public void moveZeroes(int[] nums) {
        //从头开始遍历 快指针慢指针。快指针遍历完，从慢指针开始都赋值为0
        int low = 0;
        int fast = 0;
        while (fast < nums.length) {
            if (nums[fast] != 0) {
                nums[low++] = nums[fast];
            }
            fast++;
        }
        while (low < fast) {
            nums[low++] = 0;
        }
    }

    /**
     * 53.最大子数组和
     * @param nums
     * @return
     */
    public int maxSubArray(int[] nums) {
        int max = nums[0];
        int cur = nums[0];
        for (int i = 1; i < nums.length; i++) {
            cur = Math.max(nums[i],cur+nums[i]);
            max = Math.max(max, cur);
        }
        return max;
    }
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 234 .回文链表
     * @param head
     * @return
     */
    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        //1.先计算长度
        ListNode temp = head;
        int len = 0;
        while (temp != null) {
            len++;
            temp = temp.next;
        }
        //先获取这reverse个元素，截断，然后单独反转
        int reverse = len/2;
        //2.截断前面那段
        ListNode next = head;
        while (reverse-- > 0) {
            temp = next;
            next = next.next;
            if(reverse == 0) {
                temp.next = null;
            }
        }
        //3.反转
        temp = head;
        temp = reverseListNode(temp);
        //4.比较
        if (len % 2 == 1) {
            next = next.next;
        }
        while (temp != null && next != null) {
            if (temp.val != next.val) {
                return false;
            }
            temp = temp.next;
            next = next.next;
        }
        return true;
    }

    private ListNode reverseListNode(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode prev = head;
        ListNode temp = head.next;
        ListNode next = temp.next;
        prev.next = null;
        while (true) {
            temp.next = prev;
            prev = temp;
            if(next == null){
                break;
            }
            temp = next;
            next = next.next;
        }
        return temp;
    }

    public static void main(String[] args) {
        AlTest alTest = new AlTest();
        /*int[] result = alTest.twoSum(new int[]{3, 3}, 6);
        System.out.println(result);
        int[][] intervals = {{1,4},{0,4}};
        int[][] result = alTest.merge(intervals);*/
        /*int[] nums = new int[]{0,1,0,3,12};
        alTest.moveZeroes(nums);
        System.out.println("haha");*/
        //System.out.println(result);
        //System.out.println(alTest.maxSubArray(new int[]{5,4,-1,7,8}));
        ListNode head = new ListNode(3);
        head.next = new ListNode(3);
        /*head.next.next = new ListNode(2);
        head.next.next.next = new ListNode(1);
        head.next.next.next.next = new ListNode(3);*/
        System.out.println(alTest.isPalindrome(head));
    }
}
