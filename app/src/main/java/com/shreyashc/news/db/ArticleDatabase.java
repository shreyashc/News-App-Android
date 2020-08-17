package com.shreyashc.news.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.shreyashc.news.models.Article;

@Database(entities = {
        Article.class
}, version = 1)
@TypeConverters({Converters.class})
public abstract class ArticleDatabase extends RoomDatabase {
    private static volatile ArticleDatabase instance;

    public abstract ArticleDao getArticleDao();

    public static ArticleDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (ArticleDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), ArticleDatabase.class, "article_db.db")
                            .build();
                }
            }
        }
        return instance;
    }


}
