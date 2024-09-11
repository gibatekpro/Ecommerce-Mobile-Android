package com.gibatekpro.ecommerceandroid.product.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductResponse {

    @Expose
    @SerializedName("_embedded")
    private Embedded _embedded;

    @Expose
    @SerializedName("page")
    private Page page;

    public Embedded get_embedded() {
        return _embedded;
    }

    public void set_embedded(Embedded _embedded) {
        this._embedded = _embedded;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
