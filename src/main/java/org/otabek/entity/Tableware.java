package org.otabek.entity;

import java.util.Objects;

public abstract class Tableware {
    private int id;
    private String name;
    private float width;
    private String color;
    private float price;

    public Tableware(int id, String name, float width, String color, float price) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.color = color;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    protected String getData() {
        return "| " + id + " | " + name + " | " + width + " | " + color + " | " + price + " | ";
    }

    @Override
    public String toString() {
        return getData();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tableware tableware = (Tableware) o;
        return id == tableware.id && Float.compare(width, tableware.width) == 0 && Float.compare(price, tableware.price) == 0 && Objects.equals(name, tableware.name) && Objects.equals(color, tableware.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, width, color, price);
    }
}
