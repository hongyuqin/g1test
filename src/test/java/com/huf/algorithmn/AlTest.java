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
        System.out.println(alTest.maxSubArray(new int[]{5,4,-1,7,8}));
    }
}
