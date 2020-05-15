package io.artoor.desktop.component;


import javafx.scene.Parent;

public interface Component extends Supplier<Parent> {
  default Parent apply(Parent parent) {
    return get();
  }
}
