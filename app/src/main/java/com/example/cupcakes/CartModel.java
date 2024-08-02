package com.example.cupcakes;

import android.os.Parcelable;
import android.os.Parcel;


public class CartModel implements Parcelable {

    private String key;
    private String name;
    private String price;
    private int quantity;
    private String curl;
    private String userId;

    CartModel() {}

    public CartModel(String key, String name, String price, int quantity, String curl, String userId) {
        this.key = key;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.curl = curl;
        this.userId = userId;
    }
    public CartModel(String name, String price, int quantity, String curl, String userId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.curl = curl;
        this.userId = userId;
    }


    public CartModel(String name, String price, int quantity, String curl) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.curl = curl;
    }

    public CartModel(String key, String name, String price, int quantity, String curl) {
        this.key = key;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.curl = curl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCurl() {
        return curl;
    }

    public void setCurl(String curl) {
        this.curl = curl;
    }

    // Getters and Setters
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    // ... other getters and setters ...

    protected CartModel(Parcel in) {
        key = in.readString();
        name = in.readString();
        price = in.readString();
        quantity = in.readInt();
        curl = in.readString();
        userId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeInt(quantity);
        dest.writeString(curl);
        dest.writeString(userId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartModel> CREATOR = new Creator<CartModel>() {
        @Override
        public CartModel createFromParcel(Parcel in) {
            return new CartModel(in);
        }

        @Override
        public CartModel[] newArray(int size) {
            return new CartModel[size];
        }
    };

    // Equals and hashCode methods based on key or another unique field
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartModel cartModel = (CartModel) o;
        return key.equals(cartModel.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }



}
