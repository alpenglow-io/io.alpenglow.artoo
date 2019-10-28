package dev.lug.oak.desktop.component;

import javafx.scene.Parent;
import dev.lug.oak.func.sup.Supplier1;

public interface Base<P extends Parent> {
  P origin();
}
