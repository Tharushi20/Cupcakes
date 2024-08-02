package com.example.cupcakes;

import java.util.ArrayList;
import java.util.List;

public class CategoryModel {

    String name,caturl;
    List<MainModel> cupcakes;

    CategoryModel(){}

    public CategoryModel(String name, String caturl) {
        this.name = name;
        this.caturl = caturl;
    }

    public CategoryModel(List<MainModel> cupcakes) {
        this.cupcakes = cupcakes;
    }

    public CategoryModel(String name, String caturl, List<MainModel> cupcakes) {
        this.name = name;
        this.caturl = caturl;
        this.cupcakes = cupcakes;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaturl() {
        return caturl;
    }

    public void setCaturl(String caturl) {
        this.caturl = caturl;
    }

    public List<MainModel> getCupcakes() {
        return cupcakes;
    }

    public void setCupcakes(List<MainModel> cupcakes) {
        this.cupcakes = cupcakes;
    }

    public List<MainModel> getCupcakesByCategory(String category) {
        List<MainModel> filteredCupcakes = new ArrayList<>();
        if (cupcakes != null) {
            for (MainModel cupcake : cupcakes) {
                if (cupcake.getCategory().equalsIgnoreCase(category)) {
                    filteredCupcakes.add(cupcake);
                }
            }
        }
        return filteredCupcakes;
    }
}
