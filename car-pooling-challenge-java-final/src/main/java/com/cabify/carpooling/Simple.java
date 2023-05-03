package com.cabify.carpooling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simple {


    public static void main(String[] args) {

        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(4);


        List<Integer> biggestNumbers = getBiggestNumbers(numbers, 3);

        biggestNumbers.forEach(
                n -> System.out.println(n)
        );


    }

    private static List<Integer> getBiggestNumbers(List<Integer> numbers, int quantity) {

        if (quantity > numbers.size()){
            quantity = numbers.size();
        }

        Collections.sort(numbers);
        Collections.reverse(numbers);

        return numbers.subList(0,quantity);

    }

}
