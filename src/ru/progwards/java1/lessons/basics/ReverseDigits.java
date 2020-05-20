package ru.progwards.java1.lessons.basics;

public class ReverseDigits {
    // прогамма составляет цифры трёхзначного десятичного числа в обратном порядке
    public static int reverseDigits(int number) {
        System.out.println("number="+number);
        int lowDigit = number % 10;
        number /= 10;

        int middleDigit = number % 10;
        number /= 10;

        int highDigit = number % 10;

        return lowDigit * 100 + middleDigit * 10 + highDigit;
    }

    public static void main(String[] args) {
        System.out.println(reverseDigits(678));
    }
}
