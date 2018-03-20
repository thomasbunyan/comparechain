package qmul.se.g31.comparechain;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Thomas on 12/03/2018.
 */

public class NewsView extends Fragment {

    private WebView mWebView;
    View myView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_news, container, false);

        mWebView = (WebView) myView.findViewById(R.id.news_webview);
        //mWebView.loadUrl("http://google.com");

        String html = "<iframe width=\"100%\" height=\"670px\" style=\"frameborder: 0;\" src=\"https://feed.mikle.com/widget/v2/66981/\" ></iframe>";
        mWebView.loadData(html, "text/html", null);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient());
        return myView;


    }
}
