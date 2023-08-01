package org.api.payload;

import lombok.Getter;
import lombok.Setter;
import org.api.enumeration.SortEnum;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public abstract class PageCommon implements Serializable {

    private static final long serialVersionUID = 1L;
    private String sorts = SortEnum.ASC.getText();
    private String keyword = "";
    private String field = "id";
    private int page = 1;
    private int size = 100;

}
