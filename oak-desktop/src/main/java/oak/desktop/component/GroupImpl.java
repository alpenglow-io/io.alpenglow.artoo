package oak.desktop.component;

import javafx.scene.control.ToggleGroup;
import oak.desktop.property.NamedProperty;

import static java.util.Objects.nonNull;

final class GroupImpl implements Group {
  private final NamedProperty named;
  private final ToggleComponent[] components;

  GroupImpl(NamedProperty named, ToggleComponent[] components) {
    this.named = named;
    this.components = components;
  }

  @Override
  public Component[] get() {
    final ToggleGroup g = new ToggleGroup();
    named.accept(g);
    if (nonNull(components)) {
      for (ToggleComponent component : components) {
        component.element().setToggleGroup(g);
      }
    }
    return components;
  }
}
