package it.polimi.ingsw.ui.cli;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Panel {
    private int width;
    public void setHeight(int height) {
        this.height = height;
    }
    private int height;
    ArrayList<DrawableObject> objects;
    PrintWriter writer;

    Panel(int width, int height, OutputStream os) {
        this.width = width;
        this.height = height;
        StringBuilder sb = new StringBuilder();
        sb.append(" ".repeat(Math.max(0, width * height)));
        objects = new ArrayList<>();
        this.writer = new PrintWriter(os);
    }

    Panel(int width, int height, PrintWriter writer) {
        this.width = width;
        this.height = height;
        StringBuilder sb = new StringBuilder();
        sb.append(" ".repeat(Math.max(0, width * height)));
        objects = new ArrayList<>();
        this.writer = writer;
    }

    public Panel(ArrayList<DrawableObject> drawableObjects, OutputStream os, boolean putInLine) {
        this(drawableObjects, new PrintWriter(os), putInLine);
    }

    public Panel(ArrayList<DrawableObject> drawableObjects, PrintWriter printWriter, boolean putInLine) {
        if(putInLine){
            ArrayList<DrawableObject> newDrawableObjects = new ArrayList<>();
            width = 0;
            height = 0;
            for(DrawableObject drawableObject: drawableObjects){
                DrawableObject newDrawableObject = new DrawableObject(drawableObject.getTextObject(), width, 0);
                newDrawableObjects.add(newDrawableObject);
                width = width + newDrawableObject.getWidth() + 3;
                if(newDrawableObject.getHeight()>height)
                    height= newDrawableObject.getHeight();
            }
            drawableObjects = newDrawableObjects;
        }
        else {
            height = drawableObjects.stream().map(drawableObject -> drawableObject.getY() + drawableObject.getHeight()).max(Integer::compareTo).orElse(0);
            width = drawableObjects.stream().map(drawableObject -> drawableObject.getX() + drawableObject.getWidth()).max(Integer::compareTo).orElse(0);
        }
        objects = new ArrayList<>();
        drawableObjects.forEach(this::addItem);
        this.writer = printWriter;
    }



    public void addItem(DrawableObject o) throws IllegalArgumentException {
        if (o.getX() + o.getWidth() > width
                || o.getY() + o.getHeight() > height)
            throw new IllegalArgumentException();
        objects.add(o);
    }


    public void show() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            sb.append(" ".repeat(Math.max(0, width - 1)));
            sb.append('\n');
        }
        for (var object : objects) {
            String to = object.getTextObject();

            int pos = object.getX() + object.getY() * width;
            for (int i = 0; i < to.length(); i++) {
                var c = to.charAt(i);
                if (c == '\n')
                    pos += width - (pos % width - object.getX());
                else {
                    sb.setCharAt(pos, c);
                    pos++;
                }
            }
        }

        writer.println(sb);
        writer.flush();
    }
}
