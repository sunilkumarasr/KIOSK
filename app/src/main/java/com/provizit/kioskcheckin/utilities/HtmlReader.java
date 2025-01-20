package com.provizit.kioskcheckin.utilities;

import android.text.Html;
import android.text.Spanned;

import java.io.Serializable;

public class HtmlReader implements Serializable {

    public static Spanned readHtml(String htmlText) {

        return Html.fromHtml(htmlText);
    }
}
