package com.shreyashc.news.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shreyashc.news.R;
import com.shreyashc.news.activities.NewsActivity;
import com.shreyashc.news.adapters.NewsAdapter;
import com.shreyashc.news.models.Article;
import com.shreyashc.news.models.NewsResponse;
import com.shreyashc.news.util.Resource;
import com.shreyashc.news.viewmodels.NewsViewModel;

import java.util.Timer;
import java.util.TimerTask;

import static com.shreyashc.news.fragments.BreakingNewsFragment.QUERY_PAGE_SIZE;


public class SearchNewsFragment extends Fragment implements NewsAdapter.OnItemClickListener {
    private static final String TAG = "SearchNewsFragment";

    NewsViewModel newsViewModel;
    NewsAdapter newsAdapter;
    RecyclerView rvSearch;
    EditText etSearch;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_news, container, false);
    }

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private boolean isScolling = false;
    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {


        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();

            boolean isNotLoadingAndNotLastPage = !isLoading && !isLastPage;
            boolean isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount;
            boolean isNotAtBeginning = firstVisibleItemPosition >= 0;
            boolean isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE;

            boolean shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScolling;

            if (shouldPaginate) {
                newsViewModel.searchForNews(etSearch.getText().toString().trim());/////////////////
                isScolling = false;
            }

        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScolling = true;
            }
        }

    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newsViewModel = ((NewsActivity) getActivity()).getNewsViewModel();
        rvSearch = view.findViewById(R.id.rvSearch);
        etSearch = view.findViewById(R.id.etSearch);
        progressBar = view.findViewById(R.id.paginationProgressBar);
        ((NewsActivity) getActivity()).getSupportActionBar().setTitle("Search News");

        setUpRecyclerView();

        newsViewModel.searchNews.observe(getViewLifecycleOwner(), new Observer<Resource<NewsResponse>>() {
            @Override
            public void onChanged(Resource<NewsResponse> newsResponseResource) {
                progressBar.setVisibility(View.INVISIBLE);
                if (newsResponseResource.isLoading()) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (newsResponseResource.isSuccess()) {


                    newsAdapter.getDiffer().submitList(newsResponseResource.getResource().getArticles());

                    int totalPages = newsResponseResource.getResource().getTotalResults() / QUERY_PAGE_SIZE + 2;
                    isLastPage = newsViewModel.getBreakingNewsPageNo() == totalPages;
                    if (isLastPage) {
                        rvSearch.setPadding(0, 0, 0, 0);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    progressBar.setVisibility(View.INVISIBLE);

                } else if (newsResponseResource.getError() != null) {
                    Log.d(TAG, "onChanged: " + newsResponseResource.getError());
                    Toast.makeText(getContext(), newsResponseResource.getError(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setUpRecyclerView() {
        newsAdapter = new NewsAdapter(requireContext(), this);
        rvSearch.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvSearch.setAdapter(newsAdapter);
        rvSearch.addOnScrollListener(scrollListener);


        etSearch.addTextChangedListener(new TextWatcher() {
            Timer timer;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!etSearch.getText().toString().trim().isEmpty()) {
                            newsViewModel.searchForNews(etSearch.getText().toString().trim());
                            Log.d(TAG, "runinng: ");
                        }
                    }
                }, 500);

            }
        });

    }

    @Override
    public void onItemClicked(Article article) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("article", article);
        Navigation.findNavController(getView()).navigate(R.id.action_searchNewsFragment_to_articleFragment, bundle);

    }


}