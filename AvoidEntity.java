//AvoidEntities are entities the player needs to avoid colliding with.
//If a player collides with an avoid, it reduces the players Hit Points (HP).
public class AvoidEntity extends Entity implements Consumable, Scrollable {
    
    //Location of image file to be drawn for an AvoidEntity
    private static final String AVOID_IMAGE_FILE = "gifs/LBeam.gif";
   // "assets/avoid.gif";
    //Dimensions of the AvoidEntity    
    private static final int AVOID_WIDTH = 100; //75;
    public static final int AVOID_HEIGHT = 100;//75;
    //Speed that the avoid moves each time the game scrolls
    private static final int AVOID_SCROLL_SPEED = 5;
    
    public AvoidEntity(){
        this(0, 0);        
    }
    
    public AvoidEntity(int x, int y){
        super(x, y, AVOID_WIDTH, AVOID_HEIGHT, AVOID_IMAGE_FILE);  
    }
    
    
    public int getScrollSpeed(){
        return AVOID_SCROLL_SPEED;
    }
    
    //Move the avoid left by the scroll speed
    public void scroll(){
        setX(getX() - AVOID_SCROLL_SPEED);
    }
    
    //Colliding with an AvoidEntity does not affect the player's score
    public int getPointsValue(){
       return 0;
       //throw new IllegalStateException("Hey 102 Student! You need to implement getPointsValue in AvoidEntity.java!");
    }
    
    //Colliding with an AvoidEntity Reduces players HP by 1
    public int getDamageValue(){
        return -1;
    }
    
}
