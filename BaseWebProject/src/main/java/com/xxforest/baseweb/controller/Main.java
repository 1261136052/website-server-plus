package com.xxforest.baseweb.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mysql.cj.protocol.Message;
import lombok.Data;
import org.ehcache.core.spi.store.Store;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.websocket.Session;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();  // 元素个数
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = sc.nextInt();
        }

        // 使用插入排序进行升序排序
        int[] sortedNums = insertionSort(nums);
        for (int num : sortedNums) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    // 插入排序算法
    public static int[] insertionSort(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            int temp = nums[i];
            int j = i - 1;
            while (j >= 0 && nums[j] > temp) {
                nums[j+1] = nums[j];
                j--;
            }
            nums[j+1] = temp;
        }
        return nums;
    }

    // 冒泡排序算法
    public static int[] bubbleSort(int[] nums) {
        for (int i = 0; i < nums.length-1; i++) {
            for (int j = 0; j < nums.length-i-1; j++) {
                if (nums[j] > nums[j+1]) {
                    int temp = nums[j];
                    nums[j] = nums[j+1];
                    nums[j+1] = temp;
                }
            }
        }
        return nums;
    }
}



class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}

class InsertionSortInLinkedList {
//    public static ListNode insertionSortList(ListNode head) {
//        if (head == null || head.next == null) {
//            return head;
//        }
//        ListNode dummy = new ListNode(0);
//        dummy.next = head;
//        ListNode lastSorted = head, curr = head.next;
//        while (curr != null) {
//            if (lastSorted.val <= curr.val) {
//                lastSorted = lastSorted.next;
//            } else {
//                ListNode prev = dummy;
//                while (prev.next.val <= curr.val) {
//                    prev = prev.next;
//                }
//                lastSorted.next = curr.next;
//                curr.next = prev.next;
//                prev.next = curr;
//            }
//            curr = lastSorted.next;
//        }
//        return dummy.next;
//    }



    public static String[] spellchecker(String[] wordlist, String[] queries) {
        HashSet<String> onlyWords = new HashSet<>();
        HashMap<String, String> lowercaseWords = new HashMap<>();
        HashMap<String, String> specialWords = new HashMap<>();
        String[] answer = new String[queries.length];


        for (String s : wordlist) {
            onlyWords.add(s);
            lowercaseWords.putIfAbsent(s.toLowerCase(),s);

            String newWord = s.toLowerCase().replace("a","0").
                    replace("e","0").replace("i","0").
                    replace("o","0").replace("u","0");
            specialWords.put(newWord,s);
        }


        for (int i = 0; i < queries.length; i++) {
            if (onlyWords.contains(queries[i])){
                answer[i] = queries[i];
                continue;
            }
            if (lowercaseWords.containsKey(queries[i].toLowerCase())){
                answer[i] = lowercaseWords.get(queries[i].toLowerCase());
                continue;
            }
            String key =  queries[i].toLowerCase().replace("a","0").
                    replace("e","0").replace("i","0").
                    replace("o","0").replace("u","0");

            answer[i] = specialWords.getOrDefault(key, "");

        }
        return answer;
    }


    public List<List<Integer>> permute(int[] nums) {
        int len  = nums.length;
        List<List<Integer>> answer = new ArrayList<>();
        if (len == 0){
            return answer;
        }
        Deque<Integer> path = new ArrayDeque<>();
        boolean[] used = new boolean[len];
        dfs(nums,len,0,path,used,answer);
        return answer;
    }

    private void dfs(int[] nums, int len, int depth, Deque<Integer> path, boolean[] used, List<List<Integer>> answer) {
        if (len == depth){
            answer.add(new ArrayList<>(path));
            return;
        }

        for (int i = 0; i < len; i++) {
            if (used[i]){
                continue;
            }

            path.push(nums[i]);
            used[i] = true;
            dfs(nums,len,depth+1,path,used,answer);
            used[i] = false;
            path.pop();
        }

    }



     static class CombinationIterator {
        int[] index;
        String str;
        boolean flag;

        public CombinationIterator(String characters, int combinationLength) {
            index = new int[combinationLength];
            str = characters;
            flag = false;

            for (int i = 0; i < combinationLength; i++) {
                index[i] = i;
            }
        }

        public String next() {
            StringBuilder s = new StringBuilder();
            for (int i : index) {
                s.append(str.charAt(i));
            }
            int is = -1;
            for (int i = index.length - 1; i >= 0; i--) {
                if (index[i]!=str.length() - index.length +i){
                    is = i;
                    break;
                }
            }

            if (is==-1){
                flag = true;
            }else {
                index[is]++;
                for (int i = is+1; i < index.length; i++) {
                    index[i] = index[i-1]+1;
                }
            }


            return s.toString();
        }

        public boolean hasNext() {
            return !flag;
        }
    }


    public static int findMaxLength(int[] nums) {
        Map<Integer,Integer> hashMap = new HashMap<>();
        hashMap.put(0,-1);
        int maxLength = 0;
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i]==0?-1:nums[i];

