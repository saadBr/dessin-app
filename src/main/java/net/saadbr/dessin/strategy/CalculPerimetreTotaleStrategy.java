package net.saadbr.dessin.strategy;

import net.saadbr.dessin.model.Figure;

import java.util.List;

/**
 * @author saade
 **/
public class CalculPerimetreTotaleStrategy implements TraitementFiguresStrategy{
    @Override
    public void traiter(List<Figure> figures) {
        double total = 0;
        for (Figure f : figures) {
            total += f.calculerPerimetre();
        }
        System.out.println("Périmètre total du dessin = " + total);
    }
}
