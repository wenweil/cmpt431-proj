package Game.Game;

public class netMutex {
    private String ownerID = "";
    private boolean locked = false;

    public netMutex(){

    }

    public boolean reqLock(String userID){
        if (!locked){
            locked = true;
            ownerID = userID;
            return true;
        }else
            return false;
    }

    public boolean hasAccess (String userID){
        return userID.equals(ownerID);
    }

    public boolean reqUnlock(String userID) {
        if (hasAccess(userID)) {
            ownerID = "";
            locked = false;
            return true;
        } else
            return false;
    }

}
