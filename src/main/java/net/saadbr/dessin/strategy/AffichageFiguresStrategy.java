package net.saadbr.dessin.strategy;

import net.saadbr.dessin.model.Figure;

import java.util.List;

/**
 * @author saade
 **/
public class AffichageFiguresStrategy implements TraitementFiguresStrategy{

    @Override
    public void traiter(List<Figure> figures) {
        System.out.println("Affichage de toutes les figures :");
        for (Figure f : figures) {
            f.dessiner();
        }
    }
}
