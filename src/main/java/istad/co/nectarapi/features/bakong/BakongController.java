package istad.co.nectarapi.features.bakong;


import istad.co.nectarapi.features.bakong.dto.BakongDataRequest;
import kh.gov.nbc.bakong_khqr.model.KHQRData;
import kh.gov.nbc.bakong_khqr.model.KHQRResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/bakong")
@RequiredArgsConstructor
public class BakongController {

    private final BakongService service;

    @PostMapping("/generate-qr")
    public KHQRResponse<KHQRData> generateQR(@RequestBody BakongDataRequest request){
        return service.generateQR(request);
    }

    @PostMapping("/get-qr-image")
    public ResponseEntity<byte[]> getQRImage(@RequestBody KHQRData qr) {
        return service.getQRImage(qr);
    }

    @PostMapping("/check-transaction")
    public ResponseEntity<?> checkTransaction(@RequestBody Map<String, String> body) {
        String md5 = body.get("md5");
        return service.checkTransactionByMD5(md5);
    }

//    @PostMapping("/generate-deeplink")
//    public ResponseEntity<?> generateDeeplink(@RequestBody KHQRData qr) {
//        return service.generateDeeplink(qr);
//    }
}
