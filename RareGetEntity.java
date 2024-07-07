//A RareGetEntity is a special kind of GetEntity that spawns more infrequently than the regular GetEntity
//When consumed, RareGetEntities restores the PlayerEntity's HP in addition to awarding points
//Otherwise, behaves the same as a regular GetEntity
public class RareGetEntity extends GetEntity{
    
    //Location of image file to be drawn for a RareGetEntity
    private static final String RAREGET_IMAGE_FILE = "gifs/PotionHP.png";
    //"assets/rare_get.gif";
    
    public RareGetEntity(){
        this(0, 0);        
    }
    
    public RareGetEntity(int x, int y){
        super(x, y, RAREGET_IMAGE_FILE);  
    }
    
    
    //I'm missing something here...
    //What's special about RareGetEntities?
    public int getPointsValue(){
        return 20;
        //throw new IllegalStateException("Hey 102 Student! You need to implement getPointsValue in AvoidEntity.java!");
     }
     
     //Colliding with a RareGetEntity increases a players HP by 1
     public int getDamageValue(){
         return 1;
     }
    
}
