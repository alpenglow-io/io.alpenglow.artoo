package io.artoo.anacleto;

import io.artoo.anacleto.ui.Scene;

public class SnowflakeCli {
  public static void main(String[] args) {
    final var scene = Scene.frame();
    scene.open(
      scene.sectionBorder(location -> location
        .top(
          scene.label("Something nice over here!")
        )
        .left(
          scene.sectionVertical(
            scene.button("Cliccami tutto!")
          )
        )
        .center(
          scene.textArea()
        )
        .bottom(
          scene.buttonClose()
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
