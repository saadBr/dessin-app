package net.saadbr.dessin.model;

import net.saadbr.dessin.security.Role;
import net.saadbr.dessin.security.Secured;
import net.saadbr.dessin.strategy.TraitementFiguresStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author saade
 **/
public class Dessin implements Serializable {
    private String nom;
    private Parametrage parametrage;
    private List<Figure> figures = new ArrayList<Figure>();
    private transient TraitementFiguresStrategy strategy;
    public Dessin(String nom, Parametrage parametrage) {
        this.nom = nom;
        this.parametrage = parametrage;
    }
    @Secured({Role.USER, Role.ADMIN})
    public void add(Figure figure) {
        if (figure == null) return;
        figures.add(figure);
    }
    @Secured({Role.USER, Role.ADMIN})
    public void del(Figure figure) {
        if (figure == null) return;
        figures.remove(figure);
    }
    public List<Figure> getFigures() {
        return Collections.unmodifiableList(figures);
    }
    @Secured({Role.USER, Role.ADMIN})
    public void afficherFigures() {
        System.out.println("=== Dessin : " + nom + " ===");
        for (Figure f : figures) {
            f.dessiner();
        }
    }
    public void setTraitementFiguresStrategy(TraitementFiguresStrategy traitementFiguresStrategy) {
        this.strategy = traitementFiguresStrategy;
    }
    @Secured({Role.USER, Role.ADMIN})
    public void traiter() {
        if (strategy == null) {
            System.out.println("Aucune stratégie de traitement n'est définie pour le dessin " + nom);
            return;
        }
        strategy.traiter(figures);
    }
    @Secured(Role.ADMIN)
    public void serialiserDansFichier(String fileName) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
            System.out.println("Dessin sérialisé dans le fichier : " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Dessin deserialiserDepuisFichier(String fileName) {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(fileName))) {
            Object obj = ois.readObject();
            if (obj instanceof Dessin dessin) {
                System.out.println("Dessin désérialisé depuis : " + fileName);
                return dessin;
            } else {
                throw new IOException("Fichier invalide : " + fileName);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getNom() {
        return nom;
    }

    public Parametrage getParametrage() {
        return parametrage;
    }

    @Override
    public String toString() {
        return "Dessin{" +
                "nom='" + nom + '\'' +
                ", nbFigures=" + figures.size() +
                ", parametrage=" + parametrage +
                '}';
    }
}
