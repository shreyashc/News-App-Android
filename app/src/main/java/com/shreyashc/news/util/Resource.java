package com.shreyashc.news.util;

import androidx.annotation.Nullable;
/*

public class Resource<T> {
    @NonNull
    public final Status status;
    @Nullable
    public final T data;

    @Nullable
    public final String message;

    public Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }


    public enum Status {SUCCESS, ERROR, LOADING}
}
*/

public class Resource<T> {
    private T resource;
    private String error;
    private boolean loading = false;
    private boolean suc;

    private Resource() {
    }

    public static <T> Resource<T> success(@Nullable T body) {
        final Resource<T> resource = new Resource<>();
        resource.error = null;
        resource.resource = body;
        resource.suc = true;
        return resource;
    }

    public static <T> Resource error(@Nullable String error) {
        final Resource<T> resource = new Resource<>();
        resource.error = error;
        resource.suc = false;
        return resource;
    }

    public static <T> Resource<T> loading(@Nullable T body) {
        final Resource<T> resource = new Resource<>();
        resource.error = null;
        resource.loading = true;
        resource.resource = body;
        return resource;
    }

    public boolean isSuccess() {
        return suc;
    }

    public boolean isFail() {
        return suc;
    }

    @Nullable
    public T getResource() {
        return resource;
    }

    public String getError() {
        return error;
    }

    public boolean isLoading() {
        return loading;
    }
}