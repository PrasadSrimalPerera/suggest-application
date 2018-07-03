package com.task.suggest.controllers;

import com.task.suggest.index.GeoLocation;
import com.task.suggest.index.search.SuggestRequest;
import com.task.suggest.services.SuggestAPIResponse;
import com.task.suggest.services.SuggestService;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

/**
 * Created by prasad on 6/30/18.
 * SuggestController provides RestController API for the suggest requests
 */
@RestController
public class SuggestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestController.class);
    @Autowired
    private SuggestService suggestService;

    @ResponseBody
    @RequestMapping(value = "/suggest", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity suggest(@RequestParam(value = "q") String query,
                                  @RequestParam(value = "latitude", required = false) Double latitude,
                                  @RequestParam(value = "longitude", required = false) Double longitude) {

        if (!validateParams(query, latitude, longitude))
            return ResponseEntity.badRequest().body("Invalid request parameters!");

        LOGGER.debug("Suggest Request with query= {}, latitude= {}, longitude= {}", query, latitude, longitude);
        GeoLocation geoLocation = null;
        if (Objects.nonNull(latitude) && Objects.nonNull(longitude))
            geoLocation = new GeoLocation(latitude, longitude);

        SuggestRequest suggestRequest = suggestService.createSuggestRequest(query, geoLocation);
        SuggestAPIResponse suggestAPIResponse = suggestService.suggest(suggestRequest);
        LOGGER.debug("Found {} results for query= {}, latitude= {}, longitude= {}",
                suggestAPIResponse.getSuggestions().size(), query, latitude, longitude);
        return ResponseEntity.ok(suggestAPIResponse);
    }


    /**
     * Validate the input parameters if query is blank or one of the geo parameters are not present
     * @param query input query
     * @param latitude  geo latitude
     * @param longitude geo longitude
     * @return  true if parameters are valid, else false
     */
    private boolean validateParams(String query, Double latitude, Double longitude) {
        return !(Strings.isBlank(query) ||
                (Objects.nonNull(latitude) && Objects.isNull(longitude)) ||
                (Objects.nonNull(longitude) && Objects.isNull(latitude)));
    }
}
