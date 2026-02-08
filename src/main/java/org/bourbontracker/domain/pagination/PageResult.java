package org.bourbontracker.domain.pagination;

import java.util.List;

public class PageResult<T> {
    public List<T> items;
    public int page;
    public int size;
    public long totalElements;
    public int totalPages;
    public boolean first;
    public boolean last;

    public static <T> PageResult<T> of(List<T> items, int page, int size, long totalElements) {
        PageResult<T> result = new PageResult<>();
        result.items = items;
        result.page = page;
        result.size = size;
        result.totalElements = totalElements;
        result.totalPages = size == 0 ? 0 : (int) Math.ceil(totalElements / (double) size);
        result.first = page <= 0;
        result.last = result.totalPages == 0 || page >= result.totalPages - 1;
        return result;
    }
}
