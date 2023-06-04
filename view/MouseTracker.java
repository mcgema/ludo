package view;

import java.awt.event.MouseEvent;

public class MouseTracker implements java.awt.event.MouseListener {


    public void mousePressed(MouseEvent m) {
        System.out.printf("Mouse Pressed: %d,\t%d\n",m.getX(), m.getY());

    }

    public void mouseClicked(MouseEvent m) {
        System.out.printf("Mouse Clicked: %d,\t%d\n",m.getX(), m.getY());
    }

    public void mouseEntered(MouseEvent m) {
        System.out.printf("Mouse Entered: %d,\t%d\n",m.getX(), m.getY());

    }

    public void mouseReleased(MouseEvent m) {
        System.out.printf("Mouse Released: %d,\t%d\n",m.getX(), m.getY());

    }

    public void mouseExited(MouseEvent m) {
        System.out.printf("Mouse Exited: %d,\t%d\n",m.getX(), m.getY());

    }
}
