package com.example;

import java.util.ArrayList;

public class HeapSort {

    public void heapSort(ArrayList<Double> list) {
        int n = list.size();

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(list, n, i);
        }

        for (int i = n - 1; i >= 0; i--) {
            double temp = list.get(0);
            list.set(0, list.get(i));
            list.set(i, temp);

            heapify(list, i, 0);
        }
    }

    private void heapify(ArrayList<Double> list, int n, int i) {
        int largest = i; // Inicializar o maior como raiz
        int left = 2 * i + 1; 
        int right = 2 * i + 2;

        if (left < n && list.get(left) > list.get(largest)) {
            largest = left;
        }


        if (right < n && list.get(right) > list.get(largest)) {
            largest = right;
        }

        if (largest != i) {
            double swap = list.get(i);
            list.set(i, list.get(largest));
            list.set(largest, swap);

            heapify(list, n, largest);
        }
    }
    
    public void printArray(double[] array) {
        for (double i : array) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

}
