package dev.lug.oak.desktop.component;

import javafx.scene.control.ToggleGroup;
import dev.lug.oak.desktop.property.NamedProperty;

import static java.util.Objects.nonNull;

final class GroupImpl implements Group {
  private final NamedProperty named;
  private final ToggleComponent[] components;

  GroupImpl(NamedProperty named, ToggleComponent[] components) {
    this.named = named;
    this.components = components;
  }

  @Override
  public Component[] get() {
    final var g = new ToggleGroup();
    named.accept(g);
    if (nonNull(components)) {
      for (final var component : components) {
        component.element().setToggleGroup(g);
      }
    }
    return components;
  }
}
