package com.huf.algorithmn;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * 11
     * @param height
     * @return
     */
    public int maxArea(int[] height) {
        //其实用动态规划就行
        int max = 0;
        int begin = 0;
        int end = height.length-1;
        while (begin < end) {
            max = Math.max(max,(end-begin)*Math.min(height[begin],height[end]));
            if (height[begin] < height[end]) {
                begin++;
            }else{
                end--;
            }
        }
        return max;
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        inorderTraversalSub(root, list);
        return list;
    }

    private void inorderTraversalSub(TreeNode root, List<Integer> list) {
        if (root == null) {
            return;
        }
        //左中右
        inorderTraversalSub(root.left, list);
        list.add(root.val);
        inorderTraversalSub(root.right,list);
    }

    /**
     * 109  再战
     * @param head
     * @return
     */
    public TreeNode sortedListToBST(ListNode head) {
        if (head == null) {
            return null;
        }
        TreeNode tree = new TreeNode();
        sortedListToBSTSub(tree, head, null);
        return tree;
    }

    private void sortedListToBSTSub(TreeNode tree, ListNode head, ListNode end) {
        ListNode mid = getMid(head, end);
        tree.val = mid.val;
        if (head != mid) {
            tree.left = new TreeNode();
            sortedListToBSTSub(tree.left,head,mid);
        }
        if (end != mid.next) {
            tree.right = new TreeNode();
            sortedListToBSTSub(tree.right,mid.next,end);
        }
    }


    private ListNode getMid(ListNode head,ListNode end) {
        ListNode slow = head;
        ListNode fast = head;
        while (true) {
            if (fast.next == end || fast.next.next == end) {
                return slow;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
    }

    /**
     * 49
     * @param strs
     * @return
     */

    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            //排序
            char[] chs = str.toCharArray();
            Arrays.sort(chs);
            String sortStr = new String(chs);
            if (map.containsKey(sortStr)) {
                List<String> list = map.get(sortStr);
                list.add(str);
            }else{
                List<String> list = new ArrayList<>();
                list.add(str);
                map.put(sortStr, list);
            }
        }
        List<List<String>> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> stringListEntry : map.entrySet()) {
            result.add(stringListEntry.getValue());
        }
        return result;
    }
    /*public List<List<String>> groupAnagrams(String[] strs) {
        Map<Integer[], List<String>> map = new HashMap<>();
        for (String str : strs) {
            //排序str
            int[] charCounts = new int[26];
            for (int i = 0; i < str.length(); i++) {
                int index = str.charAt(i)-'a';
                charCounts[index]++;
            }
            groupAnagramsAddMap(str,charCounts,map);
        }
        List<List<String>> result = new ArrayList<>();
        for (Map.Entry<Integer[], List<String>> listEntry : map.entrySet()) {
            result.add(listEntry.getValue());
        }
        return result;
    }*/

    private void groupAnagramsAddMap(String str,int[] charCounts, Map<Integer[], List<String>> map) {
        Integer[] matchKey = groupAnagramsMapContain(charCounts, map);
        if (matchKey == null) {
            Integer[] integerArray = Arrays.stream(charCounts).boxed().toArray(Integer[]::new);
            List<String> list = new ArrayList<>();
            list.add(str);
            map.put(integerArray, list);
        }else{
            List<String> list = map.get(matchKey);
            list.add(str);
            map.put(matchKey, list);
        }
    }

    private Integer[] groupAnagramsMapContain(int[] charCounts, Map<Integer[], List<String>> map) {
        for (Integer[] integers : map.keySet()) {
            //跟charCounts对比
            boolean allMatch = true;
            for (int i = 0; i < 26; i++) {
                if (charCounts[i] != integers[i]) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                return integers;
            }
        }
        return null;
    }

    /**
     * 206
     * @param head
     * @return
     */
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }

    /**
     * 3. 无重复字符的最长子串
     * @param s
     */
    public int lengthOfLongestSubstring(String s) {
        int maxLen = 0;
        int begin = 0;
        int end = 0;
        Map<Character, Integer> map = new HashMap<>();
        while (end < s.length()) {
            char c = s.charAt(end);
            if (map.get(c)!=null) {
                //移动begin,直到找到c
                while (begin < end) {
                    char beginC = s.charAt(begin);
                    map.remove(beginC);
                    begin++;
                    if (beginC == c) {
                        break;
                    }
                }
            }
            map.put(c, 1);
            maxLen = Math.max(maxLen, end - begin + 1);
            end++;
        }
        return maxLen;
    }

    static class LRUCache {

        private int currentCapacity;
        private int capacity;
        private LinkedHashMap<Integer,Integer> map = new LinkedHashMap<>();

        public LRUCache(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            Integer value = map.get(key);
            if (value == null) {
                return -1;
            }
            map.remove(key);
            map.put(key, value);
            return value;
        }

        public void put(int key, int value) {
            Integer existValue = map.get(key);
            if (existValue == null) {
                map.put(key, value);
                //超过最大容量，淘汰一个
                currentCapacity++;
            }else{
                map.remove(key);
                map.put(key,value);
            }

            if (currentCapacity > capacity) {
                Map.Entry<Integer, Integer> firstEntry = map.entrySet().iterator().next();
                map.remove(firstEntry.getKey());
                currentCapacity--;
            }
        }
    }

    /**
     * 215
     * @param nums
     * @param k
     * @return
     */
    public int findKthLargest(int[] nums, int k) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if(list.size()==k && nums[i]<list.get(0)){
                continue;
            }
            list.add(nums[i]);
            list = list.stream().sorted().collect(Collectors.toList());
            if (list.size() > k) {
                list.remove(list.get(0));
            }
        }
        return list.get(0);
    }

    public static void main(String[] args) {
        AlTest alTest = new AlTest();
        System.out.println(alTest.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4));
        /*int[] result = alTest.twoSum(new int[]{3, 3}, 6);
        System.out.println(result);
        int[][] intervals = {{1,4},{0,4}};
        int[][] result = alTest.merge(intervals);*/
        /*int[] nums = new int[]{0,1,0,3,12};
        alTest.moveZeroes(nums);
        System.out.println("haha");*/
        //System.out.println(result);
        //System.out.println(alTest.maxSubArray(new int[]{5,4,-1,7,8}));
        /*ListNode head = new ListNode(3);
        head.next = new ListNode(3);
        head.next.next = new ListNode(2);
        head.next.next.next = new ListNode(1);
        head.next.next.next.next = new ListNode(3);
        System.out.println(alTest.isPalindrome(head));
        ListNode head = new ListNode(10);
        alTest.sortedListToBST(head);
        List<List<String>> list = alTest.groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"});
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);

        System.out.println(alTest.reverseList(head));*/
        //试试treeMap


        //System.out.println(alTest.lengthOfLongestSubstring("pwwkew"));


        /*LRUCache lRUCache = new LRUCache(2);
        lRUCache.put(1, 1); // 缓存是 {1=1}
        lRUCache.put(2, 2); // 缓存是 {1=1, 2=2}
        lRUCache.get(1);    // 返回 1
        lRUCache.put(3, 3); // 该操作会使得关键字 2 作废，缓存是 {1=1, 3=3}
        lRUCache.get(2);    // 返回 -1 (未找到)
        lRUCache.put(4, 4); // 该操作会使得关键字 1 作废，缓存是 {4=4, 3=3}
        lRUCache.get(1);    // 返回 -1 (未找到)
        lRUCache.get(3);    // 返回 3
        lRUCache.get(4);    // 返回 4*/
    }
}
