package oak.desktop.component;

import oak.desktop.property.InputProperty;
import javafx.scene.Parent;
import javafx.scene.control.TextInputControl;

import static java.util.Objects.nonNull;

final class InputComponentImpl<I extends TextInputControl> implements InputComponent, Base<I> {
  private final Base<I> input;
  private final InputProperty[] properties;

  InputComponentImpl(Base<I> input, InputProperty[] properties) {
    this.input = input;
    this.properties = properties;
  }

  @Override
  public Parent get() {
    return origin();
  }

  @Override
  public I origin() {
    final I origin = input.origin();
    if (nonNull(properties)) for (InputProperty property : properties) property.onInput(origin);
    return origin;
  }
}
