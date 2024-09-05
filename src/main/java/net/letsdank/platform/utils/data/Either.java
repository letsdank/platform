package net.letsdank.platform.utils.data;

import lombok.Getter;

@Getter
public record Either<L, R>(L left, R right) {
    public boolean isLeft() {
        return left != null;
    }

    public boolean isRight() {
        return right != null;
    }

    public static <L, R> Either<L, R> left(L left) {
        return new Either<>(left, null);
    }

    public static <L, R> Either<L, R> right(R right) {
        return new Either<>(null, right);
    }
}
