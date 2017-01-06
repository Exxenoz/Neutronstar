package at.autrage.projects.zeta.module;


import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import at.autrage.projects.zeta.activity.MainActivity;
import at.autrage.projects.zeta.activity.SuperActivity;

public class Util {
    public static String addLeadingZeros(int src, int length, boolean seperator) {
        String out = "";
        String str = ""+src;
        String sep = MainActivity.getSeparatorForNumbers();

        for (int i = 0, j = str.length() - 1; i < length; i++) {
            if (seperator && i != 0 && i % 3 == 0) {
                out = sep + out;
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

    public static void setViewMargin(View view, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
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

    public static void setViewLeftAndTopMargin(View view, int leftMargin, int topMargin) {
        setViewMargin(view, leftMargin, topMargin, -1, -1);
    }

    public static void setViewLeftMargin(View view, int leftMargin) {
        setViewMargin(view, leftMargin, -1, -1, -1);
    }

    public static void setViewTopMargin(View view, int topMargin) {
        setViewMargin(view, -1, topMargin, -1, -1);
    }

    public static void setViewRightMargin(View view, int rightMargin) {
        setViewMargin(view, -1, -1, rightMargin, -1);
    }

    public static void setViewBottomMargin(View view, int bottomMargin) {
        setViewMargin(view, -1, -1, -1, bottomMargin);
    }

    public static void setViewPadding(View view, int leftPadding, int topPadding, int rightPadding, int bottomPadding) {
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

    public static void setViewLeftAndTopPadding(View view, int leftPadding, int topPadding) {
        setViewPadding(view, leftPadding, topPadding, -1, -1);
    }

    public static void setViewLeftPadding(View view, int leftPadding) {
        setViewPadding(view, leftPadding, -1, -1, -1);
    }

    public static void setViewTopPadding(View view, int topPadding) {
        setViewPadding(view, -1, topPadding, -1, -1);
    }

    public static void setViewRightPadding(View view, int rightPadding) {
        setViewPadding(view, -1, -1, rightPadding, -1);
    }

    public static void setViewBottomPadding(View view, int bottomPadding) {
        setViewPadding(view, -1, -1, -1, bottomPadding);
    }

    public static void resetViewLayoutRules(View view) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)view.getLayoutParams();
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.removeRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.removeRule(RelativeLayout.CENTER_VERTICAL);
    }

    public static void addViewLayoutRule(View view, int rule) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)view.getLayoutParams();
        layoutParams.addRule(rule);
    }
}
