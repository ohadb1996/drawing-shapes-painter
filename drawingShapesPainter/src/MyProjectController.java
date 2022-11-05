import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class MyProjectController {
	private double SXcord,SYcord,EXcord,EYcord;
	private boolean fillTheShape = false;
	private final int BLACK_LINE = 68;
	private final int BOTTOM_PANE = 500;
    @FXML
    private ColorPicker colorSelector;

    @FXML
    private ComboBox<String> combox;

    @FXML
    private CheckBox isFilled;

    @FXML
    private Pane pane;

    @FXML
    public void fillShape(ActionEvent event) {
    	//this check box will aloud to fill up the created shape!
    	if(fillTheShape == false)
    		fillTheShape = true;
    	else
    		fillTheShape = false;
    }
    
    @FXML
    void shapeStartPoint(MouseEvent event) {
    	SXcord = event.getSceneX();
    	SYcord = event.getSceneY();
    }

    @FXML
    public void shapeEndPoint(MouseEvent event) {
    	EXcord = event.getSceneX();
    	EYcord = event.getSceneY();
    	if(!(SXcord==EXcord && SYcord==EYcord))//this if ment for to do not create "points"
    		//this if ment for to check if the created shape is in our pane!
    		if(EYcord >= BLACK_LINE && EYcord <= BOTTOM_PANE 
    		&& SYcord >= BLACK_LINE && SYcord <= BOTTOM_PANE 
    		&& EXcord <= pane.getWidth() && EXcord >= 0 
    		&& SXcord <= pane.getWidth() && SXcord >= 0)
    			createShape(SXcord,SYcord,EXcord,EYcord);
    }

    @FXML
    public void clearBtn(ActionEvent event) {
    	pane.getChildren().clear();
    }
    
    @FXML
    public void undoBtn(ActionEvent event) {
    	if (pane.getChildren().size() >= 1) 
    		pane.getChildren().remove(pane.getChildren().size() - 1);
    }

    
    public void initialize() {
    	//entering the shapes to the combo box
    	enteringShapes(combox);
    	//default color
    	colorSelector.setValue(Color.BLACK);
    }
    
    private void enteringShapes(ComboBox<String> combobox) {
    	combobox.getItems().addAll("Line", "Circle","Rectangle");
    	//default shape
    	combobox.setValue("Line");
    }
    
    private void createShape(double Sx,double Sy,double Ex,double Ey) {
        Shape shape;
        shape = pickShape(Sx,Sy, Ex, Ey);
        if (fillTheShape == false) {
            shape.setFill(Color.TRANSPARENT);
            shape.setStroke(colorSelector.getValue());
        } 
        else {
            shape.setFill(colorSelector.getValue());

        }
        pane.getChildren().add(shape);
    }

    
    @FXML
    private Shape pickShape(double Sx,double Sy,double Ex,double Ey) {
    	//the shape we createing depends on the string from our combobox
    	Shape selectedShape = null;
        if(combox.getValue().equals("Rectangle")) 
        	selectedShape = createRectangle(Sx,Sy, Ex, Ey);
        else if(combox.getValue().equals("Circle"))     
        	selectedShape = createCircle(Sx,Sy, Ex, Ey);
        else if(combox.getValue().equals("Line"))
        		selectedShape = createLine(Sx,Sy, Ex, Ey);
        return selectedShape;
    }
    

    private Rectangle createRectangle(double Sx,double Sy,double Ex,double Ey) {
        double width = Math.abs(Sx - Ex);
        double height = Math.abs(Sy - Ey);
        //those if s are ment to check from where to where we creating our shapes
        if(Sy<Ey)
        	if(Sx<Ex)
        		return new Rectangle(Sx, Sy-pane.getLayoutY(), width, height);
        	else
        		return new Rectangle(Ex, Sy-pane.getLayoutY(), width, height);
        else
        	if(Ex<Sx)
        		return new Rectangle(Ex, Ey-pane.getLayoutY(), width, height);
        	else
        		return new Rectangle(Sx, Ey-pane.getLayoutY(), width, height);
    }
    
    private Line createLine(double Sx,double Sy,double Ex,double Ey) {
    	//creating line
    	return new Line(Sx,Sy-pane.getLayoutY(), Ex, Ey-pane.getLayoutY());
    }
    
   
    private Circle createCircle(double Sx,double Sy,double Ex,double Ey) {
    	//creating the circle parameters
    	double radius = Math.sqrt(Math.pow(Ex-Sx,2)+Math.pow(Ey-Sy,2))/2; 
    	double centerX = (Sx+Ex)/2;
    	double centerY = (Sy+Ey)/2-pane.getLayoutY();
    	//before creating the circle we check if his parameters are valid 
    	//incase they are not - the method will replace the circle in the supremum position. 
    	if(centerX+radius > pane.getWidth())
    		centerX -= (radius-Math.abs(centerX-pane.getWidth()));
    	if(centerX-radius < 0)
    		centerX += (radius - centerX);
    	if(centerY+radius > pane.getHeight())
    		centerY -= (radius - Math.abs(centerY-pane.getHeight()));
    	if(centerY-radius < BLACK_LINE)
    		centerY += (radius-Math.abs(centerY));
    	//incase there is no room for placeing the circle because of his size.
    	if(radius*2>pane.getHeight()-BLACK_LINE)
    		return new Circle(pane.getWidth()/2,pane.getHeight()/2, Math.min(pane.getHeight()/2, pane.getWidth()/2));
    	return new Circle(centerX,centerY, radius);
    	
    }
}
