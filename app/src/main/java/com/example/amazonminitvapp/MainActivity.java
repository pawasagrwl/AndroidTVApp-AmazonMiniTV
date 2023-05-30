package com.example.amazonminitvapp;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private WebView myWebView;
    private boolean isPlaying = true;
    private boolean isFullScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWebView = findViewById(R.id.webview);

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        myWebView.loadUrl("https://www.amazon.in/minitv");

        myWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_LEFT:
                            // Handle seek backward
                            myWebView.evaluateJavascript("videoElement.currentTime -= 10;", null);
                            return true;
                        case KeyEvent.KEYCODE_DPAD_RIGHT:
                            // Handle seek forward
                            myWebView.evaluateJavascript("videoElement.currentTime += 10;", null);
                            return true;
                        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                            // Toggle play/pause
                            if (isPlaying) {
                                myWebView.evaluateJavascript("videoElement.pause();", null);
                                isPlaying = false;
                            } else {
                                myWebView.evaluateJavascript("videoElement.play();", null);
                                isPlaying = true;
                            }
                            return true;
                        case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                            // Handle fast-forward
                            myWebView.evaluateJavascript("videoElement.playbackRate += 0.5;", null);
                            return true;
                        case KeyEvent.KEYCODE_MEDIA_REWIND:
                            // Handle rewind
                            myWebView.evaluateJavascript("videoElement.playbackRate -= 0.5;", null);
                            return true;
                    }
                }
                return false;
            }
        });

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                // Enter full-screen mode
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                isFullScreen = true;
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                // Exit full-screen mode
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                isFullScreen = false;
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        int action = event.getAction();

        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && action == KeyEvent.ACTION_DOWN) {
            if (isFullScreen) {
                // Exit full-screen mode
                myWebView.evaluateJavascript("exitFullScreenFunction();", null);
            } else {
                // Enter full-screen mode
                myWebView.evaluateJavascript("enterFullScreenFunction();", null);
            }
            return true;
        }

        return super.dispatchKeyEvent(event);
    }
}
