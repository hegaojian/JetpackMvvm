package me.hgj.jetpackmvvm.widget.loadsir.callback;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.StyleRes;

/**
 * Description:TODO
 * Create Time:2017/10/9 14:12
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class ProgressCallback extends Callback {

    private String title;
    private String subTitle;
    private int subTitleStyleRes = -1;
    private int titleStyleRes = -1;

    private ProgressCallback(Builder builder) {
        this.title = builder.title;
        this.subTitle = builder.subTitle;
        this.subTitleStyleRes = builder.subTitleStyleRes;
        this.titleStyleRes = builder.titleStyleRes;
        setSuccessVisible(builder.aboveable);
    }

    @Override
    protected int onCreateView() {
        return 0;
    }

    @Override
    protected View onBuildView(Context context) {
        return new LinearLayout(context);
    }

    @Override
    protected void onViewCreate(Context context, View view) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        LinearLayout ll = (LinearLayout) view;
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);

        ProgressBar progressBar = new ProgressBar(context);
        ll.addView(progressBar, lp);

        if (!TextUtils.isEmpty(title)) {
            TextView tvTitle = new TextView(context);
            tvTitle.setText(title);
            if (titleStyleRes == -1) {
                tvTitle.setTextAppearance(context, android.R.style.TextAppearance_Large);
            } else {
                tvTitle.setTextAppearance(context, titleStyleRes);
            }
            ll.addView(tvTitle, lp);
        }
        if (!TextUtils.isEmpty(subTitle)) {
            TextView tvSubtitle = new TextView(context);
            tvSubtitle.setText(subTitle);
            if (subTitleStyleRes == -1) {
                tvSubtitle.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            } else {
                tvSubtitle.setTextAppearance(context, subTitleStyleRes);
            }
            ll.addView(tvSubtitle, lp);
        }
    }

    public static class Builder {

        private String title;
        private String subTitle;
        private int subTitleStyleRes = -1;
        private int titleStyleRes = -1;
        private boolean aboveable;

        public Builder setTitle(String title) {
            return setTitle(title, -1);
        }

        public Builder setTitle(String title, @StyleRes int titleStyleRes) {
            this.title = title;
            this.titleStyleRes = titleStyleRes;
            return this;
        }

        public Builder setSubTitle(String subTitle) {
            return setSubTitle(subTitle, -1);
        }

        public Builder setSubTitle(String subTitle, @StyleRes int subTitleStyleRes) {
            this.subTitle = subTitle;
            this.subTitleStyleRes = subTitleStyleRes;
            return this;
        }

        public Builder setAboveSuccess(boolean aboveable) {
            this.aboveable = aboveable;
            return this;
        }

        public ProgressCallback build() {
            return new ProgressCallback(this);
        }
    }
}