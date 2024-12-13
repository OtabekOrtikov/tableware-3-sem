package org.otabek.entity.item;

import org.otabek.entity.Tableware;

public class Teapot extends Tableware {
    private String style;

    public Teapot(int id, String name, float width, String color, float price, String style) {
        super(id, name, width, color, price);
        this.style = style;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return super.getData() + style + " | ";
    }
}
