package org.sufficientlysecure.htmltextview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.QuoteSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

public class MyHtmlTextView extends HtmlTextView {
    public MyHtmlTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyHtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHtmlTextView(Context context) {
        super(context);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text instanceof Spannable){
            replaceQuoteSpans((Spannable) text);
        }

        super.setText(text, type);
    }

    private void replaceQuoteSpans(Spannable spannable) {
        QuoteSpan[] quoteSpans = spannable.getSpans(0, spannable.length(), QuoteSpan.class);

        for (QuoteSpan quoteSpan : quoteSpans) {
            int start = spannable.getSpanStart(quoteSpan);
            int end = spannable.getSpanEnd(quoteSpan);
            int flags = spannable.getSpanFlags(quoteSpan);
            spannable.removeSpan(quoteSpan);
            spannable.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannable.length(), 0);
            spannable.setSpan(new CustomQuoteSpan(
                            Color.parseColor("#ededed"), //MY_BACKGROUND_COLOR,
                            Color.parseColor("#777777"), //MY_STRIPE_COLOR,
                            8, //MY_STRIPE_WIDTH,
                            16  //MY_GAP_WIDTH
                    ),
                    start,
                    end,
                    flags);
        }
    }
}
