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
        else if (width <= -3) {
            width = layoutParams.width;
        }

        if (height >= 0) {
            height *= scaleFactor;
        }
        else if (width <= -3) {
            height = layoutParams.height;
        }

        layoutParams.width = width;
        layoutParams.height = height;

        view.setLayoutParams(layoutParams);
    }

    public static void setViewWidth(View view, int width) {
        setViewSize(view, width, -3);
    }

    public static void setViewHeight(View view, int height) {
        setViewSize(view, -3, height);
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

    public static void setPadding(View view, int leftPadding, int topPadding, int rightPadding, int bottomPadding) {
        float scaleFactor = SuperActivity.getScaleFactor();

        if (leftPadding >= 0) {
            leftPadding *= scaleFactor;
        }
        else {
            leftPadding = view.getPaddingLeft();
        }

        if (rightPadding >= 0) {
            rightPadding *= scaleFactor;
        }
        else {
            rightPadding = view.getPaddingRight();
        }

        if (topPadding >= 0) {
            topPadding *= scaleFactor;
        }
        else {
            topPadding = view.getPaddingTop();
        }

        if (bottomPadding >= 0) {
            bottomPadding *= scaleFactor;
        }
        else {
            bottomPadding = view.getPaddingBottom();
        }

        view.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
    }

    public static void setLeftAndTopPadding(View view, int leftPadding, int topPadding) {
        setPadding(view, leftPadding, topPadding, -1, -1);
    }

    public static void setLeftPadding(View view, int leftPadding) {
        setPadding(view, leftPadding, -1, -1, -1);
    }

    public static void setTopPadding(View view, int topPadding) {
        setPadding(view, -1, topPadding, -1, -1);
    }

    public static void setRightPadding(View view, int rightPadding) {
        setPadding(view, -1, -1, rightPadding, -1);
    }

    public static void setBottomPadding(View view, int bottomPadding) {
        setPadding(view, -1, -1, -1, bottomPadding);
    }
}
