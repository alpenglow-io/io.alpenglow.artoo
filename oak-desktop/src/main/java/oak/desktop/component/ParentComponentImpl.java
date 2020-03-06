package oak.desktop.component;

import oak.desktop.property.IdProperty;
import oak.desktop.property.ParentProperty;
import javafx.scene.Parent;

import java.util.function.Supplier;

import static java.util.Objects.nonNull;

final class ParentComponentImpl<P extends Parent> implements ParentComponent, Base<P> {
  private final Supplier<P> parent;
  private final IdProperty id;
  private final ParentProperty[] properties;

  ParentComponentImpl(Supplier<P> parent, IdProperty id) {
    this(parent, id, null);
  }

  private ParentComponentImpl(Supplier<P> parent, IdProperty id, ParentProperty[] properties) {
    this.parent = parent;
    this.id = id;
    this.properties = properties;
  }

  @Override
  public Parent get() {
    return origin();
  }

  @Override
  public P origin() {
    final P p = parent.get();
    id.onParent(p);
    if (nonNull(properties)) for (ParentProperty property : properties) property.onParent(p);
    return p;
  }
}
