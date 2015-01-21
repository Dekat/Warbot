package edu.warbot.gui.toolbar;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class DefaultCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
 
        setHorizontalAlignment(JLabel.CENTER);
    	setBackground(Color.WHITE);
        setIcon(null);
        if (value instanceof Color) {
        	Color color = (Color) value;
        	setText("");
        	setBackground(color);
        } else if (value instanceof ImageIcon) {
	        setText("");
	        setIcon((ImageIcon) value);
        } else {
        	setText(value.toString());
        }
        return this;
    }
}