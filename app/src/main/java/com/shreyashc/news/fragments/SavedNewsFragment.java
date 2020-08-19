package com.shreyashc.news.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.shreyashc.news.R;
import com.shreyashc.news.activities.NewsActivity;
import com.shreyashc.news.adapters.NewsAdapter;
import com.shreyashc.news.models.Article;
import com.shreyashc.news.viewmodels.NewsViewModel;

import java.util.ArrayList;
import java.util.List;


public class SavedNewsFragment extends Fragment implements NewsAdapter.OnItemClickListener {
    NewsViewModel newsViewModel;
    NewsAdapter newsAdapter;
    RecyclerView rvSavedNews;
    ArrayList<Article> articleArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newsViewModel = ((NewsActivity) getActivity()).getNewsViewModel();
        rvSavedNews = view.findViewById(R.id.rvSavedNews);
        ((NewsActivity) getActivity()).getSupportActionBar().setTitle("Saved News");

        setUpRecyclerView();

        newsViewModel.getSavedArticles().observe(getViewLifecycleOwner(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                newsAdapter.getDiffer().submitList(articles);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                final Article article = newsAdapter.getDiffer().getCurrentList().get(position);
                newsViewModel.deleteArticle(article);
                Snackbar.make(view, "Article Deleted", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newsViewModel.saveArticle(article);
                    }
                }).show();
            }
        }).attachToRecyclerView(rvSavedNews);

    }

    private void setUpRecyclerView() {

        newsAdapter = new NewsAdapter(requireContext(), this);
        rvSavedNews.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvSavedNews.setAdapter(newsAdapter);
    }

    @Override
    public void onItemClicked(Article article) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("article", article);
        Navigation.findNavController(getView()).navigate(R.id.action_savedNewsFragment_to_articleFragment, bundle);
    }

}