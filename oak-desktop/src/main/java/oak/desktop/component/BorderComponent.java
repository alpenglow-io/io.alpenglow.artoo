package oak.desktop.component;

import io.martian.internal.fx.dsl.property.BorderPosition;
import io.martian.internal.fx.dsl.property.IdProperty;
import io.martian.internal.fx.dsl.property.ParentProperty;
import javafx.scene.layout.BorderPane;

public interface BorderComponent extends Source<BorderPane> {
  static BorderComponent border(IdProperty id, ParentProperty... properties) {
    return new BorderComponentImpl<>(new ParentComponentImpl<>(BorderPane::new, id), properties);
  }

  BorderComponent with(BorderPosition... positions);
}
