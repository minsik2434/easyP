package com.easy_p.easyp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PageDto {
    private List<?> projectDtoList;
    private int currentPage;
    private Long totalPage;
    private int pageSize;
    private Long totalElement;
}
