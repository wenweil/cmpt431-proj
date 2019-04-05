package Game.Server;

public class Mutex {
  private String ownerID = "";
  private boolean locked = false;

  public Mutex(){

  }

  public boolean reqLock(String userID){
    if (!locked){
      System.out.println("locked "+ userID);
      locked = true;
      ownerID = userID;
      return true;
    }else if (userID.equals(ownerID)) {
      System.out.println("locked ID eq "+ userID);
      return true;
    }
    else {
      System.out.println("access denied");
      return false;
    }
  }

  public boolean hasAccess (String userID){
    return userID.equals(ownerID);
  }

  public boolean reqUnlock(String userID) {
    if (hasAccess(userID)) {
      ownerID = "";
      locked = false;
      System.out.println("unlocked "+ userID);
      return true;
    } else {
      System.out.println("unlock access denied "+ userID);
      return false;
    }
  }

}
