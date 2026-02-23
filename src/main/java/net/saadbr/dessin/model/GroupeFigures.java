package net.saadbr.dessin.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author saade
 **/
public class GroupeFigures extends Figure{
    private String nom;
    private List<Figure> figures = new ArrayList<Figure>();

    public GroupeFigures(Parametrage parametrage, String nom) {
        super(parametrage);
        this.nom = nom;
    }
    public void ajouterFigure(Figure figure) {
        figures.add(figure);
    }

    public void supprimerFigure(Figure figure) {
        figures.remove(figure);
    }

    public List<Figure> getFigures() {
        return figures;
    }

    @Override
    public double calculerSurface() {
        double total = 0;
        for (Figure f : figures) {
            total += f.calculerSurface();
        }
        return total;
    }

    @Override
    public double calculerPerimetre() {
        double total = 0;
        for (Figure f : figures) {
            total += f.calculerPerimetre();
        }
        return total;
    }

    @Override
    public void dessiner() {
        System.out.println("GroupeFigures{" +
                "nom='" + nom + '\'' +
                ", epaisseurContour=" + epaisseurContour +
                ", couleurContour=" + couleurContour +
                ", couleurRemplissage=" + couleurRemplissage +
                ", nbFigures=" + figures.size() +
                '}');
        for (Figure f : figures) {
            f.dessiner();
        }
    }

    @Override
    public void update(Parametrage parametrage) {
        super.update(parametrage);
        for (Figure f : figures) {
            f.update(parametrage);
        }
    }
}
