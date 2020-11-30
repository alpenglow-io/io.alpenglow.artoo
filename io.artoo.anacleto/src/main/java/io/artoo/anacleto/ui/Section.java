package io.artoo.anacleto.ui;

import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import io.artoo.lance.func.Func;

import static com.googlecode.lanterna.gui2.BorderLayout.Location;
import static com.googlecode.lanterna.gui2.Direction.HORIZONTAL;
import static com.googlecode.lanterna.gui2.Direction.VERTICAL;

public interface Section extends Element<Panel> {
  static Section border(Func.Uni<? super Location, ? extends Element<? extends Component>> elements) {
    return new Border(elements);
  }

  @SafeVarargs
  static Section vertical(Element<? extends Component>... elements) {
    return new Linear(VERTICAL, elements);
  }

  @SafeVarargs
  static Section horizontal(Element<? extends Component>... elements) {
    return new Linear(HORIZONTAL, elements);
  }

  final class Border implements Section {
    private final Func.Uni<? super Location, ? extends Element<? extends Component>> elements;

    public Border(final Func.Uni<? super Location, ? extends Element<? extends Component>> elements) {
      this.elements = elements;
    }

    @Override
    public final Panel content() {
      final var panel = new Panel(new BorderLayout());

      for (final var location : Location.values()) {
        final var node = elements.apply(location);

        if (node != null) panel.addComponent(node.content().setLayoutData(location));
      }

      return panel;
    }
  }

  final class Linear implements Section {
    private final Direction direction;
    private final Element<? extends Component>[] nodes;

    public Linear(final Direction direction, final Element<? extends Component>[] nodes) {
      this.direction = direction;
      this.nodes = nodes;
    }

    @Override
    public final Panel content() {
      final var panel = new Panel(new LinearLayout(direction));

      for (final var node : nodes) panel.addComponent(node.content());

      return panel;
    }
  }

}

