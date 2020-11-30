package io.artoo.anacleto;

import io.artoo.anacleto.ui.Button;
import io.artoo.anacleto.ui.Frame;
import io.artoo.anacleto.ui.Label;
import io.artoo.anacleto.ui.Section;

import static io.artoo.anacleto.ui.Scene.scene;

public class SnowflakeCli {
  public static void main(String[] args) {
    final var scene = scene();
    scene.open(
      Frame.textual(
        "Winter",
        Section.border(location ->
          switch (location) {
            case TOP -> Label.text("Something nice over here!");
            case LEFT -> Section.vertical(
              Label.text("I'm on the left")
            );
            case CENTER -> scene.prop().textArea();
            case BOTTOM -> Button.close();
            case RIGHT -> null;
          }
        )
      )
    );

/*
      window.setComponent(
        new Panel()
          .setLayoutManager(new BorderLayout())
          .addComponent(
            new Panel(new LinearLayout(VERTICAL))
              .setLayoutData(TOP)
              .addComponent(title)
              .addComponent(new EmptySpace())
          )
          .addComponent(
            new Panel()
              .setLayoutManager(new BorderLayout())
              .addComponent(
                new Panel(new LinearLayout(VERTICAL))
                  .setLayoutData(LEFT)
                  .addComponent(new Label("Sono a sinistra"))
                  .addComponent(new Button("Close", () -> {
                    try {
                      Files.writeString(
                        Paths.get(
                          System.getProperty("user.home"),
                          UUID.randomUUID().toString() +
                            ".txt"
                        ),
                        textArea.ofType(TextBox.class).yield().getText()
                      );
                    } catch (IOException e) {
                      e.printStackTrace();
                    } finally {
                      window.close();
                    }
                  }))
              )
              .addComponent(textArea.yield())
          )
      );
      gui.addWindowAndWait(window);*/
  }
}
