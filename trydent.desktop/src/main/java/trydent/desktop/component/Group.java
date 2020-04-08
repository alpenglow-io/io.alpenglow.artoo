package trydent.desktop.component;

import trydent.desktop.property.NamedProperty;

import java.util.function.Supplier;

public interface Group extends Supplier<Component[]> {
  static Component[] group(NamedProperty named, ToggleComponent... components) {
    return new GroupImpl(named, components).get();
  }
}
