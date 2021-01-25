package io.artoo.fxcalibur.component;

import io.artoo.fxcalibur.property.BorderPosition;
import io.artoo.fxcalibur.property.ParentProperty;
import javafx.scene.layout.BorderPane;

final class BorderComponentImpl<B extends BorderPane> implements BorderComponent, Base<B> {
  private final Base<B> border;
  private final ParentProperty[] properties;
  private final BorderPosition[] positions;

  BorderComponentImpl(Base<B> border, ParentProperty[] properties) {
    this(border, properties, new BorderPosition[]{});
  }

  private BorderComponentImpl(Base<B> border, ParentProperty[] properties, BorderPosition[] positions) {
    this.border = border;
    this.properties = properties;
    this.positions = positions;
  }

  @Override
  public BorderComponent with(BorderPosition... positions) {
    return new BorderComponentImpl<>(border, properties, positions);
  }

  @Override
  public B origin() {
    final var origin = border.origin();
    for (var property : properties)
      property.onParent(origin);
    for (var position : positions)
      position.onBorder(origin);
    return origin;
  }

  @Override
  public BorderPane element() {
    return origin();
  }
}
