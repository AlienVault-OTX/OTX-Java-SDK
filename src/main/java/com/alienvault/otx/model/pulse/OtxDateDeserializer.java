package com.alienvault.otx.model.pulse;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * © AlienVault Inc. 2017
 * All rights reserved
 * This code is protected by copyright and distributed under licenses
 * restricting its use, copying, distribution, and decompilation. It may
 * only be reproduced or used under license from AlienVault Inc. or its
 * authorised licensees.
 *
 * Created by crosa on 12/07/2017.
*/
public class OtxDateDeserializer extends JsonDeserializer<Date> {


    @Override
    public Date deserialize(JsonParser jsonparser,
                            DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String date = jsonparser.getText();
        try {
            return format.parse(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
