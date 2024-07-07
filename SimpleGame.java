import java.awt.*;
import java.awt.event.*;
import java.util.*;

//A Simple version of the scrolling game, featuring Avoids, Gets, and RareGets
//Players must reach a score threshold to win
//If player runs out of HP (via too many Avoid collisions) they lose
public class SimpleGame extends ScrollingGameEngine {
    
    //Dimensions of game window
    private static final int DEFAULT_WIDTH = 900;
    private static final int DEFAULT_HEIGHT = 600;  
    
    //Starting PlayerEntity coordinates
    private static final int STARTING_PLAYER_X = 0;
    private static final int STARTING_PLAYER_Y = 100;
    
    //Score needed to win the game
    private static final int SCORE_TO_WIN = 650;
    
    //Maximum that the game speed can be increased to
    //(a percentage, ex: a value of 300 = 300% speed, or 3x regular speed)
    private static final int MAX_GAME_SPEED = 300;
    //Interval that the speed changes when pressing speed up/down keys
    private static final int SPEED_CHANGE = 20;    
    
    private static final String INTRO_SPLASH_FILE = "gifs/FSplash.gif";       
    private static final String INTRO_BG_FILE = "gifs/DPlanet.jpg";
    //"gifs/FCity.jpg";
    //"gifs/DarkSun.jpg";
    
    //Key pressed to advance past the splash screen
    public static final int ADVANCE_SPLASH_KEY = KeyEvent.VK_ENTER;
    
    //Interval that Entities get spawned in the game window
    //ie: once every how many ticks does the game attempt to spawn new Entities
    private static final int SPAWN_INTERVAL = 45;
    
    
    //A Random object for all your random number generation needs!
    public static final Random rand = new Random();

    public static final int AVS_CHANGE = 60;
    public static final int GET_CHANGE = 90;
    public static final int RAREG_CHANGE = 100;
    public boolean shield = false;
    public int potionCount = 0;


    
    
    
    //Player's current score
    private int score;
    
    //Stores a reference to game's PlayerEntity object for quick reference
    //(This PlayerEntity will also be in the displayList)
    private PlayerEntity player;
    
    
    
    
    
