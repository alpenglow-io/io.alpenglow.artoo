package oak.desktop.component;

import javafx.scene.Parent;
import oak.func.sup.Supplier1;

public interface Base<P extends Parent> {
  P origin();
}
