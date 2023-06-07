package re.artoo.fxcalibur.ui.element;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import re.artoo.fxcalibur.Asset;

public final class Button extends javafx.scene.control.Button {
  public enum Size { Mini, Tiny, Small, Medium, Large }
  public enum Type { Primary, Secondary }

  private static final Asset css = Asset.css("button.css");
  private static final Color primary = Color.rgb(79, 70, 229);
  private static final Color hover = Color.rgb(99, 102, 241);
  private static final Color borderHover = Color.rgb(229, 231, 235);

  {
    getStylesheets().add(css.location().toExternalForm());
    setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(209, 213, 219, 0.5), 5, 25, 0, 2));
  }

  public Button(String text) {
    this(text, Type.Primary, Size.Medium);
  }
  public Button(String text, Type type, Size size) {
    super(text);
    getStyleClass().add(type.name().toLowerCase());
    getStyleClass().add(size.name().toLowerCase());
  }
}
