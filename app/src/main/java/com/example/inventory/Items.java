package com.example.inventory;

public class Items {
    private String itemname;
    private String itemcategory;
    private String itemquantity;
    private String itemunit;


    public Items() {

    }

    public Items(String itemname, String itemcategory, String itemquantity, String itemunit) {
        this.itemname = itemname;
        this.itemcategory = itemcategory;
        this.itemquantity = itemquantity;
        this.itemunit = itemunit;
    }

    public String getItemname() {
        return itemname;
    }

    public String getItemcategory() {
        return itemcategory;
    }

    public String getItemquantity() {
        return itemquantity;
    }

    public String getItemunit() {
        return itemunit;
    }

}
