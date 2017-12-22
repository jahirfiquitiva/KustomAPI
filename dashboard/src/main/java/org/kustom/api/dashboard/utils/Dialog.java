package org.kustom.api.dashboard.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.kustom.api.dashboard.R;

@SuppressWarnings("WeakerAccess")
public class Dialog implements View.OnClickListener {
    private final View mView;
    private AlertDialog mDialog;
    private ButtonCallback mCallback;

    public static final int BUTTON_POSITIVE = R.id.button_positive;
    public static final int BUTTON_NEUTRAL = R.id.button_neutral;
    public static final int BUTTON_NEGATIVE = R.id.button_negative;

    public Dialog(Builder builder) {
        Context context = ThemeHelper.getDialogThemedContext(builder.mContext);
        mCallback = builder.mButtonCallback;
        mView = View.inflate(context, R.layout.kustom_dashboard_dialog, null);
        ((TextView) mView.findViewById(R.id.title)).setText(builder.getTitle());
        ((TextView) mView.findViewById(R.id.content)).setText(builder.getContent());
        setButtonText(BUTTON_POSITIVE, builder.getPositiveText());
        setButtonText(BUTTON_NEUTRAL, builder.getNeutralText());
        setButtonText(BUTTON_NEGATIVE, builder.getNegativeText());
        mDialog = new AlertDialog
                .Builder(context)
                .setView(mView)
                .create();
    }

    @Override
    public void onClick(View v) {
        if (mCallback != null) mCallback.onButtonClick(v, v.getId());
        try {
            mDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        mDialog.show();
    }

    private void setButtonText(@IdRes int id, String text) {
        View button = mView.findViewById(id);
        if (button instanceof Button) {
            button.setOnClickListener(this);
            button.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
            ((Button) button).setText(text);
        }
    }

    public static class Builder {
        private final Context mContext;
        private String mTitle = "";
        private int mTitleId = 0;
        private String mContent = "";
        private int mContentId = 0;
        private int mPositiveTextId = 0;
        private int mNeutralTextId = 0;
        private int mNegativeTextId = 0;
        private ButtonCallback mButtonCallback;

        public Builder(Context context) {
            mContext = context;
        }

        public Dialog build() {
            return new Dialog(this);
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setTitle(@StringRes int title) {
            mTitleId = title;
            return this;
        }

        public Builder setContent(@StringRes int content) {
            mContentId = content;
            return this;
        }

        public Builder setContent(String content) {
            mContent = content;
            return this;
        }

        public Builder setButtonCallback(ButtonCallback buttonCallback) {
            mButtonCallback = buttonCallback;
            return this;
        }

        public Builder setPositiveText(@StringRes int positiveText) {
            mPositiveTextId = positiveText;
            return this;
        }

        public Builder setNeutralText(@StringRes int neutralTextId) {
            mNeutralTextId = neutralTextId;
            return this;
        }

        public Builder setNegativeText(@StringRes int negativeTextId) {
            mNegativeTextId = negativeTextId;
            return this;
        }

        public void show() {
            build().show();
        }

        String getTitle() {
            return mTitleId != 0 ? mContext.getString(mTitleId) : mTitle;
        }

        String getContent() {
            return mContentId != 0 ? mContext.getString(mContentId) : mContent;
        }

        String getPositiveText() {
            return mPositiveTextId != 0 ? mContext.getString(mPositiveTextId) : "";
        }

        String getNegativeText() {
            return mNegativeTextId != 0 ? mContext.getString(mNegativeTextId) : "";
        }

        String getNeutralText() {
            return mNeutralTextId != 0 ? mContext.getString(mNeutralTextId) : "";
        }
    }

    public interface ButtonCallback {
        void onButtonClick(View view, int id);
    }
}
