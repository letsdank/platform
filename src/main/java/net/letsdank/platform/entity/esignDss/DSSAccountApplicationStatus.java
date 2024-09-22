package net.letsdank.platform.entity.esignDss;

import lombok.Getter;

@Getter
public enum DSSAccountApplicationStatus {
    SENT("Отправлено"),
    PREPARED("Подготовлено"),
    NOT_PREPARED("Не подготовлено"),
    DECLINED("Отклонено"),
    DONE("Исполнено");

    private final String name;

    DSSAccountApplicationStatus(String name) {
        this.name = name;
    }
}
