package oak.desktop.component;

import io.martian.internal.fx.dsl.event.ControlEvent;
import io.martian.internal.fx.dsl.property.ControlProperty;
import io.martian.internal.fx.dsl.property.IdProperty;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;

import static java.util.Objects.nonNull;

public interface ScrollComponent extends ControlComponent {
  static ScrollComponent scroll(IdProperty id, ControlProperty... properties) {
    return new ScrollComponentImpl<>(new ParentComponentImpl<>(ScrollPane::new, id), properties);
  }

  ScrollComponent with(Component component);

  final class ScrollComponentImpl<S extends ScrollPane> implements ScrollComponent, Base<S> {
    private final Base<S> scroll;
    private final ControlProperty[] properties;
    private final Component component;

    ScrollComponentImpl(Base<S> scroll, ControlProperty[] properties) {
      this(scroll, properties, null);
    }

    private ScrollComponentImpl(Base<S> scroll, ControlProperty[] properties, Component component) {
      this.scroll = scroll;
      this.properties = properties;
      this.component = component;
    }

    @Override
    public ScrollComponent with(Component component) {
      return new ScrollComponentImpl<>(scroll, properties, component);
    }

    @Override
    public S origin() {
      final S origin = scroll.origin();
      for (ControlProperty property : properties) property.onControl(origin);
      if (nonNull(component)) origin.setContent(component.get());
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
}
