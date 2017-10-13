package org.whispersystems.textsecuregcm.storage;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface BlockedAccounts {

    @SqlQuery("SELECT anonymous_only FROM blocked_accounts WHERE blocked_account_number = :blocked_account_number AND account_number = :account_number")
    Boolean isAnonymousBlockedAccountNumberAndAccountNumber(@Bind("blocked_account_number") String blockedAccountNumber, @Bind("account_number") String accountNumber);

}
