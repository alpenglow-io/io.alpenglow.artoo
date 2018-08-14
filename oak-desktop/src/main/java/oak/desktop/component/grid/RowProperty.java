package oak.desktop.component.grid;

import io.martian.internal.lang.Consumer1;
import javafx.scene.layout.RowConstraints;

import static javafx.geometry.VPos.BASELINE;
import static javafx.geometry.VPos.BOTTOM;
import static javafx.geometry.VPos.CENTER;
import static javafx.geometry.VPos.TOP;
import static javafx.scene.layout.Priority.ALWAYS;
import static javafx.scene.layout.Priority.NEVER;
import static javafx.scene.layout.Priority.SOMETIMES;

@FunctionalInterface
public interface RowProperty extends Consumer1<RowConstraints> {
  static RowProperty height(Percent value) {
    return it -> it.setPercentHeight(value.get());
  }

  static RowProperty height(Pixel value) {
    return it -> it.setPrefHeight(value.get());
  }

  static RowProperty alignMiddle() {
    return it -> it.setValignment(CENTER);
  }

  static RowProperty alignBottom() {
    return it -> it.setValignment(BOTTOM);
  }

  static RowProperty alignBaseline() {
    return it -> it.setValignment(BASELINE);
  }

  static RowProperty alignTop() {
    return it -> it.setValignment(TOP);
  }

  static RowProperty fillHeight() {
    return it -> it.setFillHeight(true);
  }

  static RowProperty vgrowNever() {
    return it -> it.setVgrow(NEVER);
  }

  static RowProperty vgrowAlways() {
    return it -> it.setVgrow(ALWAYS);
  }

  static RowProperty vgrowSometimes() {
    return it -> it.setVgrow(SOMETIMES);
  }
}
