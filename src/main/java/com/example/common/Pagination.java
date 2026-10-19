package com.example.common;

import java.util.List;
import java.util.stream.IntStream;

public record Pagination(
        int currentPage,
        int totalPages,
        List<Integer> pages
) {
    public static Pagination create(int currentPage, int totalPages) {
        if (totalPages <= 0) {
            return new Pagination(1, 0, List.of());
        }

        int current = Math.max(1, Math.min(currentPage, totalPages));

        List<Integer> first = IntStream.rangeClosed(1, 4)
                .filter(p -> p <= totalPages)
                .boxed()
                .toList();

        List<Integer> last = IntStream.rangeClosed(
                        Math.max(1, totalPages - 3),
                        totalPages
                )
                .boxed()
                .toList();

        List<Integer> pages;

        if (first.contains(current)) {
            pages = first;
        } else if (last.contains(current)) {
            pages = last;
        } else {
            pages = IntStream.rangeClosed(current - 1, current + 1)
                    .filter(p -> p > 0 && p <= totalPages)
                    .boxed()
                    .toList();
        }

        return new Pagination(current, totalPages, pages);
    }
}