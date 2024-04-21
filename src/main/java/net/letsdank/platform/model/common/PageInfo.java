package net.letsdank.platform.model.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@Getter
public class PageInfo<T> {
    private List<T> items;
    private int currentPage;
    private long totalItems;
    private int totalPages;
    private int pageSize;

    public PageInfo(Page<T> page) {
        this.prepare(page);
    }

    public void prepare(Page<T> page) {
        this.items = page.getContent();
        this.currentPage = page.getNumber() + 1;
        this.totalItems = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.pageSize = page.getSize();
    }
}
