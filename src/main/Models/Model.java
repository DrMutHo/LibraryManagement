package main.Models;

import main.Views.ViewFactory;

public class Model {
    private static Model model;
    private ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;

    private boolean signup_SucccessFlag;
    
    private Model() {
        this.viewFactory = new ViewFactory();
        this.databaseDriver = new DatabaseDriver();
        this.signup_SucccessFlag = false;
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

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }

    public boolean getsignup_SuccessFlag() {
        return this.signup_SucccessFlag;
    }

    public void setsignup_SuccessFlag(boolean flag) {
        this.signup_SucccessFlag = flag;
    }

}
