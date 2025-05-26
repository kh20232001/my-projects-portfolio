package com.api._dummy;

public class ShopDummyData {

    public ShopDummyData(int id, int price, double discount, int stock, int size) {
        this.id = id;
        this.price = price;
        this.discount = discount;
        this.stock = stock;
        this.size = size;
    }

    private int id;

    private int price;

    private double discount;

    private int stock;

    private int size;

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public double getDiscount() {
        return discount;
    }

    public int getSize() {
        return size;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
