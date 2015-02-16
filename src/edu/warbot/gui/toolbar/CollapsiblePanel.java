package edu.warbot.gui.toolbar;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CollapsiblePanel extends JPanel {

    private List<CollapsiblePanelListener> listeners;
    String title;
    TitledBorder border;

    public CollapsiblePanel(String title) {
        this.title = title;
        listeners = new ArrayList<>();
        border = BorderFactory.createTitledBorder(title);
        setBorder(border);
        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);
        addMouseListener(mouseListener);
    }

    MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            toggleVisibility();
        }
    };

    ComponentListener contentComponentListener = new ComponentAdapter() {
        @Override
        public void componentShown(ComponentEvent e) {
            updateBorderTitle();
            for(CollapsiblePanelListener listener : listeners)
                listener.onUnfold(CollapsiblePanel.this);
        }
        @Override
        public void componentHidden(ComponentEvent e) {
            updateBorderTitle();
            for(CollapsiblePanelListener listener : listeners)
                listener.onCollapse(CollapsiblePanel.this);
        }
    };

    @Override
    public Component add(Component comp) {
        comp.addComponentListener(contentComponentListener);
        Component r = super.add(comp);
        updateBorderTitle();
        return r;
    }

    @Override
    public Component add(String name, Component comp) {
        comp.addComponentListener(contentComponentListener);
        Component r = super.add(name, comp);
        updateBorderTitle();
        return r;
    }

    @Override
    public Component add(Component comp, int index) {
        comp.addComponentListener(contentComponentListener);
        Component r = super.add(comp, index);
        updateBorderTitle();
        return r;
    }

    @Override
    public void add(Component comp, Object constraints) {
        comp.addComponentListener(contentComponentListener);
        super.add(comp, constraints);
        updateBorderTitle();
    }

    @Override
    public void add(Component comp, Object constraints, int index) {
        comp.addComponentListener(contentComponentListener);
        super.add(comp, constraints, index);
        updateBorderTitle();
    }

    @Override
    public void remove(int index) {
        Component comp = getComponent(index);
        comp.removeComponentListener(contentComponentListener);
        super.remove(index);
    }

    @Override
    public void remove(Component comp) {
        comp.removeComponentListener(contentComponentListener);
        super.remove(comp);
    }

    @Override
    public void removeAll() {
        for (Component c : getComponents()) {
            c.removeComponentListener(contentComponentListener);
        }
        super.removeAll();
    }

    protected void toggleVisibility() {
        toggleVisibility(hasInvisibleComponent());
    }

    protected void toggleVisibility(boolean visible) {
        for (Component c : getComponents()) {
            c.setVisible(visible);
        }
        updateBorderTitle();
    }

    protected void updateBorderTitle() {
        String arrow = "";
        if (getComponentCount() > 0) {
            arrow = (hasInvisibleComponent()?"▽":"△");
        }
        border.setTitle(title +" "+ arrow);
        repaint();
    }

    protected final boolean hasInvisibleComponent() {
        for (Component c : getComponents()) {
            if (!c.isVisible()) {
                return true;
            }
        }
        return false;
    }

    public void addCollapsiblePanelListener(CollapsiblePanelListener listener) {
        listeners.add(listener);
    }

    public void removeCollapsiblePanelListener(CollapsiblePanelListener listener) {
        listeners.remove(listener);
    }

    public interface CollapsiblePanelListener {
        public void onCollapse(CollapsiblePanel collapsiblePanel);
        public void onUnfold(CollapsiblePanel collapsiblePanel);
    }

}