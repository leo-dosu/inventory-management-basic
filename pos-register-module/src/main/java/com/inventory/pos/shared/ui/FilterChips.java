package com.inventory.pos.shared.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/** A row of pill buttons where exactly one is selected, like the category and status filters in the design. */
public class FilterChips<T> extends Div {

    private final Map<T, Button> chips = new LinkedHashMap<>();
    private final Function<T, String> label;
    private final Consumer<T> onChange;
    private T selected;

    /**
     * @param tone gives each option a colour name (red, amber, green, grey) or null for the default dark pill
     */
    public FilterChips(List<T> options, T initial, Function<T, String> label, Function<T, String> tone,
                       Consumer<T> onChange) {
        this.label = label;
        this.onChange = onChange;
        this.selected = initial;
        addClassName("chips");
        for (T option : options) {
            Button chip = new Button(label.apply(option));
            chip.addClassName("chip");
            String colour = tone == null ? null : tone.apply(option);
            if (colour != null) {
                chip.addClassName("tone-" + colour);
            }
            chip.addClickListener(e -> select(option));
            chips.put(option, chip);
            add(chip);
        }
        markSelected();
    }

    public T selected() {
        return selected;
    }

    /** Re-reads every label, for chips that show a live count. */
    public void refreshLabels() {
        chips.forEach((option, chip) -> chip.setText(label.apply(option)));
    }

    private void select(T option) {
        if (option.equals(selected)) {
            return;
        }
        selected = option;
        markSelected();
        onChange.accept(option);
    }

    private void markSelected() {
        chips.forEach((option, chip) -> chip.getClassNames().set("active", option.equals(selected)));
    }
}
