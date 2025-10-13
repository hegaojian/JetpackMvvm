package me.hgj.jetpackmvvm.widget.alpha;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public final class UIResHelper {

    private static TypedValue sTmpValue;

    public static float getAttrFloatValue(Context context, int attr) {
        return getAttrFloatValue(context.getTheme(), attr);
    }

    public static float getAttrFloatValue(Resources.Theme theme, int attr) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!theme.resolveAttribute(attr, sTmpValue, true)) {
            return 0;
        }
        return sTmpValue.getFloat();
    }

    public static int getAttrColor(Context context, int attrRes) {
        return getAttrColor(context.getTheme(), attrRes);
    }

    public static int getAttrColor(Resources.Theme theme, int attr) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!theme.resolveAttribute(attr, sTmpValue, true)) {
            return 0;
        }
        if (sTmpValue.type == TypedValue.TYPE_ATTRIBUTE) {
            return getAttrColor(theme, sTmpValue.data);
        }
        return sTmpValue.data;
    }

    @Nullable
    public static ColorStateList getAttrColorStateList(Context context, int attrRes) {
        return getAttrColorStateList(context, context.getTheme(), attrRes);
    }

    @Nullable
    public static ColorStateList getAttrColorStateList(Context context, Resources.Theme theme, int attr) {
        if (attr == 0) {
            return null;
        }
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!theme.resolveAttribute(attr, sTmpValue, true)) {
            return null;
        }
        if (sTmpValue.type >= TypedValue.TYPE_FIRST_COLOR_INT
                && sTmpValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return ColorStateList.valueOf(sTmpValue.data);
        }
        if (sTmpValue.type == TypedValue.TYPE_ATTRIBUTE) {
            return getAttrColorStateList(context, theme, sTmpValue.data);
        }
        if (sTmpValue.resourceId == 0) {
            return null;
        }
        return ContextCompat.getColorStateList(context, sTmpValue.resourceId);
    }


    @Nullable
    public static String getAttrString(Context context, int attrRes) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!context.getTheme().resolveAttribute(attrRes, sTmpValue, true)) {
            return null;
        }
        CharSequence str = sTmpValue.string;
        return str == null ? null : str.toString();
    }

    public static int getAttrInt(Context context, int attrRes) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        context.getTheme().resolveAttribute(attrRes, sTmpValue, true);
        return sTmpValue.data;
    }

    public static int getAttrDimen(Context context, int attrRes) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!context.getTheme().resolveAttribute(attrRes, sTmpValue, true)) {
            return 0;
        }
        return TypedValue.complexToDimensionPixelSize(sTmpValue.data,context.getResources().getDisplayMetrics());
    }
}
