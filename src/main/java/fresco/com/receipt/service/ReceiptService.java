package fresco.com.receipt.service;

import fresco.com.receipt.controller.dto.request.ReceiptImageRequest;
import fresco.com.receipt.controller.dto.response.ReceiptResponse;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService {
    public ReceiptResponse postReceiptImage(ReceiptImageRequest receiptImage) {
        return new ReceiptResponse();
    }
}