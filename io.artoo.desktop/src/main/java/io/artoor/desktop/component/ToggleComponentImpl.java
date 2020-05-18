package io.artoo.desktop.component;

import io.artoo.desktop.property.ToggleProperty;
import javafx.scene.control.ToggleButton;

import static java.util.Objects.nonNull;

final class ToggleComponentImpl<T extends ToggleButton> implements ToggleComponent, Base<T> {
  private final Base<T> toggle;
  private final ToggleProperty[] properties;

  ToggleComponentImpl(Base<T> toggle, ToggleProperty[] properties) {
    this.toggle = toggle;
    this.properties = properties;
  }

  @Override
  public ToggleButton element() {
    return origin();
  }

  @Override
  public final T origin() {
    final T origin = toggle.origin();
    if (nonNull(properties))
      for (ToggleProperty property : properties)
        property.onToggle(origin);
    return origin;
  }
}
