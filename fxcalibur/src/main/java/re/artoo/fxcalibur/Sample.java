package re.artoo.fxcalibur;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import re.artoo.fxcalibur.element.Bind;

import static re.artoo.fxcalibur.element.component.button.*;

public class Sample extends Application {
  private static final Asset buttonCss = Asset.css("button");
  private static final Asset mim = Asset.css("mim");

  public static void main(String[] args) {
    launch(Sample.class);
  }

  @Override
  public void start(Stage stage) throws Exception {
    //setUserAgentStylesheet(Dragon.Blue.getUserAgentStylesheet());
    Font.loadFont(Asset.font("DMSans-Medium.tff").load(), 14);
    var vote = new SimpleStringProperty("Vote");
    var resizeable = Bind.button(() -> size.medium);
    VBox box = new VBox(16,
      new Default(emphasis.standard, value.text("Standard")),
      new Default(variant.inverted, color.primary, value.text("Inverted Primary")),
      new Default(type.link, color.rose, value.text("Link Rose")),
      new Default(type.toggle, value.bind(vote), mouse.released(() -> {switch (vote.getValueSafe()) {
        case "Vote" -> vote.set("Voted");
        default -> vote.set("Vote");
      }})),
      new Default(emphasis.primary, size.bind(resizeable), value.text("Primary"), mouse.released(() -> resizeable.setValue(size.huge))),
      new Default(emphasis.standard, variant.inverted, size.mini, value.text("Mini")),
      new Default(emphasis.standard, variant.inverted, size.tiny, value.text("Tiny")),
      new Default(emphasis.standard, variant.inverted, size.small, value.text("Small")),
      new Default(emphasis.standard, variant.inverted, size.medium, value.text("Medium")),
      new Default(emphasis.standard, variant.inverted, size.large, value.text("Large")),
      new Default(emphasis.standard, variant.inverted, size.big, value.text("Big")),
      new Default(emphasis.standard, variant.inverted, size.huge, value.text("Huge")),
      new Default(emphasis.standard, variant.inverted, size.massive, value.text("Massive"))
    );
    box.setPadding(new Insets(16));
    var scene = new Scene(box, 1024, 768);
    //scene.getStylesheets().add(buttonCss.location().toExternalForm());
    scene.getStylesheets().add(mim.location().toExternalForm());
    box.setBackground(Background.fill(Color.TRANSPARENT));
    scene.setFill(Color.TRANSPARENT);
    stage.initStyle(StageStyle.TRANSPARENT);
    stage.setScene(scene);
    stage.show();
  }
}
