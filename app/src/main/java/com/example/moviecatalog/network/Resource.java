package com.example.moviecatalog.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {
    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable public final String message;

    private Resource(@NonNull Status status, @Nullable T data,
                     @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> network_error(String msg, @Nullable T data) {
        return new Resource<>(Status.NETWORK_ERROR, data, msg);
    }

    public static <T> Resource<T> server_error(String msg, @Nullable T data) {
        return new Resource<>(Status.SERVER_ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }

    public enum Status { SUCCESS, NETWORK_ERROR, SERVER_ERROR, LOADING }
}
