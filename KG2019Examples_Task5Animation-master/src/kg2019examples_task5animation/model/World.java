/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg2019examples_task5animation.model;

import java.awt.Color;
import java.awt.Graphics2D;
import kg2019examples_task5animation.math.Vector2;
import kg2019examples_task5animation.utils2d.ScreenConverter;
import kg2019examples_task5animation.utils2d.ScreenPoint;

import javax.print.DocFlavor;

/**
 *
 * Класс, описывающий весь мир, в целом.
 * @author Alexey
 */
public class World {
    private Puck p;
    private Field f;
    private ForceSource externalForce;

    public World(Puck p, Field f) {
        this.p = p;
        this.f = f;
        this.externalForce = new ForceSource(f.getRectangle().getCenter());
    }

    /**
     * Метод обновления состояния мира за указанное время
     * @param dt Промежуток времени, за который требуется обновить мир.
     */
    public void update(double dt) {
        Vector2 np = p.getPosition();
                /*.add(p.getVelocity().mul(dt))
                .add(p.getAcceleration().mul(-f.getG() * dt*dt*0.5));*/
        if (p.getTypeOfMove() == Puck.TypeOfMove.movement){
            double dx = p.getVelocity().getX() * dt;
            double dy = p.getVelocity().getY() * dt - f.getG() * dt * dt * 0.5;
            Borders border = behindTheBorder(np, dx, dy);

            if (border == Borders.INSIGHT){
                np.setX(np.getX() + dx);
                np.setY(np.getY() + dy);
            } else {
                shiftToBorder(np, dx, dy, border);
                p.setTypeOfMove(Puck.TypeOfMove.compression);
                p.setBorder(border);
            }
            p.getVelocity().setY(p.getVelocity().getY() - f.getG() * dt);
            Vector2 nv = p.getVelocity()
                    .add(p.getAcceleration().mul(dt));
            Vector2 newV = changeVelocity(nv, border);
            if (p.getTypeOfMove() == Puck.TypeOfMove.compression){
                double dCompr;
                if (border == Borders.DOWN || border == Borders.UP){
                    dCompr = Math.sqrt(p.getM() / p.getK()) * Math.abs(nv.getY());
                    p.setMinYR(p.getYr() - dCompr);
                    p.setMinXR(p.getXr());
                    //System.out.println(dCompr);
                } else if (border == Borders.LEFT || border == Borders.RIGHT){
                    dCompr = Math.sqrt(p.getM() / p.getK()) * Math.abs(nv.getX());
                    p.setMinXR(p.getXr() - dCompr);
                    p.setMinYR(p.getYr());
                    //System.out.println(dCompr);
                }
            }
            Vector2 Fvn = externalForce.getForceAt(np);
            Vector2 Ftr = p.getVelocity().normolized().mul(-f.getMu()*p.getM()*f.getG());
            Vector2 F = Ftr.add(Fvn);

            p.setAcceleration(F.mul(1/p.getM()));
            p.setVelocity(newV);
            p.setPosition(np);
        }
        else if (p.getTypeOfMove() == Puck.TypeOfMove.compression){
            double d;
            Vector2 newP;
            /*System.out.println(p.getBorder());
            System.out.println(p.getMinXR() + " " + p.getXr());
            System.out.println(p.getMinYR() + " " + p.getYr());
            System.out.println(p.getPosition().getX() + " " + p.getPosition().getY());*/
            if (p.getBorder() == Borders.UP || p.getBorder() == Borders.DOWN){
                if (p.getMinYR() < p.getYr()){
                    d = dt * Math.abs(p.getVelocity().getY() / Math.PI);
                    System.out.println(d);
                    if (p.getBorder() == Borders.UP){
                        newP = new Vector2(p.getPosition().getX(), p.getPosition().getY() + d);
                    }else {
                        newP = new Vector2(p.getPosition().getX(), p.getPosition().getY() - d );
                    }
                    p.setPosition(newP);
                    p.setXr(p.getXr() + d);
                    p.setYr(p.getYr() - d);
                } else p.setTypeOfMove(Puck.TypeOfMove.extension);
            }
            else {
                d = dt * Math.abs(p.getVelocity().getX() / Math.PI);
                if (p.getMinXR() < p.getXr()){
                    if (p.getBorder() == Borders.LEFT){
                        newP = new Vector2(p.getPosition().getX() - d, p.getPosition().getY());
                    } else {
                        newP = new Vector2(p.getPosition().getX() + d, p.getPosition().getY());
                    }
                    p.setPosition(newP);
                    p.setXr(p.getXr() - d);
                    p.setYr(p.getYr() + d);
                } else p.setTypeOfMove(Puck.TypeOfMove.extension);
            }
            /*switch (p.getBorder()){
                case UP ->{
                    if (p.getMinYR() < p.getYr()){
                        d = dt * (p.getVelocity().getY() / Math.PI);
                        newP = new Vector2(p.getPosition().getX(), p.getPosition().getY() + d / 2);
                        p.setPosition(newP);
                        p.setXr(p.getXr() + d);
                        p.setYr(p.getYr() - d);
                    }
                }
                case DOWN -> {
                    if (p.getMinYR() < p.getYr()){
                        d = dt * (p.getVelocity().getY() / Math.PI);
                        newP = new Vector2(p.getPosition().getX(), p.getPosition().getY() - d / 2);
                        p.setPosition(newP);
                        p.setXr(p.getXr() + d /2);
                        p.setYr(p.getYr() - d / 2);
                    }
                }
                case RIGHT -> {
                    if (p.getMinXR() < p.getXr()){
                        d = dt * (p.getVelocity().getX() / Math.PI);
                        newP = new Vector2(p.getPosition().getX() - d / 2, p.getPosition().getY());
                        p.setPosition(newP);
                        p.setXr(p.getXr() + d);
                        p.setYr(p.getYr() - d);
                    }
                }
                case LEFT -> {
                    if (p.getMinXR() < p.getXr()){
                        d = dt * (p.getVelocity().getX() / Math.PI);
                        newP = new Vector2(p.getPosition().getX() + d / 2, p.getPosition().getY());
                        p.setPosition(newP);
                        p.setXr(p.getXr() + d);
                        p.setYr(p.getYr() - d);
                    }
                }
            }*/
            System.out.println(p.getPosition().getX() + " " + p.getPosition().getY());
        }
        else if (p.getTypeOfMove() == Puck.TypeOfMove.extension){
            double d;
            Vector2 newP;
            if (p.getBorder() == Borders.UP || p.getBorder() == Borders.DOWN){
                if (p.getYr() < p.getNormalRadius()){
                    d = dt * Math.abs(p.getVelocity().getY() / Math.PI);
                    System.out.println(d);
                    if (p.getBorder() == Borders.UP){
                        newP = new Vector2(p.getPosition().getX(), p.getPosition().getY() - d);
                    }else {
                        newP = new Vector2(p.getPosition().getX(), p.getPosition().getY() + d );
                    }
                    p.setPosition(newP);
                    p.setXr(p.getXr() - d);
                    p.setYr(p.getYr() + d);
                } else p.setTypeOfMove(Puck.TypeOfMove.movement);
            }
            else {
                d = dt * Math.abs(p.getVelocity().getX() / Math.PI);
                if (p.getXr() < p.getNormalRadius()){
                    if (p.getBorder() == Borders.LEFT){
                        newP = new Vector2(p.getPosition().getX() + d, p.getPosition().getY());
                    } else {
                        newP = new Vector2(p.getPosition().getX() - d, p.getPosition().getY());
                    }
                    p.setPosition(newP);
                    p.setXr(p.getXr() + d);
                    p.setYr(p.getYr() - d);
                } else p.setTypeOfMove(Puck.TypeOfMove.movement);
            }
            /*switch (p.getBorder()){
                case UP ->{
                    if (p.getMinYR() < p.getYr()){
                        d = dt * (p.getVelocity().getY() / Math.PI);
                        newP = new Vector2(p.getPosition().getX(), p.getPosition().getY() + d / 2);
                        p.setPosition(newP);
                        p.setXr(p.getXr() + d);
                        p.setYr(p.getYr() - d);
                    }
                }
                case DOWN -> {
                    if (p.getMinYR() < p.getYr()){
                        d = dt * (p.getVelocity().getY() / Math.PI);
                        newP = new Vector2(p.getPosition().getX(), p.getPosition().getY() - d / 2);
                        p.setPosition(newP);
                        p.setXr(p.getXr() + d /2);
                        p.setYr(p.getYr() - d / 2);
                    }
                }
                case RIGHT -> {
                    if (p.getMinXR() < p.getXr()){
                        d = dt * (p.getVelocity().getX() / Math.PI);
                        newP = new Vector2(p.getPosition().getX() - d / 2, p.getPosition().getY());
                        p.setPosition(newP);
                        p.setXr(p.getXr() + d);
                        p.setYr(p.getYr() - d);
                    }
                }
                case LEFT -> {
                    if (p.getMinXR() < p.getXr()){
                        d = dt * (p.getVelocity().getX() / Math.PI);
                        newP = new Vector2(p.getPosition().getX() + d / 2, p.getPosition().getY());
                        p.setPosition(newP);
                        p.setXr(p.getXr() + d);
                        p.setYr(p.getYr() - d);
                    }
                }
            }*/
            //System.out.println(p.getPosition().getX() + " " + p.getPosition().getY());
        }

        /*np.setX(np.getX() + dx);
        np.setY(np.getY() + dy);

        p.getVelocity().setY(p.getVelocity().getY() - f.getG() * dt);
        Vector2 nv = p.getVelocity()
                .add(p.getAcceleration().mul(dt));

        double vx = nv.getX(), vy = nv.getY();
        boolean reset = false;
        if (np.getX() - p.getXr() < f.getRectangle().getLeft() || np.getX() + p.getXr() > f.getRectangle().getRight()) {
            vx = -vx;
            reset = true;
        }
        if (np.getY() - p.getYr() < f.getRectangle().getBottom() || np.getY() + p.getYr() > f.getRectangle().getTop()) {
            vy = -vy;
            reset = true;
        }
        nv = new Vector2(vx, vy);
        if (nv.length() < 1e-10)
            nv = new Vector2(0, 0);
        if (reset)
            np = p.getPosition();*/

        /*Vector2 Fvn = externalForce.getForceAt(np);
        Vector2 Ftr = p.getVelocity().normolized().mul(-f.getMu()*p.getM()*f.getG());
        Vector2 F = Ftr.add(Fvn);

        p.setAcceleration(F.mul(1/p.getM()));
        //p.setVelocity(nv);
        p.setPosition(np);*/
    }

