package org.whispersystems.textsecuregcm.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class EmailConfiguration {

    @NotEmpty
    @JsonProperty
    private String url;

    @NotEmpty
    @JsonProperty
    private String user;

    @NotEmpty
    @JsonProperty
    private String password;

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

}
