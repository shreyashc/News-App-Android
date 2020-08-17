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

    private String author;
    private String title;
    private String description;
    private String url;
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
    public boolean equals(@Nullable Object obj) {
        if(obj == this) return true;

        if(!(obj instanceof Article)) return false;

        Article article = (Article) obj;
        return author.equals(article.getAuthor()) && title.equals(article.getTitle()) && description.equals(article.getDescription()) && url.equals(article.getUrl()) &&urlToImage.equals(article.getUrlToImage()) && publishedAt.equals(article.getPublishedAt()) && content.equals(article.getContent());
    }
}