    private Borders behindTheBorder(Vector2 np, double dx, double dy){
        Borders b;
        if (np.getX() + dx - p.getXr() < f.getRectangle().getLeft()){
            //System.out.println("Left");
            b = Borders.LEFT;
        }
        else if (np.getX() + dx + p.getXr() > f.getRectangle().getRight()){
            //System.out.println("Right");
            b = Borders.RIGHT;
        }
        else if (np.getY() + dy - p.getYr() < f.getRectangle().getBottom()){
            //System.out.println("Down");
            b = Borders.DOWN;
        }
        else if (np.getY() + dy + p.getYr() > f.getRectangle().getTop()){
            //System.out.println("Up");
            b = Borders.UP;
        } else {
            //System.out.println("Insight");
            b = Borders.INSIGHT;
        }
        return b;
    }
    private void shiftToBorder(Vector2 np, double dx, double dy, Borders border){
        /*System.out.println(np.getX() + " " + np.getY());
        System.out.println("right - " + f.getRectangle().getRight());
        System.out.println("dy - " + dy);*/
        double newX = np.getX() + dx;
        double newY = np.getY() + dy;
        switch (border){
            case LEFT ->
                    newX = f.getRectangle().getLeft() + p.getXr();
            case RIGHT ->
                    newX = f.getRectangle().getRight() - p.getXr();
            case DOWN ->
                    newY = f.getRectangle().getBottom() + p.getYr();
            case UP ->
                    newY = f.getRectangle().getTop() - p.getYr();
        }
        np.setY(newY);
        np.setX(newX);
        //System.out.println(np.getX() + " " + np.getY());

    }
    private Vector2 changeVelocity(Vector2 nv, Borders border){
        Vector2 changedV = new Vector2(nv.getX(), nv.getY());
        if (border == Borders.UP || border == Borders.DOWN){
            //System.out.println("UP <-> DOWN");
            changedV.setY(-nv.getY());
        }
        if (border == Borders.LEFT || border == Borders.RIGHT){
            //System.out.println("LEFT <-> RIGHT");
            changedV.setX(-nv.getX());
        }
        if (changedV.length() < 1e-5)
            changedV = new Vector2(0, 0);
        return changedV;
    }

