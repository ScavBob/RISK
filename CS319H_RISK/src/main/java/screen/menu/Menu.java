package screen.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import screen.Screen;
import screen.button.TransitionButton;

public abstract class Menu implements Screen {

	private Canvas canvas;

	private void addCanvas(){
		canvas = new Canvas(1280.0, 720.0);
	}

	public void setBackground(Group root, String backgroundPath){
		addCanvas();
		drawImage(backgroundPath, 0, 0);
		root.getChildren().add(canvas);
	}

	public void drawImage(String imagePath, int x, int y){
		Image label = new Image(imagePath);
		canvas.getGraphicsContext2D().drawImage(label, x, y);
	}

	public void drawRect(String color, int x, int y, int width, int height){
		GraphicsContext graphics = canvas.getGraphicsContext2D();
		graphics.setFill(Paint.valueOf(color));
		graphics.fillRect(x, y ,width ,height );
	}

	public void strokeRect(String color, int x, int y, int width, int height, int girth){
		GraphicsContext graphics = canvas.getGraphicsContext2D();
		graphics.setStroke(Paint.valueOf(color));
		graphics.setLineWidth(girth);
		graphics.strokeRect(x, y, width, height);
	}

	public void addTransitionButton(Group root, String text, int x, int y, int width, int height, String imagePath, Screen screen) {
		TransitionButton button = new TransitionButton(text, x, y, width, height, imagePath, screen);
		root.getChildren().add(button);
	}

	public Button addButtons(Group root, String text, double x, double y, int width, int height, String imagePath,
							 EventHandler<ActionEvent> eventHandler) {
		Button button = new Button(text);
		button.setLayoutX(x);
		button.setLayoutY(y);
		if (height != 0)
			button.setMinHeight(height);
		if (width != 0)
			button.setMinWidth(width);
		if(eventHandler != null)
			button.setOnAction(eventHandler);
		Image image = new Image(imagePath, width, height, false, false);
		if(image != null)
			button.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		root.getChildren().add(button);
		return button;
	}


}
