/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.dm;

import com.basistech.util.TextDomain;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * A list of translations for the tokens. Each translation matches the corresponding token.
 */
public class TranslatedTokens extends BaseAttribute {
    private final TextDomain domain;
    private final List<String> translations; // 1-1 with tokens

    protected TranslatedTokens(TextDomain domain, List<String> translations, Map<String, Object> extendedProperties) {
        super(extendedProperties);
        this.domain = domain;
        this.translations = listOrNull(translations);
    }

    /**
     * Returns the domain for this object.
     *
     * @return the domain for this object
     */
    public TextDomain getDomain() {
        return domain;
    }

    /**
     * Returns the translations for this object.
     *
     * @return the translations for this object
     */
    public List<String> getTranslations() {
        return translations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        TranslatedTokens that = (TranslatedTokens) o;

        if (domain != null ? !domain.equals(that.domain) : that.domain != null) {
            return false;
        }
        return !(translations != null ? !translations.equals(that.translations) : that.translations != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        result = 31 * result + (translations != null ? translations.hashCode() : 0);
        return result;
    }

    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return Objects.toStringHelper(this)
            .add("domain", domain)
            .add("translations", translations);
    }

    /**
     * Builder class for TranslatedTokens.
     */
    public static class Builder extends BaseAttribute.Builder {
        private TextDomain domain;
        private List<String> translations;

        /**
         * Constructs a builder from the required properties
         *
         * @param domain specifies the language and script of the translation
         */
        public Builder(TextDomain domain) {
            super();
            this.domain = domain;
            this.translations = Lists.newArrayList();
        }

        /**
         * Constructs a builder from an existing TranslatedTokens object
         *
         * @param toCopy the existing object
         */
        public Builder(TranslatedTokens toCopy) {
            super(toCopy);
            this.domain = toCopy.domain;
            translations = Lists.newArrayList();
            addAllToList(translations, toCopy.getTranslations());
        }

        /**
         * Adds the translation of one token to the list of translations.
         *
         * @param translatedToken the translation for one token
         * @return this
         */
        public Builder addTranslatedToken(String translatedToken) {
            this.translations.add(translatedToken);
            return this;
        }

        /**
         * Set all of the translations for this token.
         * @param translations the translations.
         * @return this.
         */
        public Builder translatedTokens(List<String> translations) {
            this.translations = nullOrList(translations);
            return this;
        }

        /**
         * Builds a new TranslatedTokens object from the current state of the builder.
         *
         * @return a new TranslatedTokens object.
         */
        public TranslatedTokens build() {
            return new TranslatedTokens(domain, translations, buildExtendedProperties());
        }
    }
}