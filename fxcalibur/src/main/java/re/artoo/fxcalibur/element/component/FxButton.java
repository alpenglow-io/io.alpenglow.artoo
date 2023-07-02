package re.artoo.fxcalibur.element.component;

import javafx.scene.Node;
import javafx.scene.control.Button;
import re.artoo.fxcalibur.element.Element;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Many;
import re.artoo.lance.value.Array;

import static re.artoo.fxcalibur.element.component.Button.*;
import static re.artoo.fxcalibur.element.component.Button.emphasis.standard;
import static re.artoo.fxcalibur.element.component.Button.size.medium;

public final class FxButton implements Element {
  public emphasis emphasis = standard;
  public variant variant;
  public color color;
  public size size = medium;
  private final TryConsumer1<FxButton> apply;

  @SafeVarargs
  public FxButton(TryConsumer1<FxButton>... applies) {
    this.apply = applies[0];
  }

  @Override
  public Node render() {
    return Many.from(emphasis, variant, color, size)
      .coalesce()
      .keep(new Button(), (button, attribute) -> attribute.apply(button))
      .otherwise(IllegalStateException::new);
  }
}
