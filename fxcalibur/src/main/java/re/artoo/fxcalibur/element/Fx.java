package re.artoo.fxcalibur.element;

import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import re.artoo.fxcalibur.Asset;

import java.io.IOException;

public enum Fx {
  Calibur;

  private Window window;

  public static final class FxApplication extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
      Font.loadFont(Asset.font("DMSans-Medium.tff").load(), 14);
      stage.setScene(Fx.Calibur.window.render(stage));
      stage.initStyle(StageStyle.TRANSPARENT);
      stage.show();
    }
  }

  public void execute(Window window) {
    Fx.Calibur.window = window;
    javafx.application.Application.launch(FxApplication.class);
  }
}
