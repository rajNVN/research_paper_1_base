package grouping;

public class BoundingRect {
    private double x1, y1;   // lower left
    private double x2=0, y2=0;   // upper right

    public BoundingRect(double x1, double y1) {
        this.x1=x1;
        this.y1=y1;
    }

    public void intialize(double x2,double y2)
    {
        if(x2>this.x2)
            this.x2=x2;
        if(y2<this.y2)
            this.y2=y2;
    }

    // does this BoundingBox r intersect s?
    public boolean intersects(BoundingRect s) {
        BoundingRect r = this;
        return (r.x2 >= s.x1 && r.y2 >= s.y1 && s.x2 >= r.x1 && s.y2 >= r.y1);
    }


    // return the area
    public double area() {
        return (x2 - x1) * (y2 - y1);
    }

    public String DeltaMax()
    {
        double z1=x2-x1;
        double z2=y2-y1;
        String src=new Double(z1).toString();
        src=src+","+new Double(z2).toString();
        return src;
    }

    // return the area
    public String toString() {
        return "[(" + x1 + ", " + y1 + ") (" + x2 + ", " + y2 + ")]";
    }


    public double getX1()
    {
        return x1;
    }

    public double getY1()
    {
        return y1;
    }

}

