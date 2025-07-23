package com.trtm.sy.registerformula.model;

import lombok.Data;

import java.util.List;

@Data
public class SyDesignFormulasRequest {
    List<SyDesignFormulas> syDesignFormulas;
    String jlbbm;
}
