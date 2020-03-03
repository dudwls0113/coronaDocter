package com.softsquared.android.corona.src.main.community;

import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import org.xml.sax.XMLReader;
public class HtmlTagHandler implements Html.TagHandler {
    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.equals("hr")) {
            handleHrTag(opening, output);
        }
    }
    private void handleHrTag(boolean opening, Editable output) {
        final String placeholder = "\n-\n";
        if (opening) {
            output.insert(output.length(), placeholder);
        } else {
            output.setSpan(new HrSpan(), output.length() - placeholder.length(), output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}