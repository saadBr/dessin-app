package net.saadbr.dessin.model;

/**
 * @author saade
 **/
public class Cercle extends Figure{
    private Point centre;
    private double radius;
    public Cercle(Point centre, double radius, Parametrage parametrage) {
        super(parametrage);
        this.centre = centre;
        this.radius = radius;
    }
    @Override
    public double calculerSurface() {
        return Math.PI * radius * radius;
    }

    @Override
    public double calculerPerimetre() {
        return Math.PI * radius * 2;
    }

    @Override
    public void dessiner() {
        System.out.println("Cercle{" +
                "centre=" + centre +
                ", radius=" + radius +
                ", epaisseurContour=" + epaisseurContour +
                ", couleurContour=" + couleurContour +
                ", couleurRemplissage=" + couleurRemplissage +
                '}');

    }

    public Point getCentre() {
        return centre;
    }

    public double getRadius() {
        return radius;
    }
}
