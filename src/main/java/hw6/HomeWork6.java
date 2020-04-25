package hw6;

import java.util.Arrays;

public class HomeWork6 {

//    public static void main(String[] args) {
//        System.out.println(Arrays.toString(returnAfterFours(new int[]{4, 2, 3, 1, 2})));
//    }

    public int[] returnAfterFours(int[] arr) throws RuntimeException {
        int[] result;
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == 4) {
                if (i == arr.length - 1) {
                    throw new FourIsInTheEnd();
                }
                result = new int[arr.length - i - 1];
                for (int j = 0; j < result.length; j++) {
                    result[j] = arr[i + j + 1];
                }
                return result;
            }
        }

        throw new NoFoursInArray();
    }

    public boolean lookForOnesAndFours(int[] arr) {
        for (int el : arr) {
            if (el == 1 || el == 4) {
                return true;
            }
        }
        return false;
    }

    static class FourIsInTheEnd extends RuntimeException {

    }

    static class NoFoursInArray extends RuntimeException {

    }
}
