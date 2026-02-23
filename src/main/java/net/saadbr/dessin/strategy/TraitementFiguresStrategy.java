package net.saadbr.dessin.strategy;

import net.saadbr.dessin.model.Figure;

import java.util.List;

/**
 * @author saade
 **/
public interface TraitementFiguresStrategy {
    void traiter(List<Figure> figures);
}
