package com.vibrantcovers.dto;

import com.vibrantcovers.entity.CaseColor;
import com.vibrantcovers.entity.CaseFinish;
import com.vibrantcovers.entity.CaseMaterial;
import com.vibrantcovers.entity.PhoneModel;
import lombok.Data;

@Data
public class SaveConfigurationRequest {
    private CaseColor color;
    private CaseFinish finish;
    private CaseMaterial material;
    private PhoneModel model;
}

