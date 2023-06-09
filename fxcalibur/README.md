```java
var size = Bind.button(size::medium);

Application app = () -> 
  window(background.transparent, border.radius(14), size.half_screen, font.load(""),
    grid.container(gutter.gap(2),
      grid(
        button.primary(size.bind(size), text.value("Primary"))
      ),
      grid(
        button.secondary(size.auto, text.value("Secondary"), on.mouse.clicked(size::large)))
      )
    )
  )
  
```
