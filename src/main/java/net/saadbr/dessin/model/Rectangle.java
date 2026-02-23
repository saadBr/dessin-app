package net.saadbr.dessin.model;

/**
 * @author saade
 **/
public class Rectangle extends Figure {
    private Point coinSuperieurGauche;
    private double largeur;
    private double hauteur;

    public Rectangle(Point coinSuperieurGauche, double largeur, double hauteur, Parametrage parametrage) {
        super(parametrage);
        this.coinSuperieurGauche = coinSuperieurGauche;
        this.largeur = largeur;
        this.hauteur = hauteur;
    }
    @Override
    public double calculerSurface() {
        return largeur * hauteur;
    }

    @Override
    public double calculerPerimetre() {
        return 2 * (largeur + hauteur);
    }

    @Override
    public void dessiner() {
        System.out.println("Rectangle{" +
                "coinSuperieurGauche=" + coinSuperieurGauche +
                ", largeur=" + largeur +
                ", hauteur=" + hauteur +
                ", epaisseurContour=" + epaisseurContour +
                ", couleurContour=" + couleurContour +
                ", couleurRemplissage=" + couleurRemplissage +
                '}');
    }

    public Point getCoinSuperieurGauche() {
        return coinSuperieurGauche;
    }

    public double getLargeur() {
        return largeur;
    }

    public double getHauteur() {
        return hauteur;
    }
}
