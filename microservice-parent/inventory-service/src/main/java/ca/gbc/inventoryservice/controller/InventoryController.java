package ca.gbc.inventoryservice.controller;


import ca.gbc.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;


    // @RequestParam is for passing the parameters directly from http:localhost:..../api/inventory?skuCode=SKU001&...
    // THIS IS A GET REQUEST SO YOU DON'T PASS A JSON BODY FOR THIS METHOD
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity) {
        return inventoryService.isInStock(skuCode, quantity);
    }

}
