package net.saadbr.dessin.model;

import java.io.Serializable;

/**
 * @author saade
 **/
public abstract class Figure implements Serializable, FigureObserver {
    protected int epaisseurContour;
    protected int couleurContour;
    protected int couleurRemplissage;

    protected Figure(Parametrage parametrage) {
        this.epaisseurContour = parametrage.getEpaisseurContour();
        this.couleurContour = parametrage.getCouleurContour();
        this.couleurRemplissage = parametrage.getCouleurRemplissage();
        parametrage.ajouterObserver(this);
    }

    public abstract double calculerSurface();

    public abstract double calculerPerimetre();

    public abstract void dessiner();

    @Override
    public void update(Parametrage parametrage) {
        this.epaisseurContour = parametrage.getEpaisseurContour();
        this.couleurContour = parametrage.getCouleurContour();
        this.couleurRemplissage = parametrage.getCouleurRemplissage();
    }

    public int getEpaisseurContour() {
        return epaisseurContour;
    }

    public int getCouleurContour() {
        return couleurContour;
    }

    public int getCouleurRemplissage() {
        return couleurRemplissage;
    }
}
