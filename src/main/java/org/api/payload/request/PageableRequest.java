package org.api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.enumeration.SortEnum;
import org.api.payload.PageCommon;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageableRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Pageable pageable;
    private Sort sort;
    private int page;
    private int size;

    public Pageable getPageable() {
        return PageRequest.of(this.page, this.size, this.sort);
    }

}
