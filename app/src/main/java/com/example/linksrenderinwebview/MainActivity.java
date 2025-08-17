package com.example.linksrenderinwebview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.*;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String HOME_URL = "https://www.whatsapp.com/";

    // UI Elements
    private WebView webView;
    private ProgressBar progressBar;
    private LinearLayout bottomBar;
    private FloatingActionButton nativeFab;
    private MaterialCardView nativeMenuContainer;
    private View dimmingOverlay;
    private ImageView icoX, icoInsta, icoYouTube, icoFacebook;

    private boolean isErrorPage = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupWebView();
        setupFabMenu();
        setupSocialIconClicks();
        preloadSocialSites();

        webView.loadUrl(HOME_URL);
    }

    private void initializeViews() {
        nativeFab = findViewById(R.id.nativeFab);
        nativeMenuContainer = findViewById(R.id.nativeMenuContainer);
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        bottomBar = findViewById(R.id.bottomBarContent);
        dimmingOverlay = findViewById(R.id.dimming_overlay);

        icoX = findViewById(R.id.icon_twitter);
        icoInsta = findViewById(R.id.icon_instagram);
        icoYouTube = findViewById(R.id.icon_youtube);
        icoFacebook = findViewById(R.id.icon_facebook);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setBlockNetworkImage(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.setWebViewClient(new BrowserClient());
    }

    private void setupFabMenu() {
        nativeFab.setOnClickListener(v -> {
            if (nativeMenuContainer.getVisibility() == View.VISIBLE) {
                hideFabMenu();
            } else {
                showFabMenu();
            }
        });

        dimmingOverlay.setOnClickListener(v -> hideFabMenu());

        findViewById(R.id.menuItemHomepage).setOnClickListener(v -> {
            webView.loadUrl(HOME_URL);
            hideFabMenu();
        });

        findViewById(R.id.menuItemAbout).setOnClickListener(v -> {
            Toast.makeText(this, "About Us Clicked", Toast.LENGTH_SHORT).show();
            hideFabMenu();
        });
        findViewById(R.id.menuItemService).setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Service Clicked", Toast.LENGTH_SHORT).show();
            hideFabMenu();
        });
        findViewById(R.id.menuItemNews).setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "News Clicked", Toast.LENGTH_SHORT).show();
            hideFabMenu();
        });
        findViewById(R.id.menuItemContactUs).setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Contact Us Clicked", Toast.LENGTH_SHORT).show();
            hideFabMenu();
        });
    }

    private void setupSocialIconClicks() {
        icoX.setOnClickListener(v -> webView.loadUrl("https://x.com/whatsapp/"));
        icoInsta.setOnClickListener(v -> webView.loadUrl("https://www.instagram.com/"));
        icoYouTube.setOnClickListener(v -> webView.loadUrl("https://www.youtube.com/"));
        icoFacebook.setOnClickListener(v -> webView.loadUrl("https://m.facebook.com/"));
    }

    private void preloadSocialSites() {
        new Thread(() -> {
            try {
                new java.net.URL("https://x.com/whatsapp/").openConnection().getInputStream().close();
                new java.net.URL("https://static.xx.fbcdn.net/").openConnection().getInputStream().close();
                new java.net.URL("https://www.instagram.com/").openConnection().getInputStream().close();
                new java.net.URL("https://www.youtube.com/").openConnection().getInputStream().close();
            } catch (Exception ignored) {
            }
        }).start();
    }

    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        if (nativeMenuContainer.getVisibility() == View.VISIBLE) {
            hideFabMenu();
        } else if (isErrorPage) {
            isErrorPage = false;
            webView.loadUrl(HOME_URL);
        } else if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void showFabMenu() {
        dimmingOverlay.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(dimmingOverlay, "alpha", 0f, 1f).setDuration(300).start();
        nativeMenuContainer.setVisibility(View.VISIBLE);
    }

    private void hideFabMenu() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(dimmingOverlay, "alpha", 1f, 0f);
        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dimmingOverlay.setVisibility(View.GONE);
            }
        });
        animator.start();
        nativeMenuContainer.setVisibility(View.GONE);
    }

    private class BrowserClient extends WebViewClient {

        private boolean isWhatsApp(String url) {
            return url != null && Uri.parse(url).getHost() != null
                    && Uri.parse(url).getHost().contains("whatsapp.com");
        }

        private boolean isSocial(String url) {
            if (url == null) return false;
            String host = Uri.parse(url).getHost();
            return host != null && (
                    host.contains("facebook.com") ||
                            host.contains("m.facebook.com") ||
                            host.contains("instagram.com") ||
                            host.contains("x.com") ||
                            host.contains("twitter.com") ||
                            host.contains("youtube.com")
            );
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri uri = request.getUrl();
            if (!"http".equalsIgnoreCase(uri.getScheme()) && !"https".equalsIgnoreCase(uri.getScheme())) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                } catch (ActivityNotFoundException e) {
                   // Toast.makeText(MainActivity.this, "App not installed to handle this link", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            view.loadUrl(uri.toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            isErrorPage = false;
            progressBar.setVisibility(View.VISIBLE);
            bottomBar.setVisibility(isWhatsApp(url) ? View.VISIBLE : View.GONE);
            nativeFab.setVisibility(isSocial(url) ? View.VISIBLE : View.INVISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.evaluateJavascript("document.body.style.backgroundColor = '#FAFAD2';", null);
            webView.evaluateJavascript("console.log('Fast page with caching loaded!');", null);

            bottomBar.setVisibility(isWhatsApp(url) ? View.VISIBLE : View.GONE);
            nativeFab.setVisibility(isSocial(url) ? View.VISIBLE : View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            isErrorPage = true;
            /*String errorDescription = error.getDescription().toString();
            String description = errorDescription.contains("net::ERR_BLOCKED_BY_ORB")
                    ? "Blocked by ORB" : errorDescription;

            String html = "<html><body style='text-align:center;padding:20px;font-family:sans-serif'>" +
                    "<h2>Page Load Error</h2>" +
                    "<p>" + description + "</p>" +
                    "<button onclick=\"window.location.href='" + HOME_URL + "'\" " +
                    "style='padding:10px 20px;background:#25D366;color:white;border:none;border-radius:5px;'>Go Home</button>" +
                    "</body></html>";

            view.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);*/
            super.onReceivedError(view, request, error);
        }

    }
}
