package org.otabek.entity.item;

import org.otabek.entity.Tableware;

public class Cup extends Tableware {
    private String category;

    public Cup(Integer id, String name, float width, String color, float price, String category) {
        super(id, name, width, color, price);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return getData() + category + " | " + "Cup |";
    }
}
