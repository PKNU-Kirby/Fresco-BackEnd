package fresco.com.receipt.controller;

import fresco.com.receipt.controller.dto.request.ReceiptImageRequest;
import fresco.com.receipt.controller.dto.response.FoodPair;
import fresco.com.receipt.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receipt")
@RequiredArgsConstructor
public class ReceiptController {
    public final ReceiptService receiptService;

    @PostMapping("/register")
    public ResponseEntity<List<FoodPair>> getUserBookmarks(@RequestPart("receipt") MultipartFile multipartFile) {
        List<FoodPair> receiptResponse = receiptService.postReceiptImage(new ReceiptImageRequest(multipartFile));
        return ResponseEntity.ok(receiptResponse);
    }
}