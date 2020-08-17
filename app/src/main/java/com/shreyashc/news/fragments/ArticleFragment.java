package com.shreyashc.news.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.shreyashc.news.NewsViewModel;
import com.shreyashc.news.NewsViewModelFactory;
import com.shreyashc.news.R;
import com.shreyashc.news.models.Article;

public class ArticleFragment extends Fragment {
    WebView webView;
    FloatingActionButton fab;
    NewsViewModel newsViewModel;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArticleFragmentArgs args = ArticleFragmentArgs.fromBundle(getArguments());
        final Article article = args.getArticle();
        webView = view.findViewById(R.id.webView);
        fab = view.findViewById(R.id.fab);
        newsViewModel = new ViewModelProvider(this, new NewsViewModelFactory(getActivity().getApplication())).get(NewsViewModel.class);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        //webview config
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        //load article
        webView.loadUrl(article.getUrl());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsViewModel.saveArticle(article);
                Snackbar.make(getView(), "Article Saved successfully", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}