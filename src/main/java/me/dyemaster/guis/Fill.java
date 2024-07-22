package me.dyemaster.guis;

public class Fill {

    public enum Style {
        ALL,
        EDGES
    }

    private final Style style;
    private int[] exceptions;

    public Style getStyle() {
        return style;
    }

    public int[] getExceptions() {
        return exceptions;
    }

    public Fill(Style style, int[] exceptions) {
        this.style = style;
        this.exceptions = exceptions;
    }

    public Fill(Style style) {
        this.style = style;
    }
}
