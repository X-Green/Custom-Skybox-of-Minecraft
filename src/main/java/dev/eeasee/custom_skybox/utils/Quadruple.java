package dev.eeasee.custom_skybox.utils;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Objects;

public class Quadruple<A, B, C, D> implements Comparable<Quadruple<A, B, C, D>> {
    public final A a;
    public final B b;
    public final C c;
    public final D d;

    public Quadruple(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public int compareTo(final Quadruple<A, B, C, D> other) {
        return new CompareToBuilder().append(this.a, other.a)
                .append(this.b, other.b)
                .append(this.c, other.c)
                .append(this.d, other.d)
                .toComparison();
    }

    /**
     * <p>Compares this triple to another based on the three elements.</p>
     *
     * @param obj the object to compare to, null returns false
     * @return true if the elements of the triple are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Quadruple<?, ?, ?, ?>) {
            final Quadruple<?, ?, ?, ?> other = (Quadruple<?, ?, ?, ?>) obj;
            return Objects.equals(this.a, other.a)
                    && Objects.equals(this.b, other.b)
                    && Objects.equals(this.c, other.c)
                    && Objects.equals(this.d, other.d);
        }
        return false;
    }

    /**
     * <p>Returns a suitable hash code.</p>
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return (this.a == null ? 0 : this.a.hashCode()) ^
                (this.b == null ? 0 : this.b.hashCode()) ^
                (this.c == null ? 0 : this.c.hashCode()) ^
                (this.d == null ? 0 : this.d.hashCode());
    }

    /**
     * <p>Returns a String representation of this triple using the format {@code ($left,$middle,$right)}.</p>
     *
     * @return a string describing this object, not null
     */
    @Override
    public String toString() {
        return "(" + this.a + "," + this.b + "," + this.c + "," + this.d + ")";
    }
}
