package istad.co.nectarapi.features.bakong;


import istad.co.nectarapi.features.bakong.dto.BakongDataRequest;
import kh.gov.nbc.bakong_khqr.model.KHQRData;
import kh.gov.nbc.bakong_khqr.model.KHQRResponse;
import org.springframework.http.ResponseEntity;

public interface BakongService {
    KHQRResponse<KHQRData> generateQR(BakongDataRequest request);
    ResponseEntity<byte[]> getQRImage(KHQRData qr);
    ResponseEntity<?> checkTransactionByMD5(String md5);
//    ResponseEntity<?> generateDeeplink(KHQRData qr);
}
