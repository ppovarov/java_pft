package ru.stqa.pft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PointTests {

    @Test
    public void testDistance1(){
        Point p1 = new Point(1,1.5);
        Point p2 = new Point(2,1.5);
        Assert.assertEquals(p1.distance(p2),1.0);
    }

    @Test
    public void testDistance2(){
        Point p1 = new Point(1,1);
        Point p2 = new Point(2,2);
        Assert.assertEquals(p1.distance(p2),1.4142135623730951);
    }

    @Test
    public void testDistance3(){
        Point p1 = new Point(3,1);
        Point p2 = new Point(3,-1);
        Assert.assertEquals(p1.distance(p2),2.0);
    }

    @Test
    public void testDistance4(){
        Point p1 = new Point(1,-2);
        Point p2 = new Point(-2,1);
        Assert.assertEquals(p1.distance(p2),4.242640687119285);
    }
}
