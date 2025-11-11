package istad.co.nectarapi.features.user.dto;

import istad.co.nectarapi.audit.AuditResponse;
import istad.co.nectarapi.audit.Auditable;

import java.time.LocalDate;
import java.util.List;

public record UserResponse(
        String uuid,
        String phoneNumber,
        String email,
        String name,
        String profileImage,
        String gender,
        LocalDate dob,
        String cityOrProvince,
        String khanOrDistrict,
        String sangkatOrCommune,
        String village,
        String street,
        Boolean isDeleted,
        Boolean isBlocked,
        List<String> roles,
        AuditResponse audit
) {
}
