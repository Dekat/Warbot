package edu.warbot.launcher;

import madkit.gui.SwingUtil;
import madkit.kernel.AbstractAgent;
import turtlekit.kernel.TurtleKit;

import javax.swing.*;

public class WarToolBar extends JToolBar {

    public WarToolBar(final AbstractAgent agent) {
        super("Warbot");
//        TurtleKit.addTurleKitActionsTo(this, agent);
//        SwingUtil.scaleAllAbstractButtonIconsOf(this, 22);
    }

}
