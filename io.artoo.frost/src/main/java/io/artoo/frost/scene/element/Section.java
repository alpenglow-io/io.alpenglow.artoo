package io.artoo.frost.scene.element;

import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import io.artoo.frost.scene.Element;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;

import static com.googlecode.lanterna.gui2.BorderLayout.Location.BOTTOM;
import static com.googlecode.lanterna.gui2.BorderLayout.Location.CENTER;
import static com.googlecode.lanterna.gui2.BorderLayout.Location.LEFT;
import static com.googlecode.lanterna.gui2.BorderLayout.Location.RIGHT;
import static com.googlecode.lanterna.gui2.BorderLayout.Location.TOP;
import static com.googlecode.lanterna.gui2.Direction.HORIZONTAL;
import static com.googlecode.lanterna.gui2.Direction.VERTICAL;
import static io.artoo.lance.type.Nullability.nonNullable;

public interface Section extends Element<Panel> {
  static Section border(Func.Unary<Border.Location> location) {
    return new Border(location);
  }

  @SafeVarargs
  static Section vertical(Element<? extends Component>... elements) {
    return new Linear(VERTICAL, Many.from(elements));
  }

  static Section vertical(Many<Element<? extends Component>> elements) {
    return new Linear(VERTICAL, elements);
  }

  static Section horizontal(Many<Element<? extends Component>> elements) {
    return new Linear(HORIZONTAL, elements);
  }

  @SafeVarargs
  static Section horizontal(Element<? extends Component>... elements) {
    return new Linear(HORIZONTAL, Many.from(elements));
  }

  final class Border extends LazyElement<Panel> implements Section {
    private final Func.Unary<Border.Location> location;

    private Border(final Func.Unary<Location> location) {this.location = location;}

    @Override
    public final Panel content() {
      return location.apply(new Location()).apply(new Panel(new BorderLayout()));
    }

    public static final class Location {
      private final Element<? extends Component> top;
      private final Element<? extends Component> bottom;
      private final Element<? extends Component> center;
      private final Element<? extends Component> left;
      private final Element<? extends Component> right;

      private Location() {
        this(Element.empty(), Element.empty(), Element.empty(), Element.empty(), Element.empty());
      }

      private Location(
        final Element<? extends Component> top,
        final Element<? extends Component> bottom,
        final Element<? extends Component> center,
        final Element<? extends Component> left,
        final Element<? extends Component> right
      ) {
        this.top = top;
        this.bottom = bottom;
        this.center = center;
        this.left = left;
        this.right = right;
      }

      public final Location top(final Element<? extends Component> element) {
        return new Location(
          nonNullable(element, "element can't be null."),
          this.bottom,
          this.center,
          this.left,
          this.right
        );
      }

      public final Location bottom(final Element<? extends Component> element) {
        return new Location(
          this.top,
          nonNullable(element, "element can't be null."),
          this.center,
          this.left,
          this.right
        );
      }

      public final Location center(final Element<? extends Component> element) {
        return new Location(
          this.top,
          this.bottom,
          nonNullable(element, "element can't be null."),
          this.left,
          this.right
        );
      }

      public final Location left(final Element<? extends Component> element) {
        return new Location(
          this.top,
          this.bottom,
          this.center,
          nonNullable(element, "element can't be null."),
          this.right
        );
      }

      public final Location right(final Element<? extends Component> element) {
        return new Location(
          this.top,
          this.bottom,
          this.center,
          this.left,
          nonNullable(element, "element can't be null.")
        );
      }

      private Panel apply(final Panel panel) {
        top.get(component -> panel.addComponent(component.setLayoutData(TOP)));
        bottom.get(component -> panel.addComponent(component.setLayoutData(BOTTOM)));
        center.get(component -> panel.addComponent(component.setLayoutData(CENTER)));
        left.get(component -> panel.addComponent(component.setLayoutData(LEFT)));
        right.get(component -> panel.addComponent(component.setLayoutData(RIGHT)));

        return panel;
      }
    }
  }

  final class Linear extends LazyElement<Panel> implements Section {
    private final Direction direction;
    private final Many<Element<? extends Component>> elements;

    private Linear(final Direction direction, final Many<Element<? extends Component>> elements) {
      this.direction = direction;
      this.elements = elements;
    }

    @Override
    public final Panel content() {
      final var panel = new Panel(new LinearLayout(direction));

      for (final var component : elements) {
        component.get(panel::addComponent);
      }

      return panel;
    }
  }

}

