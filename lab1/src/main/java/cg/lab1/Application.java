package cg.lab1;

import cg.lab1.ui.*;

public class Application {

    public static void main(String[] args) {
        Label label = new Label();
        Panel panel = new Panel(label);
        PrevButton prevButton = new PrevButton(panel);
        NextButton nextButton = new NextButton(panel);

        MainWindow mainWindow = new MainWindow(panel, label, prevButton, nextButton);
        mainWindow.open();
    }
}
