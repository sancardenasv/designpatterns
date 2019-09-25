package com.electroeing.designpatterns.builder;

public class BuilderRunner {

    public static void main(String[] args) {
        /* Builder example */
        HtmlBuilder builder =
                new HtmlBuilder("ul")
                .addChild("li", "hello")
                .addChild("li", "world");
        System.out.println(builder);
    }

}
