package points;

public class Point3D extends Point2D {
    private double z;

    public Point3D() {
        super();
        this.z = 0;
    }

    public Point3D(double x, double y, double z) {
        super(x, y);
        this.z = z;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double distance(Point3D point) {
        return Math.sqrt(Math.pow(this.getX() - point.getX(), 2) + Math.pow(this.getY() - point.getY(), 2) + Math.pow(this.z - point.getZ(), 2));
    }

    @Override
    public String toString() {
        return "(" + this.getX() + ", " + this.getY() + ", " + this.z + ")";
    }

    public Point3D move(double dx, double dy, double dz) {
        super.move(dx, dy);
        this.z += dz;
        return this;
    }

    public static double distance3D(Point3D p1, Point3D p2) {
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2) + Math.pow(p2.getZ() - p1.getZ(), 2));
    }

    public double distance3D(Point3D p) {
        return Point3D.distance(this, p);
    }

    protected boolean equals(Point3D p) {
        return Math.abs(this.x - p.x) < getEpsilon() && Math.abs(this.y - p.y) < getEpsilon() && Math.abs(this.z - p.z) < getEpsilon();
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }

        if(obj == this){
            return true;
        }

        if(obj instanceof Point3D){
            Point3D p = (Point3D) obj;
            return equals(p);
        }
}
