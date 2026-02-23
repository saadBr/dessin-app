package net.saadbr.dessin.strategy;

import net.saadbr.dessin.model.Figure;

import java.util.List;

/**
 * @author saade
 **/
public class CalculSurfaceTotaleStrategy implements TraitementFiguresStrategy {

    @Override
    public void traiter(List<Figure> figures) {
        double total = 0;
        for (Figure f : figures) {
            total += f.calculerSurface();
        }
        System.out.println("Surface totale du dessin = " + total);
    }
}
