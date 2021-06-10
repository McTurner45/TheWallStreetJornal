package com.biust.ac.bw.thewallstreetjornal;

import java.util.ArrayList;

public class InsetionSort {
    public void ascendingSort(ArrayList<Journal> journals) {
        for (int i = 1; i < journals.size(); ++i) {

            Journal key = journals.get(i);

            int j = i - 1;

            /* Move elements of arr[0..i-1], that are

               greater than key, to one position ahead

               of their current position */

            while (j >= 0 && Integer.valueOf(journals.get(j).getTitle()) > Integer.valueOf(key.getTitle())) {

                journals.add(j + 1, journals.get(j));

                j = j - 1;

            }
            journals.add(j + 1, key);

        }

    }
}
