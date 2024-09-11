package com.gibatekpro.ecommerceandroid.cart.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.gibatekpro.ecommerceandroid.product.model.Product;

import java.math.BigDecimal;

@Entity(tableName = "cart_table")
public class CartItem {

    @PrimaryKey
    @ColumnInfo(name = "id")
    long id;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "image_url")
    String imageUrl;

    @ColumnInfo(name = "unit_price")
    BigDecimal unitPrice;

    @ColumnInfo(name = "units_inStock")
    int unitsInStock;

    @Ignore
    private Product cartItemProduct;

    @ColumnInfo(name = "quantity")
    private int quantity = 1;

    public CartItem() {

    }

    public CartItem(Product cartItemProduct) {

        this.name = cartItemProduct.getName();

        this.cartItemProduct = cartItemProduct;

        this.id = cartItemProduct.getId();

        this.unitPrice = cartItemProduct.getUnitPrice();

        this.imageUrl = cartItemProduct.getImageUrl();

        this.unitsInStock = cartItemProduct.getUnitsInStock();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(int unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public Product getCartItemProduct() {
        return cartItemProduct;
    }

    public void setCartItemProduct(Product cartItemProduct) {
        this.cartItemProduct = cartItemProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incrementQuantity() {
        quantity++;
    }

    public void decrementQuantity() {
        quantity--;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice.doubleValue() +
                ", quantity=" + quantity +
                '}';
    }
}