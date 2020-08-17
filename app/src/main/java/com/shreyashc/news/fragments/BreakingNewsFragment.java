package com.shreyashc.news.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shreyashc.news.NewsViewModel;
import com.shreyashc.news.NewsViewModelFactory;
import com.shreyashc.news.R;
import com.shreyashc.news.adapters.NewsAdapter;
import com.shreyashc.news.models.Article;
import com.shreyashc.news.models.NewsResponse;

import java.util.ArrayList;
import java.util.List;


public class BreakingNewsFragment extends Fragment implements NewsAdapter.OnItemClickListener {

    ArrayList<Article> articleArrayList = new ArrayList<>();
    NewsAdapter newsAdapter;
    NewsViewModel newsViewModel;
    RecyclerView rvBreaking;
    private static final String TAG = "BreakingNewsFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_breaking_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rvBreaking = view.findViewById(R.id.rvBreakingNews);
        newsViewModel = new ViewModelProvider(this, new NewsViewModelFactory(getActivity().getApplication())).get(NewsViewModel.class);
        newsViewModel.init();
        setUpRecyclerView();
        LiveData<NewsResponse> res = newsViewModel.getNews();
        if (res == null) {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        } else {
            res.observe(getViewLifecycleOwner(), new Observer<NewsResponse>() {
                @Override
                public void onChanged(NewsResponse newsResponse) {
                    Log.d(TAG, "onChanged: " + newsResponse);
                    if(newsResponse == null){
                        Toast.makeText(getContext(), "Falied to load news!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Article> newsArticles = newsResponse.getArticles();
                    articleArrayList.addAll(newsArticles);
                    newsAdapter.submitList(articleArrayList);
                    newsAdapter.notifyDataSetChanged();
                }
            });
        }


    }

    private void setUpRecyclerView() {
        if (newsAdapter == null) {
            newsAdapter = new NewsAdapter(requireContext(), this);
            rvBreaking.setLayoutManager(new LinearLayoutManager(requireContext()));
            rvBreaking.setAdapter(newsAdapter);
        } else {
            newsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClicked(Article article) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("article", article);
        Navigation.findNavController(getView()).navigate(R.id.action_breakingNewsFragment_to_articleFragment, bundle);

    }
}