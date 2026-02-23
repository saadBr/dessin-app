package net.saadbr.dessin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author saade
 **/
public class Parametrage implements Serializable {
    private int epaisseurContour;
    private int couleurContour;
    private int couleurRemplissage;
    private List<FigureObserver> observers = new ArrayList<FigureObserver>();

    public Parametrage(int epaisseurContour, int couleurContour, int couleurRemplissage) {
        this.epaisseurContour = epaisseurContour;
        this.couleurContour = couleurContour;
        this.couleurRemplissage = couleurRemplissage;
    }

    public int getEpaisseurContour() {
        return epaisseurContour;
    }

    public void setEpaisseurContour(int epaisseurContour) {
        this.epaisseurContour = epaisseurContour;
        notifierObservers();
    }

    public int getCouleurContour() {
        return couleurContour;
    }

    public void setCouleurContour(int couleurContour) {
        this.couleurContour = couleurContour;
        notifierObservers();
    }

    public int getCouleurRemplissage() {
        return couleurRemplissage;
    }

    public void setCouleurRemplissage(int couleurRemplissage) {
        this.couleurRemplissage = couleurRemplissage;
        notifierObservers();
    }

    public void ajouterObserver(FigureObserver observer) {
        observers.add(observer);
    }

    public void supprimerObserver(FigureObserver observer) {
        observers.remove(observer);
    }

    private void notifierObservers() {
        for (FigureObserver observer : observers) {
            observer.update(this);
        }
    }

    @Override
    public String toString() {
        return "Parametrage{" +
                "epaisseurContour=" + epaisseurContour +
                ", couleurContour=" + couleurContour +
                ", couleurRemplissage=" + couleurRemplissage +
                '}';
    }
}
