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

    public static void main(String[] args) {
        AlTest alTest = new AlTest();
        /*int[] result = alTest.twoSum(new int[]{3, 3}, 6);
        System.out.println(result);*/
        int[][] intervals = {{1,4},{0,4}};
        int[][] result = alTest.merge(intervals);
        System.out.println(result);
    }
}
