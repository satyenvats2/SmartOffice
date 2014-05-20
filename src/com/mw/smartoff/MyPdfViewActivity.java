package com.mw.smartoff;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MyPdfViewActivity extends Activity {

	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    WebView mWebView=new WebView(MyPdfViewActivity.this);
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
	    mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=");
	    setContentView(mWebView);
	  }
	
}