    public SimpleGame(){
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public SimpleGame(int gameWidth, int gameHeight){
        super(gameWidth, gameHeight);
    }
    
    
    //Performs all of the initialization operations that need to be done before the game starts
    protected void preGame(){
        
       // this.setBackgroundColor(Color.BLACK);
        this.setBackgroundImage(INTRO_BG_FILE);
        //this.setBackgroundColor(Color.BLACK);
        player = new PlayerEntity(STARTING_PLAYER_X, STARTING_PLAYER_Y);
        displayList.add(player); 
        score = 0;
        this.setSplashImage(INTRO_SPLASH_FILE);
       
    }
    
    
    //Called on each game tick
    protected void updateGame(){
        //scroll all scrollable Entities on the game board
        scrollEntities();   
        //Spawn new entities only at a certain interval
        if (ticksElapsed % SPAWN_INTERVAL == 0){            
            spawnNewEntities();
            garbageCollectEntities();
        }
        
        //Update the title text on the top of the window
        setTitleText("HP:" + player.getHP() +",Score:"+ score);        
    }
    
    
    //Scroll all scrollable entities per their respective scroll speeds
    protected void scrollEntities(){
        //int num = rand.nextInt(getWindowWidth());
        for (int i = 1; i < displayList.size(); i++){
            Entity e1 = displayList.get(i); 
            if (e1 instanceof Scrollable){
                ((Scrollable)e1).scroll();
            if (player.isCollidingWith(e1)){
               // if (e1 instanceof Consumable){
                handlePlayerCollision(((Consumable)e1));
                displayList.remove(i);
            }
            }
        }
           
        }
    
    
    
    //Handles "garbage collection" of the displayList
    //Removes entities from the displayList that are no longer relevant
    //(i.e. will no longer need to be drawn in the game window).
    protected void garbageCollectEntities(){
        for (int i=0; i<displayList.size();i++){
            Entity eRemove = displayList.get(i);
            if ((eRemove.getX() <= eRemove.getWidth()/-2) && (eRemove instanceof Scrollable)){
                displayList.remove(i);
            }
        }
    }
    
    
    //Called whenever it has been determined that the PlayerEntity collided with a consumable
    private void handlePlayerCollision(Consumable collidedWith){
      
        if (collidedWith instanceof GetEntity){
            shield = true;
            player.modifyHP(collidedWith.getDamageValue());
            score += collidedWith.getPointsValue();
        }
        if ((collidedWith instanceof AvoidEntity) && (shield == true)){
            shield = false;
            player.modifyHP(collidedWith.getDamageValue());
            score += collidedWith.getPointsValue();
        }
        else{
            potionCount++;
            player.modifyHP(collidedWith.getDamageValue());
            score += collidedWith.getPointsValue();
        }
        //****player collects a shield(Get) to protect against lasers(Avoid) and this allows them to 
        //collide into one laser without consequences
    } 

    
    
    //get bottom edge to account for entities
    //Spawn new Entities on the right edge of the game board
    private void spawnNewEntities(){

        int anum = rand.nextInt(35);

        if(anum < AVS_CHANGE){ 
            int avY = rand.nextInt(getWindowHeight() - player.getHeight()); //gets a random integer within the width of the game window
            AvoidEntity a1 = new AvoidEntity(getWindowWidth(), avY); //creates an entity at current height(x) with a random y value
            if (checkCollision(a1).size() == 0){ //checks if any entities are colliding with a1
                displayList.add(a1); //displays a1 if it is not colliding with any other entities
            }
            //DEFAULT_HEIGHT - AvoidEntity.AVOID_HEIGHT
        }
        if (anum < GET_CHANGE){ 
            int getY = rand.nextInt(getWindowHeight()-player.getHeight());
            GetEntity g1 = new GetEntity(getWindowWidth(), getY);
            if (checkCollision(g1).size() == 0){
               displayList.add(g1);   
            }   
            //DEFAULT_WIDTH - GetEntity.GET_HEIGHT
        }
        if(anum < RAREG_CHANGE){ 
            int rareY = rand.nextInt(getWindowHeight()-player.getHeight());
            RareGetEntity r1 = new RareGetEntity(getWindowWidth(), rareY);
            if (checkCollision(r1).size() == 0){
                displayList.add(r1); 
            }
            //DEFAULT_WIDTH - RareGetEntity.GET_HEIGHT
        }
    }
    
    
    //Called once the game is over, performs any end-of-game operations
    protected void postGame(){

        if ((score < SCORE_TO_WIN) && (potionCount >= 3)){
            potionCount = 0;
            super.setTitleText("Game is over! YOU WIN!");
        }
        if (player.getHP() <= 0){
            potionCount = 0;
            super.setTitleText("Game is over! YOU LOSE!");
        }
    }
    
    
    //Determines if the game is over or not
    //Game can be over due to either a win or lose state
    protected boolean isGameOver(){
        boolean result = false;
        if (score > SCORE_TO_WIN){
            return true;
        }
        if (player.getHP() <= 0){
            return true;
        }
        return result;
    }
    
    
    
    
    //Reacts to a single key press on the keyboard
    protected void handleKeyPress(int key){
        
        setDebugText("Key Pressed!: " + KeyEvent.getKeyText(key));
        
        //if a splash screen is active, only react to the "advance splash" key... nothing else!
        if (getSplashImage() != null){
            if (key == ADVANCE_SPLASH_KEY)
                super.setSplashImage(null);
            
        }else if (key == UP_KEY){
            //setDebugText("WHeight!: " + getWindowHeight()+ "PHeight"+ player.getY());
            if(0 <= player.getY()) {// making the player stay within the height
                player.setY(player.getY()-player.getMovementSpeed());
            }
        }else if (key == DOWN_KEY){
           // setDebugText("WHeight!: " + getWindowHeight()+ "PHeight"+ player.getY());
            if(getWindowHeight() >= player.getY() + player.getHeight()){
                player.setY(player.getY()+player.getMovementSpeed());
            }
        }else if (key == LEFT_KEY){
            if (0 <= player.getX())
            player.setX(player.getX()-player.getMovementSpeed());
        }else if (key == RIGHT_KEY){
           // setDebugText("WWidth!: " + getWindowWidth()+ "PWidth"+ player.getX());
            if (getWindowWidth() >= player.getX() + player.getWidth())
            player.setX(player.getX()+ player.getMovementSpeed());
        }else if (player.getMovementSpeed() == MAX_GAME_SPEED){
            if (key == SPEED_DOWN_KEY){
                setGameSpeed(getGameSpeed() - SPEED_CHANGE);
            }else if (key == SPEED_UP_KEY){
                setGameSpeed(getGameSpeed() + SPEED_CHANGE);
        }
        }else if (key == KEY_PAUSE_GAME){
            isPaused = !isPaused;
        }

        
    }    
    
    
    //Handles reacting to a single mouse click in the game window
    //****Won't be used in Simple Game... you could use it in Creative Game though!****
    protected MouseEvent handleMouseClick(MouseEvent click){
        if (click != null){ //ensure a mouse click occurred
            int clickX = click.getX();
            int clickY = click.getY();
            setDebugText("Click at: " + clickX + ", " + clickY);
        }
        return click;//returns the mouse event for any child classes overriding this method
    }
    
    
    
    
}
