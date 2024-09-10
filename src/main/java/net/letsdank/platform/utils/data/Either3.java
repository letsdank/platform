package net.letsdank.platform.utils.data;

// TODO: Temporary solution - need to think about this
public class Either3<A, B, C> {
    private A a;
    private B b;
    private C c;

    public Either3(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public boolean isLeft() {
        return a!= null;
    }

    public boolean isMiddle() {
        return b!= null;
    }

    public boolean isRight() {
        return c!= null;
    }

    public A left() {
        return a;
    }

    public B middle() {
        return b;
    }

    public C right() {
        return c;
    }

    public static <A, B, C> Either3<A, B, C> left(A a) {
        return new Either3<>(a, null, null);
    }

    public static <A, B, C> Either3<A, B, C> middle(B b) {
        return new Either3<>(null, b, null);
    }

    public static <A, B, C> Either3<A, B, C> right(C c) {
        return new Either3<>(null, null, c);
    }
}