            if (hashMap.containsKey(sum)){
                int integer = hashMap.get(sum);
                maxLength = Math.max(maxLength,i-integer);
            }else {
                hashMap.put(sum,i);
            }
        }

        return maxLength;
    }

    public static int repeatedStringMatch(String a, String b) {
        StringBuilder temp = new StringBuilder(a);
        int count = 1;
        int MAX =  2*a.length() + b.length();
        while (!temp.toString().contains(b)) {
            if (temp.length()>=MAX) {
                count = -1;
                break;
            }
            temp.append(a);
            count++;
        }
        return count;
    }


      static class TreeNode {
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

    class Solution {
        public int maxHigh = 0;
        public int val;
        public int findBottomLeftValue(TreeNode root) {
            dfs(root,0);
            return val;
        }
        public void dfs(TreeNode root,int depth){
            if(root == null) return;
            depth++;
            dfs(root.left,depth);
            dfs(root.right,depth);
            if(depth>maxHigh){
                val = root.val;
            }
        }
    }


    public int reachableNodes(int n, int[][] edges, int[] restricted) {
        int res = 0;
        List<Integer>[] list  =  new List[n];

        for (int i = 0; i < n; i++) {
            list[i] = new ArrayList<>();
        }

        int[] isREst = new int[n];

        Arrays.fill(isREst,-1);
        for (int i : restricted) {
            isREst[i] = 1;
        }

        for (int[] edge : edges) {
            int a= edge[0],b = edge[1];
            list[a].add(b);
            list[b].add(a);
        }
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{0,-1});
        while (!queue.isEmpty()){
            int[] poll = queue.poll();
            int cur = poll[0],pre = poll[1];
            for (Integer integer : list[cur]) {
                if (isREst[integer]!=0&&integer!=pre){
                    res++;
                    queue.add(new int[]{integer,cur});
                }
            }
        }
        return res;
    }

    public int[] numMovesStones(int a, int b, int c) {
        int[] answer = new int[2];
        int min = Math.min(Math.min(a,b),c);
        int max= Math.max(Math.max(a,b),c);
        int mid = a+b+c-min-max;
        int minStep = 2;
        if (max-mid==1&&mid-min==1) minStep=0;
        else if (max-mid<=2||mid-min<=2) minStep=1;

        answer[0] =minStep;
        answer[1] =max-min-2;
        return answer;

    }

    public static boolean possibleBipartition(int n, int[][] dislikes) {
        int[] colors = new int[n+1];
        List<Integer>[] list = new List[n+1];
        for (int i = 0; i < n+1; i++) {
            list[i] = new ArrayList<>();
        }

        for (int[] dislike : dislikes) {
            list[dislike[0]].add(dislike[1]);
            list[dislike[1]].add(dislike[0]);
        }



//        Set<Integer> ADislikes = new HashSet<>();
//        Set<Integer>  BDislikes = new HashSet<>();

        for (int i = 1; i <= n; i++) {
//             List<Integer> dislike = list[i];
//            if (!ADislikes.contains(i)){
//                ADislikes.addAll(dislike);
//                continue;
//            }
//
//            if (!BDislikes.contains(i)){
//                BDislikes.addAll(dislike);
//            }
//            else {
//                flag = false;
//                break;
//            }
            if (colors[i]==0&&!dfsM(i,1,list,colors)){
                return false;
            }
        }
        return true;
    }

    private static boolean dfsM(int i, int color, List<Integer>[] list, int[] colors) {
        colors[i] = color;

        for (Integer next : list[i]) {
         if (colors[next]!=0&&colors[next]==color) return false;
         if (colors[next]==0&&!dfsM(next,3^color,list,colors)) return false;
        }
        return true;
    }

    public boolean validPartition(int[] nums) {
        int length = nums.length;
        boolean[] flags = new boolean[length];
        flags[0] = false;
        flags[1] = nums[0] == nums[1];
        flags[2] = (flags[1]&&nums[1] == nums[2]) || (nums[0]+1==nums[1]&&nums[2]==nums[1]+1);
        for (int i = 3; i < length; i++) {
            boolean f1 = flags[i-2]&&(nums[i-1]==nums[i]);
            boolean f2 = flags[i-3]&&(nums[i-2]==nums[i-1]&&nums[i-1]==nums[i]);
            boolean f3 = flags[i-3]&&(nums[i-2]+1==nums[i-1]&&nums[i-1]+1==nums[i]);
            flags[i] = f1 || f2 || f3;
        }
        return flags[length-1];
    }

    public List<List<Integer>> groupThePeople(int[] groupSizes) {
        List<List<Integer>> answer = new ArrayList<>();


        Map<Integer,List<Integer>> map  = new HashMap<>();
        int length = groupSizes.length;

        for (int i = 0; i < length; i++) {
            List<Integer> list = map.computeIfAbsent(groupSizes[i], k -> new ArrayList<>());
            //                list.add(i);
            list.add(i);
            if (list.size()==groupSizes[i]) {
                answer.add(list);
                map.remove(groupSizes[i]);
            }

        }

        return  answer;
    }


    public int findString(String[] words, String s) {

        int left = 0;
        int right = words.length-1;

        while(left<=right){
            int mid = left + (right-left)/2;
            while((mid>left)&&("".equals(words[mid]))) mid--;
            if(words[mid].equals(s)) return mid;
            if(words[mid].compareTo(s) > 0) left=mid+1;
            else  right = mid-1;
        }
        return -1;

    }

    public int minOperations(int[] nums) {
        int  max = 0;
        int count = 0;

        for (int num : nums) {
            max= Math.max(max,num);
            count += Integer.bitCount(num);
        }
        count += Integer.toBinaryString(max).length() -1;
        return count;
    }


    public static List<List<Integer>> getAncestors(int n, int[][] edges) {
        List<List<Integer>> answer = new ArrayList<>();

        List<Integer>[] net = new List[n];
        int length = edges.length;


        for (int i = 0; i < n; i++) {
            net[i] = new ArrayList<>();
        }

        for (int i = 0; i < length; i++) {
            int[] edge = edges[i];
            net[edge[1]].add(edge[0]);
        }

        for (int i = 0; i < n; i++) {

            Deque<Integer> glass = new ArrayDeque<>();
            glass.add(i);
            TreeSet<Integer> item = new TreeSet<>();
            while (!glass.isEmpty()){
                Integer poll = glass.poll();
                glass.addAll(net[poll]);
                if (poll!=i)item.add(poll);
            }

            answer.add(new ArrayList<>(item));
        }
        return answer;
    }


    public List<List<Integer>> getAncestors2(int n, int[][] edges) {
        Map<Integer, List<Integer>> parents = new HashMap<>();
        for (int i = 0; i < n; i++) {
            parents.put(i, new ArrayList<>());
        }
        for (int i = 0; i < edges.length; i++) {
            parents.get(edges[i][1]).add(edges[i][0]);
        }
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Set<Integer> parent = new HashSet<>();
            Queue<Integer> queue = new LinkedList<>();
            queue.add(i);
            while (!queue.isEmpty()) {
                int index = queue.poll();
                if (index != i) parent.add(index);
                for (Integer id : parents.get(index)) {
                    if (!parent.contains(id)) {
                        queue.offer(id);
                    }
                }
            }
            result.add(parent.stream().sorted().collect(Collectors.toList()));
        }
        return result;
    }



    public int minIncrementForUnique(int[] nums) {

        Arrays.sort(nums);

        int answer = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] <= nums[i-1]){
                answer  +=  nums[i-1] - nums[i] +1;
                nums[i] = nums[i-1]+1;
            }
        }
        return answer;
    }

    public static int  halveArray(int[] nums) {
        PriorityQueue<Double> pq = new PriorityQueue<Double>((a, b) -> b.compareTo(a));
        double sum = 0;
        double cut = 0.0;
        int count = 0;
        for (int num : nums) {
            sum += num;
            pq.offer((double) num);
        }

        double half =  sum/2;
        while (half>cut){
            Double poll = pq.poll();

            cut += poll/2;
            pq.offer(poll/2);

            count++;


        }


        return count;

    }

    public static int singleNumber(int[] nums) {

        Map<Integer,Integer> map = new HashMap<>();


        for (int num : nums) {
            if (map.containsKey(num)){
                map.remove(num);
                continue;
            }

            map.put(num,1);
        }

        AtomicInteger answer = new AtomicInteger();
        map.forEach((k,v)->{
            answer.set(k);
        });
        return answer.get();
    }


    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        int length = s.length();
        Map<Character,Character> temp  = new HashMap<>();
        temp.put(')','(');
        temp.put(']','[');
        temp.put('}','{');

        for (int i = 0; i < length; i++) {
            if (stack.size()!=0&&temp.get(s.charAt(i))==stack.peek()){
                stack.pop();
            }
            else {
                stack.push(s.charAt(i));
            }
        }
        return stack.size()==0;
    }
    public boolean isPalindrome(ListNode head) {
        List<Integer> list = new ArrayList<>();
        while (head==null){
            list.add(head.val);
            head= head.next;
        }

        int first = 0;
        int last = list.size()-1;

        while (last >= first){
            if (!list.get(first).equals(list.get(last))){
                return false;
            }
            last++;
            first--;
        }

        return true;

    }


      public class ListNode {
          int val;
          ListNode next;
          ListNode() {}
          ListNode(int val) { this.val = val; }
          ListNode(int val, ListNode next) { this.val = val; this.next = next; }
      }

    public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<String>();
        if (n==0) return result;

        dfsMAX(result,n,n,"");
        return result;
    }

    private void dfsMAX(List<String> result, int left, int right, String s) {

        if (left==0&&right==0){
            result.add(s);
            return;
        }


        if (right>left) return;

        if (right>0) dfsMAX(result,left,right-1,s+")");

        if (left>0) dfsMAX(result,left-1,right,s+"(");

    }
    public List<TreeNode> generateTrees(int n) {
       if (n<1)return new ArrayList<TreeNode>();
       return helper(1,n);
    }

    private List<TreeNode> helper(int start, int end) {
        List<TreeNode> list = new ArrayList<TreeNode>();
        if (start>end){
            list.add(null);
            return list;
        }

        for (int i=start; i<=end;i++){
            List<TreeNode> left = helper(start,end-1);
            List<TreeNode> right = helper(start+1,end);


            for (TreeNode l : left) {
                for (TreeNode r : right) {
                    TreeNode root= new TreeNode(i);

                    root.right = r;
                    root.left = l ;
                    list.add(root);
                }
            }


        }

        return list;
    }

    public static int maxArea(int[] height) {
        int low = 0;
        int high = height.length-1;
        int len = high+1;
        int result =0;
        while (low<high){
           int temp = Math.min(height[low],height[high])*len;

           result = Math.max(result,temp);

           if (height[low]>height[high]){
               high-=1;
           }else {
               low +=1;
           }
            len -=1;
        }
        return result;
    }
    int maxGain = Integer.MIN_VALUE;
    public int maxPathSum(TreeNode root) {
        return finding(root);
    }

    private Integer finding(TreeNode root) {
        if(root == null) return 0;

        int maxLift = Math.max(finding(root.left),0);
        int maxRight = Math.max(finding(root.right),0);



        int priceVal = root.val + maxLift + maxRight;
        maxGain = Math.max(priceVal,maxGain);

        return root.val + Math.max(maxLift,maxRight);


    }
    public boolean canConstruct(String ransomNote, String magazine) {
        int[] hash = new int[26];
        for (int i = 0; i < magazine.length(); i++) {
            hash[magazine.charAt(i)-'a']++;
        }
        for (int i = 0; i < ransomNote.length(); i++) {
            if (hash[ransomNote.charAt(i)-'a']--==0) return  false;
        }
        return true;
    }

    public int countSegments(String s) {
        int count =  0;
        int len = s.length();
        if(len==0) return count;

        for(int i = 0;i<len;i++){
            if(s.charAt(i) == ' ') count++;
        }

        return count+1;
    }

    Map<Integer,Integer> map = new HashMap<>();
    public boolean hasPathSum(TreeNode root, int targetSum) {
        if(root == null) return false;

        dfs(root,0,targetSum);

       return map.containsKey(targetSum);
    }

    public void dfs(TreeNode root,int sum ,int targetSum){
        if(root==null) return;
        sum += root.val;
        dfs(root.left,sum,targetSum);
        dfs(root.right,sum,targetSum);

        if (root.left==null&&root.right==null) map.put(sum,sum);
    }

    public int longestArithSeqLength(int[] nums) {
        Arrays.sort(nums);
        PriorityQueue<Integer> pq = new PriorityQueue<Integer>((a, b) -> b.compareTo(a));
        int cut = Integer.MAX_VALUE;
        int count = 0;
        int len = nums.length;
        if (len==0) return count;
        for (int i = 1; i < len; i++) {
          int temp =   nums[i] - nums[i-1];
          if (temp==cut) {
              count++;
              continue;
          }
          if (count!=0) pq.offer(count);
          cut = temp;
        }
        return pq.poll()+2;
    }
    public List<Integer> findAnagrams(String s, String p) {
        int slen = s.length();
        int plen = p.length();
        if (plen>slen) return  new ArrayList<Integer>();
        int[] slist = new int[26];
        int[] plist = new int[26];
        for (int i = 0; i < plen; i++) {
            ++slist[s.charAt(i)-'a'];
            ++plist[p.charAt(i)-'a'];
        }
        List<Integer> ans = new ArrayList<Integer>();
        if (Arrays.equals(plist,slist)) ans.add(0);


        for (int i = 0; i < slen - plen; i++) {
            --slist[s.charAt(i)-'a'];
            ++slist[s.charAt(i+plen)-'a'];
            if (Arrays.equals(plist,slist)) ans.add(i+1);
        }
        return ans;

    }
    public static int search(int[] nums, int target) {

        int right = nums.length-1;
        int left =  0;

        while (right >= left) {
            if (nums[left]!=target) left++;
            if (nums[right]!=target) right--;
            if ((right>=0&&left>=0)&&nums[left]==target&&nums[right]==target) return right-left+1;
        }
        return 0;

    }
    List<List<Integer>> ans = new ArrayList<List<Integer>>();
    public List<List<Integer>> combine(int n, int k) {
        dfs(n,k,new ArrayList<Integer>(),1);
         return ans;
    }

    private void dfs(int n, int k, ArrayList<Integer> list,int index) {
        if (k==list.size()){
            ans.add(new ArrayList<Integer>(list));
            return;
        }

        for (int i = index; i <=n; i++) {
            list.add(i);
            dfs(n,k+1,list,i);
            list.remove(list.size()-1);
        }


    }

    public int[] searchRange(int[] nums, int target) {

        return new int[]{search(nums,target,true),search(nums,target,false)};
    }
    public int search(int[] nums, int target,boolean isLeft){
        int left=0,right=nums.length-1,ans = -1;
        while (left<=right){
            int mid = left + (right-left)/2;

            if (nums[mid]==target){
                ans = mid;
                if (isLeft){
                    right = mid-1;
                }else {
                    left = mid+1;
                }
            }

            else if (nums[mid]>target){
                right = mid;
            }else {
                left = mid;
            }
        }
        return ans;
    }

    String left ="";
    String right ="";
    public boolean isSymmetric(TreeNode root) {
        if (root==null) return true;
        bfsL(root.left);
        bfsR(root.right);

        if (left.equals(right)) return true;
        return false;

    }

    private void bfsL(TreeNode root) {
        if (root==null) return;

        bfsL(root.left);
        left = left + root.val;
        bfsL(root.right);
    }

    private void bfsR(TreeNode root) {
        if (root==null) return ;

        bfsR(root.right);
        right = right + root.val;
        bfsR(root.left);
    }

    int res = 0;
    public int closestCost(int[] baseCosts, int[] toppingCosts, int target) {
        res = Arrays.stream(baseCosts).min().getAsInt();
        if (res>=target) return res;

        for (int b : baseCosts) {
            dfs(toppingCosts,0,b,target);
        }
        return res;
    }

    private void dfs(int[] toppingCosts, int idx, int sum, int target) {
        if (sum-target>=Math.abs(res-target)){
            return;
        }

        if (Math.abs(sum-target)<=Math.abs(res-target)){

            if (Math.abs(sum-target)<Math.abs(res-target)){
                res = sum;
            }else {
                res = Math.min(sum,res);
            }

        }

        if(idx>=toppingCosts.length) return;

        dfs(toppingCosts,idx+1,sum,target);
        dfs(toppingCosts,idx+1,sum+toppingCosts[idx],target);
        dfs(toppingCosts,idx+1,sum+toppingCosts[idx]*2,target);
    }
    int[] prices;
    int[][] memo;
    public int maxProfit(int[] prices) {
        int len = prices.length;
        this.prices = prices;
        memo = new int[len][2];

        for (int i = 0; i < len; i++) {
            Arrays.fill(memo[i],-1);
        }
        return dfs(len-1,0);
    }

    private int dfs( int idx, int hold) {

        if (idx<0) return hold==1?Integer.MIN_VALUE:0;
        if (memo[idx][hold]!=-1) return memo[idx][hold];
        if (hold==1)
            return memo[idx][hold] = Math.max(dfs(idx+1,1),dfs(idx+1,0)+prices[idx]);
        return memo[idx][hold] = Math.max(dfs(idx+1,0),dfs(idx+1,1)-prices[idx]);
    }


    public int shortestPathBinaryMatrix(int[][] grid) {
        if (grid[0][0]==1) return 0;
        int len = grid.length;
        int[][] data = new int[len][len];
        for (int i = 0; i < len; i++) {
            Arrays.fill(data[i],Integer.MAX_VALUE);
        }
        Deque<int[]> queue = new ArrayDeque<int[]>();
        queue.offer(new int[]{0,0});
        data[0][0] = 1;
        while (!queue.isEmpty()){
            int[] poll = queue.poll();
            int x = poll[0];
            int y = poll[1];
            if (x==len-1&&y==len-1) return data[x][y];

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (x+dx<0||x+dx>=len||y+dy<0||y+dy>=len) continue;
                    if (data[x][y]+1>=data[x+dx][y+dy]||grid[x+dx][y+dy]==1) continue;

                    data[x+dx][y+dy] = data[x][y]+1;

                    queue.offer(new int[]{x+dx,y+dy});
                }
            }

        }

        return -1;

    }


    public int distMoney(int money, int children) {
        if(money<children) return -1;
        if(money/children >= 8) {
            if (children*8==money) return children;
            else return children-1;
        }

        money -= children;


       int count =  money /7;
       money -= 7*count;


       if (money==3&&count == children-1)
           return count-1;
       return count;

    }


    public static int func2(int i){
        int ans = 0;

        for (int j = 1; j <= i; j++) {
            ans += j;
        }
        return ans;
    }

    public static boolean wordPattern(String pattern, String s) {
        String[] split = s.split(" ");

        int length = split.length;
        int length1 = pattern.length();
        if (length!=length1) return false;
        Map<Character,String> data  = new HashMap<>();

        for (int i = 0; i < length; i++) {
            String str = data.get(pattern.charAt(i));
            if (str == null) {
                if (data.containsValue(split[i])) return false;
                data.put(pattern.charAt(i),split[i]);
                continue;
            }
            if (!str.equals(split[i])) return false;
        }
        return true;
    }
    public int maxProduct(String[] words) {
        int ans = 0;
        int length = words.length;
        if (length == ans ) return ans;
        int[] index = new int[length];
        for (int i = 0; i < length; i++) {
            String word = words[i];
            int len = word.length();
            for (int j = 0; j < len; j++) {
                index[i] |= 1<<(word.charAt(j) - 'a');
            }
        }

        for (int i = 0; i < length; i++) {
            for (int j = i; j < length; j++) {
                if ((index[i]&index[j])==0){
                    ans = Math.max(ans,words[i].length()*words[j].length());
                }
            }
        }
        return ans;
    }

    public String makeSmallestPalindrome(String s) {
        if(s.length()==0) return "";
        char[] data =  s.toCharArray();
        int font = 0;
        int rear = s.length()-1;

        while(font<=rear){
            if(data[font]>data[rear]){
                data[font] = data[rear];
            }else{
                data[rear] = data[font];
            }
            font++;
            rear--;
        }
        return new String(data);
    }

    public boolean isPalindrome(int x) {
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }

        List<Integer> data =   new ArrayList<>();

        while(x>0){
            data.add(x%10);
            x = x / 10;
        }


        int len =  data.size();
        int left = 0;
        int right = len-1;
        while(left<right){
            if (!data.get(left).equals(data.get(right))) return false;
            left++;
            right--;

        }
        return true;
    }

    public static boolean isValid2(String s) {
        StringBuilder builder = new StringBuilder();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (builder.length()>=3&&builder.substring(builder.length()-3).equals("abc")){
                builder.delete(builder.length()-3,builder.length());
            }
        }
        return builder.length() == 0;

    }


    public static int triangularSum(int[] nums) {
      int len =   nums.length;
      if (len == 1) return nums[0];
      Deque<Integer> queue = new ArrayDeque<Integer>();
        Deque<Integer> queue2 = new ArrayDeque<Integer>();
        for (int i = 0; i < len; i++) {
            queue.offer(nums[i]);
        }

        while (!queue.isEmpty()) {
            Integer head = queue.poll();
            if (!queue.isEmpty()){
                Integer second = queue.peek();
                int temp = (second+head)%10;
                queue2.offer(temp);
            }
            if (queue.isEmpty()&&queue2.size()>1) {
                queue = queue2;
                queue2 = new ArrayDeque<Integer>();
            }else if (queue.isEmpty()&&queue2.size()==1){
                break;
            }
        }
        return queue2.poll();
    }


    public int minPathSum(int[][] grid) {
        int y = grid.length;
        int x = grid[0].length;
        for (int i = 0; i < y; i++) {
            for (int k = 0; k < x; k++) {
                if (i==0&&k==0) continue;
                else if (i==0) grid[i][k] = grid[i][k] + grid[i][k-1];
                else if (k==0) grid[i][k] = grid[i][k] + grid[i-1][k];
                else grid[i][k] = grid[i][k] +  Math.min(grid[i][k-1],grid[i-1][k]);
            }
        }
        return grid[y-1][x-1];
    }

    public static boolean isSumEqual(String firstWord, String secondWord, String targetWord) {
        return (check(firstWord) + check(secondWord)) == check(targetWord);
    }

    public static int check(String firstWord){
        int f = 0;
        for (int length = firstWord.length(); length > 0; length--) {
            char c = firstWord.charAt(length-1);
            f +=  ((c - 'a')*Math.pow(10,firstWord.length()-length));
        }
        return f;
    }



    public int maximumValue(String[] strs) {
        int ans = Integer.MIN_VALUE;
        for(int i = 0;i<strs.length;i++){
            Boolean isMath = true;
            String str = strs[i];
            for (int k = 0; k < str.length(); k++) {
                if (str.charAt(k)-'0'<=9){
                    continue;
                }else {
                    isMath = false;
                    break;
                }
            }
            if (isMath){
                ans = Math.max(ans,Integer.parseInt(str));
            }else {
                ans = Math.max(ans,str.length());
            }

        }
        return ans;
    }


    public int passThePillow(int n, int time) {
        time %= ((n-1) *2);
        return time>n?n-(time-n-1):time+1;
    }


    public List<Integer> filterRestaurants(int[][] restaurants, int veganFriendly, int maxPrice, int maxDistance) {
        List<int[]> data = new ArrayList<int[]>();
        for (int[] restaurant : restaurants) {
            if (restaurant[3]<=maxPrice&&restaurant[4]<=maxDistance&&!(veganFriendly==1&&restaurant[2]==0)){
                data.add(restaurant);
            }
        }
        Collections.sort(data, (a, b) -> {
            if (a[1] != b[1]) {
                return b[1] - a[1];
            } else {
                return b[0] - a[0];
            }
        });
        List<Integer> ans = new ArrayList<Integer>();
        for (int[] datum : data) {
            ans.add(datum[0]);
        }

        return ans;
    }

    public static int countGoodSubstrings(String s) {
        int len = s.length();
        if(len<3) return 0;

        int count = 0;
        int head = 0;
        int tail = 3;


        for(;tail<=len;head++,tail++){
            if (check2(s.substring(head, tail))){
                count++;
            }
        }

        return count;
    }
    public static boolean check2(String s){
        if (s.charAt(0)!=s.charAt(1)&&s.charAt(1)!=s.charAt(2)&&s.charAt(2)!=s.charAt(0)){
            return true;
        }else return false;
    }



    public static int numFactoredBinaryTrees(int[] arr) {
        long ans =0,mod = 1000000007;
        int len = arr.length;
        long[] dp = new long[len];
        Arrays.sort(arr);
        for (int i = 0; i < len; i++) {
            dp[i] = 1;
            for (int left = 0,right = i-1;left<right;left++){
                while (left <= right&&arr[left]*arr[right]!= arr[i]){
                    right--;
                }
                if (right >= left &&arr[left]*arr[right]== arr[i]){
                    if (left!=right){
                        dp[i] =  ((dp[i] +(dp[left]*dp[right]*2))%mod);
                    }else {
                        dp[i] =  ((dp[i] +(dp[left]*dp[right]))%mod);
                    }
                }
            }

            ans =  (ans + dp[i])%mod;
        }

        return (int) ans;
    }


    public int matrixSum(int[][] nums) {
        int y = nums.length;
        int x = nums[0].length;

        for (int i = 0; i < y; i++) {
            Arrays.sort(nums[i]);
        }

        int ans = 0;
        for (int i = 0; i < x; i++) {
            int max = 0;
            for (int k = 0; k < y; k++) {
               max =  Math.max(max,nums[k][i]);
            }
            ans += max;
        }

        return ans;
    }

    public int maxProfit2(int[] prices,int fee) {
        int f0 = 0,f1 = Integer.MIN_VALUE;

        for (int price : prices) {
            int new_f0 =  Math.max(f0,f1+price);
            f1 = Math.max(f1,f0-price-2);
            f0 = new_f0;
        }
        return f0;
    }

    private int dfs(int[] prices, int i, boolean hold, Map<String, Integer> cache,int k) {


        if(k<0) return Integer.MIN_VALUE;
        if (i < 0) {
            return hold ? Integer.MIN_VALUE : 0;
        }

        String key = i + ":" + hold;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        int profit;
        if (hold) {
            profit = Math.max(dfs(prices, i - 1, true, cache,k), dfs(prices, i - 1, false, cache,k) - prices[i]);
        } else {
            profit = Math.max(dfs(prices, i - 1, false, cache,k), dfs(prices, i - 1, true, cache,k-1) + prices[i]);
        }

        cache.put(key, profit);

        return profit;
    }



    static class StockPrice {

        HashMap<Integer,Integer> data;
        TreeMap<Integer,Integer> prices;
        int currentIndex;

        public StockPrice() {
            data = new HashMap<Integer,Integer>();
            prices = new TreeMap<>();
            currentIndex = -1;
        }

        public void update(int timestamp, int price) {
            currentIndex = Math.max(timestamp,currentIndex);

            Integer pre = data.getOrDefault(timestamp, 0);
            data.put(timestamp,price);

            if (pre>0){
                prices.put(pre,prices.getOrDefault(pre,0)-1);
                if (prices.get(pre)==0){
                    prices.remove(pre);
                }
            }
            prices.put(pre,prices.getOrDefault(pre,0)+1);
        }


        public int current() {
            return data.get(currentIndex);
        }

        public int maximum() {
            return prices.lastKey();
        }

        public int minimum() {
            return prices.firstKey();
        }
    }

    public int splitNum(int num) {
        if (num<10) return num;
        List<Integer> data = new ArrayList<Integer>();
        while (num>0){
            data.add(num%10);
            num /= 10;
        }
        Collections.sort(data);
        int temp1 = data.get(0);
        int temp2 = 0;

        for (int i = 1; i < data.size(); i++) {
            Integer number = data.get(i);
            if (temp1*10+number>temp2*10+number){
                temp2 = temp2*10+number;
            }else {
                temp1 = temp1*10+number;
            }
        }
        return temp1 + temp2;
    }
    public static void test() {
        String url = "http://weather.uwyo.edu/cgi-bin/bufrraob.py?datetime=2023-10-10%2012:00:00&id=58457&type=TEXT:LIST";
        String url2 = "http://weather.uwyo.edu/cgi-bin/wyowx.fcgi?TYPE=sflist&DATE=current&HOUR=current&UNITS=A&STATION=ZGGG";
        String meta = "http://weather.uwyo.edu/surface/meteorogram/seasia.shtml";

        //请求列表页
        String listContent = HttpUtil.get(meta);
        //使用正则获取所有标题
        List<String> titles = ReUtil.findAll("<span class=\"text-ellipsis\">(.*?)</span>", listContent, 1);
        System.out.println(listContent);

    }


    public int[] avoidFlood(int[] rains) {
       int len = rains.length;
        int[] ans = new int[len];
        Arrays.fill(ans,1);
        TreeSet<Integer> st = new TreeSet<>();
        Map<Integer,Integer> map = new HashMap<>();

        for (int i = 0; i < len; i++) {
            if (rains[i] == 0) st.add(i);
            else {
                ans[i] = -1;
                if (map.containsKey(rains[i])){
                    Integer it = st.ceiling(rains[i]);
                    if (it==null) return new int[0];
                    ans[it] = rains[i];
                    st.remove(it);
                }
                map.put(rains[i],i);
            }
        }
        return  ans;
    }

    public int[] singleNumber2(int[] nums) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (int num : nums) {
            if (map.containsKey(num)) {
                Integer data = map.get(num);
                if (data == 2){
                    map.remove(num);
                }else {
                    map.put(num, data+1);
                }
                continue;
            }
            map.put(num,1);
        }
        Object[] integers = map.keySet().toArray();
        return  new int[]{(int)integers[0],(int)integers[1]};
    }
    public static int sumOfMultiples(int n) {
        int index3 = 3;
        int index5 = 5;
        int index7 = 7;
        int multiple = 1;
        int ans = 0;
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        while (index3 <= n){
            map.put(index3,index3);
            if (n>=index5){
                map.put(index5,index5);
            }
            if (n>=index7){
                map.put(index7,index7);
            }
            multiple++;
            index3 = 3*multiple;
            index5 = 5*multiple;
            index7 = 7*multiple;
        }


        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            ans += entry.getKey();
        }
        return ans;


    }

    public long maxKelements(int[] nums, int k) {
        PriorityQueue<Integer> que = new PriorityQueue<Integer>((a,b)->b-a);
        for (int num : nums) {
            que.offer(num);
        }
        int ans = 0;
        for (int i = 0; i < k; i++) {
            int num = que.poll();
            ans += num;
            que.offer((num+2)/3);
        }
        return ans;
    }


    public int tupleSameProduct(int[] nums) {
        int len = nums.length;
        Map<Integer,Integer> map = new HashMap<Integer,Integer>();
        for (int i = 0; i < len; i++) {
            for (int j = 1; j < len; j++) {
                map.put(nums[i]*nums[j],map.getOrDefault(nums[i]*nums[j],0)+1);
            }
        }

        int ans = 0;
        for (Integer val : map.values()) {
            ans += val*(val-1)*4;
        }
        return ans;
    }

    public String categorizeBox(int length, int width, int height, int mass) {
        long vol = (long) length * width *height;
        String status = "";

        if (length>=1000||width>=1000||height>=1000||vol>=1000000000){
            status = "Bulky";
        }

        if (mass>=100){
            if (status.equals("")) return "Heavy";
            else return "Both";
        }
        if (status.equals("")) status = "Neither";

        return status;
    }

