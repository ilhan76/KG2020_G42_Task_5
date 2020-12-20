/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg2019examples_task5animation.model;

import kg2019examples_task5animation.math.Vector2;

/**
 *
 * Класс, описывающий источник внешнего воздействия.
 * @author Alexey
 */
public class ForceSource {
    private Vector2 location;
    private double value;
    
    public ForceSource(Vector2 location) {
        this.location = location;
        value = 0;
    }

    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    /**
     * Вычисляет вектор силы в заданной точке
     * @param p Точка, для которой производят вычисления
     * @return Вектор силы
     */
    public Vector2 getForceAt(Vector2 p) {
        if (Math.abs(value) < 1e-12)
            return new Vector2(0, 0);
        return location.add(p.mul(-1)).normolized().mul(value);
    }
}
