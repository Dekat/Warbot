package edu.warbot.gui.launcher.mapSelection;

import edu.warbot.gui.GuiIconsLoader;
import edu.warbot.gui.launcher.WarLauncherInterface;
import edu.warbot.maps.AbstractWarMap;
import edu.warbot.tools.ClassFinder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class MapSelectionDialog extends JFrame implements ActionListener {

    private WarLauncherInterface warLauncherInterface;
    private Vector<MapMiniature> mapMiniaturesList;
    private JList mapMiniaturesJList;

    public MapSelectionDialog(WarLauncherInterface warLauncherInterface) {
        super("Choix de la carte");
        setLayout(new BorderLayout());

        this.warLauncherInterface = warLauncherInterface;

        /* *** Fenêtre *** */
        setSize(500, 350);
        setMinimumSize(new Dimension(200, 200));
        setLocationRelativeTo(null);
        setIconImage(GuiIconsLoader.getLogo("iconLauncher.png").getImage());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mapMiniaturesList = new Vector<>();
        for(Class mapClass : ClassFinder.find(AbstractWarMap.class.getPackage().getName())) {
            if (mapClass.getSuperclass().equals(AbstractWarMap.class)) {
                try {
                    mapMiniaturesList.add(new MapMiniature((AbstractWarMap) mapClass.newInstance(), MapMiniature.SIZE_LARGE));
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        mapMiniaturesJList = new JList(mapMiniaturesList);
        mapMiniaturesJList.setSelectedIndex(0);
        mapMiniaturesJList.setLayoutOrientation(JList.VERTICAL_WRAP);
        mapMiniaturesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mapMiniaturesJList.setCellRenderer(new MapMiniatureCellRenderer());
        add(mapMiniaturesJList, BorderLayout.CENTER);

        JButton btnValid = new JButton("Valider");
        btnValid.addActionListener(this);
        add(btnValid, BorderLayout.SOUTH);
    }

    public MapMiniature getSelectedItem() {
        return (MapMiniature) mapMiniaturesJList.getSelectedValue();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        warLauncherInterface.getGameSettings().setSelectedMap(getSelectedItem().getMap());
        warLauncherInterface.notifySelectedMapChanged();
        this.dispose();
    }
}
