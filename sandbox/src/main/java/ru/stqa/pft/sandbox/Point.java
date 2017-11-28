package ru.stqa.pft.sandbox;

// 1. Создать класс Point для представления точек на двумерной плоскости. Объекты этого класса должны содержать два атрибута,
// которые соответствуют координатам точки на плоскости.
public class Point {
    public double x;
    public double y;

    Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    // 4. Реализовать то же самое (вычисление расстояния между двумя точками) при помощи метода в классе Point,
    public double distance(Point p){
        double dx = this.x - p.x;
        double dy = this.y - p.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public String printXY(){
        return "(" + this.x + ", " + this.y + ")";
    }

}
