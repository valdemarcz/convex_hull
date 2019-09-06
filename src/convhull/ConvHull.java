/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package convhull;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.util.Collections;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author W
 */
public class ConvHull extends Application {
    int canvasWidth = 600;
    int canvasHeight = 500;
    Canvas canvas;
    GraphicsContext gc;
    public static ArrayList<Point> pointsAddedOnClick = new ArrayList<>();
    Button drawBtn = new Button("Draw");
    Button saveBtn = new Button("Save");
    Button resetBtn = new Button("Reset");
    Button randomBtn = new Button("Random");
    Label pauseTimeLbl = new Label("Pause Time:");
    TextField integerField;
    Label randomDotsCountLbl = new Label("Number of dots:");
    TextField randomField;
    String operation;
    TextArea outputArea = new TextArea();

        /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Monothone Chain");
        canvas = new Canvas(canvasWidth, canvasHeight);

        
        
        Line lineHeight = new Line(canvas.getWidth(), 0.0, canvas.getWidth(), canvas.getHeight());
        Line lineWidth = new Line(0.0, canvas.getWidth(), canvas.getWidth(), canvas.getHeight());
        lineHeight.setStroke(Color.BLACK);
        lineHeight.setStrokeWidth(4.0);
        lineWidth.setStroke(Color.BLACK);
        lineWidth.setStrokeWidth(4.0);
        gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ArrayList<Point> listPoint = new ArrayList<Point>();
 
        
        //MouseClick
        canvas.setOnMouseClicked(event ->{
           double x = event.getX(), y = event.getY();
           pointsAddedOnClick.add(new Point(x,y));  
           gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
           drawPoints(gc, pointsAddedOnClick);
           gc.setStroke(Color.BLACK);
           gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
        });
        
        
        
        integerField = new TextField("5"){
        @Override public void replaceText(int start, int end, String text) {
        if (text.matches("[0-9]*")) {
          super.replaceText(start, end, text);
        }
      }

      @Override public void replaceSelection(String text) {
        if (text.matches("[0-9]*")) {
          super.replaceSelection(text);
        }
      }
    };
        
        
        randomField = new TextField("5"){
        @Override public void replaceText(int start, int end, String text) {
        if (text.matches("[0-9]*")) {
          super.replaceText(start, end, text);
        }
      }

      @Override public void replaceSelection(String text) {
        if (text.matches("[0-9]*")) {
          super.replaceSelection(text);
        }
      }
    };
        VBox btnsVBox = new VBox(8);
        
        resetBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                listPoint.clear();
                pointsAddedOnClick.clear();
                gc.setStroke(Color.BLACK);
                gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }
        });
        
          randomBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                listPoint.clear();
                pointsAddedOnClick.clear();
                gc.setStroke(Color.BLACK);
                gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
                createRandomDot();
                drawPoints(gc, pointsAddedOnClick);
            }
        });
        
        drawBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e){
                if(pointsAddedOnClick.size() > 0){
                listPoint.addAll(pointsAddedOnClick);
                drawBtn.setDisable(false);
                convexHull(listPoint);
                }
            }
        });
        
       
        saveBtn.setOnAction((e)->{
            FileChooser savefile = new FileChooser();
            savefile.setTitle("Save File");
            
            File file = savefile.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    WritableImage writableImage = new WritableImage(600, 500);
                    canvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {
                    System.out.println("Error!");
                }
            }
            
        });
        
        btnsVBox.getChildren().addAll(resetBtn, saveBtn, drawBtn, randomBtn);
        btnsVBox.getChildren().addAll(pauseTimeLbl, integerField, randomDotsCountLbl, randomField);
        Button[] buttonsArr = {resetBtn, saveBtn, drawBtn, randomBtn};
        for(Button btn : buttonsArr) {
            btn.setMinWidth(90);
            btn.setCursor(Cursor.HAND);
            btn.setTextFill(Color.WHITE);
            btn.setStyle("-fx-background-color: #666;");
        }
        btnsVBox.setPadding(new Insets(5));
        btnsVBox.setStyle("-fx-background-color: #999");
        btnsVBox.setPrefWidth(100);
        BorderPane upperPane = new BorderPane();
        upperPane.setLeft(btnsVBox);
        upperPane.setCenter(canvas);
        BorderPane rootPane = new BorderPane();
        
        rootPane.setTop(upperPane);
        rootPane.setBottom(outputArea);
       
        Scene scene = new Scene(rootPane, 700, 650);
        
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
       
        
        }
    
    
    
    public void convexHull(ArrayList<Point> convexList){
        final int sleepTime = Integer.parseInt(integerField.getText());
        ArrayList<Point> newPoints = new ArrayList<>(convexList);
	Collections.sort(newPoints);
        
        outputArea.appendText("Sortowana jest lista z punktami zaznaczonymi, lub wylosowanymi \n");
        
        
        drawBtn.setDisable(true);
        resetBtn.setDisable(true);
        randomBtn.setDisable(true);
        
        final ArrayList<Point> listUpper = new ArrayList<Point>();
        final ArrayList<Point> listLower = new ArrayList<Point>();
        new Thread(new Runnable() {
            public void run(){
                
                for (int i = 0; i< newPoints.size(); i++) {
                   
                   while (listUpper.size() >= 2 && !rightTurn(listUpper.get(listUpper.size() - 2),
									listUpper.get(listUpper.size() - 1),
									newPoints.get(i))){
                       Platform.runLater(new Runnable(){
                           public void run() {
                               drawHullsAndPoints(gc, listUpper, Color.BLUE, null, null, newPoints);
                           }
                       });
                       try{
                           Thread.sleep(sleepTime);
                       } catch (Exception e){
                       }
                       listUpper.remove(listUpper.size() - 1);
                       
                            Platform.runLater(new Runnable(){
                                public void run(){
                                    drawHullsAndPoints(gc, listUpper, Color.BLUE, null, null, newPoints);
                                    operation = "Punkt nie pasuje, jest usunięty i kolejny jest sprawdzany\n";
                                    outputArea.appendText("Punkt nie pasuje, jest usunięty i kolejny jest sprawdzany\n\n");
                                    
                                }
                            });
                            try {
				Thread.sleep(sleepTime);
                            } catch (Exception e) {
                            }
                   }
                   listUpper.add(newPoints.get(i));
                   
                   Platform.runLater(new Runnable() {
                       public void run() {
                           drawHullsAndPoints(gc, listUpper, Color.BLUE, null, null, newPoints);
                           operation = "Brany jest punkt do otoczki wierzchniej, oraz sprawdzana jego pozycja według 2 poprzednich punktów, \nJeżeli on znajduje się w orientacji przeciwko strzałki zegaru, jest dodawany do wierzchiiej części otoczki\n \n";
                           outputArea.appendText("Brany jest punkt do otoczki wierzchniej, oraz sprawdzana jego pozycja wedłóg 2 poprzednich punktów, \nJeżeli on znajduje się w orientacji przeciwko strzałki zegaru, jest dodawany do wierzchniej części otoczki\n \n");
                       }
                   });
                   try{
                       Thread.sleep(sleepTime);
                   } catch(Exception e){
                   }
                }
                for (int i = newPoints.size()- 1; i >= 0; i--) {
                    while (listLower.size() >= 2 && !rightTurn(listLower.get(listLower.size() - 2),
									listLower.get(listLower.size() - 1),
									newPoints.get(i))) {
			Platform.runLater(new Runnable() {
                            public void run() {
				drawHullsAndPoints(gc, listUpper, Color.BLUE, listLower, Color.GREEN, newPoints);
                            }
			});
			try {
                            Thread.sleep(sleepTime);
			} catch (Exception e) {
			}
			listLower.remove(listLower.size() - 1);
			Platform.runLater(new Runnable() {
                            public void run() {
                                drawHullsAndPoints(gc, listUpper, Color.BLUE, listLower, Color.GREEN, newPoints);
                                outputArea.appendText("Punkt nie pasuje, jest usunięty i kolejny jest sprawdzany\n\n");
                            }
			});
			try {
                            Thread.sleep(sleepTime);
			} catch (Exception e) {
			}
                    }
                    listLower.add(newPoints.get(i));
                    Platform.runLater(new Runnable() {
                        public void run() {
                            drawHullsAndPoints(gc, listUpper, Color.BLUE, listLower, Color.GREEN, newPoints);
                            outputArea.appendText("Brany jest punkt do otoczki dolnej, oraz sprawdzana jego pozycja według 2 poprzednich punktów, \nJeżeli on znajduje się w orientacji przeciwko strzałki zegaru, jest dodawany do wierzchniej części otoczki\n \n");
			}
                    });
                    try {
                        Thread.sleep(sleepTime);
                    } catch (Exception e) {
                    }
                    if(i == 0){
                        drawBtn.setDisable(false);
                        resetBtn.setDisable(false);
                        randomBtn.setDisable(false);
                    }
		}
            }
            
        }).start();
        
        
        
        
    }
      private void drawPoints(GraphicsContext gc, ArrayList<Point> pointsList){
        for(Point x : pointsList){
        gc.fillOval(x.x, x.y, 20, 20);
        }
    }
    private boolean rightTurn(Point a, Point b, Point c) {
		return (b.x - a.x) * (c.y - a.y)
				- (b.y - a.y) * (c.x - a.x) > 0;
    }
    
    public void drawConvexHull(GraphicsContext gc, Color color, ArrayList<Point> list){
        gc.setStroke(color);
        gc.beginPath();
        gc.setLineWidth(5);
        double x = list.get(0).x;
        double y = list.get(0).y;
        gc.moveTo(x, y);    
        gc.stroke();
        for (int i = 0; i < list.size(); i++){
            x = list.get(i).x;
            y = list.get(i).y;
            gc.lineTo(x, y);
            gc.stroke();
        }
    }
    
    public void drawHullsAndPoints(GraphicsContext gc, ArrayList<Point> list, Color color, ArrayList<Point> secondList, Color c, ArrayList<Point> convexList){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        for (int i = 0; i< convexList.size(); i++){
            gc.fillOval(convexList.get(i).x-10, convexList.get(i).y - 10, 20, 20);
        }
        if (list != null) {
			drawConvexHull(gc, color, list);
		}
		if(secondList != null){
			drawConvexHull(gc, c, secondList);
		}
    }
    
 public void createRandomDot(){
            int width = ((int) canvas.getWidth());
            int height = ((int) canvas.getHeight());
            for(int i = 0; i < Integer.parseInt(randomField.getText()); i++){
                int c = ((int) (Math.random() * (width)));
                int d = ((int) (Math.random() * (height)));
                Point p = new Point(c, d);
                pointsAddedOnClick.add(p);
            }
        }
    
    

    
}
