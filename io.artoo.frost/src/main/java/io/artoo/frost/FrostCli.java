package io.artoo.frost;

import io.artoo.frost.dropbox.Dropbox;
import io.artoo.frost.scene.Scene;
import io.artoo.frost.ui.Folders;

public class FrostCli {
  public static void main(String[] args) {
    final var scene = Scene.frame();
    scene.open(
      scene.sectionBorder(location -> location
        .top(
          scene.label("Something nice over here!")
        )
        .left(
          Folders.dropbox(
            scene,
            Dropbox.client(
              System.getenv("DBX_ACCESS_TOKEN")
            )
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
  }
}
