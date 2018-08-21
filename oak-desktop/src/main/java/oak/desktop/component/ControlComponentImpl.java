package oak.desktop.component;

import javafx.scene.control.Control;
import oak.desktop.event.ControlEvent;
import oak.desktop.property.ControlProperty;

import static java.util.Objects.nonNull;

final class ControlComponentImpl<C extends Control> implements ControlComponent, Base<C> {
  private final Base<C> control;
  private final ControlProperty[] properties;
  private final ControlEvent[] events;

  ControlComponentImpl(Base<C> control) {
    this(control, null);
  }

  ControlComponentImpl(Base<C> control, ControlProperty[] properties) {
    this(control, properties, null);
  }

  private ControlComponentImpl(Base<C> control, ControlProperty[] properties, ControlEvent[] events) {
    this.control = control;
    this.properties = properties;
    this.events = events;
  }

  @Override
  public ControlComponent on(ControlEvent... events) {
    return new ControlComponentImpl<>(control, properties, events);
  }

  @Override
  public C origin() {
    final C origin = control.origin();
    if (nonNull(properties)) for (ControlProperty property : properties) property.onControl(origin);
    if (nonNull(events)) for (ControlEvent event : events) event.onControl(origin);
    return origin;
  }

  @Override
  public Control element() {
    return origin();
  }
}
