package moe.shizuku.bridge.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import moe.shizuku.bridge.R;

/**
 * Created by Rikka on 2017/4/2.
 */

public class HtmlTextView extends TextView {

    public HtmlTextView(Context context) {
        this(context, null);
    }

    public HtmlTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HtmlTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HtmlTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        // Attribute initialization.
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HtmlTextView,
                defStyleAttr, 0);

        String html = a.getString(R.styleable.HtmlTextView_textHtml);
        if (html != null) {
            setText(Html.fromHtml(html));
        }

        a.recycle();
    }
}
