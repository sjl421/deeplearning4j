package org.deeplearning4j.nn.conf.distribution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BinomialDistribution extends Distribution {

    private static final long serialVersionUID = 7407024251874318749L;

    private final int numberOfTrials;
    private double probabilityOfSuccess;

    /**
     * Create a distribution
     *
     * @param numberOfTrials the number of trials
     * @param probabilityOfSuccess the probability of success
     */
    @JsonCreator
    public BinomialDistribution(@JsonProperty("numberOfTrials") int numberOfTrials, @JsonProperty("probabilityOfSuccess") double probabilityOfSuccess) {
        this.numberOfTrials = numberOfTrials;
        this.probabilityOfSuccess = probabilityOfSuccess;
    }

    public double getProbabilityOfSuccess() {
        return probabilityOfSuccess;
    }

    public void setProbabilityOfSuccess(double probabilityOfSuccess) {
        this.probabilityOfSuccess = probabilityOfSuccess;
    }

    public int getNumberOfTrials() {
        return numberOfTrials;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + numberOfTrials;
        long temp;
        temp = Double.doubleToLongBits(probabilityOfSuccess);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BinomialDistribution other = (BinomialDistribution) obj;
        if (numberOfTrials != other.numberOfTrials)
            return false;
        if (Double.doubleToLongBits(probabilityOfSuccess) != Double
                .doubleToLongBits(other.probabilityOfSuccess))
            return false;
        return true;
    }
    
    public String toString() {
        return "BinomialDistribution{" +
                "numberOfTrials=" + numberOfTrials +
                ", probabilityOfSuccess=" + probabilityOfSuccess +
                '}';
    }
}
