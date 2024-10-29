package main.Models;

import main.Views.ViewFactory;

public class Model {
    private static Model model;
    private ViewFactory viewFactory;
    private boolean signupSuccessFlag; 
    private final DatabaseDriver databaseDriver;

    private Model() {
        this.viewFactory = new ViewFactory();
        this.signupSuccessFlag = false;
        this.databaseDriver = new DatabaseDriver();
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public boolean getSignupSuccessFlag() {
        return this.signupSuccessFlag;
    }

    public void setSignupSuccessFlag(boolean flag) {
        this.signupSuccessFlag = flag;
    }

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }
}
