package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class algo {

    public static void main(String[] args) {

    }
    public List<Integer> majorityElement(int[] nums) {

        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> ans = new ArrayList<>();
        for (int i: nums) {
            map.merge(i,1, Integer::sum);
        }
        int min = nums.length/3;
        for (Map.Entry<Integer, Integer> val: map.entrySet()) {
            if (val.getValue() > min) {
                ans.add(val.getKey());
            }
        }
        return ans;
    }
}
