package com.provizit.kioskcheckin.api;

import android.content.Context;
import android.util.Log;


import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.io.InputStream;

public class AESUtil {
    Context context;
    InputStream inputStream;
    org.mozilla.javascript.Context rhinoContext;
    Scriptable scope;

    public AESUtil(Context context) {
        this.context = context;
    }

    public void setvalues() {

        inputStream = context.getResources().openRawResource(
                context.getResources().getIdentifier("aes",
                        "raw", context.getPackageName()));
        String data = convertStreamToString(inputStream);
        Log.d("animesh_data", data);

        // Create a new Rhino context explicitly
        rhinoContext = org.mozilla.javascript.Context.enter();

        // Turn off optimization to make Rhino Android compatible
        rhinoContext.setOptimizationLevel(-1);
        scope = rhinoContext.initStandardObjects();
        rhinoContext.evaluateString(scope, data, "JavaScript", 0, null);
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public String encrypt(String data, String key) {

        setvalues();
        Object obj = scope.get("encryption", scope);
        if (obj instanceof Function) {
            Function jsFunction = (Function) obj;
            Object[] params = new Object[]{data, key};

            // Call the function with params
            Object jsResult = jsFunction.call(rhinoContext, scope, scope, params);
            // Parse the jsResult object to a String
            String result = org.mozilla.javascript.Context.toString(jsResult);
            return result;
        }
        return null;
    }


}
