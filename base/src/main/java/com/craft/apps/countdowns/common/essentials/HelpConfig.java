package com.craft.apps.countdowns.common.essentials;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

/**
 * A POJO used to hold values for {@link com.craft.essentials.userhelp.activity.HelpActivity}
 * population.
 * <p>
 * Use {@link Builder} to create a new HelpConfig object.
 *
 * @author willie
 * @version 1.0.0
 * @see com.craft.essentials.userhelp.activity.HelpActivity
 * @see Builder
 * @since 1.0.0
 */
@SuppressWarnings("JavaDoc")
public class HelpConfig implements Parcelable {

    public static final Creator<HelpConfig> CREATOR = new Creator<HelpConfig>() {
        @Override
        public HelpConfig createFromParcel(Parcel in) {
            return new HelpConfig(in);
        }

        @Override
        public HelpConfig[] newArray(int size) {
            return new HelpConfig[size];
        }
    };

    private String emailAddress;

    private String phoneNumber;

    private String privacyPolicyUrl;

    private String tosUrl;

    private HelpConfig(String emailAddress, String phoneNumber, String privacyPolicyUrl,
                       String tosUrl) {
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.privacyPolicyUrl = privacyPolicyUrl;
        this.tosUrl = tosUrl;
    }

    @SuppressWarnings("WeakerAccess")
    protected HelpConfig(Parcel in) {
        emailAddress = in.readString();
        phoneNumber = in.readString();
        privacyPolicyUrl = in.readString();
        tosUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(emailAddress);
        dest.writeString(phoneNumber);
        dest.writeString(privacyPolicyUrl);
        dest.writeString(tosUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getTosUrl() {
        return this.tosUrl;
    }

    public String getPrivacyPolicyUrl() {
        return this.privacyPolicyUrl;
    }

    /**
     * A builder used to create new {@link HelpConfig}s.
     */
    @SuppressWarnings("unused")
    public static class Builder {

        private String emailAddress;

        private String phoneNumber;

        private String privacyPolicyUrl;

        private String tosUrl;

        public Builder setEmailAddress(String email) {
            this.emailAddress = email;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setPrivacyPolicyUrl(String privacyPolicyUrl) {
            this.privacyPolicyUrl = privacyPolicyUrl;
            return this;
        }

        public Builder setTosUrl(String tosUrl) {
            this.tosUrl = tosUrl;
            return this;
        }

        /**
         * Returns a new HelpConfig object with the given parameters from this builder.
         *
         * @return A new non-null {@link HelpConfig} object
         */
        @NonNull
        public HelpConfig build() {
            return new HelpConfig(emailAddress, phoneNumber, privacyPolicyUrl, tosUrl);
        }
    }
}
