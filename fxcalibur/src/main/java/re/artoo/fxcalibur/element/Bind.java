package re.artoo.fxcalibur.element;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import java.util.function.Supplier;

public interface Bind {
  static <T> Property<T> button(Supplier<? extends T> supplier) {
    return new SimpleObjectProperty<>(supplier.get());
  }
}
