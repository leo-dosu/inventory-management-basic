package com.inventory.pos.shared.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.function.Supplier;

/** A download link for a generated PDF. The PDF is built when the link is clicked, not when the page loads. */
public final class Downloads {

    private Downloads() {
    }

    public static Anchor pdf(String filename, Supplier<byte[]> content, Component... label) {
        StreamResource resource = new StreamResource(filename, () -> new ByteArrayInputStream(content.get()));
        resource.setContentType("application/pdf");
        Anchor anchor = new Anchor(resource, "");
        anchor.add(label);
        anchor.getElement().setAttribute("download", true);
        anchor.setTarget("_blank");
        return anchor;
    }

    /** Turns a name into something safe to use as a file name. */
    public static String safeName(String name) {
        return name.replaceAll("[^A-Za-z0-9_-]+", "-").replaceAll("^-+|-+$", "");
    }
}
