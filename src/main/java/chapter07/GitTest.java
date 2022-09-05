package chapter07;

import java.util.ArrayList;
import java.util.List;

public class GitTest {
    public static void main(String[] args) {
//        System.out.println("Hello git1");
//        System.out.println("Hello git2");
//        System.out.println("Hello git4");
//
//        System.out.println("Hello master test");
//        System.out.println("Hello hot-fix test");
        new GitTest().spiralOrder(new int[][]{{1,2,3},{4,5,6},{7,8,9}});

    }
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> ans = new ArrayList<>();
        int up = 0;
        int down = matrix.length - 1;
        int left = 0;
        int right = matrix[0].length - 1;
        while(true){
            for(int i = left;i <= right;i ++){
                ans.add(matrix[up][i]);
            }
            up ++;
            if(up > down) break;
            for(int i = up;i <= down;i ++){
                ans.add(matrix[i][right]);
            }
            right --;
            if(right < left) break;
            for(int i = right;i >= left;i --){
                ans.add(matrix[down][i]);
            }
            down --;
            if(down < up) break;
            for(int i = down;i >= up;i --){
                ans.add(matrix[i][left]);
            }
            left ++;
            if(left > right) break;
        }
        return ans;
    }
}