    /**
     * Метод рисует ткущее состояние мира.
     * На самом деле всю логику рисования стоит вынести из этого класса
     * куда-нибудь в WorldDrawer, унаследованный от IDrawer
     * @param g Графикс, на котором надо нарисовать текущее состояние.
     * @param sc Актуальный конвертер координат.
     */
    public void draw(Graphics2D g, ScreenConverter sc) {
        ScreenPoint tl = sc.r2s(f.getRectangle().getTopLeft());
        int w = sc.r2sDistanceH(f.getRectangle().getWidth());
        int h = sc.r2sDistanceV(f.getRectangle().getHeight());
        g.setColor(Color.WHITE);
        g.fillRect(tl.getI(), tl.getJ(), w, h);
        g.setColor(Color.RED);
        g.drawRect(tl.getI(), tl.getJ(), w, h);
        ScreenPoint pc = sc.r2s(p.getPosition());
        int rh = sc.r2sDistanceH(p.getXr());
        int rv = sc.r2sDistanceV(p.getYr());
        g.setColor(Color.RED);
        g.fillOval(pc.getI() - rh, pc.getJ() - rv, rh + rh, rv + rv);

        g.drawString(String.format("Mu=%.2f", f.getMu()), 10, 30);
        g.drawString(String.format("F=%.0f", externalForce.getValue()), 10, 50);
    }


    public Field getF() {
        return f;
    }

    public void setF(Field f) {
        this.f = f;
    }

    public Puck getP() {
        return p;
    }

    public void setP(Puck p) {
        this.p = p;
    }
    
    public ForceSource getExternalForce() {
        return externalForce;
    }
}
