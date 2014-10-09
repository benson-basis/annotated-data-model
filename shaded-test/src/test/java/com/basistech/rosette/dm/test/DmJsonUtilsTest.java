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

package com.basistech.rosette.dm.test;

import com.basistech.rosette.dm.AnnotatedText;
import com.basistech.rosette.dm.ArabicMorphoAnalysis;
import com.basistech.rosette.dm.BaseNounPhrase;
import com.basistech.rosette.dm.CategorizerResult;
import com.basistech.rosette.dm.DmJsonUtils;
import com.basistech.rosette.dm.EntityMention;
import com.basistech.rosette.dm.ResolvedEntity;
import com.basistech.rosette.dm.HanMorphoAnalysis;
import com.basistech.rosette.dm.LanguageDetection;
import com.basistech.rosette.dm.ListAttribute;
import com.basistech.rosette.dm.MorphoAnalysis;
import com.basistech.rosette.dm.ScriptRegion;
import com.basistech.rosette.dm.Sentence;
import com.basistech.rosette.dm.Token;
import com.basistech.rosette.dm.TranslatedData;
import com.basistech.rosette.dm.TranslatedTokens;
import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;
import com.basistech.util.TextDomain;
import com.basistech.util.TransliterationScheme;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Copied mostly from JsonTest, but this one is special for the shaded-test.
// Note that intellj has problems with this file, but the maven build works.
// We do NOT depend on guava here, so you'll see some old-style constructions
// like List<String> list = new ArrayList<String>();
public class DmJsonUtilsTest extends Assert {
    public static final String THIS_IS_THE_TERRIER_SHOT_TO_BOSTON = "This is the terrier shot to Boston.";
    private BaseNounPhrase baseNounPhrase;
    private EntityMention entityMention;
    private ResolvedEntity resolvedEntity;
    private LanguageDetection languageDetection;
    private ListAttribute<LanguageDetection> languageDetectionRegions;
    private LanguageDetection languageDetectionRegion;
    private ScriptRegion scriptRegion;
    private Sentence sentence;
    private MorphoAnalysis morphoAnalysis;
    private TextDomain germanDomain;
    private TextDomain spanishDomain;
    private Token token;
    private TranslatedData germanTranslatedData;
    private TranslatedData spanishTranslatedData;
    private TranslatedTokens germanTranslation;
    private TranslatedTokens spanishTranslation;
    private CategorizerResult categoryResult;
    private CategorizerResult sentimentResult;
    private AnnotatedText referenceText;

