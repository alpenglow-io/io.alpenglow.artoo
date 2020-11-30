package io.artoo.anacleto.ui;

import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Component;
import io.artoo.lance.func.Func;

public final class Props {
  private final Id id;
  private final Scene scene;

  public Props(final Id id, final Scene scene) {
    this.id = id;
    this.scene = scene;
  }

  public Text input() {
    final var text = Text.input();
    scene.prop(id, text);
    return text;
  }

  public Text textArea() {
    final var text = Text.area();
    scene.prop(id, text);
    return text;
  }

  public Section border(Func.Uni<? super BorderLayout.Location, ? extends Element<? extends Component>> elements) {
    final var section = Section.border(elements);
    scene.prop(id, section);
    return section;
  }
}
