package io.artoo.desktop.component;

import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import io.artoo.desktop.event.ControlEvent;
import io.artoo.desktop.property.ControlProperty;

import static java.util.Objects.nonNull;

final class ListComponent<L extends ListView<?>> implements CompositeComponent, Base<L> {
  private final Base<L> list;
  private final ControlProperty[] properties;
  private final Component[] components;

  ListComponent(Base<L> list, ControlProperty[] properties) {
    this(list, properties, null);
  }

  private ListComponent(Base<L> list, ControlProperty[] properties, Component[] components) {
    this.list = list;
    this.properties = properties;
    this.components = components;
  }

  @Override
  public CompositeComponent with(Component... components) {
    return new ListComponent<>(list, properties, components);
  }

  @Override
  public L origin() {
    final L origin = list.origin();
    if (nonNull(properties))
      for (ControlProperty property : properties)
        property.onControl(origin);
    if (nonNull(properties))
      for (Component component : components)
        component.apply(origin);
    return origin;
  }

  @Override
  public ControlComponent on(ControlEvent... events) {
    return null;
  }

  @Override
  public Control element() {
    return origin();
  }
}
