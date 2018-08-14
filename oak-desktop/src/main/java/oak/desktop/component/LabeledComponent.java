package oak.desktop.component;

import io.martian.internal.fx.dsl.event.LabeledEvent;
import io.martian.internal.fx.dsl.property.IdProperty;
import io.martian.internal.fx.dsl.property.LabeledProperty;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;

public interface LabeledComponent extends Component, Source<Labeled> {
  static LabeledComponent button(IdProperty id, LabeledProperty... properties) {
    return new LabeledComponentImpl<>(new ParentComponentImpl<>(Button::new, id), properties);
  }

  static LabeledComponent label(IdProperty id, LabeledProperty... properties) {
    return new LabeledComponentImpl<>(new ParentComponentImpl<>(Label::new, id), properties);
  }

  LabeledComponent on(LabeledEvent... events);

  @Override
  default Parent get() {
    return element();
  }
}
