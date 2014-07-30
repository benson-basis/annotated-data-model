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

import java.util.Map;

/**
 * A base noun phrase.
 */
public class BaseNounPhrase extends Attribute {

    BaseNounPhrase(int startOffset, int endOffset) {
        super(startOffset, endOffset);
    }

    BaseNounPhrase(int startOffset, int endOffset, Map<String, Object> extendedProperties) {
        super(startOffset, endOffset, extendedProperties);
    }

    // Make json happy
    protected BaseNounPhrase() {
    }

    /**
     * Builder for base noun phrase attributes.
     */
    public static class Builder extends Attribute.Builder {
        /**
         * Construct a builder from the required properties.
         * @param startOffset start offset in characters.
         * @param endOffset end offset in characters.
         */
        public Builder(int startOffset, int endOffset) {
            super(startOffset, endOffset);
        }

        /**
         * Construct a builder from the 'traditional Rosette' data structure; an int[]
         * in which the even-numbered items are token start offsets, and the odd numbered
         * items are token end offsets. This constructor accept token indices for
         * beginning and end of a sentence, token and obtains the character offsets from
         * the token offsets.
         * @param tokenOffsets array of token start/end offsets.
         * @param tokenStartIndex token offset of the sentence start.
         * @param tokenEndIndex token offset of the end offset.
         * @adm.ignore
         */
        public Builder(int[] tokenOffsets, int tokenStartIndex, int tokenEndIndex) {
            this(tokenOffsets[2 * tokenStartIndex], tokenOffsets[2 * (tokenEndIndex - 1) + 1]);
        }

        /**
         * Construct a builder from an existing BaseNounPhrase.
         * @param toCopy the object to copy.
         * @adm.ignore
         */
        public Builder(BaseNounPhrase toCopy) {
            super(toCopy);
        }

        public BaseNounPhrase build() {
            return new BaseNounPhrase(startOffset, endOffset, extendedProperties);
        }
    }
}
