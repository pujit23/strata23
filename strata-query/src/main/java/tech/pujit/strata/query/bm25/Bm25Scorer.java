package tech.pujit.strata.query.bm25;

/**
 * BM25 sparse lexical relevance scorer.
 * TODO(phase-5): Inverted posting list traversal and BM25 term frequency saturation.
 */
public class Bm25Scorer {

    private final float k1;
    private final float b;

    public Bm25Scorer(float k1, float b) {
        this.k1 = k1;
        this.b = b;
    }

    public float score(int termFreq, int docLength, float avgDocLength, float idf) {
        float numerator = termFreq * (k1 + 1.0f);
        float denominator = termFreq + k1 * (1.0f - b + b * (docLength / avgDocLength));
        return idf * (numerator / denominator);
    }
}
