package at.autrage.projects.zeta.module;


import android.view.View;
import android.view.ViewGroup;

import at.autrage.projects.zeta.activity.SuperActivity;

public class Util {
    public static String addLeadingZeros(int src, int length, boolean seperator) {
        String out = "";
        String str = ""+src;

        for (int i = 0, j = str.length() - 1; i < length; i++) {
            if (seperator && i != 0 && i % 3 == 0) {
                out = "," + out;
            }

            if (j >= 0) {
                out = str.charAt(j) + out;
                j--;
            }
            else {
                out = "0" + out;
            }
        }

        return out;
    }

    public static int roof(float value) {
        if (value > (int) value) {
            return (int) (value + 1);
        }
        else {
            return (int) value;
        }
    }

    public static int roof(double value) {
        if (value > (int) value) {
            return (int) (value + 1);
        }
        else {
            return (int) value;
        }
    }

    public static void setViewSize(View view, int width, int height) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();

        float scaleFactor = SuperActivity.getScaleFactor();

        if (width >= 0) {
            width *= scaleFactor;
        }
        else {
            width = layoutParams.width;
        }

        if (height >= 0) {
            height *= scaleFactor;
        }
        else {
            height = layoutParams.height;
        }

        layoutParams.width = width;
        layoutParams.height = height;

        view.setLayoutParams(layoutParams);
    }

    public static void setViewWidth(View view, int width) {
        setViewSize(view, width, -1);
    }

    public static void setViewHeight(View view, int height) {
        setViewSize(view, -1, height);
    }

    public static void setMargin(View view, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();

        float scaleFactor = SuperActivity.getScaleFactor();

        if (leftMargin >= 0) {
            leftMargin *= scaleFactor;
        }
        else {
            leftMargin = layoutParams.leftMargin;
        }

        if (rightMargin >= 0) {
            rightMargin *= scaleFactor;
        }
        else {
            rightMargin = layoutParams.rightMargin;
        }

        if (topMargin >= 0) {
            topMargin *= scaleFactor;
        }
        else {
            topMargin = layoutParams.topMargin;
        }

        if (bottomMargin >= 0) {
            bottomMargin *= scaleFactor;
        }
        else {
            bottomMargin = layoutParams.bottomMargin;
        }

        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        view.setLayoutParams(layoutParams);
    }

    public static void setLeftAndTopMargin(View view, int leftMargin, int topMargin) {
        setMargin(view, leftMargin, topMargin, -1, -1);
    }

    public static void setLeftMargin(View view, int leftMargin) {
        setMargin(view, leftMargin, -1, -1, -1);
    }

    public static void setTopMargin(View view, int topMargin) {
        setMargin(view, -1, topMargin, -1, -1);
    }

    public static void setRightMargin(View view, int rightMargin) {
        setMargin(view, -1, -1, rightMargin, -1);
    }

    public static void setBottomMargin(View view, int bottomMargin) {
        setMargin(view, -1, -1, -1, bottomMargin);
    }
}
