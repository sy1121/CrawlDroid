package edu.iscas.expdroidclient.tools;

import java.util.Comparator;

import android.view.View;

public class  EspressoViewComparator  implements Comparator<View> {

    @Override
    public int compare(View v1, View v2) {
        int[] l1 = {0,0};
        int[] l2 = {0,0};
        v1.getLocationOnScreen(l1);
        v2.getLocationOnScreen(l2);
        if (l1[0] == l2[0]){
            return (l1[1] == l2[1])? 0 : (l1[1] < l2[1])? -1 : 1;
        } else if (l1[0] < l2[0]) {
            return (l1[1] == l2[1])? -1 : (l1[1] < l2[1])? -1 : 1;
        } else {
            return (l1[1] == l2[1])? 1 : (l1[1] < l2[1])? -1 : 1;
        }
    }
}
