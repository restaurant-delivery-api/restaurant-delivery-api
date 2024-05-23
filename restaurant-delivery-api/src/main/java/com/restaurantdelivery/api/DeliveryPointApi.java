package com.restaurantdelivery.api;

import com.restaurantdelivery.configuration.Constants;
import com.restaurantdelivery.dto.DeliveryPointDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RequestMapping(Constants.BASE_API_PATH + "/delivery_point")
public interface DeliveryPointApi {

    @GetMapping("/all")
    @ResponseBody
    List<DeliveryPointDto> getDeliveryPoints();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    DeliveryPointDto addDeliveryPoint(@RequestBody DeliveryPointDto deliveryPointDto);

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    DeliveryPointDto updateDeliveryPoint(@PathVariable("id") @PositiveOrZero(message = "id must be not negative") Long id,
                                         @RequestBody DeliveryPointDto deliveryPointDto);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteDeliveryPoint(@PathVariable("id") @PositiveOrZero(message = "id must be not negative") Long id);

}
