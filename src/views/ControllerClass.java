package views;

import models.FictionBook;
import models.User;


public interface ControllerClass {
    public abstract void preloadData(FictionBook book);
    public abstract void preloadData(User user);
}