    @Before
    public void oneWithEverything() {
        AnnotatedText.Builder builder = new AnnotatedText.Builder();
        builder.data(THIS_IS_THE_TERRIER_SHOT_TO_BOSTON);
        /* Zen text: make me one with everything. */
        ListAttribute.Builder<BaseNounPhrase> bnpListBuilder = new ListAttribute.Builder<BaseNounPhrase>(BaseNounPhrase.class);
        BaseNounPhrase.Builder bnpBuilder = new BaseNounPhrase.Builder(8, 19);
        bnpBuilder.extendedProperty("bnp-ex", "bnp-ex-val");
        baseNounPhrase = bnpBuilder.build();
        bnpListBuilder.add(baseNounPhrase);
        builder.baseNounPhrases(bnpListBuilder.build());

        ListAttribute.Builder<EntityMention> emListBuilder = new ListAttribute.Builder<EntityMention>(EntityMention.class);
        EntityMention.Builder emBuilder = new EntityMention.Builder(27, 33, "place");
        emBuilder.flags(42);
        emBuilder.normalized("bahston");
        emBuilder.source("test");
        emBuilder.confidence(1.0);
        emBuilder.coreferenceChainId(43);
        emBuilder.extendedProperty("em-ex", "em-ex-val");
        entityMention = emBuilder.build();
        emListBuilder.add(entityMention);
        builder.entityMentions(emListBuilder.build());

        ListAttribute.Builder<ResolvedEntity> reListBuilder = new ListAttribute.Builder<ResolvedEntity>(ResolvedEntity.class);
        ResolvedEntity.Builder reBuilder = new ResolvedEntity.Builder(27, 33, "Q100");
        reBuilder.coreferenceChainId(43);
        reBuilder.confidence(1.0);
        reBuilder.extendedProperty("resolvedEntity-ex", "resolvedEntity-ex-val");
        resolvedEntity = reBuilder.build();
        reListBuilder.add(resolvedEntity);
        builder.resolvedEntities(reListBuilder.build());

        ListAttribute.Builder<LanguageDetection> ldListBuilder = new ListAttribute.Builder<LanguageDetection>(LanguageDetection.class);
        List<LanguageDetection.DetectionResult> dets = new ArrayList<LanguageDetection.DetectionResult>();
        dets.add(new LanguageDetection.DetectionResult.Builder(LanguageCode.ENGLISH).encoding("utf-8").script(ISO15924.Latn).confidence(1.0).build());
        LanguageDetection.Builder ldBuilder = new LanguageDetection.Builder(0, builder.data().length(), dets);
        ldBuilder.extendedProperty("ldr-ex", "ldr-ex-val");
        languageDetectionRegion = ldBuilder.build();
        ldListBuilder.add(languageDetectionRegion);
        languageDetectionRegions =  ldListBuilder.build();
        builder.languageDetectionRegions(languageDetectionRegions);

        dets = new ArrayList<LanguageDetection.DetectionResult>();
        dets.add(new LanguageDetection.DetectionResult.Builder(LanguageCode.FRENCH).encoding("utf-8").script(ISO15924.Latn).confidence(1.0).build());
        ldBuilder = new LanguageDetection.Builder(0, builder.data().length(), dets);
        ldBuilder.extendedProperty("ld-ex", "ld-ex-val");
        languageDetection = ldBuilder.build();
        builder.wholeDocumentLanguageDetection(languageDetection);

        ListAttribute.Builder<ScriptRegion> srListBuilder = new ListAttribute.Builder<ScriptRegion>(ScriptRegion.class);
        ScriptRegion.Builder srBuilder = new ScriptRegion.Builder(0, builder.data().length(), ISO15924.Latn);
        srBuilder.extendedProperty("sr-ex", "sr-ex-val");
        scriptRegion = srBuilder.build();
        srListBuilder.add(scriptRegion);
        builder.scriptRegions(srListBuilder.build());

        ListAttribute.Builder<Sentence> sentListBuilder = new ListAttribute.Builder<Sentence>(Sentence.class);
        Sentence.Builder sentBuilder = new Sentence.Builder(0, 8);
        sentBuilder.extendedProperty("sb-ex", "sb-ex-val");
        sentence = sentBuilder.build();
        sentListBuilder.add(sentence);
        builder.sentences(sentListBuilder.build());

        ListAttribute.Builder<Token> tokenListBuilder = new ListAttribute.Builder<Token>(Token.class);
        Token.Builder tokenBuilder = new Token.Builder(0, 4, "This");
        tokenBuilder.source("test");
        tokenBuilder.addNormalized("abnormal");
        tokenBuilder.extendedProperty("tok-ex", "tok-ex-val");

        MorphoAnalysis.Builder maBuilder = new MorphoAnalysis.Builder();
        maBuilder.raw("cooked");
        maBuilder.partOfSpeech("+woof");
        Token.Builder compTokBuilder = new Token.Builder(0, 2, "Th");
        maBuilder.addComponent(compTokBuilder.build());
        morphoAnalysis = maBuilder.build();
        tokenBuilder.addAnalysis(morphoAnalysis);

        ArabicMorphoAnalysis.Builder araMaBuilder = new ArabicMorphoAnalysis.Builder();
        araMaBuilder.addPrefix("pre", "PRE");
        araMaBuilder.addStem("stem", "STEM");
        araMaBuilder.addSuffix("suff", "SUFF");
        araMaBuilder.definiteArticle(true);
        araMaBuilder.strippablePrefix(true);
        araMaBuilder.root("root");
        araMaBuilder.lengths(2, 3);
        araMaBuilder.lemma("lemma");
        araMaBuilder.partOfSpeech("pos");
        araMaBuilder.raw("raw");
        tokenBuilder.addAnalysis(araMaBuilder.build());

        HanMorphoAnalysis.Builder hanMaBuilder = new HanMorphoAnalysis.Builder();
        hanMaBuilder.addReading("proust");
        araMaBuilder.lemma("lemma");
        araMaBuilder.partOfSpeech("pos");
        tokenBuilder.addAnalysis(hanMaBuilder.build());

        token = tokenBuilder.build();
        tokenListBuilder.add(token);
        builder.tokens(tokenListBuilder.build());

        ListAttribute.Builder<TranslatedData> translatedDataBuilder =
            new ListAttribute.Builder<TranslatedData>(TranslatedData.class);

        germanDomain = new TextDomain(ISO15924.Latn, LanguageCode.GERMAN, TransliterationScheme.NATIVE);
        String germanText = "Ein.  Zwei.";
        TranslatedData.Builder tdBuilder = new TranslatedData.Builder(germanDomain, germanText);
        germanTranslatedData = tdBuilder.build();
        translatedDataBuilder.add(germanTranslatedData);
        spanishDomain = new TextDomain(ISO15924.Latn, LanguageCode.SPANISH, TransliterationScheme.NATIVE);
        String spanishText = "Uno.  Dos.";
        tdBuilder = new TranslatedData.Builder(spanishDomain, spanishText);
        spanishTranslatedData = tdBuilder.build();
        translatedDataBuilder.add(spanishTranslatedData);
        builder.translatedData(translatedDataBuilder.build());

        ListAttribute.Builder<TranslatedTokens> translatedTokensListBuilder =
            new ListAttribute.Builder<TranslatedTokens>(TranslatedTokens.class);

        TranslatedTokens.Builder ttBuilder = new TranslatedTokens.Builder(germanDomain);
        ttBuilder.addTranslatedToken("Ein");
        ttBuilder.addTranslatedToken(".");
        ttBuilder.addTranslatedToken("Zwei");
        ttBuilder.addTranslatedToken(".");
        germanTranslation = ttBuilder.build();
        translatedTokensListBuilder.add(germanTranslation);
        spanishDomain = new TextDomain(ISO15924.Latn, LanguageCode.SPANISH, TransliterationScheme.NATIVE);
        ttBuilder = new TranslatedTokens.Builder(spanishDomain);
        ttBuilder.addTranslatedToken("Uno");
        ttBuilder.addTranslatedToken(".");
        ttBuilder.addTranslatedToken("Dos");
        ttBuilder.addTranslatedToken(".");
        spanishTranslation = ttBuilder.build();
        translatedTokensListBuilder.add(spanishTranslation);
        builder.translatedTokens(translatedTokensListBuilder.build());

        ListAttribute.Builder<CategorizerResult> crBuilder
            = new ListAttribute.Builder<CategorizerResult>(CategorizerResult.class);
        Map<String, Double> perFeatureScores = new HashMap<String, Double>();
        perFeatureScores.put("foo", 1.2);
        perFeatureScores.put("bar", -2.4);
        List<String> explanationSet = new ArrayList<String>();
        explanationSet.add("foo");
        explanationSet.add("bar");
        categoryResult = new CategorizerResult.Builder("POLITICS", -0.2)
            .confidence(0.3)
            .explanationSet(explanationSet)
            .perFeatureScores(perFeatureScores).build();
        crBuilder.add(categoryResult);
        builder.categorizerResults(crBuilder.build());

        crBuilder = new ListAttribute.Builder<CategorizerResult>(CategorizerResult.class);
        sentimentResult = new CategorizerResult.Builder("negative", -0.2)
            .confidence(0.3)
            .explanationSet(explanationSet)
            .perFeatureScores(perFeatureScores).build();
        crBuilder.add(sentimentResult);
        builder.sentimentResults(crBuilder.build());

        referenceText = builder.build();
    }

