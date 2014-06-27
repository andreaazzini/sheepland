package it.polimi.deib.provaFinale2014.gui;

import java.awt.Image;
import java.awt.image.ImageObserver;

/**
 * Classe che si occupa della gestione del dragging di
 * un'immagine
 */
public class DraggableImageComponent extends DraggableComponent implements ImageObserver {
	private static final long serialVersionUID = 1L;
	
	protected Image image;

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        if (infoflags == ALLBITS) {
            repaint();
            return false;
        }
        return true;
    }
}
