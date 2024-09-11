package com.gibatekpro.ecommerceandroid.product.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Page {


    @SerializedName("size")
    @Expose
    private int size;

    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;

    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;

    @SerializedName("number")
    @Expose
    private Integer page;


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

}
