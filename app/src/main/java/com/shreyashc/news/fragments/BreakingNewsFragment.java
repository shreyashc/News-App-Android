package com.shreyashc.news.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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


public class BreakingNewsFragment extends Fragment implements NewsAdapter.OnItemClickListener {
    public static final int QUERY_PAGE_SIZE = 20;
    private NewsAdapter newsAdapter;
    private ProgressBar progressBar;
    private NewsViewModel newsViewModel;
    private RecyclerView rvBreaking;
    private static final String TAG = "BreakingNewsFragment";
    private boolean isLoading = false;
    private boolean isLastPage = false;


    @Override
    public void onItemClicked(Article article) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("article", article);
        Navigation.findNavController(getView()).navigate(R.id.action_breakingNewsFragment_to_articleFragment, bundle);

    }

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
                newsViewModel.safeBreakingNewsCall();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_breaking_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.paginationProgressBar);
        rvBreaking = view.findViewById(R.id.rvBreakingNews);
        newsViewModel = ((NewsActivity) getActivity()).getNewsViewModel();
        newsViewModel.init();
        ((NewsActivity) getActivity()).getSupportActionBar().setTitle("News");

        setUpRecyclerView();
        newsViewModel.breakingNews.observe(getViewLifecycleOwner(), new Observer<Resource<NewsResponse>>() {
            @Override
            public void onChanged(Resource<NewsResponse> newsResponseResource) {
                if (newsResponseResource.isLoading()) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (newsResponseResource.isSuccess()) {

                    progressBar.setVisibility(View.INVISIBLE);

                    newsAdapter.getDiffer().submitList(newsResponseResource.getResource().getArticles());
//                    newsAdapter.notifyDataSetChanged();

                    int totalPages = newsResponseResource.getResource().getTotalResults() / QUERY_PAGE_SIZE + 2;
                    isLastPage = newsViewModel.getBreakingNewsPageNo() == totalPages;
                    if (isLastPage) {
                        rvBreaking.setPadding(0, 0, 0, 0);
                        progressBar.setVisibility(View.INVISIBLE);
                    }


                } else if (newsResponseResource.getError() != null) {
                    Log.d(TAG, "onChanged: " + newsResponseResource.getError());
                    Toast.makeText(getContext(), newsResponseResource.getError(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void setUpRecyclerView() {
        newsAdapter = new NewsAdapter(requireContext(), this);
        rvBreaking.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvBreaking.setAdapter(newsAdapter);
        rvBreaking.addOnScrollListener(scrollListener);

    }


}