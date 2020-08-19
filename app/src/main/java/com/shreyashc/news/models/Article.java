package com.shreyashc.news.models;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "articles")
public class Article implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int pk;

    @SerializedName("source")
    @Expose
    private Source SourceObject;

    @Nullable
    private String author;

    private String title;

    @Nullable
    private String description;

    private String url;

    @Nullable
    private String urlToImage;

    private String publishedAt;

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public Source getSourceObject() {
        return SourceObject;
    }

    public void setSourceObject(Source sourceObject) {
        SourceObject = sourceObject;
    }

    private String content;


    // Getter Methods

    public Source getSource() {
        return SourceObject;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getContent() {
        return content;
    }

    // Setter Methods

    public void setSource(Source sourceObject) {
        this.SourceObject = sourceObject;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return
//              getAuthor().equals(article.getAuthor()) && nullable
                getTitle().equals(article.getTitle()) &&
//                getDescription().equals(article.getDescription()) &&
                        getUrl().equals(article.getUrl()) &&
//                getUrlToImage().equals(article.getUrlToImage()) &&
                        getPublishedAt().equals(article.getPublishedAt());//&&
//                getContent().equals(article.getContent());
    }


}
