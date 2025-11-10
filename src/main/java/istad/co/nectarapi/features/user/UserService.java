package istad.co.nectarapi.features.user;

import istad.co.nectarapi.base.BasedMessage;

public interface UserService {

    BasedMessage assignAdminRole(String email);
    BasedMessage removeAdminRole(String email);

}
