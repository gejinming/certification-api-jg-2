package com.gnet.plugin.poi;

import lombok.Data;

import java.util.List;

@Data
public class ExcelDefinition {

    private RowDefinition header;
    private List<RowDefinition> body;

}
