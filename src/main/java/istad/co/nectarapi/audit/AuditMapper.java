package istad.co.nectarapi.audit;

import istad.co.nectarapi.features.user.UserResolver;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AuditMapper {

    @Autowired
    protected UserResolver userResolver;

    public AuditResponse toDto(Auditable auditable) {
        if (auditable == null) return null;
        return new AuditResponse(
                userResolver.getUserName(auditable.getCreatedBy()),
                userResolver.getUserName(auditable.getUpdatedBy()),
                auditable.getCreatedAt(),
                auditable.getUpdatedAt()
        );
    }
}
