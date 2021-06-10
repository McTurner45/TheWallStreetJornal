package com.biust.ac.bw.thewallstreetjornal;

import android.util.Log;

import java.math.BigInteger;
import java.util.ArrayList;

public class BubbleSort {
    public ArrayList<Journal> ascendingBubbleSort(ArrayList<Journal> journals) {
        int i, j;

        for (i = 0; i < journals.size(); i++) {
            for (j = 0; j < journals.size() - i; j++) {
                int value=journals.get(j).getTitle().compareToIgnoreCase(journals.get(j + 1).getTitle());
                if (value>0) {
                    //swapping
                    Journal temp= new Journal(journals.get(j).getDate(),
                            journals.get(j).getTitle(),
                            journals.get(j).getContent(),
                            journals.get(j).getId());
                    journals.add(j, journals.get(j + 1));
                    journals.add(j, temp);
                }
            }
        }

        return journals;

    }

    public ArrayList<Journal> descendingBubbleSort(ArrayList<Journal> journals) {
        int i, j;
        Journal temp;

        for (i = 0; i < journals.size(); i++) {
            for (j = 0; j < journals.size() - i; j++) {
                int value=journals.get(j).getTitle().compareToIgnoreCase(journals.get(j + 1).getTitle());
                if (value<0) {
                    //swapping
                    temp = journals.get(j);
                    journals.add(j, journals.get(j + 1));
                    journals.add(j, temp);
                }
            }
        }

        return journals;

    }

}