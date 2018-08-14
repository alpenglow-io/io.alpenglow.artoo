package oak.desktop.component.grid;

import io.martian.internal.lang.Consumer1;
import javafx.scene.layout.ColumnConstraints;

import static javafx.geometry.HPos.CENTER;
import static javafx.geometry.HPos.LEFT;
import static javafx.geometry.HPos.RIGHT;
import static javafx.scene.layout.Priority.ALWAYS;
import static javafx.scene.layout.Priority.NEVER;
import static javafx.scene.layout.Priority.SOMETIMES;

@FunctionalInterface
public interface ColumnProperty extends Consumer1<ColumnConstraints> {
  static ColumnProperty width(Percent value) {
    return it -> it.setPercentWidth(value.get());
  }

  static ColumnProperty width(Pixel value) {
    return it -> it.setPrefWidth(value.get());
  }

  static ColumnProperty alignLeft() {
    return it -> it.setHalignment(LEFT);
  }

  static ColumnProperty alignCenter() {
    return it -> it.setHalignment(CENTER);
  }

  static ColumnProperty alignRight() {
    return it -> it.setHalignment(RIGHT);
  }

  static ColumnProperty fillWidth() {
    return it -> it.setFillWidth(true);
  }

  static ColumnProperty hgrowNever() {
    return it -> it.setHgrow(NEVER);
  }

  static ColumnProperty hgrowAlways() {
    return it -> it.setHgrow(ALWAYS);
  }

  static ColumnProperty hgrowSometimes() {
    return it -> it.setHgrow(SOMETIMES);
  }
}
