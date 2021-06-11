package it.polimi.ingsw.ui.cli;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Panel {
    int width;

    public void setHeight(int height) {
        this.height = height;
    }

    int height;
    ArrayList<DrawableObject> objects;
    PrintWriter writer;

    Panel(int width, int height, OutputStream os) {
        this.width = width;
        this.height = height;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width * height; i++)
            sb.append(" ");
        objects = new ArrayList<>();
        this.writer = new PrintWriter(os);
    }

    Panel(int width, int height, PrintWriter writer) {
        this.width = width;
        this.height = height;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width * height; i++)
            sb.append(" ");
        objects = new ArrayList<>();
        this.writer = writer;
    }

    public Panel(ArrayList<DrawableObject> drawableObjects, OutputStream os) {
        this(drawableObjects, new PrintWriter(os));
    }

    public Panel(ArrayList<DrawableObject> drawableObjects, PrintWriter printWriter) {
        height = drawableObjects.stream().map(drawableObject -> drawableObject.getY() + drawableObject.getHeight()).max(Integer::compareTo).orElse(0);
        width = drawableObjects.stream().map(drawableObject -> drawableObject.getX() + drawableObject.getWidth()).max(Integer::compareTo).orElse(0);
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
            for (int j = 0; j < width - 1; j++)
                sb.append(' ');
            sb.append('\n');
        }
        for (var object : objects) {
            String to = object.getTextObject();

            int pos = object.getX() + object.getY() * width;
            int line = 0;
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

        writer.println(sb.toString());
        writer.flush();
    }
}
