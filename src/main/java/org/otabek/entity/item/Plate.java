package org.otabek.entity.item;

import org.otabek.entity.Tableware;

public class Plate extends Tableware {
    private float radius;

    public Plate(int id, String name, float width, String color, float price, float radius) {
        super(id, name, width, color, price);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    protected String getData() {
        return super.getData() + radius + " | " + "Plate |";
    }
}
