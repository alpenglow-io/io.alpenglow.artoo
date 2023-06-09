package re.artoo.fxcalibur.ui.component;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import re.artoo.fxcalibur.Asset;
import re.artoo.lance.query.Many;

import java.util.function.Supplier;

import static javafx.animation.Interpolator.EASE_BOTH;
import static javafx.scene.effect.BlurType.ONE_PASS_BOX;
import static javafx.util.Duration.ZERO;

public interface Button {
  enum size implements Default.Attribute<size> {mini, tiny, small, medium, large}

  enum color implements Default.Attribute<color> {primary, secondary, success, warning, error, gradient}

  enum effect implements Default.Attribute<effect> {shadow}

  default Node primary() {
    return new Default("");
  }

  final class Default extends StackPane implements Button {
    public sealed interface Attribute<A extends Attribute<A>> extends Supplier<A> {
      @SuppressWarnings("unchecked")
      @Override
      default A get() {
        return (A) this;
      }
    }



    private static final Asset css = Asset.css("button.css");

    {
      setAlignment(Pos.CENTER);
      setPrefHeight(44);
      setPrefWidth(196);
      setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
      setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
      getStylesheets().add(css.location().toExternalForm());
    }

    public Default(String text) {
      this(text, color.primary, size.medium);
    }

    public Default(String text, Attribute<?>... attributes) {
      var button = new javafx.scene.control.Button(text);
      button.setDefaultButton(true);
      var background = new Rectangle(192, 40, Color.web("0072f5"));
      background.setArcHeight(22);
      background.setArcWidth(22);
      getChildren().addAll(background, button);

      var attrs = Many.from(attributes);
      attrs.ofType(color.class).peek(color -> {
        switch (color) {
          case primary -> background.setFill(Color.rgb(0, 114, 245));
          case secondary -> background.setFill(Color.rgb(151, 80, 221));
          case success -> background.setFill(Color.rgb(23, 201, 100));
          case warning -> background.setFill(Color.rgb(245, 165, 36));
          case error -> background.setFill(Color.rgb(243, 18, 96));
          case gradient -> background.setFill(
            new LinearGradient(0, 0, 1, 1, true, null,
              new Stop(0, Color.rgb(255, 78, 205)),
              new Stop(0.4, Color.rgb(0, 114, 245)))
          );
        }
      }).eventually();
      attrs.ofType(effect.class).peek(effect -> {
        switch (effect) {
          case shadow -> {
            var shadow = new Rectangle(174, 40, Color.rgb(0, 114, 245));
            shadow.setEffect(new DropShadow(ONE_PASS_BOX, Color.rgb(0, 114, 245), 28, 0, 0, 4));
            getChildren().add(0, shadow);
          }
        }
      }).eventually();

      var radius = new SimpleDoubleProperty(22.0);
      var width = new SimpleDoubleProperty(192.0);
      var height = new SimpleDoubleProperty(40.0);

      var smaller = new Timeline(
        new KeyFrame(ZERO, new KeyValue(radius, 22), new KeyValue(width, 192, EASE_BOTH), new KeyValue(height, 40, EASE_BOTH)),
        new KeyFrame(Duration.millis(150), new KeyValue(radius, 23), new KeyValue(width, 188, EASE_BOTH), new KeyValue(height, 36, EASE_BOTH))
      );
      var bigger = new Timeline(
        new KeyFrame(ZERO, new KeyValue(radius, 23), new KeyValue(width, 188, EASE_BOTH), new KeyValue(height, 36, EASE_BOTH)),
        new KeyFrame(Duration.millis(150), new KeyValue(radius, 22), new KeyValue(width, 192, EASE_BOTH), new KeyValue(height, 40, EASE_BOTH))
      );

      background.arcWidthProperty().bind(radius);
      background.arcHeightProperty().bind(radius);
      background.widthProperty().bind(width);
      background.heightProperty().bind(height);

      button.setOnMousePressed(it -> smaller.play());
      button.setOnMouseReleased(it -> bigger.play());
    }
  }
}
