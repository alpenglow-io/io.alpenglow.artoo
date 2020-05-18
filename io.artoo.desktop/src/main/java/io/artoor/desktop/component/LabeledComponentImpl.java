package io.artoo.desktop.component;

import io.artoo.desktop.event.LabeledEvent;
import io.artoo.desktop.property.LabeledProperty;
import javafx.scene.control.Labeled;

import static java.util.Objects.nonNull;

final class LabeledComponentImpl<L extends Labeled> implements LabeledComponent, Base<L> {
  private final Base<L> labeled;
  private final LabeledProperty[] properties;
  private final LabeledEvent[] events;

  LabeledComponentImpl(Base<L> labeled, LabeledProperty[] properties) {
    this(labeled, properties, null);
  }

  private LabeledComponentImpl(Base<L> labeled, LabeledProperty[] properties, LabeledEvent[] events) {
    this.labeled = labeled;
    this.properties = properties;
    this.events = events;
  }

  @Override
  public LabeledComponent on(LabeledEvent... events) {
    return new LabeledComponentImpl<>(labeled, properties, events);
  }

  @Override
  public L origin() {
    final L l = labeled.origin();
    if (nonNull(properties))
      for (LabeledProperty property : properties)
        property.onLabeled(l);
    if (nonNull(events))
      for (LabeledEvent event : events)
        event.onLabeled(l);
    return l;
  }

  @Override
  public Labeled element() {
    return origin();
  }
}
