package net.saadbr.dessin;

import net.saadbr.dessin.model.*;
import net.saadbr.dessin.security.Role;
import net.saadbr.dessin.security.SecurityContext;
import net.saadbr.dessin.security.User;
import net.saadbr.dessin.strategy.AffichageFiguresStrategy;
import net.saadbr.dessin.strategy.CalculPerimetreTotaleStrategy;
import net.saadbr.dessin.strategy.CalculSurfaceTotaleStrategy;

import java.util.Set;

/**
 * @author saade
 **/
public class DemoApp {
    public static void main(String[] args) {
        User admin = new User("saad", "1234", Set.of(Role.ADMIN, Role.USER));
        SecurityContext.setCurrentUser(admin);

        Parametrage parametrage = new Parametrage(1, 0x000000, 0xFFFFFF);
        Dessin dessin = new Dessin("Mon dessin sécurisé", parametrage);

        Figure c1 = new Cercle(new Point(0, 0), 5, parametrage);
        Figure r1 = new Rectangle(new Point(1, 1), 4, 3, parametrage);

        dessin.add(c1);
        dessin.add(r1);

        dessin.setTraitementFiguresStrategy(new AffichageFiguresStrategy());
        dessin.traiter();

        dessin.setTraitementFiguresStrategy(new CalculSurfaceTotaleStrategy());
        dessin.traiter();

        dessin.setTraitementFiguresStrategy(new CalculPerimetreTotaleStrategy());
        dessin.traiter();

        dessin.serialiserDansFichier("dessin.bin");

        SecurityContext.clear();
    }

}