//    10 + 1 + 2
    public int countSeniors(String[] details) {
        int ans = 0;
        for (String detail : details) {
           int age =  (detail.charAt(11) - '0')*10 + detail.charAt(12) - '0';

           if (age>60){
               ans++;
           }
        }
        return  ans;
    }

    public int hIndex(int[] citations) {
        int len =  citations.length;
        int left = 0,right = len-1;

        while (left > right){
            int mid = left + (right - left)/2;
            if (citations[mid]<=len-mid) right = mid-1;
            else left = mid+1;
        }
        return len - left;
    }
    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    };

    public Node connect(Node root) {
        if (root == null) return null;
        Deque<Node> queue = new ArrayDeque<Node>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node last  =null;
            int size = queue.size();
            for (int i = 1; i < size; i++) {
                Node f = queue.poll();
                if (f.left != null) queue.offer(f.left);
                if (f.right != null) queue.offer(f.right);
                if (size!=1) last.next = f;
                last = f;
            }
        }
        return root;
    }

    public int maxSubArray(int[] nums) {
        int len = nums.length;
        int[] dp = new int[len];

        dp[0] = nums[0];
        for (int i = 1; i < len; i++) {
            if (dp[i-1]>0) dp[i] = dp[i-1]+nums[i];
            else dp[i] = nums[i];
        }

        int ans = dp[0];

        for (int i = 1; i < len; i++) {
            ans = Math.max(ans,dp[i]);
        }

        return ans;


    }

    public TreeNode reverseOddLevels(TreeNode root) {
        Deque<TreeNode> queue = new ArrayDeque<TreeNode>();
        queue.offer(root);
        boolean odd = false;
        while (!queue.isEmpty()){
            int size = queue.size();
            List<TreeNode> list = new ArrayList<TreeNode>();
            for (int i = 0; i < size; i++) {
                TreeNode poll = queue.poll();
                if (odd){
                    list.add(poll);
                }
                if (poll.left!=null){
                    queue.offer(poll.left);
                    queue.offer(poll.right);
                }
            }
            if (odd) {
                for(int l = 0,r = list.size()-1;l<r;l++,r--){
                    int temp = list.get(l).val;
                    list.get(l).val = list.get(r).val;
                    list.get(r).val = temp;
                }
            }
            odd = !odd;
        }
        return root;
    }
    public static List<Integer> numOfBurgers(int tomatoSlices, int cheeseSlices) {
        if(tomatoSlices%2!=0) return new ArrayList<Integer>();

        int surplus =  tomatoSlices - cheeseSlices*2;

        if(surplus<0) return new ArrayList<Integer>();
        System.out.println(surplus);
        int bigCount =  surplus / 2;
        List<Integer> ans =  new ArrayList<>();
        ans.add(bigCount);
        ans.add(cheeseSlices - bigCount);
        return ans;
    }
    public static int buyChoco(int[] prices, int money) {
        Map<Integer,Integer> map = new HashMap<>();

        for(int i = 0;i<prices.length;i++){
            int a = money - prices[i];
            if(a>=0) continue;
            Integer index =  map.get(a);
            if (index!=null) return 0;
            map.put(prices[i],i);
        }
        List<Integer> sortedKeys = map.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        int ans =money- sortedKeys.get(0) - sortedKeys.get(1);
        if (ans<0) return money;
        return ans;
    }


    public int numberOfBoomerangs(int[][] points) {
        int ans = 0;
        for (int[] point : points) {
            Map<Integer, Integer> map = new HashMap<>();
            for (int[] point1 : points) {
                int dis = (point[0] - point1[0]) * (point[0] - point1[0]) + (point[1] - point1[1]) * (point[1] - point1[1]);
                map.put(dis, map.getOrDefault(dis, 0) + 1);
            }

            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                int val = entry.getValue();
                ans += val*(val-1);
            }
        }
        return ans;
    }

    public int minExtraChar(String s, String[] dictionary) {
        int len = s.length();
        int[] dp = new int[len +1];
        Set<String> set  = new HashSet<>();
        for(String str : dictionary){
            set.add(str);
        }

        dp[0] = 0;

        for(int i = 1;i<=len;i++){
            dp[i] = dp[i-1]+1;
            for(int j = i-1;j>=0;j--){
                if(set.contains(s.substring(j,i))){
                    dp[i] = Math.min(dp[i],dp[j]);
                }
            }
        }

        return dp[len];
    }

    public ListNode deleteDuplicates(ListNode head) {
        ListNode ans = new ListNode(0,head);
        head = ans;
        while(head.next!=null&&head.next.next!=null){
            int val = head.next.val;
            if(val == head.next.next.val){
                while(head.next!=null&&head.next.val==val){
                    head.next = head.next.next;
                }
            }else{
                head = head.next;
            }
        }
        return ans.next;
    }
    public int minLength(String s) {
        List<Character> stack  = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int size = stack.size();
            if(size>=2&&stack.get(size-1)=='B'&&stack.get(size-2)=='A'||
                size>=2&&stack.get(size-1)=='D'&&stack.get(size-2)=='C'){
                stack.remove(size-1);
                stack.remove(size-2);
            }
            stack.add(c);
        }
        return stack.size();
    }

    public static int countWords(String[] words1, String[] words2) {
        Map<String,Integer> map = new HashMap<>();
        Map<String,Integer> map2 = new HashMap<>();
        for(String s : words1){
            map.put(s,map.getOrDefault(s,0));
        }

        for(String s : words2){
            map2.put(s,map2.getOrDefault(s,0));
        }

        int ans = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue()==1&&map2.get(entry.getKey())==1) ans++;
        }
        return  ans;
    }
    public static void main(String[] args) {
        countWords(new String[]{"leetcode","is","amazing","as","is"},new String[]{"amazing","leetcode","is"});
    }

    public int addMinimum(String word) {
        int length = word.length();
        int[] dp = new int[length+1];
        for (int i = 1; i <= length; i++) {
            dp[i] = dp[i-1]+2;
            if(i>1&&word.charAt(i-1)<word.charAt(i-2)){
                dp[i] = dp[i-1]-1;
            }
        }
        return dp[length];
    }
    @Data
    static class MetaData{
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT")
        private Date datetime;
        private List<Station> stations = new ArrayList<>();
    }
    @Data
    static class Station{
        private String stationid;
        private Double lat;
        private Double lon;
        private String src;
        private String name;
    }
}

