package org.closure.laser.service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import org.closure.laser.domain.Location;
import org.closure.laser.repository.LocationRepository;
import org.closure.laser.service.dto.LocationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Scheduler {

    @Autowired
    private LocationRepository locationRepository;

    @Scheduled(fixedRate = 21600000)
    public void fixedRateSch() {
        String token =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI0IiwianRpIjoiNWM3MmVjMWUyZjkxN2IzNGI3YjU4MWU0YjczNGM0NDAyZTZhZTZhOTMxZTliZmExYjg5YjQ2OTg2OWQwZTU5MjBiNzNlY2Q0ODFmMWRkNzYiLCJpYXQiOjE2NjExMTE0MzcsIm5iZiI6MTY2MTExMTQzNywiZXhwIjoxNjkyNjQ3NDM3LCJzdWIiOiIxMDk3NCIsInNjb3BlcyI6W119.BScZhkjYVuQarl9uu7t0DIAlB-PVEja-T9E1Nt7tz5ow3gPjSOnTavtRZsSBmc5dBW6K6f9N4J7rohNIXQjJCA";
        String uri = "https://app.goflightlabs.com/airports?access_key=" + token;/* */
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LocationModel[]> responseEntity = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<LocationModel[]>() {}
        );

        LocationModel[] locationModels = responseEntity.getBody();
        Arrays
            .asList(locationModels)
            .stream()
            .map(model -> {
                Location location = new Location();
                if (!locationRepository.findByAirport(model.getAirport_name()).isPresent()) {
                    location.setCity(model.getTimezone().split("/")[1]);
                    location.setCountry(model.getCountry_name());
                    location.setAirport(model.getAirport_name());
                    location.setCreatedAt(new Date());
                    location = locationRepository.save(location);
                }
                return location;
            })
            .collect(Collectors.toList());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");/* */

        Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("Get Locations From API scheduler:: " + strDate);
    }
}