    @Test
    public void roundTrip() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DmJsonUtils.serialize(referenceText, byteArrayOutputStream);

        //System.out.print(writer.toString());

        AnnotatedText read = DmJsonUtils.deserialize(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

        ListAttribute<BaseNounPhrase> bnpList = read.getBaseNounPhrases();
        assertNotNull(bnpList);
        assertEquals(1, bnpList.size());
        BaseNounPhrase bnp = bnpList.get(0);
        assertEquals(baseNounPhrase, bnp);

        ListAttribute<EntityMention> emList = read.getEntityMentions();
        assertNotNull(emList);
        assertEquals(1, emList.size());
        EntityMention em = emList.get(0);
        assertEquals(entityMention, em);

        ListAttribute<ResolvedEntity> resolvedEntityList = read.getResolvedEntities();
        assertNotNull(resolvedEntityList);
        assertEquals(1, resolvedEntityList.size());
        ResolvedEntity e = resolvedEntityList.get(0);
        assertEquals(resolvedEntity, e);

        ListAttribute<LanguageDetection> languageDetectionList = read.getLanguageDetectionRegions();
        assertNotNull(languageDetectionList);
        assertEquals(1, languageDetectionList.size());

        assertEquals(languageDetectionRegion, languageDetectionList.get(0));

        ListAttribute<ScriptRegion> scriptRegionList = read.getScriptRegions();
        assertNotNull(scriptRegionList);
        assertEquals(1, scriptRegionList.size());

        assertEquals(scriptRegion, scriptRegionList.get(0));

        ListAttribute<Sentence> sentences = read.getSentences();
        assertNotNull(sentences);

        assertEquals(sentence, sentences.get(0));

        ListAttribute<Token> tokenList = read.getTokens();
        assertNotNull(tokenList);
        assertEquals(1, tokenList.size());
        assertEquals(token, tokenList.get(0));

        ListAttribute<TranslatedData> dataTranslations = read.getTranslatedData();
        assertEquals(germanTranslatedData, dataTranslations.get(0));
        assertEquals(spanishTranslatedData, dataTranslations.get(1));

        ListAttribute<TranslatedTokens> translatedTokens = read.getTranslatedTokens();
        assertEquals(germanTranslation, translatedTokens.get(0));
        assertEquals(spanishTranslation, translatedTokens.get(1));

        assertEquals(categoryResult, read.getCategorizerResults().get(0));

        assertEquals(sentimentResult, read.getSentimentResults().get(0));
    }
}
