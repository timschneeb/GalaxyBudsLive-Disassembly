package androidx.constraintlayout.solver.widgets;

public class Rectangle {
    public int height;
    public int width;
    public int x;
    public int y;

    public void setBounds(int i, int i2, int i3, int i4) {
        this.x = i;
        this.y = i2;
        this.width = i3;
        this.height = i4;
    }

    /* access modifiers changed from: package-private */
    public void grow(int i, int i2) {
        this.x -= i;
        this.y -= i2;
        this.width += i * 2;
        this.height += i2 * 2;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000b, code lost:
        r0 = r3.y;
        r1 = r4.y;
     */
    public boolean intersects(Rectangle rectangle) {
        int i;
        int i2;
        int i3 = this.x;
        int i4 = rectangle.x;
        return i3 >= i4 && i3 < i4 + rectangle.width && i >= i2 && i < i2 + rectangle.height;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0009, code lost:
        r3 = r2.y;
     */
    public boolean contains(int i, int i2) {
        int i3;
        int i4 = this.x;
        return i >= i4 && i < i4 + this.width && i2 >= i3 && i2 < i3 + this.height;
    }

    public int getCenterX() {
        return (this.x + this.width) / 2;
    }

    public int getCenterY() {
        return (this.y + this.height) / 2;
    }
}
