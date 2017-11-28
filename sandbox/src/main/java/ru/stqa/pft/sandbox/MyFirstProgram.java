package ru.stqa.pft.sandbox;

public class MyFirstProgram {

    // 3.1 Сделать запускаемый класс, то есть содержащий функцию public static void main(String[] args) {...}
	public static void main(String[] args) {
	    Point p1 = new Point(1,1.5);
        Point p2 = new Point(-1,-4);

        //3.2 убедиться, что функция вычисления расстояния между точками действительно работает. Результат вычисления выводить на экран и контролировать визуально.
		System.out.println("Distance between P1(" + p1.x + ", " + p1.y + ") and P2(" + p2.x + ", " + p2.y + ") = " + distance(p1, p2));

        // 4.2 добавить в созданный в предыдущем пункте запускаемый класс примеры использования метода вместо ранее созданной функции.
        System.out.println("Distance between P1" + p1.printXY()+ " and P2" + p2.printXY() + " = " + p1.distance(p2));
	}

	// 2. Создать функцию public static double distance(Point p1, Point p2)
    // которая вычисляет расстояние между двумя точками. Для вычисления квадратного корня можно использовать функцию Math.sqrt
    public static double distance(Point p1, Point p2){
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
	    return Math.sqrt(dx * dx + dy * dy);
    }
}

