package qmul.se.g31.comparechain.GUIClasses;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 12/03/2018.
 */

public class NewsView extends Fragment {

    private WebView mWebView;
    View myView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_news, container, false);
        hideKeyboard(getActivity());

        mWebView = (WebView) myView.findViewById(R.id.news_webview);
        //mWebView.loadUrl("http://google.com");

        String html = "<iframe width=\"100%\" height=\"670px\" style=\"frameborder: 0;\" src=\"https://feed.mikle.com/widget/v2/66981/\" ></iframe>";
        mWebView.loadData(html, "text/html", null);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient());
        return myView;


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
