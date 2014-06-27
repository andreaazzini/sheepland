package it.polimi.deib.provaFinale2014.gui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

/**
 * Component draggabile
 */
public class DraggableComponent extends JComponent {
	private static final long serialVersionUID = 1L;

    protected Point anchorPoint;
    protected int x, y;
    private Point position;

    private void addDragListeners() {
        final DraggableComponent handle = this;
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
            	anchorPoint = e.getPoint();
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int anchorX = anchorPoint.x;
                int anchorY = anchorPoint.y;
                
                Point parentOnScreen = getParent().getLocationOnScreen();
                Point mouseOnScreen = e.getLocationOnScreen();
                position = new Point(mouseOnScreen.x - parentOnScreen.x -
                		anchorX, mouseOnScreen.y - parentOnScreen.y - anchorY);
                setLocation(position);
                
                if (handle instanceof ShepherdGUI) {
                	((ShepherdGUI)handle).setX(position.x);
                	((ShepherdGUI)handle).setY(position.y);
                	((ShepherdGUI)handle).getParent().repaint();
                }
            }
        });
    }
    
    public Point getPosition() {
    	return position;
    }

    private void removeDragListeners() {
        for (MouseMotionListener listener : this.getMouseMotionListeners()) {
            removeMouseMotionListener(listener);
        }
        setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Imposta il componente come draggable o non draggable
     * @param draggable true se draggable, false altrimenti
     */
    public void setDraggable(boolean draggable) {
        if (draggable) {
            addDragListeners();
        } else {
            removeDragListeners();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}