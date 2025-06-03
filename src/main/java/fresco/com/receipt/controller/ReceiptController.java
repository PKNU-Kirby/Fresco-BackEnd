package fresco.com.receipt.controller;

import fresco.com.receipt.controller.dto.request.ReceiptImageRequest;
import fresco.com.receipt.controller.dto.response.ReceiptResponse;
import fresco.com.receipt.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/receipt")
@RequiredArgsConstructor
public class ReceiptController {
    public final ReceiptService receiptService;

    @PostMapping("/register")
    public ResponseEntity<ReceiptResponse> getUserBookmarks(@RequestBody ReceiptImageRequest receiptImage) {
        receiptService.postReceiptImage(receiptImage);
        return ResponseEntity.ok(new ReceiptResponse());
    }
}
