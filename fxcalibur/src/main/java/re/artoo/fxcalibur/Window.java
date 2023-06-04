package re.artoo.fxcalibur;

import javafx.stage.Stage;

@FunctionalInterface
public interface Window {
  Window open(Stage stage);
}
