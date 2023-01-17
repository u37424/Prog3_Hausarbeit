package de.medieninformatik.client.controller;

import de.medieninformatik.client.model.Model;
import de.medieninformatik.client.view.MainWindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller extends WindowAdapter {

    private final Model model;
    private final MainWindow window;

    public Controller(MainWindow window, Model model){
        //Kennt Model und View
        this.model = model;
        this.window = window;
    }

    private void exitProgram() {
        System.exit(0);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        exitProgram();
    }
}
